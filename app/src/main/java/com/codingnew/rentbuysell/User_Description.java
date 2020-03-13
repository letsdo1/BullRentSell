package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class User_Description extends AppCompatActivity {
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    Button Deleteproduct;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Switch aSwitch;
    TextView warning;
    ImageView back;
     String cat,uid,parentid;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__description);
        Deleteproduct=findViewById(R.id.Delete);
        warning=findViewById(R.id.warning_text);
        aSwitch=findViewById(R.id.switch1);
        aSwitch.setTextOff("OFF");
        aSwitch.setTextOn("ON");
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(User_Description.this,MyData.class);
                startActivity(i);
            }
        });
        getdata();
        Intent i = getIntent();
        uid=i.getStringExtra("UID");
        cat=i.getStringExtra("CATEGORY");
        parentid=getIntent().getStringExtra("PARENTID");
        String public_feed=i.getStringExtra("public_feed");
        if(public_feed.equals("true")){
            aSwitch.setChecked(true);
            warning.setVisibility(View.GONE);
            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setoff();

                }
            });

        }
        else if(public_feed.equals("false")){
            aSwitch.setChecked(false);
            warning.setVisibility(View.VISIBLE);
            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seton();

                }
            });
        }
            else{
                aSwitch.setVisibility(View.GONE);
                warning.setVisibility(View.GONE);

            }



        //Boolean switchState = aSwitch.isChecked();
//        aSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(aSwitch.isChecked()){
//                deleteproductpublic(cat,uid);
//                String parentid=getIntent().getStringExtra("PARENTID");
//                String useruid = mAuth.getCurrentUser().getUid();
//                db.collection("users").document(useruid).collection("Product").document(parentid).update("UID","");
//                }
//                else{
//                    String parentid=getIntent().getStringExtra("PARENTID");
//                    String useruid = mAuth.getCurrentUser().getUid();
//                    db.collection("users").document(useruid).collection("Product").document(parentid).get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(User_Description.this, "connected with firebase", Toast.LENGTH_SHORT).show();
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                            product_part temp=new product_part();
//                                            String NAME=document.getString("NAME");
//                                            String DESCRIPTION=document.getString("DESCRIPTION");
//                                            String IMAGEURL=document.getString("IMAGEURL");
//                                            String MODE=document.getString("MODE");
//                                            String PRICE=document.getString("PRICE");
//                                            String MOBILENO=document.getString("MOBILENO");
//                                            String UID=document.getString("UID");
//                                            int Myid=Integer.valueOf(document.getString("Myid"));
//                                            String DURATION_OF_RENT=document.getString("DURATION_OF _RENT");
//                                            String PARENTID=document.getString("PARENTID");
//                                            String CATAGORY=document.getString("CATEGORY");
//                                            product_part temp=new product_part(PARENTID,MODE,IMAGEURL,MOBILENO,CATAGORY, NAME, PRICE, DESCRIPTION,UID, DURATION_OF_RENT,Myid);
//                                        }
//                                    }
//                                });
//
//                             else {
//                        Toast.makeText(User_Description.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
//                    }
//
//            });
//
//                }
//
//            }
//        });



      //  Toast.makeText(this,cat, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this,uid, Toast.LENGTH_SHORT).show();
        Deleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deleteproduct.setVisibility(View.INVISIBLE);
                deleteproductpublic(cat,uid);

            }
        });




    }
    public void setoff(){

        db.collection(cat).document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        aSwitch.setChecked(false);
                        warning.setVisibility(View.VISIBLE);
                        //Toast.makeText(User_Description.this, parentid, Toast.LENGTH_SHORT).show();
                        db.collection("users").document(mAuth.getUid()).collection("Product").document(parentid).update("public_feed","false")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(User_Description.this, "Successfully removed data from public feed", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i=new Intent(User_Description.this,MyData.class);
                                startActivity(i);


                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(User_Description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(User_Description.this, "Not able to delete data from public feed", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void seton(){
        db.collection("users").document(mAuth.getUid()).collection("Product").document(parentid).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document != null) {
                                            String NAME=document.getString("name");
                                            String DESCRIPTION=document.getString("description");
                                            String IMAGEURL=document.getString("imageUrl");
                                            String MODE=document.getString("mode");
                                            String PRICE=document.getString("price");
                                            String MOBILENO=document.getString("mobileNo");
                                            String UID=document.getString("uid");
                                            int myidint=Integer.valueOf(uid);
                                            String DURATION_OF_RENT=document.getString("duration_of_rent");
                                            String PARENTID=document.getString("parentid");
                                            String CATAGORY=document.getString("category");
                                          Uploadtext(NAME,CATAGORY,DESCRIPTION,UID,MOBILENO,DURATION_OF_RENT,PRICE,IMAGEURL,myidint);
                                        }
                                    }


                             else
                             Toast.makeText(User_Description.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();


            }});

    }
    public void Uploadtext(String name,String cat,String description,String uid,String number,String period,String price,String s1,int myidint) {
        final Map<String, Object> data = new HashMap<>();
        data.put(KEY_CATEGORY, cat);
        data.put(KEY_NAME, name);
        data.put(KEY_DESCRIPTION, description);
        data.put(KEY_PRICE, price);
        data.put(KEY_URL, s1);
        data.put(KEY_MODE, "ON RENT");
        data.put(KEY_CONTACT, number);
        data.put(KEY_PERIOD, period);
        data.put(KEY_UID, mAuth.getUid());
        data.put("myid", uid);
        data.put("myidint",myidint);
        data.put("public_feed", "true");
        DocumentReference ref = db.collection(cat).document(uid);
                                ref.set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                aSwitch.setChecked(true);
                                                warning.setVisibility(View.GONE);
                                                //Toast.makeText(User_Description.this, parentid, Toast.LENGTH_SHORT).show();
                                                db.collection("users").document(mAuth.getUid()).collection("Product").document(parentid).update("public_feed","true")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(User_Description.this, "Successful putted data in public feed", Toast.LENGTH_LONG).show();
                                                                finish();
                                                                Intent i=new Intent(User_Description.this,MyData.class);
                                                                startActivity(i);


                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(User_Description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        });



                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(User_Description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
    }

    /* public void deleteproductpublicfeed(String cat,String uid)
    {
        db.collection(cat).document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(User_Description.this, "Succesfully removed data from public feed", Toast.LENGTH_SHORT).show();
                        warning.setVisibility(View.VISIBLE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(User_Description.this, "Not able to delete data from public feed", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/
    public void deleteproductpublic(String cat,String uid)
    {
        db.collection(cat).document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(User_Description.this, "Successfully deleted data from public feed", Toast.LENGTH_SHORT).show();
                        Deleteuserproduct();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(User_Description.this, "Not able to delete data from public feed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void Deleteuserproduct(){
        String parentid=getIntent().getStringExtra("PARENTID");
        String useruid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(useruid).collection("Product").document(parentid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(User_Description.this, "Successfully deleted Data", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i= new Intent(User_Description.this,MyData.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(User_Description.this, "Not able to delete data from public feed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getdata()
    {
        if(getIntent().hasExtra("Name")&&getIntent().hasExtra("Price")&&getIntent().hasExtra("Shortdesc")&&getIntent().hasExtra("ImageURL")&&getIntent().hasExtra("Mobile_no")&&getIntent().hasExtra("CATEGORY"))
      {
        Intent i = getIntent();
        String name = i.getStringExtra("Name");
        String desc = i.getStringExtra("Shortdesc");
        String price = i.getStringExtra("Price");
        String imageurl=i.getStringExtra("ImageURL");
        addData(name,desc,price,imageurl);
      }

    }
    private void addData(String name,String desc,String price,String imageurl){
        TextView Name,Desc,Price;
        ImageView pic;
        Name=findViewById(R.id.text_name);
        Desc=findViewById(R.id.text_desc);
        Price=findViewById(R.id.Price);
        pic=findViewById(R.id.image_pic);
        Name.setText(name);
        Desc.setText(desc);
        Price.setText(price);
        Glide.with(User_Description.this).load(imageurl).into(pic);
    }
}
