package com.example.gimme2202;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup extends AppCompatActivity {

    // -------------- VARIABLES ----------------------
    private EditText UserName, FullName, CountryName;
    private Button saveInfoBTN;
    private CircleImageView profilePic;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth; //because we will need the current user ID
    private DatabaseReference UsersRef; //
    private StorageReference UserProfileImageRef; //
    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up);

        // -------------- INITIALISATION / CASTING ----------------------

        mAuth = FirebaseAuth.getInstance(); //establish a connection for this page
        currentUserID = mAuth.getCurrentUser().getUid(); //getting the unique user ID of the current user
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images"); //creating a folder in the database

        UserName = (EditText) findViewById(R.id.usernameTF_setup);
        FullName = (EditText) findViewById(R.id.fullNameTF_setup);
        CountryName = (EditText) findViewById(R.id.countryTF_setup);
        saveInfoBTN = (Button) findViewById(R.id.saveBTN_setup);
        profilePic = (CircleImageView) findViewById(R.id.profilePic_setup);
        loadingBar = new ProgressDialog(this);



        // -------------- SAVE INFO BUTTON CLICK ----------------------

        saveInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveAccountSetupInformation();
            }
        });

        // -------------- SELECT PROFILE PICTURE ----------------------
        //Once they click on the picture, they get sent to their mobile phone gallery

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); //which type of files you want to pick from the mobile phone. Only images
                startActivityForResult(galleryIntent, Gallery_Pick); //2 parameters needed, the intent, and a variable,

            }
        });
        // -------------- DATABASE REFERENCE ----------------------

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    if (snapshot.hasChild("profileimage"))
                    {
                        String image = snapshot.child("profileimage").getValue().toString();
                       Picasso.get().load(image).placeholder(R.drawable.grey_profile).into(profilePic);

                    }
                    else
                    {
                        Toast.makeText(Setup.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        }); // usersRef CLosing





      // --------------!! END OF CREATE METHOD !!----------------------
    } // --------------!! END OF CREATE METHOD !!----------------------

    private void SaveAccountSetupInformation() {

        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String country = CountryName.getText().toString();


    // validation
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please type your username...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(country))
        {
            Toast.makeText(this, "Please write your country...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap(); //storing information within the Firebase Database
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("country", country);
            userMap.put("Bio", "This is my Bio"); //hard coded for now
            //userMap.put("gender", "none");
           // userMap.put("dob", "none");
           // userMap.put("relationshipstatus", "none");

            //storing all of the above in the database
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        //SendUserToMainActivity();
                        Toast.makeText(Setup.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(Setup.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        //ideally use the code that doesn't allow the user to go back to the setup activity
    }


    // ----------- ADDING A PICTURE AND SAVING IT ------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    //if the image is picked from the gallery and the data is not null (no selected image)
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {

            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri) //ImageUri in the brackets?
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        //now we grab that cropped image to use it
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) { //resultCode?

                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg"); //saving the name of the new cropped picture with their unique id as a jpg

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl=uri.toString();
                                UsersRef.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent selfIntent = new Intent(Setup.this, Setup.class);
                                            startActivity(selfIntent);
                                            Toast.makeText(Setup.this, "Image Stored", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else{
                                            String message = task.getException().getMessage();
                                            Toast.makeText(Setup.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                            }
                        });


                    }
                });
            } else {
                Toast.makeText(this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }

    }
    // ----------- ADDING A PICTURE AND SAVING IT ------------ //




    }


// -------------- END OF FILE ----------------------
