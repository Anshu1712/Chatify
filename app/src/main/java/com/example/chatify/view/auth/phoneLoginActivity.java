package com.example.chatify.view.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.chatify.R;
import com.example.chatify.databinding.ActivityPhoneLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class phoneLoginActivity extends AppCompatActivity {

    private static final String TAG = "PhoneLoginActivity";
    private ActivityPhoneLoginBinding binding;

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;


    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(this, SetUserInfoActivity.class));

        }
        progressDialog = new ProgressDialog(this);

        // Set up button click listener
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = binding.button.getText().toString();
                if (buttonText.equals("Next")) {
                    // Format the phone number correctly (E.164 format)
                    String phone =  "+91" + binding.phoneNumberEt2.getText().toString();

                    // Ensure the phone number is valid
                    if (isPhoneNumberValid(phone)) {
                        progressDialog.setMessage("Verifying Phone Number...");
                        progressDialog.show();
                        startPhoneNumberVerification(phone);
                    } else {
                        Toast.makeText(phoneLoginActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    }
                } else if (buttonText.equals("Confirm")) {
                    String code = binding.phoneNumberEt3.getText().toString();
                    if (isCodeValid(code)) {
                        verifyPhoneNumberWithCode(mVerificationId, code);
                    } else {
                        Toast.makeText(phoneLoginActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set up the verification callbacks
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: Complete");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: " + e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(phoneLoginActivity.this, "Verification failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: Verification code sent.");
                mVerificationId = verificationId;
                mResendToken = token;
                binding.button.setText("Confirm");
                progressDialog.dismiss();
                Toast.makeText(phoneLoginActivity.this, "Verification code sent. Please check your phone.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    // Start the phone number verification process
    private void startPhoneNumberVerification(String phoneNumber) {
        progressDialog.setMessage("Sending code to phone number...");
        progressDialog.show();

        // Make sure phone number is in the correct E.164 format
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+1" + phoneNumber;  // Add default country code if missing (e.g., for US)
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,  // Timeout duration in seconds
                TimeUnit.SECONDS,
                this,
                mCallback
        );
    }

    // Verify the phone number with the code entered by the user
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    // Sign in the user with the provided phone authentication credential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(phoneLoginActivity.this, SetUserInfoActivity.class));
//                            if (user != null) {
//                                String userID = user.getUid();
//                                Users users = new Users(userID,
//                                        "",
//                                        user.getPhoneNumber(),
//                                        "",
//                                        "",
//                                        "",
//                                        "",
//                                        "",
//                                        "",
//                                        "");
//
//                                firestore.collection("Users")
//                                        .document("userInfo")
//                                        .collection(userID).add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                startActivity(new Intent(phoneLoginActivity.this, SetUserInfoActivity.class));
//                                            }
//                                        });
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
//                                //startActivity(new Intent(phoneLoginActivity.this, SetUserInfoActivity.class));
//                            }
                            // Update UI or proceed to the next activity
                            Toast.makeText(phoneLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.d(TAG, "Error Message: " + task.getException().getMessage());
                            }
                            Toast.makeText(phoneLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Check if the phone number is valid
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() >= 10;  // Adjust validation as needed
    }

    // Check if the verification code is valid
    private boolean isCodeValid(String code) {
        return code != null && code.length() == 6;  // Typically, verification code is 6 digits
    }
}
