package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class signIn extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText mobile_no, iitg_roll_no;
    Button register;
    Spinner hostel,ch1,ch2;
    private users myuser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef= storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mobile_no = findViewById(R.id.regMobileNo);
        iitg_roll_no = findViewById(R.id.regRollNo);
        hostel=findViewById(R.id.Hostel_list);
        ch1=findViewById(R.id.sch1);
        ch2=findViewById(R.id.sch2);
        register = findViewById(R.id.regBtn);
        mAuth = FirebaseAuth.getInstance();
        myuser= new users(this);
        FirebaseUser user = mAuth.getCurrentUser();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.setVisibility(View.INVISIBLE);
                regiser();
            }
        });

    }
    private void regiser(){

        final String RollNo = iitg_roll_no.getText().toString().trim();
        final String Hostel = hostel.getSelectedItem().toString();
        final String MobNo=mobile_no.getText().toString().trim();
        final String choice1=ch1.getSelectedItem().toString();
        final String choice2=ch2.getSelectedItem().toString();
        if(choice1=="Select your favourite product category"||choice2=="Select your favourite product category"||choice1.equals(choice2))
        {   Toast.makeText(signIn.this, "Please select valid Category and different Category", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
        }
        else if ( (RollNo.isEmpty()) || (Hostel.isEmpty())|| (MobNo.isEmpty())) {
            Toast.makeText(signIn.this, "Please Verify all", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
        }
        else
            putdata();
    }
    private void putdata(){
        Intent i=getIntent();
        final String email = i.getStringExtra("Email");
        final String name = i.getStringExtra("Name");
        final String ImageUrl = i.getStringExtra("Image Url");
        final String RollNo = iitg_roll_no.getText().toString().trim();
        final String Hostel = hostel.getSelectedItem().toString();
        final String MobNo=mobile_no.getText().toString().trim();
        final String choice1=ch1.getSelectedItem().toString();
        final String choice2=ch2.getSelectedItem().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("Name",name);
        data.put("Email",email);
        data.put("Hostel",Hostel);
        data.put("Mobile Number",MobNo);
        data.put("Roll Number",RollNo);
        data.put("Image Url",ImageUrl);
        data.put("Choice1",choice1);
        data.put("Choice2",choice2);
        data.put("SignInmode","Google");
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(this, "Firestore returning Null", Toast.LENGTH_SHORT).show();
            }
            else {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                db.collection("users").document(uid).set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               onSuccessfulAuthentication();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(signIn.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }

        }
    private void onSuccessfulAuthentication() {

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        myuser.createSession(document.getString("Name"),
                                document.getString("Hostel"),
                                document.getString("Roll Number"),
                                document.getString("Email"),
                                document.getString("Mobile Number"),
                                document.getString("Image Url"),
                                mAuth.getCurrentUser().getUid());
                        finish();
                        Intent p = new Intent(signIn.this, Drawer.class);
                        startActivity(p);
                    } else {
                        Toast.makeText(signIn.this, "Your  session cannot be created", Toast.LENGTH_SHORT).show();
                    }
                } else {


                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
