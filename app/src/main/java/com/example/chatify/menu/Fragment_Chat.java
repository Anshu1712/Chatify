package com.example.chatify.menu;

// Importing necessary Android and custom classes for the fragment, RecyclerView, adapter, and model

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.adapter.ChatListAdapter;
import com.example.chatify.databinding.FragmentChatBinding;
import com.example.chatify.model.ChatListModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_Chat extends Fragment {


    private static final String TAG = "Fragment_Chat";

    // Default constructor for the Fragment. No special initialization is needed here.
    public Fragment_Chat() {
        // Required empty public constructor
    }

    // Declare RecyclerView variable, which will be used to display the list of chat data.

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;
    private Handler handler = new Handler();
    private ChatListAdapter adapter;
    private List<ChatListModel> list;
    private ArrayList<String> allUserID;
    private RecyclerView recyclerView;
    private FragmentChatBinding binding;

    // onCreateView method is called when the fragment's view is created. It's where we set up the UI for the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, which is defined in fragment_chat.xml
        // This creates a view hierarchy from the layout file.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__chat, container, false);

        // Create a list to hold the chat data objects, which will be displayed in the RecyclerView.
        list = new ArrayList<>();
        allUserID = new ArrayList<>();

        // Set the layout manager for the RecyclerView. The LinearLayoutManager arranges the items in a vertical list.
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(list, getContext());
        binding.recyclerView.setAdapter(adapter);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();


        if (firebaseUser != null) {
            getChatList();
        }

        // Return the root view of the fragment. This will be displayed on the screen.
        return binding.getRoot();
    }

    private void getChatList() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        reference.child("ChatList").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        allUserID.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userID = Objects.requireNonNull(snapshot.child("chatid").getValue()).toString();
                            Log.d(TAG, "onDataChange: userid" + userID);
                            binding.progressCircular.setVisibility(View.GONE);

                            allUserID.add(userID);
                        }
                        getUserInfo();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getUserInfo() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (String userId : allUserID) {
                    firestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: add " + documentSnapshot.getString("username"));
                            try {
                                ChatListModel chat = new ChatListModel(
                                        documentSnapshot.getString("userID"),
                                        documentSnapshot.getString("username"),
                                        "This is description..",
                                        "",
                                        documentSnapshot.getString("imageProfile")
                                        , documentSnapshot.getString("userPhone")
                                        , documentSnapshot.getString("bio")
                                );
                                list.add(chat);
                            } catch (Exception e) {
                                Log.d(TAG, "onSuccess :" + e.getMessage());
                            }
                            if (adapter != null) {
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();

                                Log.d(TAG, "onSuccess: adapter" + adapter.getItemCount());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure : L" + e.getMessage());
                        }
                    });
                }
            }
        });


    }

}
