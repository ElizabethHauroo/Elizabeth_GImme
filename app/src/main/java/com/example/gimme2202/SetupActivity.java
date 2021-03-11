package com.example.gimme2202;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText userName, fullName, countryName;
    private Button saveInfoBTN;
    private CircleImageView profilePic;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    String currentUserID;
    ProgressDialog loadingBar;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // --------  Initialize  ----------

        userName=(EditText)findViewById(R.id.usernameTF_setup);
        fullName=(EditText)findViewById(R.id.fullNameTF_setup);
        countryName=(EditText)findViewById(R.id.countryTF_setup);
        saveInfoBTN = (Button)findViewById(R.id.saveBTN_setup);
        profilePic = (CircleImageView)findViewById(R.id.profilePic_setup);
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        currentUserID = mAuth.getCurrentUser().getUid();
        loadingBar = new ProgressDialog(this);


        // --------  Initialize  ----------
        
        saveInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // SaveAccountSetupInfo();
            }
        });

    }

    // --------  OUTSIDE OF CREATE METHOD  ----------
    // --------  OUTSIDE OF CREATE METHOD  ----------
    // --------  OUTSIDE OF CREATE METHOD  ----------

/*
    private void SaveAccountSetupInfo() {


        String username = userName.getText().toString();
        String fullname = fullName.getText().toString();
        String country = countryName.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please Enter a Username", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please Enter your Full Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(country)){
            Toast.makeText(this, "Please Enter a country", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please Wait while we are creating your account.");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true); //box will not disappear until it authenticates the user, even if tapped


            HashMap userMap = new HashMap(); //with the help of this we can store onto the firebase database
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("country", country);
            userMap.put("bio", "Hi this is my bio hard-coded in. SetupActivity"); //he used status here
            userMap.put("gender", "None");
            userMap.put("D.O.B", "None");

            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        sendUsertoMainActivity();
                        Toast.makeText(SetupActivity.this, "Your account is created Successfully", Toast.LENGTH_LONG).show();

                        loadingBar.dismiss();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occurred: "+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });


        }



    }//saveAccountSetup

 */

    private void sendUsertoMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }



}