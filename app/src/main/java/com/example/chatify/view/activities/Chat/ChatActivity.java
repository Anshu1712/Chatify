package com.example.chatify.view.activities.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.adapter.ChatsAdapter;
import com.example.chatify.databinding.ActivityChatBinding;
import com.example.chatify.model.chat.Chats;
import com.example.chatify.view.activities.Profile.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String receiverID;
    private ChatsAdapter adapter;
    private List<Chats> list;
    private String userProfile, userName;
    private boolean isActionShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        receiverID = intent.getStringExtra("userID");
        userProfile = intent.getStringExtra("imageProfile");

        if (receiverID != null) {
            binding.userName.setText(userName);
            if (userProfile != null && userProfile.isEmpty()) {
                binding.imageProfile.setImageResource(R.drawable.user);
            }
        }

        // Back button click listener
        binding.backbtn.setOnClickListener(view -> finish());

        // TextWatcher for the input field
        binding.etd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.etd.getText().toString())) {
                    binding.sentBtn.setImageDrawable(getDrawable(R.drawable.baseline_keyboard_voice_24));
                } else {
                    binding.sentBtn.setImageDrawable(getDrawable(R.drawable.baseline_send_24));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        initBtnClick();

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        readChat();
    }

    private void initBtnClick() {
        binding.sentBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.etd.getText().toString())) {
                sendTextMessage(binding.etd.getText().toString());
                binding.etd.setText("");
            }
        });

        // Open user profile when image is clicked
        binding.imageProfile1.setOnClickListener(view -> {
            startActivity(new Intent(ChatActivity.this, UserProfileActivity.class)
                    .putExtra("userID", receiverID)
                    .putExtra("userProfile", userProfile)
                    .putExtra("username", userName)
            );
        });

        // Handle the attachment button click
        binding.attachment.setOnClickListener(view -> {
            if (isActionShown) {
                binding.layoutAction.setVisibility(View.GONE);  // Hide the layout
                isActionShown = false;
            } else {
                binding.layoutAction.setVisibility(View.VISIBLE);  // Show the layout
                isActionShown = true;
            }
        });
    }

    private void sendTextMessage(String text) {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String today = format.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String currentTime = df.format(currentDateTime.getTime());

        Chats chats = new Chats(
                today + ", " + currentTime,
                text,
                "TEXT",
                firebaseUser.getUid(),
                receiverID);

        String SenderRoom = FirebaseAuth.getInstance().getUid() + receiverID;
        String reciverRoom = receiverID + FirebaseAuth.getInstance().getUid();

        reference.child("Chats").child(SenderRoom).child("msg").push().setValue(chats).addOnSuccessListener(aVoid ->
                reference.child("Chats").child(reciverRoom).child("msg").push().setValue(chats)
        ).addOnFailureListener(e -> Log.d("Send", "onFailure:" + e.getMessage()));

        // Update ChatList
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid()).child(receiverID);
        chatRef1.child("chatid").setValue(receiverID);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(receiverID).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());
    }

    private void readChat() {
        try {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this
                    , LinearLayoutManager.VERTICAL, false));
            adapter = new ChatsAdapter(list, ChatActivity.this);
            binding.recyclerView.setAdapter(adapter);
            reference1.child("Chats").child(FirebaseAuth.getInstance().getUid() + receiverID).child("msg")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    String dateTime = snap.child("dateTime").getValue(String.class);
                                    String receiver = snap.child("receiver").getValue(String.class);
                                    String sender = snap.child("sender").getValue(String.class);
                                    String textMessage = snap.child("textMessage").getValue(String.class);
                                    String type = snapshot.child("type").getValue(String.class);
                                    Chats chats = new Chats(dateTime, textMessage, type, sender, receiver);
                                    list.add(chats);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}