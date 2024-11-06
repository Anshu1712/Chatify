package com.example.chatify.view.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.chatify.R;
import com.example.chatify.databinding.ActivityProfileBinding;
import com.example.chatify.view.setting.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    // Declare the binding object that links the layout (activity_profile.xml) with this activity
    private ActivityProfileBinding binding;

    // Declare the ImageView for the back button
    private ImageView backButton1;

    // Declare FirebaseUser object to get information about the current authenticated user
    private FirebaseUser firebaseUser;

    // Declare Firestore instance to interact with Firebase Firestore
    private FirebaseFirestore firestore;

    // Declare the toolbar object
    private Toolbar toolbar;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the binding object using DataBindingUtil to link layout and activity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        // Call a method to set up edge-to-edge UI, adjusting for system bars (status bar, navigation bar)
        setupEdgeToEdgeDisplay();

        bottomSheetDialog = new BottomSheetDialog(this);

        // Initialize the back button (ImageView) from the layout
        backButton1 = binding.backButton2;

        // Initialize the toolbar from the layout, assuming there's a Toolbar in the layout
        toolbar = binding.toolbar4;

        // Set the toolbar as the action bar (top navigation bar)
        setSupportActionBar(toolbar);

        // Set a click listener on the back button to handle back navigation
        backButton1.setOnClickListener(v -> navigateBackToMainActivity());

        // Initialize FirebaseAuth and Firestore instances to interact with Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        // If the user is authenticated, fetch their information from Firestore
        if (firebaseUser != null) {
            getUserInfo();  // Fetch and display user info from Firestore.
        }

        initActionClick();
    }

    private void initActionClick() {
        binding.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButtomSheetPickPhoto();
            }
        });
    }

    private void showButtomSheetPickPhoto() {
        View view = getLayoutInflater().inflate(R.layout.botton_sheet, null);
        bottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });
        bottomSheetDialog.show();
    }

    // This method is responsible for handling edge-to-edge UI to provide a modern immersive experience
    private void setupEdgeToEdgeDisplay() {
        // Set on apply window insets listener on the root layout (main container) of the activity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Get system bars insets (status bar, navigation bar)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Set the padding of the layout to avoid the system bars covering the UI elements
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // This method retrieves user data from Firebase Firestore
    private void getUserInfo() {
        // Access the Firestore collection "Users" and get the document corresponding to the current user's UID
        firestore.collection("Users").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // If the document exists, retrieve the data
                        if (documentSnapshot.exists()) {
                            // Get the username and phone number from the Firestore document
                            String userName = documentSnapshot.getString("username");
                            String userPhone = documentSnapshot.getString("userPhone");

                            // Set the username and phone number to the corresponding TextViews in the layout
                            binding.nameProfile.setText(userName);
                            binding.phone.setText(userPhone);
                        } else {
                            // If the document doesn't exist, log an error
                            showError("User data not found");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // If there's a failure when fetching data from Firestore, log the error
                    showError("Failed to load user info");
                });
    }

    // This method displays an error message (in this case, logs it to Logcat)
    private void showError(String message) {
        // Log the error message to Logcat with a tag for easy identification
        android.util.Log.e("ProfileActivity", message);
    }

    @Override
    public void onBackPressed() {
        // Override the back button behavior to also call the navigateBackToMainActivity method
        super.onBackPressed();
        navigateBackToMainActivity();
    }

    // This method handles the navigation back to the SettingsActivity
    private void navigateBackToMainActivity() {
        // Create an intent to navigate to the SettingsActivity (not MainActivity as implied by the method name)
        Intent intent = new Intent(Profile.this, SettingsActivity.class);

        // Add flags to clear the activity stack and start a new task to avoid returning to the current activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the activity and finish the current one (Profile activity)
        startActivity(intent);
        finish();
    }
}
