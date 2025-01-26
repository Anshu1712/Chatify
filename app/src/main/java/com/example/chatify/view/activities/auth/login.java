package com.example.chatify.view.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatify.R;
import com.example.chatify.view.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText email;
    private MaterialButton registerButton;
    private EditText password;
    private FirebaseAuth auth;

    private MaterialButton sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        sign = findViewById(R.id.singup);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        sign.setOnClickListener((v)->{
            Intent intent = new Intent(this, emailverification.class);
            startActivity(intent);
            finish();
        });


        registerButton = findViewById(R.id.verify);
        email = findViewById(R.id.email);
        password = findViewById(R.id.passcode);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email.getText().toString();
                String pass = password.getText().toString();
                registerUser(emailAddress, pass);
            }
        });
    }

    private void registerUser(String emailAddress, String password) {

        auth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //sendVerificationEmail();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                    }
                });
    }

    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {

                    }
                });
    }
}