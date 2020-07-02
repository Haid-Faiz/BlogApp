package com.example.blogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateAccount extends AppCompatActivity {

    Button LoginBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        LoginBackButton = (Button) findViewById(R.id.logInBackID);
        LoginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(CreateAccount.this, MainActivity.class));
                   finish();
            }
        });

    }
}