//package com.example.chatify.view.activities.auth;  // Declares the package where this activity belongs (authentication part).
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;
//
//import com.example.chatify.R;
//import com.example.chatify.databinding.ActivityPhoneLoginBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.concurrent.TimeUnit;
//
//public class phoneLoginActivity extends AppCompatActivity {
//
//    private static final String TAG = "PhoneLoginActivity";  // TAG for logging messages.
//    private ActivityPhoneLoginBinding binding;  // The variable that holds the view binding object.
//
//    private FirebaseAuth mAuth;  // The FirebaseAuth instance used for authentication operations.
//    private String mVerificationId;  // The verification ID sent via SMS for phone verification.
//    private PhoneAuthProvider.ForceResendingToken mResendToken;  // The token used to resend verification code.
//    private ProgressDialog progressDialog;  // ProgressDialog to show loading spinner while verifying.
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;  // Callback methods for phone verification states.
//
//    private FirebaseUser firebaseUser;  // Represents the current Firebase authenticated user.
//    private FirebaseFirestore firestore;  // Firestore instance for interacting with Firebase Firestore database.
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);  // Using DataBinding to bind the XML views to the activity.
//
//        mAuth = FirebaseAuth.getInstance();  // Initializes FirebaseAuth instance for user authentication.
//        firestore = FirebaseFirestore.getInstance();  // Initializes Firestore instance for database operations.
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();  // Gets the current logged-in Firebase user, if any.
//
//        if (firebaseUser != null) {  // If the user is already logged in (firebaseUser is not null):
//            startActivity(new Intent(this, SetUserInfoActivity.class));  // Start the SetUserInfoActivity.
//            finish();  // Finish the current activity so the user can't return to the phone login screen.
//        }
//
//        progressDialog = new ProgressDialog(this);  // Initialize the ProgressDialog to show loading state.
//        progressDialog.setCancelable(false);  // Prevent the dialog from being dismissed by the back button or touch outside.
//
//        // Set the onClickListener for the button in the layout.
//        binding.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String buttonText = binding.button.getText().toString();  // Get the text of the button to decide whether it's "Next" or "Confirm".
//
//                if (buttonText.equals("Next")) {  // If the button text is "Next", start the phone verification process.
//                    String phone = "+91" + binding.phoneNumberEt2.getText().toString();  // Get the phone number entered by the user and prepend the country code (+91 for India).
//
//                    if (isPhoneNumberValid(phone)) {  // Check if the phone number is valid (length check).
//                        progressDialog.setMessage("Verifying Phone Number...");  // Set message for the progress dialog.
//                        progressDialog.show();  // Show the progress dialog.
//                        startPhoneNumberVerification(phone);  // Start the phone number verification process.
//                    } else {
//                        Toast.makeText(phoneLoginActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();  // Show a toast if the phone number is invalid.
//                    }
//                } else if (buttonText.equals("Confirm")) {  // If the button text is "Confirm", verify the code entered by the user.
//                    String code = binding.phoneNumberEt3.getText().toString();  // Get the verification code entered by the user.
//                    if (isCodeValid(code)) {  // Check if the verification code is valid (6 digits long).
//                        verifyPhoneNumberWithCode(mVerificationId, code);  // Verify the phone number with the entered code.
//                    } else {
//                        Toast.makeText(phoneLoginActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();  // Show a toast if the verification code is not valid.
//                    }
//                }
//            }
//        });
//
//        // Callback methods to handle various verification states.
//        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Log.d(TAG, "onVerificationCompleted: Complete");  // Log that the verification is complete.
//                signInWithPhoneAuthCredential(phoneAuthCredential);  // Sign in with the phone authentication credentials.
//                progressDialog.dismiss();  // Dismiss the progress dialog after verification.
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Log.d(TAG, "onVerificationFailed: " + e.getMessage());  // Log the error message in case verification fails.
//                progressDialog.dismiss();  // Dismiss the progress dialog.
//                String errorMessage = e.getMessage();  // Get the error message from the exception.
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {  // If the exception is related to invalid credentials:
//                    errorMessage = "Invalid phone number format.";  // Set a specific error message.
//                }
//                Toast.makeText(phoneLoginActivity.this, "Verification failed: " + errorMessage, Toast.LENGTH_LONG).show();  // Show a toast with the error message.
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                Log.d(TAG, "onCodeSent: Verification code sent.");  // Log that the verification code has been sent.
//                mVerificationId = verificationId;  // Save the verification ID for later use.
//                mResendToken = token;  // Save the token for resending the verification code if needed.
//                binding.button.setText("Confirm");  // Change the button text to "Confirm" for entering the verification code.
//                progressDialog.dismiss();  // Dismiss the progress dialog after the code is sent.
//                Toast.makeText(phoneLoginActivity.this, "Verification code sent. Please check your phone.", Toast.LENGTH_SHORT).show();  // Show a toast notifying the user to check their phone.
//            }
//        };
//    }
//
//    // Starts the phone number verification process by sending a code.
//    private void startPhoneNumberVerification(String phoneNumber) {
//        progressDialog.setMessage("Sending code to phone number...");  // Set the message on the progress dialog.
//        progressDialog.show();  // Show the progress dialog.
//
//        if (!phoneNumber.startsWith("+")) {  // If the phone number does not start with "+" (country code):
//            phoneNumber = "+91" + phoneNumber;  // Prepend the country code (+91 for India).
//        }
//
//        // Use Firebase PhoneAuthProvider to initiate phone number verification.
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,  // The phone number to verify.
//                60,  // Timeout duration in seconds.
//                TimeUnit.SECONDS,  // Time unit for the timeout.
//                this,  // The activity context.
//                mCallback  // The callback to handle different verification states.
//        );
//    }
//
//    // Verifies the phone number with the entered verification code.
//    private void verifyPhoneNumberWithCode(String verificationId, String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);  // Create the PhoneAuthCredential using the verification ID and entered code.
//        signInWithPhoneAuthCredential(credential);  // Call method to sign in with the credential.
//    }
//
//    // Signs in with the provided PhoneAuthCredential.
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)  // Attempt to sign in using the provided credential.
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {  // If sign-in is successful:
//                            progressDialog.dismiss();  // Dismiss the progress dialog.
//                            Log.d(TAG, "signInWithCredential:success");  // Log the success.
//                            FirebaseUser user = task.getResult().getUser();  // Get the authenticated user.
//                            startActivity(new Intent(phoneLoginActivity.this, SetUserInfoActivity.class));  // Start the SetUserInfoActivity.
//                            Toast.makeText(phoneLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();  // Show a toast for success.
//                        } else {
//                            progressDialog.dismiss();  // Dismiss the progress dialog.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());  // Log the failure.
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {  // If the exception is invalid credentials:
//                                Log.d(TAG, "Error Message: " + task.getException().getMessage());  // Log the error message.
//                            }
//                            Toast.makeText(phoneLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();  // Show a toast for authentication failure.
//                        }
//                    }
//                });
//    }
//
//    // Validates the phone number (should be 13 characters long, including country code).
//    private boolean isPhoneNumberValid(String phoneNumber) {
//        return phoneNumber != null && phoneNumber.length() == 13;  // Checks if the phone number is valid.
//    }
//
//    // Validates the verification code (should be 6 digits long).
//    private boolean isCodeValid(String code) {
//        return code != null && code.length() == 6;  // Checks if the code is valid.
//    }
//}
