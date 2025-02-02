package com.example.chatify.view.activities.auth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.databinding.ActivitySetUserInfoBinding;
import com.example.chatify.model.user.Users;
import com.example.chatify.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SetUserInfoActivity extends AppCompatActivity {

    private Spinner spinnerBio;


    private ActivitySetUserInfoBinding binding;  // Binding object to access views directly.
    private ProgressDialog progressDialog;
    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;// ProgressDialog to show loading state while updating user info.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);  // Set up view binding for the layout.

        EdgeToEdge.enable(this);  // Enable edge-to-edge content to use full screen space, without status bar overlap.

        progressDialog = new ProgressDialog(this);  // Initialize ProgressDialog to show loading indicator during update.
        initBottomClick();  // Initialize button click handlers.


        //  iise code se profile photo ayega
        if (!CloudinaryHelper.INSTANCE.getStarted())CloudinaryHelper.INSTANCE.initializeConfig(this);
        CloudinaryHelper.INSTANCE.fetchThatImage(FirebaseAuth.getInstance().getCurrentUser().getUid() + "@userinfo", binding.Change1);

        // Find the Spinner in the layout
        spinnerBio = findViewById(R.id.spinner);

        // Create a list of predefined bios
        String[] predefinedBios = new String[]{
                "Or choose your own bio.",
                "I love coding and building cool apps.",
                "I am passionate about technology and learning new things.",
                "I enjoy photography and traveling the world.",
                "I'm a full-stack developer with a passion for mobile apps."
        };

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, predefinedBios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        spinnerBio.setAdapter(adapter);

    }

    private void initBottomClick() {
        // Set click listener for the "Update" button.
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Check if the username (input field) is empty.
                if (TextUtils.isEmpty(binding.phoneNumberEt2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please input username", Toast.LENGTH_SHORT).show();  // Show a toast if username is empty.
                } else {
                    doUpdate();  // Call the method to update the user information if username is valid.
                }
                if (TextUtils.isEmpty(binding.phoneNumberEt3.getText().toString())) {
                    binding.phoneNumberEt3.setText("Hey there I'M using chatify");
                    doUpdate();
                } else {
                    doUpdate();
                }
            }
        });

        // Set click listener for the "Change Profile Picture" button (currently disabled).
        binding.Change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "This Function is not ready yet", Toast.LENGTH_SHORT).show();  // Notify user that this feature is not ready.
                // Uncomment below line if functionality for picking an image is implemented.
                pickImage();  // Open image picker dialog (currently disabled).
            }
        });
    }

    // Method to update user information in Firebase Firestore.
    private void doUpdate() {
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        // Get the instance of Firebase Firestore to store user data.
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        // Get the current logged-in user from Firebase Authentication.
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {  // Check if the user is authenticated.
            String userID = firebaseUser.getUid();  // Get the unique user ID from Firebase Authentication.

            // Get the phone number input by the user from the EditText.
            String phoneNumber = binding.phoneNumberEt4.getText().toString().trim();

            // Check if the phone number is empty
            if (TextUtils.isEmpty(phoneNumber)) {
                progressDialog.dismiss();  // Dismiss the progress dialog
                Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();  // Show a toast message
                return;  // Exit the method early since phone number is required
            }

            // Create a new Users object to hold user data (including the phone number).
            Users users = new Users(
                    userID,
                    binding.phoneNumberEt2.getText().toString(),  // Username
                    phoneNumber,  // Phone number entered by the user
                    "", "", firebaseUser.getEmail(), "", "", "", binding.phoneNumberEt3.getText().toString() // Bio and other data
            );

            // Update the Firestore "Users" collection with the updated user data.
            firebaseFirestore.collection("Users")
                    .document(userID)  // Use the user ID as the document ID to store user data.
                    .set(users)  // Set the user object in the Firestore database.
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();  // Dismiss the progress dialog when the update is successful.
                            Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();  // Show success message.
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));  // Navigate to MainActivity after successful update.
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();  // Dismiss the progress dialog if the update fails.
                            Toast.makeText(getApplicationContext(), "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();  // Show failure message.
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();  // Show error message if user is null.
            progressDialog.dismiss();  // Dismiss the progress dialog if user is not authenticated.
        }
    }



    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.Change1.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imageUri != null) {
                String img_name = FirebaseAuth.getInstance().getCurrentUser().getUid() + "@userinfo";
                String filePath = CloudinaryHelper.INSTANCE.getRealPathFromURI(imageUri, this).toString();
                CloudinaryHelper.INSTANCE.uploadImage(img_name, filePath, new Function1<String, Unit>() {
                    @Override
                    public Unit invoke(String s) {
                        System.out.println(s);
                        return null;
                    }
                });

            }
        }
    }
}


