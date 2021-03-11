package com.example.gimme2202;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomePage extends AppCompatActivity {

    //variables
    Button wSignUpBTN, wLoginBTN;
    FirebaseAuth fAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            //If the user is already logged in on their phone, then bring them straight to the Dashboard before loading this page
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        wSignUpBTN = findViewById(R.id.wSignUpBTN);
        wLoginBTN = findViewById(R.id.wloginBTN);



        wSignUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        wLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });










    } //second last close. Closes onCreate
}