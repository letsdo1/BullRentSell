package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Swap extends AppCompatActivity {


    Button Choose,Upload,SeeList;
    EditText text_name,text_description,text_price,txt_number,txt_description_back;
    TextView txt_path;
    private static final String KEY_NAME="NAME/BRAND:";
    private static final String KEY_DESCRIPTION="DESCRIPTION:";
    private static final String KEY_PRICE="PRICE:";
    private static final String KEY_URL="URL TO IMAGE:";
    private static final String KEY_MODE="MODE:";
    private static final String KEY_CONTACT="CONTACT NO:";
    private static final String KEY_DESCRIPTION_RETURN_ITEM="DESCRIPTION OF ITEM YOU WISH:";
    private static final String KEY_UID="UID";
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
        setContentView(R.layout.activity_swap);

        Choose=(Button)findViewById(R.id.btn_choose);
        Upload=(Button)findViewById(R.id.btn_upload);
        //  Submit=(Button)findViewById(R.id.btn_submit);
        text_name=(EditText)findViewById(R.id.edt_name);
        text_description=(EditText)findViewById(R.id.edt_description);
        text_price=(EditText)findViewById(R.id.edt_price);
        category=(Spinner)findViewById(R.id.category);
        txt_number=(EditText)findViewById(R.id.edt_number);
        txt_description_back=(EditText)findViewById(R.id.edt_rent);
        txt_path=(TextView) findViewById(R.id.filepath);
        SeeList=(Button)findViewById(R.id.seelist);




        String url_string;


        Upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*progressDialog.setMessage("Uploading.....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();*/
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
                    public void onClick(View v) {
                        Intent it = new Intent(Swap.this,Swap_Item_List.class);
                        startActivity(it);
                    }
                }
        );
    }
    public void onBackPressed() {
        Intent i=new Intent(Swap.this,Drawer.class);
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
            txt_path.setText(filePath.toString());
            Toast.makeText(Swap.this, "Image successfully Choosen!!!!", Toast.LENGTH_SHORT).show();
           /* try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }else {
            Toast.makeText(Swap.this, "Error in Choosing Image!!!!", Toast.LENGTH_SHORT).show();
        }
    }



    public String UploadButtonClicked(){


        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        String price= text_price.getText().toString();
        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        String dback=txt_description_back.getText().toString();



        if (filePath!= null  &&  (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(dback) || TextUtils.isEmpty(price) || TextUtils.isEmpty(number)))) {

            final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
            Log.d("prince",ref.toString());
            ref.putFile(filePath)

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(Swap.this, "Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            Toast.makeText(Swap.this, "Uploaded", Toast.LENGTH_SHORT).show();



                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    s= uri.toString();
                                    Toast.makeText(Swap.this, s, Toast.LENGTH_SHORT).show();
                                    Uploadtext(s);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(Swap.this, "Error is"+exception.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("pra",exception.toString());

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
        String price= text_price.getText().toString();
        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        String dback=txt_description_back.getText().toString();


        //  if (!(TextUtils.isEmpty(name) && TextUtils.isEmpty(description) && TextUtils.isEmpty(period) && TextUtils.isEmpty(price) && TextUtils.isEmpty(number))){


        Map<String, Object> data = new HashMap<>();
        data.put(KEY_NAME,name);
        data.put(KEY_DESCRIPTION,description);
        data.put(KEY_PRICE,price);
        data.put(KEY_URL,s1);
        data.put(KEY_MODE,"SWAP");
        data.put(KEY_CONTACT,number);
        data.put(KEY_DESCRIPTION_RETURN_ITEM,dback);
        data.put(KEY_UID,uid);



        db.collection(cat).document().set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Swap.this,"Successful",Toast.LENGTH_LONG).show();

                        text_name.setText("");
                        text_description.setText("");
                        text_price.setText("");
                        txt_number.setText("");
                        txt_description_back.setText("");
                        txt_path.setText("");
                        //   progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Swap.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });












     /*   else{

            Toast.makeText(Rent.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }*/







    }


}
