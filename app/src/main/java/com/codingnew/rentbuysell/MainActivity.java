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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    TextView sign_up;
    private GoogleSignInButton loginnwGmail;
    //private SignInButton loginnwGmail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore  db= FirebaseFirestore.getInstance();
    EditText email,password;
    TextView forgot_pass;
    private users myuser;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.email_txt);
        password=(EditText)findViewById(R.id.pass_txt);
        forgot_pass=(TextView)findViewById(R.id.forgot);
        login=(Button)findViewById(R.id.log_in);
        loginnwGmail= (GoogleSignInButton)findViewById(R.id.gmail_btn);
        //loginnwGmail=(Button)findViewById(R.id.gmail_btn);
        myuser= new users(this);
        loginnwGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        signIn();

            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,forgot_password.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normallogin();
            }
        });
        sign_up=findViewById(R.id.signup);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    /*LogIn With Google*/
    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account!=null){
                firebaseAuthWithGoogle(account);

            }
            else{
                Toast.makeText(this,"Login authentication failed",Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().exists()){
                                        onSuccessfulAuthentication();
                                    }
                                    else {
                                        Intent intent = new Intent(MainActivity.this, signIn.class);
                                        intent.putExtra("Image Url", acct.getPhotoUrl().toString());
                                        intent.putExtra("Name", acct.getDisplayName());
                                        intent.putExtra("Email", acct.getEmail());
                                        startActivity(intent);
                                    }
                                }
                            });


                           // Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

        private void normallogin(){
            String e = email.getText().toString();
            String p = password.getText().toString();
            if(e.equals(""))
                Toast.makeText(this, "Email should not be empty", Toast.LENGTH_SHORT).show();
            else if(p.equals(""))
                Toast.makeText(this, "Password should not be empty", Toast.LENGTH_SHORT).show();
            else {
                mAuth.signInWithEmailAndPassword(e, p)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "WELCOME", Toast.LENGTH_SHORT).show();
                                    onSuccessfulAuthentication();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                   // Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

        }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }




    private void onSuccessfulAuthentication() {
        //Toast.makeText(this, "21222", Toast.LENGTH_SHORT).show();

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
                        Intent p = new Intent(MainActivity.this, Drawer.class);
                        startActivity(p);
                    } else {
                        Toast.makeText(MainActivity.this, "Your session cannot be created", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

}
