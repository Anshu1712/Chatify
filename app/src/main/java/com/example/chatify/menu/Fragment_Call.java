package com.example.chatify.menu;

// Import necessary classes for the Fragment, RecyclerView, and other Android components

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.model.CallList;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Call extends Fragment {

    // Default constructor for Fragment. It is empty since we don't need any initialization in this case.
    public Fragment_Call() {
        // Required empty public constructor
    }

    // Called when the fragment is created. This method is used to set up the layout of the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, which is defined in fragment_call.xml
        // This creates a view hierarchy from the XML layout file
        View view = inflater.inflate(R.layout.fragment__call, container, false);

        // Suppressing lint warning related to missing resource or inflated ID
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        // Find the RecyclerView from the inflated view by its ID, which is defined in fragment_call.xml
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        // Set the layout manager for the RecyclerView to a LinearLayoutManager.
        // This means that the RecyclerView will display items in a vertical list.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create a new list of CallList objects. This will be used to populate the RecyclerView.
        // CallList is presumably a model class that contains data for each item in the call list.
        List<CallList> list = new ArrayList<>();

        // We have commented out the line where the adapter is set.
        // Normally, the adapter binds the data (in this case, the 'list') to the RecyclerView.
        // recyclerView.setAdapter(new CallListAdapter(list, getContext()));

        // Return the root view of the fragment, which has been created and set up.
        return view;
    }
}
