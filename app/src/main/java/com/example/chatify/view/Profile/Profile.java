//package com.example.chatify.view.Profile;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityOptionsCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.databinding.DataBindingUtil;
//
//import com.example.chatify.R;
//import com.example.chatify.common.Common;
//import com.example.chatify.databinding.ActivityProfileBinding;
//import com.example.chatify.display.ViewImageActivity;
//import com.example.chatify.view.setting.SettingsActivity;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.Objects;
//
//public class Profile extends AppCompatActivity {
//
//    // Declare the binding object that links the layout (activity_profile.xml) with this activity
//    private ActivityProfileBinding binding;
//
//    // Declare the ImageView for the back button
//    private ImageView backButton1;
//
//    // Declare FirebaseUser object to get information about the current authenticated user
//    private FirebaseUser firebaseUser;
//
//    // Declare Firestore instance to interact with Firebase Firestore
//    private FirebaseFirestore firestore;
//
//    // Declare the toolbar object
//    private Toolbar toolbar;
//
//    private BottomSheetDialog bottomSheetDialog, bsDialogEditName;
//
//    private int IMAGE_GALLERY_REQUEST = 111;
//    private Uri imageUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Initialize the binding object using DataBindingUtil to link layout and activity
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
//
//        // Call a method to set up edge-to-edge UI, adjusting for system bars (status bar, navigation bar)
//        setupEdgeToEdgeDisplay();
//
//        bottomSheetDialog = new BottomSheetDialog(this);
//
//        // Initialize the back button (ImageView) from the layout
//        backButton1 = binding.backButton2;
//
//        // Initialize the toolbar from the layout, assuming there's a Toolbar in the layout
//        toolbar = binding.toolbar4;
//
//        // Set the toolbar as the action bar (top navigation bar)
//        setSupportActionBar(toolbar);
//
//        // Set a click listener on the back button to handle back navigation
//        backButton1.setOnClickListener(v -> navigateBackToMainActivity());
//
//        // Initialize FirebaseAuth and Firestore instances to interact with Firebase
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firestore = FirebaseFirestore.getInstance();
//
//        // If the user is authenticated, fetch their information from Firestore
//        if (firebaseUser != null) {
//            getUserInfo();  // Fetch and display user info from Firestore.
//        }
//
//        initActionClick();
//    }
//
//    private void initActionClick() {
//        binding.fabCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showButtomSheetPickPhoto();
//            }
//        });
//        binding.nameEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showButtomSheetEditName();
//            }
//        });
//        binding.Change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.Change.invalidate();
//                Drawable dr = binding.Change.getDrawable();
//                Common.IMAGE_BITMAP = ((GlideBitmapDrawable) dr.getCurrent()).getBitmap();
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation(Profile.this, binding.Change, "Name");
//                Intent intent = new Intent(Profile.this, ViewImageActivity.class);
//                startActivity(intent,activityOptionsCompat.toBundle());
//            }
//        });
//    }
//
//
//    private void showButtomSheetPickPhoto() {
//
////        ((View) view.findViewById(R.id.gallery1)).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                openGallery();
////                bottomSheetDialog.dismiss();
////            }
////        });
////        ((View) view.findViewById(R.id.gallery1)).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(), "Camera Clicked", Toast.LENGTH_SHORT).show();
////                bottomSheetDialog.dismiss();
////            }
////        });
//        if (bottomSheetDialog == null) {
//            bottomSheetDialog = new BottomSheetDialog(this);
//        }
//        View view = getLayoutInflater().inflate(R.layout.botton_sheet, null);
//        bottomSheetDialog.setContentView(view);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                bottomSheetDialog = null;
//            }
//        });
//        bottomSheetDialog.show();
//    }
//
//    private void showButtomSheetEditName() {
//        if (bottomSheetDialog == null) {
//            bottomSheetDialog = new BottomSheetDialog(this);
//        }
//        View view = getLayoutInflater().inflate(R.layout.botton_sheet_edit_name, null);
//
//        ((View) view.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bsDialogEditName.dismiss();
//            }
//        });
//
//        EditText edUserName = view.findViewById(R.id.phoneNumberEt4);
//
//        ((View) view.findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (TextUtils.isEmpty(edUserName.getText().toString())) {
//                    Toast.makeText(getApplicationContext(), "Name Section Can't be empty", Toast.LENGTH_SHORT).show();
//                } else {
//                    updateName(edUserName.getText().toString());
//                    bsDialogEditName.dismiss();
//                }
//            }
//        });
//
//        bsDialogEditName = new BottomSheetDialog(this);
//        bsDialogEditName.setContentView(view);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Objects.requireNonNull(bsDialogEditName.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        bsDialogEditName.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                bsDialogEditName = null;
//            }
//        });
//        bsDialogEditName.show();
//    }
//
//    // This method is responsible for handling edge-to-edge UI to provide a modern immersive experience
//    private void setupEdgeToEdgeDisplay() {
//        // Set on apply window insets listener on the root layout (main container) of the activity
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            // Get system bars insets (status bar, navigation bar)
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            // Set the padding of the layout to avoid the system bars covering the UI elements
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    // This method retrieves user data from Firebase Firestore
//    private void getUserInfo() {
//        // Access the Firestore collection "Users" and get the document corresponding to the current user's UID
//        firestore.collection("Users").document(firebaseUser.getUid())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        // If the document exists, retrieve the data
//                        if (documentSnapshot.exists()) {
//                            // Get the username and phone number from the Firestore document
//                            String userName = documentSnapshot.getString("username");
//                            String userPhone = documentSnapshot.getString("userPhone");
//
//                            // Set the username and phone number to the corresponding TextViews in the layout
//                            binding.nameProfile.setText(userName);
//                            binding.phone.setText(userPhone);
//                        } else {
//                            // If the document doesn't exist, log an error
//                            showError("User data not found");
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // If there's a failure when fetching data from Firestore, log the error
//                    showError("Failed to load user info");
//                });
//    }
//
//    // This method displays an error message (in this case, logs it to Logcat)
//    private void showError(String message) {
//        // Log the error message to Logcat with a tag for easy identification
//        android.util.Log.e("ProfileActivity", message);
//    }
//
//    @Override
//    public void onBackPressed() {
//        // Override the back button behavior to also call the navigateBackToMainActivity method
//        super.onBackPressed();
//        navigateBackToMainActivity();
//    }
//
//    // This method handles the navigation back to the SettingsActivity
//    private void navigateBackToMainActivity() {
//        // Create an intent to navigate to the SettingsActivity (not MainActivity as implied by the method name)
//        Intent intent = new Intent(Profile.this, SettingsActivity.class);
//
//        // Add flags to clear the activity stack and start a new task to avoid returning to the current activity
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Start the activity and finish the current one (Profile activity)
//        startActivity(intent);
//        finish();
//    }
//
//    private void openGallery() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select image"), IMAGE_GALLERY_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                binding.Change.setImageBitmap(bitmap);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void updateName(String newName) {
//        firestore.collection("Users").document(firebaseUser.getUid()).update("username", newName)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
//                        getUserInfo();
//                    }
//                });
//    }
//}

package com.example.chatify.view.Profile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chatify.R;
import com.example.chatify.common.Common;
import com.example.chatify.databinding.ActivityProfileBinding;
import com.example.chatify.display.ViewImageActivity;
import com.example.chatify.view.setting.SettingsActivity;
import com.example.chatify.view.startup.SplachActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ImageView backButton1;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private Toolbar toolbar;
    private BottomSheetDialog bottomSheetDialog, bsDialogEditName, bsDialog;

    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;

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
        binding.fabCamera.setOnClickListener(v -> showButtomSheetPickPhoto());

        binding.nameEdit.setOnClickListener(view -> showButtomSheetEditName());

        binding.bioEdit.setOnClickListener(view -> showBioBottomSheet() );

        binding.Change.setOnClickListener(view -> {
            // Invalidate the ImageView
            binding.Change.invalidate();

            // Glide method to get Bitmap from ImageView
            Glide.with(Profile.this)
                    .asBitmap()  // Request the Bitmap from Glide
                    .load(binding.Change.getDrawable())  // Load the ImageView drawable
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            // Save the Bitmap globally in the Common class or any other storage
                            Common.IMAGE_BITMAP = resource;

                            // Launch the activity with the image transition
                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(Profile.this, binding.Change, "Name");
                            Intent intent = new Intent(Profile.this, ViewImageActivity.class);
                            startActivity(intent, activityOptionsCompat.toBundle());
                        }
                    });
        });

        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });
    }

    private void showButtomSheetPickPhoto() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
        }
        View view = getLayoutInflater().inflate(R.layout.botton_sheet, null);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bottomSheetDialog.setOnDismissListener(dialog -> bottomSheetDialog = null);
        bottomSheetDialog.show();
    }

    @SuppressLint("MissingInflatedId")
    private void showBioBottomSheet() {
        if (bsDialog == null) {
            bsDialog = new BottomSheetDialog(this);
        }
        View view = getLayoutInflater().inflate(R.layout.biobottomsheet, null);
        ((View) view.findViewById(R.id.button7)).setOnClickListener(view1 -> bsDialog.dismiss());

        EditText edBio = view.findViewById(R.id.phoneNumberEt6);

        ((View) view.findViewById(R.id.button8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edBio.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Bio Section Can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    updateBio(edBio.getText().toString());
                    bsDialog.dismiss();
                }
            }
        });
        bsDialog = new BottomSheetDialog(this);
        bsDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bsDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bsDialog.setOnDismissListener(dialog -> bsDialogEditName = null);
        bsDialog.show();
    }

    private void showButtomSheetEditName() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
        }
        View view = getLayoutInflater().inflate(R.layout.botton_sheet_edit_name, null);

        ((View) view.findViewById(R.id.button3)).setOnClickListener(view1 -> bsDialogEditName.dismiss());

        EditText edUserName = view.findViewById(R.id.phoneNumberEt4);

        ((View) view.findViewById(R.id.button4)).setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(edUserName.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Name Section Can't be empty", Toast.LENGTH_SHORT).show();
            } else {
                updateName(edUserName.getText().toString());
                bsDialogEditName.dismiss();
            }
        });

        bsDialogEditName = new BottomSheetDialog(this);
        bsDialogEditName.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bsDialogEditName.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bsDialogEditName.setOnDismissListener(dialog -> bsDialogEditName = null);
        bsDialogEditName.show();
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
                .addOnSuccessListener(documentSnapshot -> {
                    // If the document exists, retrieve the data
                    if (documentSnapshot.exists()) {
                        // Get the username and phone number from the Firestore document
                        String userName = documentSnapshot.getString("username");
                        String userPhone = documentSnapshot.getString("userPhone");
                        String userBio = documentSnapshot.getString("bio");

                        // Set the username and phone number to the corresponding TextViews in the layout
                        binding.nameProfile.setText(userName);
                        binding.phone.setText(userPhone);
                        binding.bio.setText(userBio);
                    } else {
                        // If the document doesn't exist, log an error
                        showError("User data not found");
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.Change.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateName(String newName) {
        firestore.collection("Users").document(firebaseUser.getUid()).update("username", newName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    getUserInfo();
                });
    }

    private void updateBio(String newBio){
        firestore.collection("Users").document(firebaseUser.getUid()).update("bio",newBio)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    getUserInfo();
                });
    }

    private void showDialogSignOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setMessage("Do you want to sign out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, SplachActivity.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}