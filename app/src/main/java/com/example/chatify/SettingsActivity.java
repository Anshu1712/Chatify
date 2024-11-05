package com.example.chatify;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatify.databinding.ActivitySettingsBinding;
import com.example.chatify.view.MainActivity;
import com.example.chatify.view.auth.Profile;

public class SettingsActivity extends AppCompatActivity {

    private @NonNull ActivitySettingsBinding binding; // View binding for the activity
    private ImageView backButton1;
    private Toolbar toolbar;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and set the content view
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views using binding
        backButton1 = binding.backButton1;
        layout = binding.profileLiner;
        toolbar = binding.toolbar4;

        // Set up the toolbar as the app bar
        setSupportActionBar(toolbar);

        // Handle back button click
        backButton1.setOnClickListener(v -> navigateBackToMainActivity());

        // Handle click on the profile layout to navigate to the Profile activity
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // Custom behavior when the back button is pressed
        super.onBackPressed();
        navigateBackToMainActivity();
    }

    private void navigateBackToMainActivity() {
        // Navigate back to MainActivity with appropriate flags
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // End the current activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource file to add items to the app bar
        getMenuInflater().inflate(R.menu.setting_menu, menu);

        // Set up the search action in the menu
        MenuItem searchItem = menu.findItem(R.id.action_search4);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search text changes
                return false;
            }
        });
        return true;
    }
}
