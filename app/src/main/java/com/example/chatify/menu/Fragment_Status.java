package com.example.chatify.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.chatify.R;
import com.example.chatify.databinding.FragmentStatusBinding;


public class Fragment_Status extends Fragment {


    public Fragment_Status() {
        // Required empty public constructor
    }


    private FragmentStatusBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__status, container, false);

//        getProfile();


        return binding.getRoot();

    }

//    private void getProfile() {
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseFirestore firestore =FirebaseFirestore.getInstance();
//        firestore.collection("Users").document(firebaseUser.getUid())
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    // If the document exists, retrieve the data
//                    if (documentSnapshot.exists()) {
//                        // Get the username and phone number from the Firestore document
//                        String userName = documentSnapshot.getString("username");
//                        String userPhone = documentSnapshot.getString("userPhone");
//                        String userBio = documentSnapshot.getString("bio");
//
//                        // Set the username and phone number to the corresponding TextViews in the layout
//                        binding.nameProfile.setText(userName);
//                        binding.phone.setText(userPhone);
//                        binding.bio.setText(userBio);
//                    } else {
//                        // If the document doesn't exist, log an error
//                        showError("User data not found");
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // If there's a failure when fetching data from Firestore, log the error
//                    showError("Failed to load user info");
//                });
//    }
}