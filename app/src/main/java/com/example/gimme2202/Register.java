package com.example.gimme2202;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;

public class Register extends AppCompatActivity {

    // --------  Declare Variables  ----------

    EditText rFullName, rEmail, rPassword, rConfirmPassword;
    Button rRegisterBTN;
    TextView RgoLogin;
    FirebaseAuth fAuth;
    ProgressDialog loadingBar;

    // --------  Declare Variables  ----------


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
        setContentView(R.layout.activity_register);

        // --------  Initialize  ----------

        rFullName = findViewById(R.id.RnameTF);
        rEmail = findViewById(R.id.RemailTF);
        rPassword = findViewById(R.id.rPswdTF);
        rConfirmPassword = findViewById(R.id.rPswdTF2);
        rRegisterBTN = findViewById(R.id.RsignUpBTN);
        RgoLogin = findViewById(R.id.RgoLogin);
        fAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        // --------  Initialize  ----------
        /*
        if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //send them to the main activity
            //If the user is already logged in, then we bypass all of the below code and send them straight to the main activity.
        }

         */

        // --------  Create the Account  ----------
        rRegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();

            }
        });



        RgoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));
            }
        });

    } // end of CREATE



    private void CreateNewAccount() {
        //extract data in the fields when register button is clicked
        String fullName= rFullName.getText().toString();
        String email = rEmail.getText().toString();
        String password = rPassword.getText().toString();
        String confirmPassword = rConfirmPassword.getText().toString();

        //Making sure that no fields are left blank

        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(Register.this, "please enter your name", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(Register.this, "Please Enter Email", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(Register.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(Register.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();

        }

        //Now we need to make sure that both passwords are the same, and at least 6 characters long
        else if(!password.equals(confirmPassword)){
            Toast.makeText(Register.this, "Your passwords do no match", Toast.LENGTH_SHORT).show();
        }
        else if(password.length()<6){
            Toast.makeText(Register.this, "Password needs to be at least 6 character", Toast.LENGTH_SHORT).show();
        }

        //at this stage, the data is validated, and passwords are matching.
        //Now we need to Register the User to Firebase

        else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true); //box will not disappear until it authenticates the user, even if tapped

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(),Setup.class));
                        Toast.makeText(Register.this, "You are authenticated Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(Register.this, "Error Occurred"+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });

        }
    }


/*
        RgoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));
            }
        });

 */



    private void SendUserToSetupActivity() {
        /*Intent setupIntent = new Intent(Register.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish(); //user can't come back to this page using the back button

         */

        Intent goSetup = new Intent(Register.this,Setup.class);
        startActivity(goSetup);

    }


}//final final bracket