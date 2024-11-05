package com.example.chatify.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatify.R;
import com.example.chatify.SettingsActivity;
import com.example.chatify.databinding.ActivityProfileBinding;

public class Profile extends AppCompatActivity {

    private ActivityProfileBinding binding; // Binding for activity_profile layout
    private ImageView backButton1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the binding object
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Set the content view using binding

        // Handle edge-to-edge display (if you intended to use EdgeToEdge library, enable it properly)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton1 = binding.backButton2;

        toolbar = binding.toolbar4; // Assuming you have a Toolbar with this ID in your layout
        setSupportActionBar(toolbar);

        backButton1.setOnClickListener(v -> navigateBackToMainActivity());

    }
    @Override
    public void onBackPressed() {
        // When the back button is pressed, navigate back to MainActivity
        super.onBackPressed();
        navigateBackToMainActivity();
    }

    private void navigateBackToMainActivity() {
        // Create an intent to start MainActivity
        Intent intent = new Intent(Profile.this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
