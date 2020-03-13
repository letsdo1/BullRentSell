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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Rent extends AppCompatActivity {


    Button Choose,Upload;
    ProgressDialog pd2;
    ImageView prod_img;
    EditText text_name,text_description,text_price,txt_number,txt_period;
    TextView txt_path;
    private static final String KEY_NAME="name";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_PRICE="price";
    private static final String KEY_URL="imageUrl";
    private static final String KEY_MODE="mode";
    private static final String KEY_CONTACT="mobileNo";
    private static final String KEY_PERIOD="duration_of_rent";
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
    ImageView imageView,back;
    FirebaseFirestore db = FirebaseFirestore.getInstance();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(Rent.this,Drawer.class);
                startActivity(i);
            }
        });




        Choose=(Button)findViewById(R.id.btn_choose);
        Upload=(Button)findViewById(R.id.btn_upload);
        //  Submit=(Button)findViewById(R.id.btn_submit);
        text_name=(EditText)findViewById(R.id.edt_name);
        text_description=(EditText)findViewById(R.id.edt_description);
        text_price=(EditText)findViewById(R.id.edt_price);
        category=(Spinner)findViewById(R.id.category);
        txt_number=(EditText)findViewById(R.id.edt_number);
        txt_period=(EditText)findViewById(R.id.edt_rent);
        prod_img=(ImageView)findViewById(R.id.regitem2Photo);
        txt_path=(TextView) findViewById(R.id.filepath);
        pd2= new ProgressDialog(this);




        String url_string;


        Upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd2.setMessage("Uploading.....");
                        pd2.setCancelable(false);
                        pd2.show();
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

      /*  Submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uploadtext(t);
                    }
                }
        );*/


    }
    public void onBackPressed() {
        Intent i=new Intent(Rent.this,Drawer.class);
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
            prod_img.setImageURI(filePath);

           // txt_path.setText(filePath.toString());
            //Toast.makeText(Rent.this, "Image successfully Choosen!!!!", Toast.LENGTH_SHORT).show();
           /* try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }else {
            Toast.makeText(Rent.this, "Error in Choosing Image!!!!", Toast.LENGTH_SHORT).show();
        }
    }



    public String UploadButtonClicked(){


        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        String price = text_price.getText().toString();
        String cat = category.getSelectedItem().toString();
        String number = txt_number.getText().toString();
        String period = txt_period.getText().toString();



        if (filePath!= null  &&  (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(period) || TextUtils.isEmpty(price) || TextUtils.isEmpty(number)))) {

            final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
            Log.d("prince",ref.toString());
            ref.putFile(filePath)

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(Rent.this, "Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                            //Toast.makeText(Rent.this, "Uploaded", Toast.LENGTH_SHORT).show();



                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    s= uri.toString();
                                    //Toast.makeText(Rent.this, s, Toast.LENGTH_SHORT).show();
                                    Uploadtext(s);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //Toast.makeText(Rent.this, "Error is"+exception.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("pra",exception.toString());
                                    pd2.dismiss();

                                }
                            });


                        }
                    });










        }else{
            Toast.makeText(this,"Image not selected or any field left empty!!!!!!!!!",Toast.LENGTH_LONG).show();
            pd2.dismiss();
        }
        Log.d("Str",s);
        return s;
    }


    public void Uploadtext(final String s1){


        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        String price= text_price.getText().toString();
        final String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        String period=txt_period.getText().toString();


        //  if (!(TextUtils.isEmpty(name) && TextUtils.isEmpty(description) && TextUtils.isEmpty(period) && TextUtils.isEmpty(price) && TextUtils.isEmpty(number))){


        final Map<String, Object> data = new HashMap<>();
        data.put(KEY_CATEGORY,cat);
        data.put(KEY_NAME,name);
        data.put(KEY_DESCRIPTION,description);
        data.put(KEY_PRICE,price);
        data.put(KEY_URL,s1);
        data.put(KEY_MODE,"ON RENT");
        data.put(KEY_CONTACT,number);
        data.put(KEY_PERIOD,period);
        data.put(KEY_UID,uid);
        data.put("public_feed","true");
        db.collection("productid").document("CsRaWRYVoTQF0mNR1fv6").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                final String n = document.getString("productid");
                                int m=Integer.valueOf(n);
                                data.put("myid",String.valueOf(m+1));
                                data.put("myidint",m+1);
                                DocumentReference ref= db.collection(cat).document(String.valueOf(m+1));
                                ref.set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                               // Toast.makeText(Rent.this,"Successful",Toast.LENGTH_LONG).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                               // Toast.makeText(Rent.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                pd2.dismiss();

                                            }
                                        });
                                String id=ref.getId();
                                putuserdata(s1,id);


                            } else
                                {
                                //Toast.makeText(Rent.this, "Getted both choice", Toast.LENGTH_SHORT).show();
                            }
                        } else {


                        }
                    }
                });
       /* DocumentReference dref= db.collection(cat).document();
        dref.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Rent.this,"Successful",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Rent.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        String id= dref.getId();
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        putuserdata(s1,id);*/
    }
    public void putuserdata(String s1,String id){
        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        String price= text_price.getText().toString();
        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        String period=txt_period.getText().toString();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_CATEGORY,cat);
        data.put(KEY_NAME,name);
        data.put(KEY_DESCRIPTION,description);
        data.put(KEY_PRICE,price);
        data.put(KEY_URL,s1);
        data.put(KEY_MODE,"ON RENT");
        data.put(KEY_CONTACT,number);
        data.put(KEY_PERIOD,period);
        data.put(KEY_UID,id);
        data.put("public_feed","true");
        Map<String,Object> upid=new HashMap<>();
        upid.put("productid",id);
        db.collection("productid").document("CsRaWRYVoTQF0mNR1fv6").set(upid);
        final DocumentReference userdref= db.collection("users").document(uid).collection("Product").document();
        data.put(KEY_PARENT_ID,userdref.getId());
        //Toast.makeText(Rent.this,userdref.getId()+" Parent id",Toast.LENGTH_LONG).show();
        userdref.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(Rent.this,userdref.getId(),Toast.LENGTH_LONG).show();

                        text_name.setText("");
                        text_description.setText("");
                        text_price.setText("");
                        txt_number.setText("");
                        txt_period.setText("");
                        prod_img.setImageResource(R.drawable.splashscreen);
                        pd2.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Toast.makeText(Rent.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd2.dismiss();
                    }
                });


    }




}








