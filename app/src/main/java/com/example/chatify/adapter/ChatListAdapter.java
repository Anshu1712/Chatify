package com.example.chatify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.model.ChatListModel;
import com.example.chatify.view.activities.Chat.ChatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    private List<ChatListModel> list; // List of chat items
    private Context context; // Context for accessing resources and activities

    // Constructor for initializing the adapter
    public ChatListAdapter(List<ChatListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the RecyclerView item layout
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ChatListModel chatlist = list.get(position);

        // Set the user name, description, and date in TextViews
        holder.tvName.setText(chatlist.getUserName());
        holder.tvDescription.setText(chatlist.getDescription());
        holder.tvDate.setText(chatlist.getDate());

        // Initialize Cloudinary configuration if not started
        if (!CloudinaryHelper.INSTANCE.getStarted()) {
            CloudinaryHelper.INSTANCE.initializeConfig(context);
        }

        // Load profile image using CloudinaryHelper
        if (chatlist.getUrlProfile() != null && !chatlist.getUrlProfile().isEmpty()) {
            CloudinaryHelper.INSTANCE.fetchThatImage(chatlist.getUserID() + "@userinfo", holder.profile);
        } else {
            // Set a default image if no profile image is available
            holder.profile.setImageResource(R.drawable.user);
        }

        // Handle click events for each RecyclerView item
        holder.itemView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, ChatActivity.class)
                    .putExtra("userID", chatlist.getUserID())
                    .putExtra("username", chatlist.getUserName())
                    .putExtra("imageProfile", chatlist.getUrlProfile())
                    .putExtra("userPhone", chatlist.getUserPhone())
                    .putExtra("bio", chatlist.getUserBio()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size(); // Return the number of chat items
    }

    // ViewHolder class for efficient item recycling
    public static class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvDate; // TextViews for name, description, and date
        private CircleImageView profile; // CircleImageView for profile picture

        public Holder(@NonNull View itemView) {
            super(itemView);

            // Bind views from the layout
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDescription = itemView.findViewById(R.id.tv_message);
            tvName = itemView.findViewById(R.id.tv_name);
            profile = itemView.findViewById(R.id.image_profile);
        }
    }
}
