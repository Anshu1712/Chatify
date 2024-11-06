package com.example.chatify.menu;

// Importing necessary Android and custom classes for the fragment, RecyclerView, adapter, and model

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.model.ChatListModel;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Chat extends Fragment {

    // Default constructor for the Fragment. No special initialization is needed here.
    public Fragment_Chat() {
        // Required empty public constructor
    }

    // Declare RecyclerView variable, which will be used to display the list of chat data.
    private RecyclerView recyclerView;

    // onCreateView method is called when the fragment's view is created. It's where we set up the UI for the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, which is defined in fragment_chat.xml
        // This creates a view hierarchy from the layout file.
        View view = inflater.inflate(R.layout.fragment__chat, container, false);

        // Create a list to hold the chat data objects, which will be displayed in the RecyclerView.
        List<ChatListModel> list = new ArrayList<>();

        // Find the RecyclerView by its ID from the inflated view.
        // This RecyclerView will hold the list of chat items.
        recyclerView = view.findViewById(R.id.recyclerView);

        // Set the layout manager for the RecyclerView. The LinearLayoutManager arranges the items in a vertical list.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Below commented-out lines show how you can add items to the list and bind it to the RecyclerView with an adapter.
        // The list is populated with sample data for now (commented out).

        // Add items to the list (example data for chat conversations).
        // Each ChatListModel object contains chat details such as user ID, user name, message, date, and profile image URL.

        // Example: Adding a chat entry to the list.
        // list.add(new ChatListModel("11", "Priyanka", "Hello Friends", "17/12/2002",
        //    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s"));
        // list.add(new ChatListModel("11", "Priyanka", "Hello Friends", "17/12/2002",
        //    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s"));

        // Set the adapter for the RecyclerView. The adapter binds the data (the list) to the RecyclerView.
        // recyclerView.setAdapter(new ChatListAdapter(list, getContext()));

        // Return the root view of the fragment. This will be displayed on the screen.
        return view;
    }
}
