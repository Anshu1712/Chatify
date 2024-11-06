package com.example.chatify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatify.R;
import com.example.chatify.model.CallList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.Holder> {

    // List that holds data to be displayed in the RecyclerView (CallList objects)
    private List<CallList> list;

    // Context to be used for inflating views and loading images
    private Context context;

    // Constructor to initialize the adapter with the list of CallList items and the context
    public CallListAdapter(List<CallList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    // This method is called when RecyclerView needs a new view holder to represent an item
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView (layout_call_list.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_list, parent, false);

        // Return a new Holder instance (the view holder for the call list item)
        return new Holder(view);
    }

    // This method binds data to the view holder for each item in the list
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // Get the CallList object at the given position in the list
        CallList callList = list.get(position);

        // Set the username of the caller to the tvName TextView
        holder.tvName.setText(callList.getUserName());

        // Set the date of the call to the tvDate TextView
        holder.tvDate.setText(callList.getDate());

        // Check the call type (missed, income, or other) and set the arrow icon and color accordingly
        if (callList.getCallType().equals("missed")) {
            // For missed calls, use a downward arrow and set the color to red
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_downward_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if (callList.getCallType().equals("income")) {
            // For incoming calls, use an upward arrow and set the color to green
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_upward_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            // For any other call type (e.g., outgoing), use the same upward arrow and green color
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_upward_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        // Load the user's profile image into the CircleImageView using Glide
        Glide.with(context).load(callList.getUrlProfile()).into(holder.profile);
    }

    // This method returns the number of items in the list, which determines how many times onCreateViewHolder will be called
    @Override
    public int getItemCount() {
        return list.size();  // Return the size of the list (number of call items)
    }

    // The ViewHolder class is used to hold references to the views of each item
    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDate;  // TextViews for displaying the caller's name and the call date
        private CircleImageView profile;  // CircleImageView for displaying the user's profile picture
        private ImageView arrow;  // ImageView for displaying the arrow indicating call direction (incoming, missed)

        // Constructor for the ViewHolder, which is called when the RecyclerView item view is created
        public Holder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views by finding them from the layout
            tvDate = itemView.findViewById(R.id.tv_date);  // Date TextView
            tvName = itemView.findViewById(R.id.tv_name);  // Name TextView
            profile = itemView.findViewById(R.id.image_profile);  // Profile ImageView
            arrow = itemView.findViewById(R.id.image_call);  // Call arrow ImageView
        }
    }
}
