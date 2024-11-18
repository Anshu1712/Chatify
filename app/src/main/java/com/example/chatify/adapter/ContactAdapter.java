package com.example.chatify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.model.user.Users;
import com.example.chatify.view.activities.Chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Users> list;
    private Context context;

    public ContactAdapter(List<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each contact item
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = list.get(position);

        // Bind the user data to the views
        holder.username.setText(user.getUsername());
        holder.userbio.setText(user.getBio());

        // Optionally, you can use CloudinaryHelper for other image handling
        if (user.getImageProfile() != null && !user.getImageProfile().isEmpty()) {
            CloudinaryHelper.INSTANCE.fetchThatImage(FirebaseAuth.getInstance().getCurrentUser().getUid() + "@userinfo", holder.imageProfile);
        } else {
            // Set a default image if there's no profile image
            holder.imageProfile.setImageResource(R.drawable.user);
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the chat activity with the user's details
                context.startActivity(new Intent(context, ChatActivity.class)
                        .putExtra("userID", user.getUserID())
                        .putExtra("username", user.getUsername())
                        .putExtra("imageProfile", user.getImageProfile()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();  // Return the size of the contact list
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageProfile;  // Image view for user's profile
        private TextView username, userbio;  // Text views for username and bio

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            imageProfile = itemView.findViewById(R.id.image_Profile1);
            username = itemView.findViewById(R.id.userName);
            userbio = itemView.findViewById(R.id.userBio);
        }
    }
}
