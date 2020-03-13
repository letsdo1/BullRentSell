package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class forgot_password extends AppCompatActivity {

    private EditText email;
    private Button Reset;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email= (EditText) findViewById(R.id.email_txt);
        Reset=(Button) findViewById(R.id.reset);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(forgot_password.this,MainActivity.class);
                startActivity(i);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = email.getText().toString().trim();
                if (useremail.equals("")) {
                    Toast.makeText(forgot_password.this, "Please enter registered email address.", Toast.LENGTH_SHORT).show();
                } else {


                    mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgot_password.this, "Password reset email send", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(forgot_password.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(forgot_password.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }
}
