package com.codingnew.rentbuysell;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sell extends AppCompatActivity {

    Button Choose,Upload;
    ProgressDialog pd1;
    ImageView prodimg;
    TextView txt_path;
    EditText text_name,text_description,text_price,txt_number;
    private static final String KEY_NAME="name";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_PRICE="price";
    private static final String KEY_URL="imageUrl";
    private static final String KEY_MODE="mode";
    private static final String KEY_CONTACT="mobileNo";
    private static final String KEY_CATEGORY="category";
    private static final String KEY_UID="uid";
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
    TextView welcome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Choose=(Button)findViewById(R.id.btn_choose);
        Upload=(Button)findViewById(R.id.btn_upload);
        text_name=(EditText)findViewById(R.id.edt_name);
        text_description=(EditText)findViewById(R.id.edt_description);
        text_price=(EditText)findViewById(R.id.edt_price);
        category=(Spinner)findViewById(R.id.category);
        txt_number=(EditText)findViewById(R.id.edt_number);
        prodimg=(ImageView)findViewById(R.id.regitemPhoto);
        pd1= new ProgressDialog(this);
//        txt_path=(TextView) findViewById(R.id.filepath);
        welcome=(TextView) findViewById(R.id.welcome);
      //  Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        String url_string;
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(Sell.this,Drawer.class);
                startActivity(i);
            }
        });



        Upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd1.setMessage("Uploading...");
                        pd1.show();
                        pd1.setCancelable(false);
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




    }
    public void onBackPressed() {
        Intent i=new Intent(Sell.this,Drawer.class);
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
            prodimg.setImageURI(filePath);
           // txt_path.setText(filePath.toString());

        }
    }



    //UUID.randomUUID().toString()

    public String UploadButtonClicked(){

        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        String price= text_price.getText().toString();
        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        File file = new File(String.valueOf(filePath));
        long length = file.length()/1024;
        if(length>1)
        {  Toast.makeText(this, "Image size should be less then 500KB", Toast.LENGTH_SHORT).show();
            pd1.dismiss();
        }


        else if (filePath!= null  &&  (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(description) ||  TextUtils.isEmpty(number) || TextUtils.isEmpty(price)|| TextUtils.isEmpty(cat)))) {

            final StorageReference ref = storageRef.child("images/"+UUID.randomUUID().toString());
            Log.d("prince",ref.toString());
            ref.putFile(filePath)

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                           // Toast.makeText(Sell.this, "Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            pd1.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    //Toast.makeText(Sell.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            s= uri.toString();
                            //Toast.makeText(Sell.this, s, Toast.LENGTH_SHORT).show();
                            Uploadtext(s);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                           // Toast.makeText(Sell.this, "Error is"+exception.toString(), Toast.LENGTH_SHORT).show();
                            pd1.dismiss();
                            Log.d("pra",exception.toString());

                        }
                    });


                }
            });}
        else{
            Toast.makeText(this,"Image not selected or any field left empty !",Toast.LENGTH_LONG).show();
            pd1.dismiss();
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


        final Map<String, Object> data = new HashMap<>();
        data.put(KEY_NAME,name);
        data.put(KEY_DESCRIPTION,description);
        data.put(KEY_PRICE,price);
        data.put(KEY_URL,s1);
        data.put(KEY_MODE,"ON SALE");
        data.put(KEY_CONTACT,number);
        data.put(KEY_CATEGORY,cat);
        data.put(KEY_UID,uid);
        data.put("public_feed","tr");
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
                                               // Toast.makeText(Sell.this,"Successful",Toast.LENGTH_LONG).show();
                                                pd1.dismiss();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                               // Toast.makeText(Sell.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                pd1.dismiss();

                                            }
                                        });
                                String id=ref.getId();
                                putuserdtata(s1,id);


                            } else {
                                //Toast.makeText(Sell.this, "Getted both choice", Toast.LENGTH_SHORT).show();
                            }
                        } else {


                        }
                    }
                });




    }
    public void putuserdtata(String s1, String id){
        String name = text_name.getText().toString();
        String description = text_description.getText().toString();
        String price= text_price.getText().toString();
        String cat= category.getSelectedItem().toString();
        String number=txt_number.getText().toString();
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_NAME,name);
        data.put(KEY_DESCRIPTION,description);
        data.put(KEY_PRICE,price);
        data.put(KEY_URL,s1);
        data.put(KEY_MODE,"ON SALE");
        data.put(KEY_CONTACT,number);
        data.put(KEY_CATEGORY,cat);
        data.put(KEY_UID,id);
        data.put("public_feed","tr");
        Map<String,Object> upid=new HashMap<>();
        upid.put("productid",id);
        db.collection("productid").document("CsRaWRYVoTQF0mNR1fv6").set(upid);
        DocumentReference refuser=db.collection("users").document(uid).collection("Product").document();
        data.put(KEY_PARENT_ID,refuser.getId());
        refuser.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(Sell.this,"Successful",Toast.LENGTH_LONG).show();
                        text_name.setText("");
                        text_description.setText("");
                        text_price.setText("");
                        txt_number.setText("");
//                        txt_path.setText("");
                        prodimg.setImageResource(R.drawable.splashscreen);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(Sell.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

}
