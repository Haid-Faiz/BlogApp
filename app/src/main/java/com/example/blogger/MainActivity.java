package com.example.blogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import Data.ListData;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button logInBtn, createAcBtn;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;

    TextInputLayout textInputLayoutEmail, textInputLayoutPass;
    TextInputEditText textInputEditTextEmail, textInputEditTextPass;
    String Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        createAcBtn = (Button) findViewById(R.id.createAcID);
        createAcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.TextInputLayEmailID);
        textInputLayoutPass = (TextInputLayout) findViewById(R.id.TextInputLayPassID);
//        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.EmailEdittextID);
//        textInputEditTextPass = (TextInputEditText) findViewById(R.id.PassEditID);

        logInBtn = (Button) findViewById(R.id.loginBtnID);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Email = textInputLayoutEmail.getEditText().getText().toString().trim();
                Password = textInputLayoutPass.getEditText().getText().toString().trim();

                if ( !Email.isEmpty() && !Password.isEmpty() ) {

                    textInputLayoutEmail.setError(null);
                    textInputLayoutPass.setError(null);

                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Logging...");

                        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    progressDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, MainList.class));
                                    finish();
                                } else
                                    Toast.makeText(MainActivity.this, "Failed SignIn", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                else {
                    if (!Email.isEmpty())
                        textInputLayoutEmail.setError(null);
                    else
                        textInputLayoutEmail.setError("Enter Email");

                    if (!Password.isEmpty())
                        textInputLayoutPass.setError(null);
                    else
                        textInputLayoutPass.setError("Enter Password");
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (authStateListener != null)
//            auth.removeAuthStateListener(authStateListener);
    }
}