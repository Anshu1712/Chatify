package com.example.chatify.view.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.databinding.ActivitySettingsBinding;
import com.example.chatify.view.MainActivity;
import com.example.chatify.view.activities.Profile.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;  // View binding object to link the layout with the activity
    private FirebaseUser firebaseUser;  // FirebaseUser object to manage user-related info
    private FirebaseFirestore firestore;  // FirebaseFirestore object to interact with Firestore database

    private ImageView backButton1;  // ImageView for handling the back button click
    private Toolbar toolbar;  // Toolbar for managing the top navigation bar
    private LinearLayout layout;  // LinearLayout to represent the profile section in the settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and bind it to the activity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        // Apply window insets listener to adjust content for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Extract the system bar insets (status and navigation bars)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Adjust the padding to avoid overlap with system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!CloudinaryHelper.INSTANCE.getStarted()) CloudinaryHelper.INSTANCE.initializeConfig(this);
        CloudinaryHelper.INSTANCE.fetchThatImage(FirebaseAuth.getInstance().getCurrentUser().getUid()+"@userinfo",binding.imageProfile);

        binding.delAccount.setOnClickListener(v ->{
            CloudinaryHelper.INSTANCE.deleteImageAsync(FirebaseAuth.getInstance().getUid().toString()+"@userInfo");
            Toast.makeText(this,"deleted",Toast.LENGTH_SHORT).show();
        });


        // Initialize Firebase Firestore and Authentication instances
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();  // Get the current authenticated user
        if (firebaseUser != null) {
            getInfo();  // Fetch user information from Firestore if the user is authenticated
        }

        // Initialize views using the binding object
        backButton1 = binding.backButton1;  // Initialize the back button
        layout = binding.profileLiner;  // Initialize the profile layout container
        toolbar = binding.toolbar4;  // Initialize the toolbar

        // Set the custom toolbar as the app's action bar
        setSupportActionBar(toolbar);

        // Set a click listener on the back button to navigate back to MainActivity
        backButton1.setOnClickListener(v -> navigateBackToMainActivity());

        // Set a click listener on the profile layout to navigate to the Profile activity
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Profile.class);  // Intent to open Profile activity
            startActivity(intent);  // Start Profile activity
        });
    }

    @Override
    public void onBackPressed() {
        // Override default back button behavior to navigate back to MainActivity
        super.onBackPressed();
        navigateBackToMainActivity();
    }

    private void navigateBackToMainActivity() {
        // Create an Intent to navigate back to the MainActivity
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        // Add flags to clear the activity stack and create a new task for MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);  // Start MainActivity
        finish();  // Close the current activity (SettingsActivity)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource into the app bar
        getMenuInflater().inflate(R.menu.setting_menu, menu);

        // Find the search item in the menu to handle search functionality
        MenuItem searchItem = menu.findItem(R.id.action_search4);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        // Set up query text listener for the search view
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission (not implemented here)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle changes in search text (not implemented here)
                return false;
            }
        });

        return true;  // Return true to display the menu
    }

    private void getInfo() {
        // Fetch user info from Firestore using the authenticated user's UID
        firestore.collection("Users").document(firebaseUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Get the "username" from the Firestore document and display it in the layout
                        String userName = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                        String userBio = Objects.requireNonNull(documentSnapshot.get("bio")).toString();
                        binding.nameTxt.setText(userName);  // Set the fetched username to the corresponding TextView
                        binding.bioTxt.setText(userBio);  // Set the fetched bio
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log failure to fetch user info if an error occurs
                        Log.d("Get Data", "onfailur:" + e.getMessage());
                    }
                });
    }

}
