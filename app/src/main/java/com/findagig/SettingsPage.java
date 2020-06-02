package com.findagig;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.firebase.storage.StorageException;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.findagig.MyAppGlideModule;
import com.google.firebase.storage.UploadTask;


public class SettingsPage extends AppCompatActivity {
    // CODES
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    // Tag
    private static final String TAG = "Settings";

    // Elements from XML
    EditText email_et;
    EditText password_et;
    EditText username_et;
    ImageView imageView_settings;
    Button saveButton;
    ProgressDialog progressDialog;

    // Firebase variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage ;

    // User variables to be set
    private String email;
    private String password;
    private String imagePath;
    private String name;
    private String wallet;
    private String userUID;

    // Photo variables
    String currentPhotoPath;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        // Initializing Firebase variables
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage  = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initializing edit texts and buttom
        email_et = findViewById(R.id.username);
        password_et = findViewById(R.id.password);
        username_et = findViewById(R.id.nickname);
        saveButton = findViewById(R.id.submit_btn);

        if (mAuth.getCurrentUser() != null) {
            userUID = mAuth.getCurrentUser().getUid();
            getUserInfo(userUID);
        }
        else {
            Log.d(TAG, "Error getting user.");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserSettings();
            }
        });
    }

    private void getImage(String userUID) throws IOException {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("avatars/" + userUID);

        Log.d(TAG, "Image path: " + pathReference.toString());
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pathReference.toString());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "File exists");
                loadImage();
            }
        });
    }

    public void loadImage()
    {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("avatars/" + userUID);

        Log.d(TAG, "Image path: " + pathReference.toString());
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pathReference.toString());

        imageView_settings = findViewById(R.id.imageView_settings);

        // Load the image using Glide
        GlideApp.with(this)
                .load(pathReference)
                .into(imageView_settings);
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
                            password = document.getData().get("password").toString();
                            imagePath = document.getData().get("image").toString();
                            wallet = document.getData().get("wallet").toString();

                            email_et.setText(email);
                            username_et.setText(name);
                            password_et.setText(password);

                            try {
                                getImage(userUID);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, document.getId() + " => " + name + ", " + email + ", " + imagePath);
                            progressDialog.dismiss();
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                }
            });
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "I do not have permissions");

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            Log.d(TAG, "I have permissions");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "ON ACTIVITY RESULT");

        if(requestCode == 1 ){
            Log.d(TAG, "CAMERA_REQUEST_CODE");

            if(resultCode == Activity.RESULT_OK){
                Log.d(TAG, "ok");

                File f = new File(currentPhotoPath);
                imageView_settings = findViewById(R.id.imageView_settings);

                imageView_settings.setImageURI(Uri.fromFile(f));
                Log.d(TAG, "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                Log.d(TAG, " => Before Uploaded" + contentUri);

                uploadImageToFirebase(mAuth.getCurrentUser().getUid(), contentUri);
            }
        }

    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        Log.d(TAG, " => Uploaded :" + contentUri);

        final StorageReference imageRef = storageReference.child("avatars/" + name);
        imageRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });

                Toast.makeText(SettingsPage.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsPage.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "Entered dispatchTakePictureIntent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d(TAG, "Created image file");

            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "Entered photo!=null");

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.findagig",
                        photoFile);

                Log.d(TAG, "=>  ---" + photoURI.toString());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void clickCamera(View view) {
        Log.d(TAG, "Clicked camera icon.");
        askCameraPermissions();
    }

    public void saveUserSettings() {
        final FirebaseFirestore db =  FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("email", email_et.getText().toString());
        user.put("image", "https://www.pavilionweb.com/wp-content/uploads/2017/03/man-300x300.png");
        user.put("name", username_et.getText().toString());
        user.put("password", password_et.getText().toString());
        user.put("wallet", wallet);
        Log.d(TAG, "ADDDING THIS USER: " + user.toString());

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(SettingsPage.this, "User information was updated.",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(SettingsPage.this, "User information was not updated, check you connection!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
