package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GiveAway extends AppCompatActivity {

    Button Choose,Upload,SeeList;
    ProgressDialog pd3;
    ImageView imv,back;
    EditText text_name,text_description,text_price,txt_number,txt_period;
    TextView txt_path;


    private static final String KEY_NAME="name";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_URL="imageUrl";
    private static final String KEY_MODE="mode";
    private static final String KEY_CONTACT="mobileNo";
    private static final String KEY_UID="uid";
    private static final String KEY_CATEGORY="category";
    private static final String KEY_PARENT_ID="parentid";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  String uid = mAuth.getCurrentUser().getUid();


    Spinner category;
    public String s="null",t="null1";



    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef= storage.getReference();
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ImageView imageView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // ProgressDialog progressDialog=new ProgressDialog(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_away);
        Choose=(Button)findViewById(R.id.btn_choose);
        Upload=(Button)findViewById(R.id.btn_upload);
        //  Submit=(Button)findViewById(R.id.btn_submit);
        text_name=(EditText)findViewById(R.id.edt_name);
        text_description=(EditText)findViewById(R.id.edt_description);
        // text_price=(EditText)findViewById(R.id.edt_price);
        category=(Spinner)findViewById(R.id.category);
        txt_number=(EditText)findViewById(R.id.edt_number);
        //txt_period=(EditText)findViewById(R.id.edt_rent);
        txt_path=(TextView) findViewById(R.id.filepath);
        SeeList=(Button)findViewById(R.id.btn_seelist);
        imv=(ImageView)findViewById(R.id.regitem3Photo);
        pd3=new ProgressDialog(this);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(GiveAway.this,Drawer.class);
                startActivity(i);
            }
        });

        Upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd3.setMessage("Uploading.....");
                        pd3.setCancelable(false);
                        pd3.show();
                        t = UploadButtonClicked();


                    }
                }
        );
        Choose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseImage();
                    }
                }
        );

        SeeList.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { finish();
                        Intent i1 = new Intent(GiveAway.this,ListGiveAwayItems.class);
                        startActivity(i1);

                    }
                }
        );



    }
    public void onBackPressed() {
        Intent i=new Intent(GiveAway.this,Drawer.class);
        startActivity(i);
    }









    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            imv.setImageURI(filePath);
            //Toast.makeText(GiveAway.this, "Image successfully Choosen!!!!", Toast.LENGTH_SHORT).show();
           /* try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }else {
            Toast.makeText(GiveAway.this, "Error in Choosing Image!!!!", Toast.LENGTH_SHORT).show();
        }
    }



    public String UploadButtonClicked(){


        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        //String price= text_price.getText().toString();
        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        //  String period=txt_period.getText().toString();



        if (filePath!= null  &&  (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(description) ||  TextUtils.isEmpty(number)))) {

            final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
            Log.d("prince",ref.toString());
            ref.putFile(filePath)

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(GiveAway.this, "Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            pd3.dismiss();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            Toast.makeText(GiveAway.this, "Uploaded", Toast.LENGTH_SHORT).show();



                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    s= uri.toString();
                                    Toast.makeText(GiveAway.this, s, Toast.LENGTH_SHORT).show();
                                    Uploadtext(s);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(GiveAway.this, "Error is"+exception.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("pra",exception.toString());
                                    pd3.dismiss();

                                }
                            });


                        }
                    });
        }else{
            Toast.makeText(this,"Image not selected or Field left Empty!!!!!!!!!",Toast.LENGTH_LONG).show();
        }
        Log.d("Str",s);
        return s;
    }


    public void Uploadtext(String s1){


        String name = text_name.getText().toString();
        String description = text_description.getText().toString();

        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();



        //  if (!(TextUtils.isEmpty(name) && TextUtils.isEmpty(description) && TextUtils.isEmpty(period) && TextUtils.isEmpty(price) && TextUtils.isEmpty(number))){


        Map<String, Object> data = new HashMap<>();
        data.put(KEY_NAME,name);
        data.put(KEY_DESCRIPTION,description);

        data.put(KEY_URL,s1);
        data.put(KEY_MODE,"GIVE AWAY");
        data.put(KEY_CONTACT,number);
        data.put(KEY_UID,uid);
        String time=String.valueOf(System.currentTimeMillis());
        data.put("Servertime",time);
        db.collection("Giveaway").document().set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GiveAway.this,"Successful",Toast.LENGTH_LONG).show();
                        pd3.dismiss();
                        text_name.setText("");
                        text_description.setText("");
                        txt_path.setText("");
                        txt_number.setText("");
                        imv.setImageResource(R.drawable.splashscreen);
                        //   progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GiveAway.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd3.dismiss();

                    }
                });


    }


}
