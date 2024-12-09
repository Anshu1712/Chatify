package com.example.chatify.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.chatify.R;
import com.example.chatify.adapter.MyAdapter;
import com.example.chatify.databinding.ActivityMainBinding;
import com.example.chatify.menu.Fragment_Call;
import com.example.chatify.menu.Fragment_Chat;
import com.example.chatify.menu.Fragment_Status;
import com.example.chatify.view.activities.contact.AddContact;
import com.example.chatify.view.activities.contact.NewGroup;
import com.example.chatify.view.activities.setting.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    // Declaring member variables for binding, ViewPager2, Toolbar, adapter, TabLayout, and FloatingActionButton
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private MyAdapter adapter;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing the binding object to bind the layout file
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        


        // Initializing TabLayout, ViewPager2, and FloatingActionButton using the binding object
        tabLayout = binding.tab;
        viewPager = binding.viewPager;
        floatingActionButton = binding.addContactnew;

        // Initializing the Toolbar and setting it as the action bar
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Setting up the adapter with fragments representing each tab
        adapter = new MyAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new Fragment_Chat());   // Adding Chat fragment
        adapter.addFragment(new Fragment_Status()); // Adding Status fragment
        adapter.addFragment(new Fragment_Call());   // Adding Call fragment

        // Setting the orientation of ViewPager2 and attaching the adapter
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);

        // Attaching TabLayout with ViewPager2 using TabLayoutMediator to synchronize tab clicks and swipes
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Setting the text of each tab based on its position
            switch (position) {
                case 0:
                    tab.setText("Chat");  // Tab 1: Chat
                    break;
                case 1:
                    tab.setText("Status"); // Tab 2: Status
                    break;
                case 2:
                    tab.setText("Call");  // Tab 3: Call
                    break;
            }
        }).attach();

        // Setting up a listener to update the FloatingActionButton icon and action when the page changes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateFloatingActionButtonIcon(position); // Update the FAB icon based on the selected page
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the menu resource file to add options to the app bar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Finding the search item in the menu and setting up its behavior
        MenuItem searchItem = menu.findItem(R.id.action_search1);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handling item selection in the app bar menu
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Handling the settings menu item click
            Log.d("MainActivity", "Settings item clicked");
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.action_new_group) {
            // Handling the new group menu item click
            Log.d("MainActivity", "New Group item clicked");
            Intent newGroupIntent = new Intent(MainActivity.this, NewGroup.class);
            startActivity(newGroupIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item); // Default action if the item is not recognized
        }
    }

    // Method to update the FloatingActionButton icon based on the selected page
    private void updateFloatingActionButtonIcon(int position) {
        switch (position) {
            case 0:
                floatingActionButton.setImageResource(R.drawable.add); // Set FAB icon to "add"

                // Set click listener to start the AddContact activity
                floatingActionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, AddContact.class);
                    startActivity(intent);
                });
                break;
            case 1:
                floatingActionButton.setImageResource(R.drawable.camera); // Set FAB icon to "camera"
                // Set click listener to start the AddContact activity
                floatingActionButton.setOnClickListener(v -> {
                    Toast.makeText(this, "This feature is not available", Toast.LENGTH_SHORT).show();
                });
                break;
            case 2:
                floatingActionButton.setImageResource(R.drawable.call); // Set FAB icon to "call"
                floatingActionButton.setOnClickListener(v -> {
                    Toast.makeText(this, "This feature is not available", Toast.LENGTH_SHORT).show();
                });
                break;
            default:
                floatingActionButton.setImageResource(R.drawable.add); // Default icon
                break;
        }
    }
}