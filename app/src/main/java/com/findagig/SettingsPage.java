package com.findagig;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.findagig.MyAppGlideModule;
import com.google.firebase.storage.UploadTask;


public class SettingsPage extends AppCompatActivity {
    // CODES
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    // Tag
    private static final String TAG = "Settings";

    // Firebase variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage ;

    // User variables to be set
    private String email;
    private String imagePath;
    private String name;
    private String userUID;
    ImageView imageView;

    // Photo variables
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        uploadImage();
        // Initializing Firebase variables
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage  = FirebaseStorage.getInstance();

        if (mAuth.getCurrentUser() != null) {
            userUID = mAuth.getCurrentUser().getUid();
            getUserInfo(userUID);
        }
        else {
            Log.d(TAG, "Error getting user.");
        }
    }

    private void getImage(String userUID) throws IOException {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("avatars/" + userUID);

        Log.d(TAG, "Image path: " + pathReference.toString());

        imageView = findViewById(R.id.imageView_settings);

        // Load the image using Glide
        GlideApp.with(this)
                .load(pathReference)
                .into(imageView);
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private void getUserInfo(final String userUID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(userUID)) {
                            name = document.getData().get("name").toString();
                            email = document.getData().get("email").toString();
                            imagePath = document.getData().get("image").toString();
                            try {
                                getImage(userUID);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, document.getId() + " => " + name + ", " + email + ", " + imagePath);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                }
            });
    }

    private void uploadImage() {
        Uri file = Uri.fromFile(new File("bgapps.png"));
        UploadTask uploadTask;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageRef.child("avatars/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Image loadded: ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Image not loaded: " );
            }
        });


    }


    public void clickCamera(View view) {
        askCameraPermissions();
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, 101);
        }else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 102);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }

        }

        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
                imageView.setImageURI(contentUri);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
