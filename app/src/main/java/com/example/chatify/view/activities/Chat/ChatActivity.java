package com.example.chatify.view.activities.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.adapter.ChatsAdapter;
import com.example.chatify.databinding.ActivityChatBinding;
import com.example.chatify.model.chat.Chats;
import com.example.chatify.view.activities.Profile.UserProfileActivity;
import com.example.chatify.view.activities.dialog.DialogReviewSendImage;
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
    private String userProfile, userName, userPhone, UserBio;
    private boolean isActionShown = false;

    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        userPhone = intent.getStringExtra("userPhone");
        UserBio = intent.getStringExtra("bio");

        if (receiverID != null) {
            binding.userName.setText(userName);
            if (userProfile != null && userProfile.isEmpty()) {
                binding.imageProfile.setImageResource(R.drawable.user);
            }
        }

        if(!CloudinaryHelper.INSTANCE.getStarted()) CloudinaryHelper.INSTANCE.initializeConfig(this);
        CloudinaryHelper.INSTANCE.fetchThatImage(receiverID + "@userinfo", binding.imageProfile);

        // Back button click listener
        binding.backbtn.setOnClickListener(view -> finish());

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        binding.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "Video call", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChatActivity.this, "This feature is not available", Toast.LENGTH_SHORT).show();
            }
        });

        binding.audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "Audio call", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChatActivity.this, "This feature is not available", Toast.LENGTH_SHORT).show();
            }
        });

        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "More function", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChatActivity.this, "This feature is not available", Toast.LENGTH_SHORT).show();
            }
        });


        // TextWatcher for the input field
        binding.etd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.etd.getText().toString())) {
                    binding.sentBtn.setImageDrawable(getDrawable(R.drawable.baseline_keyboard_voice_24));
                } else {
                    binding.sentBtn.setImageDrawable(getDrawable(R.drawable.baseline_send_24));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        initBtnClick();

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isActionShown) {
                    binding.layoutAction.setVisibility(View.GONE);
                    isActionShown = false;
                } else {
                    binding.layoutAction.setVisibility(View.VISIBLE);
                    isActionShown = true;
                }

                Intent pick = new Intent(Intent.ACTION_PICK);
                pick.setType("image/*");
                startActivityForResult(pick,IMAGE_GALLERY_REQUEST);
            }
        });

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
                    .putExtra("userPhone", userPhone)
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
                "text",
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
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            layoutManager.setStackFromEnd(true);
            binding.recyclerView.setLayoutManager(layoutManager);
            adapter = new ChatsAdapter(receiverID,list, ChatActivity.this);
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
                                    String type = snap.child("type").getValue(String.class);
                                    String img_name = snap.child("ImageName").getValue(String.class);

                                    Chats chats;
                                    if(img_name == null)  chats = new Chats(dateTime, textMessage, type, sender, receiver);
                                    else{
                                        chats = new Chats(dateTime, textMessage, type, sender, receiver,img_name);
                                        Log.d("empty07","fine okay");
                                    }
                                    list.add(chats);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImage() {

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), IMAGE_GALLERY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                reviewImage(bitmap);

                if(bitmap != null){
                    Intent intent = new Intent(this,send_imgchat.class);
                    intent.putExtra("img",imageUri);
                    intent.putExtra("recuid",receiverID);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//
//            if (imageUri != null) {
//                String img_name = FirebaseAuth.getInstance().getCurrentUser().getUid() + "@userinfo";
//                String filePath = CloudinaryHelper.INSTANCE.getRealPathFromURI(imageUri, this).toString();
//                CloudinaryHelper.INSTANCE.uploadImage(img_name, filePath, new Function1<String, Unit>() {
//                    @Override
//                    public Unit invoke(String s) {
//                        System.out.println(s);
//                        return null;
//                    }
//                });
//
//            }
        }
    }

    private void reviewImage(Bitmap bitmap) {
        new DialogReviewSendImage(ChatActivity.this, bitmap).show(new DialogReviewSendImage.OnCallBack() {
            @Override
            public void onButtonSendClick() {

            }
        });
    }

    public String getUserBio() {
        return UserBio;
    }

    public void setUserBio(String userBio) {
        UserBio = userBio;
    }
}