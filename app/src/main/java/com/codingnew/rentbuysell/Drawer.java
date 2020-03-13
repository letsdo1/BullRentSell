package com.codingnew.rentbuysell;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codingnew.rentbuysell.users.editor;
import static java.lang.Integer.valueOf;

public class Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentuser = mAuth.getCurrentUser();
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<product_part> feed=new ArrayList<product_part>();
    private String token;
    ImageView pro_pic;
    TextView name, Email;
    public  users myuser ;
    static String choice1,choice2;
    NotificationChannel mChannel;
    private static final String CHANNEL_ID="CHANNEL_ID";
    NotificationManager notificationManager;
    //productAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = findViewById(R.id.toolbar);
        putnewsfeed();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        put_userdata_header(acct);



        int importance = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(Constants.CHANNEL_ID, "Noti", importance);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(mChannel);
        }


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //shownoti();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

        }




    }

    private CollectionReference userref = db.collection("users");
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new com.codingnew.rentbuysell.HomeFragment()).commit();

                break;
            case R.id.nav_buy:
              Intent s=new Intent(Drawer.this,Buy.class);
              startActivity(s);
              break;
            case R.id.nav_sell:
                Intent s1 = new Intent(Drawer.this, Sell.class);
                startActivity(s1);
                break;
            case R.id.nav_rent:
                finish();
                Intent s2 = new Intent(Drawer.this, Rent.class);
                startActivity(s2);
                break;

            case R.id.nav_aboutUs:
                finish();
                Intent s3 = new Intent(Drawer.this, AboutUs.class);
                startActivity(s3);
                break;

                case R.id.nav_giveAway:
                    //finish();
                    Intent s5 = new Intent(Drawer.this, GiveAway.class);
                    startActivity(s5);
                break;
            case R.id.nav_myinfo:
                Intent s6=new Intent(Drawer.this,MyData.class);
                startActivity(s6);
                break;
            case R.id.signOut:
                signout();
                break;
            case R.id.nav_chats:
                Intent s7 =new Intent(Drawer.this,Chat.class);
                startActivity(s7);
                break;
            case R.id.nav_bug:
                       Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://forms.gle/Ur2QyL6fUbvYCszd9"));
                startActivity(viewIntent);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);

        }
    }

    public void putnewsfeed() {
        DocumentReference docref = db.collection("users").document(mAuth.getUid());
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        choice1 = document.getString("Choice1");
                        choice2 = document.getString("Choice2");
                        putinrecyclerview(choice1,choice2);

                    } else {
                        //Toast.makeText(Drawer.this, "Getted both choice", Toast.LENGTH_SHORT).show();
                    }
                } else {


                }
            }
        });
       // Toast.makeText(Drawer.this, "Choice1" + choice1, Toast.LENGTH_SHORT).show();
    }


    private void gettoken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()){
                    token = task.getResult().getToken();
                    Map<String, Object> data = new HashMap<>();
                    data.put(Constants.KEY_TOKEN,token);
                    db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("NOT_ID").document(mAuth.getCurrentUser().getUid()).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(Drawer.this, token.toString(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   // Toast.makeText(Drawer.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }else{
                    Toast.makeText(Drawer.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }






    public void putinrecyclerview(String ch1,String ch2)
    {   db = FirebaseFirestore.getInstance();
        //Toast.makeText(Drawer.this, "Choice1" + ch1, Toast.LENGTH_SHORT).show();


        final CollectionReference productref1 = db.collection(ch1);
        final CollectionReference productref2 = db.collection(ch2);
        productref1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           // Toast.makeText(Drawer.this, "connected with firebase", Toast.LENGTH_SHORT).show();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //product_part temp=new product_part();
                                String NAME=document.getString("name");
                                String DESCRIPTION=document.getString("description");
                                String IMAGEURL=document.getString("imageUrl");
                                String MODE=document.getString("mode");
                                String PRICE=document.getString("price");
                                String MOBILENO=document.getString("mobileNo");
                                String UID=document.getString("uid");
                                int Myidint=Integer.valueOf(document.getString("myid"));
                                String Myid=document.getString("myid");
                                String DURATION_OF_RENT=document.getString("duration_of_rent");
                                String PARENTID=document.getString("parentid");
                                String CATAGORY=document.getString("category");
                                String public_feed=document.getString("public_feed");
                                product_part temp=new product_part(MODE,IMAGEURL,MOBILENO,CATAGORY, NAME, PRICE, DESCRIPTION,UID, DURATION_OF_RENT,PARENTID,Myid,Myidint,public_feed);
                                feed.add(temp);

                            }

                            productref2.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //product_part temp;
                                                    String NAME=document.getString("name");
                                                    String DESCRIPTION=document.getString("description");
                                                    String IMAGEURL=document.getString("imageUrl");
                                                    String MODE=document.getString("mode");
                                                    String PRICE=document.getString("price");
                                                    String MOBILENO=document.getString("mobileNo");
                                                    String UID=document.getString("uid");
                                                    int Myidint=Integer.valueOf(document.getString("myid"));
                                                    String Myid=document.getString("myid");
                                                    String DURATION_OF_RENT=document.getString("duration_of_rent");
                                                    String PARENTID=document.getString("parentid");
                                                    String CATAGORY=document.getString("category");
                                                    String public_feed=document.getString("public_feed");
                                                    product_part temp=new product_part(MODE,IMAGEURL,MOBILENO,CATAGORY, NAME, PRICE, DESCRIPTION,UID, DURATION_OF_RENT,PARENTID,Myid,Myidint,public_feed);
                                                    feed.add(temp);

                                                }
                                                Mergesortdata msd=new Mergesortdata(feed);
                                                msd.sortGivenArray();
                                                adapter adapter1;
                                                adapter1 = new adapter(msd.getSortedArray(), Drawer.this);
                                                RecyclerView recyclerView = findViewById(R.id.drawer_recyclerview);
                                                recyclerView.setHasFixedSize(true);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(Drawer.this));
                                                recyclerView.setAdapter(adapter1);

                                            } else {
                                                Toast.makeText(Drawer.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(Drawer.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

       // Toast.makeText(this,feed.size()+"size", Toast.LENGTH_SHORT).show();











        //Query query1 = productref.whereEqualTo("CATEGORY",ch1).orderBy("Myid", Query.Direction.DESCENDING);
        //Query query2 = productref.whereEqualTo("CATEGORY",ch2).orderBy("Myid", Query.Direction.DESCENDING);
      /*  Task task1=productref1.get();//.whereEqualTo("CATEGORY",ch1).orderBy("Myid", Query.Direction.DESCENDING).get();
        Task task2=productref2.get();//.whereEqualTo("CATEGORY",ch1).orderBy("Myid", Query.Direction.DESCENDING).get();
        Task<List<QuerySnapshot>> alltask= Tasks.whenAllComplete(task1,task2)
                        .addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                            @Override
                            public void onSuccess(List<QuerySnapshot> query) {
                                for(QuerySnapshot queryDocumentSnapshots: query){
                                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                        product_part feed=queryDocumentSnapshot.toObject(product_part.class);
                                        FirestoreRecyclerOptions<product_part> options = new FirestoreRecyclerOptions.Builder<product_part>().setSnapshotArray().build();
                                    adapter = new productAdapter(options, Drawer.this);
                                    RecyclerView recyclerView = findViewById(R.id.drawer_recyclerview);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Drawer.this));
                                    recyclerView.setAdapter(adapter);
                                    adapter.startListening();
                                }
                            }

                        });
     }}

      public void setuprecyclerview(product_part feed){
          FirestoreRecyclerOptions<product_part> options = new FirestoreRecyclerOptions.Builder<product_part>().setQuery(feed, product_part.class).build();
          adapter = new productAdapter(feed, Drawer.this);
          RecyclerView recyclerView = findViewById(R.id.drawer_recyclerview);
          recyclerView.setHasFixedSize(true);
          recyclerView.setLayoutManager(new LinearLayoutManager(Drawer.this));
          recyclerView.setAdapter(adapter);
          adapter.startListening();*/

      }





    @Override
    protected void onStart() {
        super.onStart();
        get_put_userDetails();
        gettoken();



    }
//
//
//     @Override
//     protected void onStop() {
//         super.onStop();
//       //  adapter.stopListening();
//     }
    private void signout() {
        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot != null) {
                                String signInmode = documentSnapshot.getString("SignInmode");
                                if(signInmode.equals("Normal"))

                                {   File cacheDirectory = getCacheDir();
                                    File applicationDirectory = new File(cacheDirectory.getParent());
                                    if (applicationDirectory.exists()) {
                                        Log.i("Delete","applicationDirectory.exists()"+applicationDirectory.toString());
                                        String[] fileNames = applicationDirectory.list();
                                        for (String fileName : fileNames) {
                                            if (!fileName.equals("lib")) {
                                                deleteFile(new File(applicationDirectory, fileName));
                                                Log.i("Deleted",fileName);
                                            }
                                        }
                                    }
                                    Toast.makeText(Drawer.this, "Log Out Successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    editor.clear();
                                    editor.commit();
                                    Intent it= new Intent(Drawer.this,MainActivity.class);
                                    startActivity(it);
                                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                }
                                else if(signInmode.equals("Google"))
                                {
                                    signOutg();
                                }
                            } else {
                                Toast.makeText(Drawer.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(Drawer.this, "Task is unsuccessfully because"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void signOutg() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        File cacheDirectory = getCacheDir();
                        File applicationDirectory = new File(cacheDirectory.getParent());
                        if (applicationDirectory.exists()) {
                            Log.i("Delete","applicationDirectory.exists()"+applicationDirectory.toString());
                            String[] fileNames = applicationDirectory.list();
                            for (String fileName : fileNames) {
                                if (!fileName.equals("lib")) {
                                    deleteFile(new File(applicationDirectory, fileName));
                                    Log.i("Deleted",fileName);
                                }
                            }
                        }
                        Toast.makeText(Drawer.this, "Log Out Successfully", Toast.LENGTH_SHORT).show();
                        editor.clear();
                        editor.commit();
                        Intent it= new Intent(Drawer.this,MainActivity.class);
                        startActivity(it);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                });

    }
    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }



    private void shownoti()
    {db.collection("Electronics and Appliances").whereEqualTo("category","Electronics and Appliances").addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
            if(e!=null){
                Toast.makeText(Drawer.this, "failed"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                return;
            }

            for(DocumentChange dc:queryDocumentSnapshots.getDocumentChanges()){
                if(dc.getType()== DocumentChange.Type.ADDED)
                {
                    Log.d("pra",dc.getDocument().getData().toString());
                    showNotification(getApplicationContext(),"HEY","A new User is Added!!!");
                }
            }

        }
    });

    }

    public static void showNotification(Context context, String title, String mess) {
        Intent intent = new Intent(context, Rent.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        //notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mess));
        notificationBuilder.setContentText(mess);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, notificationBuilder.build());

    }



    private void put_userdata_header(GoogleSignInAccount acct) {
        navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        pro_pic = headerview.findViewById(R.id.pic);
        name = headerview.findViewById(R.id.name_user);
        Email = headerview.findViewById(R.id.email_user);

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            // String Url=personPhoto.toString();
            name.setText(personName);
            Email.setText(personEmail);
            // Picasso.get().load(String.valueOf(personPhoto)).fit().into(pro_pic);
            Glide.with(this).load(String.valueOf(personPhoto)).into(pro_pic);
        }
//       name.setText(currentuser.getDisplayName());
//       Email.setText(currentuser.getEmail());
//       Glide.with(this).load(currentuser.getPhotoUrl()).into(pro_pic);

    }


    private void get_put_userDetails(){
        final String uid= mAuth.getCurrentUser().getUid();
        DocumentReference docref= db.collection("users").document(uid);
        docref.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot != null) {

                                navigationView = findViewById(R.id.nav_view);
                                View headerview = navigationView.getHeaderView(0);
                                pro_pic = headerview.findViewById(R.id.pic);
                                name = headerview.findViewById(R.id.name_user);
                                Email = headerview.findViewById(R.id.email_user);
                                name.setText(documentSnapshot.getString("Name"));
                                Email.setText(documentSnapshot.getString("Email"));
                                String url_string = documentSnapshot.getString("Image Url");
                                Glide.with(Drawer.this).load(url_string).into(pro_pic);
                            } else {
                                Toast.makeText(Drawer.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(Drawer.this, "Task is unsuccessful because "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }






}













