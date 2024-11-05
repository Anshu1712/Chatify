package com.example.chatify;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chatify.databinding.ActivityNewGroupBinding;
import com.example.chatify.view.MainActivity;

public class NewGroup extends AppCompatActivity {

    private ActivityNewGroupBinding binding;
    private ImageView backButton1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the correct layout and set the content view
        binding = ActivityNewGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the backButton1 with the binding object
        backButton1 = binding.backButton1;

        toolbar = binding.toolbar2;
        setSupportActionBar(toolbar);

        // Set up a click listener for the back button
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // When the back button is pressed, navigate back to MainActivity
        super.onBackPressed();
        navigateBackToMainActivity();
    }

    private void navigateBackToMainActivity() {
        // Create an intent to start MainActivity
        Intent intent = new Intent(NewGroup.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the menu resource file to add options to the app bar
        getMenuInflater().inflate(R.menu.group_menu, menu);

        // Finding the search item in the menu and setting up its behavior
        MenuItem searchItem = menu.findItem(R.id.action_search3);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        // Setting up the search bar behavior to respond to text submission and changes
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query submission (e.g., filter a list)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the text change in the search bar (e.g., update search results)
                return false;
            }
        });
        return true; // Indicating that the menu was successfully created
    }

}
