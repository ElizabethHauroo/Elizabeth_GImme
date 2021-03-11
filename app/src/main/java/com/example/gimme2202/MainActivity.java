package com.example.gimme2202;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

//Tutorial 17!!

public class MainActivity extends AppCompatActivity {

   // TabLayout home, add, profile;
    Button addNewPost,HomeBTN, ProfileBTN, searchBtn;
    EditText SearchInput;
    RecyclerView SearchResultList; //should be changed to postList later

    //----- About the Navigation Bar -----
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;



    private DatabaseReference profileUserRef, userRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // -------------------




        // -----   Top Gimme Static Bar  -------

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Gimme");

        // -----   Side Drawer   -------

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View navView = navigationView.inflateHeaderView(R.layout.nav_header); //storing the header View in the layout folder here
        NavProfileImage=(CircleImageView)navView.findViewById(R.id.nav_profile_image); //we have to parse Nav View here
        NavProfileUserName = (TextView)navView.findViewById(R.id.nav_fullName);



        // -----   FireBase Related Initialisation  -------
        userRef =FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth=FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    if(snapshot.hasChild("fullname")) {

                        String fullname = snapshot.child("fullname").getValue().toString(); //get the name from the DB and store it in this variable
                        NavProfileUserName.setText(fullname);
                    }

                    if(snapshot.hasChild("profileimage")) {

                        String image = snapshot.child("profileimage").getValue().toString();
                        //let's display them on the navigation view
                        Picasso.get().load(image).placeholder(R.drawable.grey_profile).into(NavProfileImage);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Profile Name does not exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }


        });






    }//onCreate

                 // --------- OUTSIDE ON CREATE METHOD -----------
                 // --------- OUTSIDE ON CREATE METHOD -----------
                 // --------- OUTSIDE ON CREATE METHOD -----------

    // --------- Check User Authentication  -----------

    @Override
    protected void onStart() {
        //if the user does not have an account, send them to the login page.

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser ==null){
           // SendUserToLogin();
        }
        else{
            checkUserExistence();
        }
    }

    private void checkUserExistence() {
        final String currentUserID = mAuth.getCurrentUser().getUid();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(currentUserID)){
                    //the user is authenticated but is not on the realtime database record
                    //sendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendUserToSetupActivity() {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();


/*
        Intent goSetup = new Intent(MainActivity.this,SetupActivity.class);
        startActivity(goSetup);

         */
    }

    private void SendUserToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LogIn.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void UserMenuSelector(MenuItem item) {
        switch(item.getItemId()){
            //enabling the user to tap on the option they want from the nav bar
            case R.id.homePage:
                Intent goHomeIntent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(goHomeIntent);
                break;

            case R.id.friendsPage:
                Toast.makeText(this, "Friends Page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profilePage:
                Intent goProfileIntent = new Intent(MainActivity.this,Profile.class);
                startActivity(goProfileIntent);
                break;

            case R.id.settingsPage:
                Toast.makeText(this, "Settings Page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logoutPage:
                //Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LogIn.class));
                finish();
                break;

        }
    }

    // --------- Hamburger toggle -----------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --------- Hopping Between Pages -----------

    private void goProfile() {
        Intent goProfileIntent = new Intent(MainActivity.this,Profile.class);
        startActivity(goProfileIntent);
    }

    private void goHome() {
        Intent goHomeIntent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(goHomeIntent);
    }

    private void AddPostActivity() {
        Intent addNewPostIntent=new Intent(MainActivity.this, Add.class);
        startActivity(addNewPostIntent);
    }
}