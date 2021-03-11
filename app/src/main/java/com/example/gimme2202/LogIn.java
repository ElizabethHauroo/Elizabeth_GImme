package com.example.gimme2202;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
    TextView LGoCreate;
    EditText email, password;
    Button loginBTN;
    FirebaseAuth fAuth;
    ProgressDialog loadingBar;


    protected void onStart(){
        super.onStart();
       /*if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            //If the user is already logged in on their phone, then bring them straight to the Dashboard before loading this page
        }*/
        FirebaseUser currentUser = fAuth.getCurrentUser();

        if(currentUser!=null){
            SendUserToMainActivity();
        }


    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LogIn.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // -----   Initialise   -------

        LGoCreate = findViewById(R.id.LgoCreate);
        email = findViewById(R.id.LemailTF);
        password = findViewById(R.id.lPswdTF);
        loginBTN = findViewById(R.id.LlogInBTN);
        fAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        // -----   Initialise   -------



        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract text from the fields and check that they aren't empty

                if (email.getText().toString().isEmpty()) {
                    email.setError("Email is missing");
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Password is missing");
                } else {

                    loadingBar.setTitle("Login In..");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true); //box will not disappear until it authenticates the user, even if tapped

                    //Login
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //login Successful
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            loadingBar.dismiss();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
                }
            }
        });

        // -----   Go to the Create Account Page   -------

        LGoCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


    }

    // -----  Outside of OnCreate   -------



}