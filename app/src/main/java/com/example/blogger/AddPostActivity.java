package com.example.blogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import Data.ListData;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextInputLayout textInputLayoutTitle, textInputLayoutDesc;
    private Button uploadButton;

    //FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser user;


    private String Title, Desc;
    private ProgressDialog progressDialog;
    private final int MY_REQUEST_CODE = 1;
    private Uri imageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        progressDialog = new ProgressDialog(this);

        imageButton = (ImageButton) findViewById(R.id.imageButtonID);
        uploadButton = (Button) findViewById(R.id.uploadPostButtonID);
        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.textInputLayTitleID);
        textInputLayoutDesc = (TextInputLayout) findViewById(R.id.textInputLayDescrID);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("MyBlog");
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, MY_REQUEST_CODE);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Title = textInputLayoutTitle.getEditText().getText().toString().trim();
                Desc = textInputLayoutDesc.getEditText().getText().toString().trim();

                if ( !Title.isEmpty() && !Desc.isEmpty() && imageURI != null){

                    progressDialog.setMessage("Your blog is posting...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    // start posting the post
                    StorageReference filepath = storageReference.child("MyBlog").child(imageURI.getLastPathSegment());
                    filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                           DatabaseReference newPostData = databaseReference.push();

                           Map<String,String> dataToSave = new HashMap<>();
                            dataToSave.put("desc", Desc);
                            dataToSave.put("imageurl", String.valueOf(downloadUrl));
                            dataToSave.put("title", Title);

                           newPostData.setValue(dataToSave);
                           progressDialog.dismiss();
                           startActivity(new Intent(AddPostActivity.this, MainList.class));
                        }
                    });
                        }

                else {
                    if (!Title.isEmpty())
                        textInputLayoutTitle.setError(null);
                    else
                        textInputLayoutTitle.setError("Enter title");

                    if (!Desc.isEmpty())
                        textInputLayoutDesc.setError(null);
                    else
                        textInputLayoutDesc.setError("Enter Description");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK){

            imageURI =  data.getData();
            imageButton.setImageURI(imageURI);
        }
    }
}