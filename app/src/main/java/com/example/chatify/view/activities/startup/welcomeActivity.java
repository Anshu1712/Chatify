package com.example.chatify.view.activities.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatify.R;
import com.example.chatify.view.activities.auth.phoneLoginActivity;
import com.google.android.material.button.MaterialButton;

public class welcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        MaterialButton btnAgree = findViewById(R.id.button);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(welcomeActivity.this, phoneLoginActivity.class));
            }
        });
       
    }
}