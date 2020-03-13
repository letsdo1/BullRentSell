package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    EditText fullname, Email, mobile_no, iitg_roll_no, pass1, pass2;
    ImageView pro_pic,back;
    Button register;
    Spinner hostel,ch1,ch2;
    ImageView ImgUserPhoto;
    TextView path;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    private Uri pickedImgUri;
    private ProgressBar loadingProgress;
    private FirebaseAuth mAuth;
    String Url;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef= storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fullname = findViewById(R.id.regName);
        Email = findViewById(R.id.regEmail);
        mobile_no = findViewById(R.id.regMobileNo);
        iitg_roll_no = findViewById(R.id.regRollNo);
        pass1 = findViewById(R.id.regPass);
        pass2 = findViewById(R.id.regPass2);
        hostel=findViewById(R.id.Hostel_list);
        ch1=findViewById(R.id.sch1);
        ch2=findViewById(R.id.sch2);
        register = findViewById(R.id.regBtn);
        mAuth = FirebaseAuth.getInstance();
        pro_pic = findViewById(R.id.regUserPhoto) ;
        pro_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22)
                    checkAndRequestForPermission();
                else
                    openGallery();
            }
        });
        pd=new ProgressDialog(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Creating account");
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();
                register.setVisibility(View.INVISIBLE);
                regiser();
            }
        });
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(SignUp.this,MainActivity.class);
                startActivity(i);
            }
        });



    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUESCODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "line 147", Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null&& data.getData() != null ) {
            pickedImgUri = data.getData();
            pro_pic.setImageURI(pickedImgUri);
            Toast.makeText(this, "Image Successfully Chosen", Toast.LENGTH_SHORT).show();

        }


    }
    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(SignUp.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(SignUp.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }


    private void regiser(){
        final String email = Email.getText().toString().trim();
        final String password = pass1.getText().toString();
        final String password2 = pass2.getText().toString();
        final String name = fullname.getText().toString().trim();
        final String RollNo = iitg_roll_no.getText().toString().trim();
        final String Hostel = hostel.getSelectedItem().toString();
        final String MobNo=mobile_no.getText().toString().trim();
        final String choice1=ch1.getSelectedItem().toString();
        final String choice2=ch2.getSelectedItem().toString();
        if(pickedImgUri==null)
        {
            Toast.makeText(this, "Please select a image by clicking on the image icon", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();
        }
        else if((name.isEmpty()))
        {Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();}
        else if((email.isEmpty()))
        {Toast.makeText(this, "Email address is empty", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();}
        else if((MobNo.isEmpty()))
        {Toast.makeText(this, "Mobile number is empty", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();}
        else if((RollNo.isEmpty()))
        {Toast.makeText(this, "Roll number is empty", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();}
        else if((password.isEmpty()))
        {Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();}
        else if(choice1=="Select your favourite product category"||choice2=="Select your favourite product category"||choice1.equals(choice2))
        {   Toast.makeText(SignUp.this, "Please select valid Category and different Category", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();
        }
        else if(!password.equals(password2))
        {Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            pd.dismiss();}

//        else if (pickedImgUri==null|| (email.isEmpty()) || (name.isEmpty()) || (password.isEmpty()) || !password.equals(password2) || (RollNo.isEmpty()) || (Hostel.isEmpty())|| (MobNo.isEmpty())) {
//            Toast.makeText(SignUp.this, "Please Verify all", Toast.LENGTH_SHORT).show();
//            register.setVisibility(View.VISIBLE);
//        }
        else
        CreateUserAccount(email,password);
    }
    private  void CreateUserAccount(final String email,final String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            imageUrl();
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else
                        {
                            Toast.makeText(SignUp.this,"Account creation failed because " + task.getException().getMessage().toString(),Toast.LENGTH_SHORT).show();
                            register.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }
                    }
                });

    }
    private  void imageUrl()
    {
        if(pickedImgUri!=null){
        final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
        Log.d("prince",ref.toString());
        ref.putFile(pickedImgUri)

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        Toast.makeText(SignUp.this, "Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        register.setVisibility(View.VISIBLE);
                        pd.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(SignUp.this, "Uploaded", Toast.LENGTH_SHORT).show();

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Url= uri.toString();
                        //Toast.makeText(SignUp.this, "Url Created succeffully", Toast.LENGTH_SHORT).show();
                        updateUserInfo(Url);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //Toast.makeText(SignUp.this, "Error is"+exception.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("pra",exception.toString());
                        register.setVisibility(View.VISIBLE);
                        pd.dismiss();

                    }
                });
            }
        });
        }


//        Log.d("Str",Url);


    }

    private void updateUserInfo( String Url) {
        final String email = Email.getText().toString().trim();
        final String password = pass1.getText().toString();
        final String name = fullname.getText().toString();
        final String RollNo = iitg_roll_no.getText().toString();
        final String Hostel = hostel.getSelectedItem().toString();
        final String MobNo=mobile_no.getText().toString();
        final String choice1=ch1.getSelectedItem().toString();
        final String choice2=ch2.getSelectedItem().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("Name",name);
        data.put("Email",email);
        data.put("Password",password);
        data.put("Hostel",Hostel);
        data.put("Mobile Number",MobNo);
        data.put("Roll Number",RollNo);
        data.put("Image Url",Url);
        data.put("Choice1",choice1);
        data.put("Choice2",choice2);
        data.put("SignInmode","Normal");
        db.collection("users").document(mAuth.getUid()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                         Toast.makeText(SignUp.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                        Intent intent= new Intent(SignUp.this,Drawer.class);
                        startActivity(intent);
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        register.setVisibility(View.VISIBLE);
                        pd.dismiss();

                    }
                });






    }



}
