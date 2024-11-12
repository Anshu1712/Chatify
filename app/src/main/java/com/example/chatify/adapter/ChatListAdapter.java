package com.example.chatify.adapter;

// Import necessary classes for handling UI and RecyclerView components

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatify.R;
import com.example.chatify.model.ChatListModel;
import com.example.chatify.view.activities.Chat.ChatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    // List to hold the chat data (list of ChatListModel objects)
    private List<ChatListModel> list;

    // Context object for accessing resources, inflating views, and loading images
    private Context context;

    // Constructor that initializes the adapter with a list of chat data and a context
    public ChatListAdapter(List<ChatListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    // Called when RecyclerView needs a new ViewHolder to represent an item (chat)
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView (layout_chat.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat, parent, false);

        // Return a new instance of the Holder class, passing the inflated view
        return new Holder(view);
    }

    // Called to bind the data to the views in the ViewHolder for the given position
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // Get the ChatListModel object for the current position in the list
        ChatListModel chatlist = list.get(position);

        // Set the user's name to the tvName TextView
        holder.tvName.setText(chatlist.getUserName());

        // Set the message description to the tvDescription TextView
        holder.tvDescription.setText(chatlist.getDescription());

        // Set the date of the chat to the tvDate TextView
        holder.tvDate.setText(chatlist.getDate());

        // Use Glide to load the profile image URL into the CircleImageView (profile image)
        if (chatlist.getUrlProfile().equals("")) {
            holder.profile.setImageResource(R.drawable.user);
        } else {
            Glide.with(context).load(chatlist.getUrlProfile()).into(holder.profile);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChatActivity.class)
                        .putExtra("userID", chatlist.getUserID())
                        .putExtra("username", chatlist.getUserName())
                        .putExtra("imageProfile", chatlist.getUrlProfile()));
            }
        });
    }

    // Returns the total number of items in the list (the number of chat items)
    @Override
    public int getItemCount() {
        return list.size();  // Return the size of the list, which determines how many items will be displayed
    }

    // The ViewHolder class holds references to the views in each item of the RecyclerView
    public class Holder extends RecyclerView.ViewHolder {
        // Declare the TextViews for displaying the name, description, and date of the chat
        private TextView tvName, tvDescription, tvDate;

        // Declare the CircleImageView for displaying the profile picture
        private CircleImageView profile;

        // Constructor for initializing the views from the inflated layout
        public Holder(@NonNull View itemView) {
            super(itemView);

            // Initialize the TextViews and CircleImageView by finding them from the layout
            tvDate = itemView.findViewById(R.id.tv_date);  // Date TextView
            tvDescription = itemView.findViewById(R.id.tv_message);  // Message/Description TextView
            tvName = itemView.findViewById(R.id.tv_name);  // Name TextView
            profile = itemView.findViewById(R.id.image_profile);  // Profile image CircleImageView
        }
    }
}
