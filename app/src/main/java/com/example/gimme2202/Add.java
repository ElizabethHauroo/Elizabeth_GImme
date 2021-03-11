package com.example.gimme2202;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Add extends AppCompatActivity {

    ImageButton selectPostImage;
    Button addWishBTN;
    EditText title, description, link;
    final static int Gallery_Pick=1;
    private Uri ImageUri;

    String titleTF;
    String descriptionTF;
    String linkTF;

    ProgressBar progressBar;
    public static final int DELAY_MILLIS = 5000;

    private StorageReference PostImagesRef;
    private String saveCurrentDate, saveCurrentTime, postRandomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        selectPostImage = (ImageButton)findViewById(R.id.postPic_add);
        addWishBTN = (Button)findViewById(R.id.addBTN_add);
        title = (EditText)findViewById(R.id.titleTF_add);
        description = (EditText)findViewById(R.id.descriptionTF_add);
        link = (EditText)findViewById(R.id.linkTF_add);
        PostImagesRef = FirebaseStorage.getInstance().getReference();


        selectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        addWishBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePostInfo();
            }
        });


    }

    private void validatePostInfo() {
        titleTF = title.getText().toString();
        descriptionTF = description.getText().toString();
        linkTF = link.getText().toString();


        if(TextUtils.isEmpty(titleTF)){
            Toast.makeText(this, "Please add a Title", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(descriptionTF)){
            Toast.makeText(this, "Please add a short description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(linkTF)){
            Toast.makeText(this, "Please add a Link", Toast.LENGTH_SHORT).show();
        }
        else{
           StoringImageToFireBaseStorage();

        }
        

    }



    
    private void StoringImageToFireBaseStorage() {
        //give a random/unique name to the entry to avoid duplicate on firebase (Tutorial20)
        //here we are getting the current date and time and adding that onto the file's original name making it almost impossible to have a duplicate on the firebase storage
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime=currentTime.format(callForDate.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = PostImagesRef.child("Wish Pictures").child(ImageUri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() { //this stores to firebase db
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) { //tells the user if succesful or not
                if (task.isSuccessful()) {
                    Toast.makeText(Add.this, "Image Sucessfully Added", Toast.LENGTH_SHORT).show();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(Add.this, "Error Occured" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //this code allows the user to pick an image from the phone's gallery
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*"); //pick only images from the phone
        startActivityForResult(galleryIntent, Gallery_Pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode ==RESULT_OK && data!=null); //save in firebase storage
        ImageUri = data.getData();
        selectPostImage.setImageURI(ImageUri);
    }
}