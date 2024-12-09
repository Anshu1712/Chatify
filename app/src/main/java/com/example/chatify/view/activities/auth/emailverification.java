package com.example.chatify.view.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatify.R;
import com.example.chatify.model.user.Users;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class emailverification extends AppCompatActivity {

    private EditText email;
    private MaterialButton registerButton;
    private FirebaseFirestore firestore;
    private EditText password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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

        auth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //sendVerificationEmail();
                        firestore.collection("Users").document(auth.getCurrentUser().getUid()).set(new Users(auth.getCurrentUser().getUid(),"name","","","","","","","",""))
                                .addOnSuccessListener(unused -> {
                                });
                        Intent intent = new Intent(this,login.class);
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