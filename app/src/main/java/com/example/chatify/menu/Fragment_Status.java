package com.example.chatify.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cloudinary.android.MediaManager;
import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.databinding.FragmentStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fragment_Status extends Fragment {

    private FragmentStatusBinding binding;

    public Fragment_Status() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__status, container, false);

        // Get the current Firebase user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            // Initialize Cloudinary and load image if necessary
            if (!CloudinaryHelper.INSTANCE.getStarted()) {
                CloudinaryHelper.INSTANCE.initializeConfig(getContext());  // Ensure context is passed correctly
            }

            // Fetch the user's profile image from Cloudinary
            CloudinaryHelper.INSTANCE.fetchThatImage(firebaseUser.getUid() + "@userinfo", binding.imageProfile);

            // Fetch user profile data from Firestore
            getProfile(firebaseUser.getUid());

        } else {
            showError("User not logged in");
        }

        return binding.getRoot();
    }

    private void getProfile(String userId) {
        // Initialize Firestore instance
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Users").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    // If the document exists, retrieve the data
                    if (documentSnapshot.exists()) {
                        // Get the username and phone number from the Firestore document
//                        String userName = documentSnapshot.getString("username");
//                        String userPhone = documentSnapshot.getString("userPhone");
//                        String userBio = documentSnapshot.getString("bio");
//
//                        // Set the username and phone number to the corresponding TextViews in the layout
//                        binding.nameProfile.setText(userName);
//                        binding.phone.setText(userPhone);
//                        binding.bio.setText(userBio);
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

    private void showError(String message) {
        // Log the error and show a Toast message to the user
        Log.e("Fragment_Status", message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
