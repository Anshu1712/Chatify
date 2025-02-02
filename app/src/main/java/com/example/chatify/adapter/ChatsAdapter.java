package com.example.chatify.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper;
import com.example.chatify.R;
import com.example.chatify.model.chat.Chats;
import com.example.chatify.view.activities.Chat.View_chat_image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter {

    List<Chats> list;
    Context context;
    static final int MSG_TYPE_LEFT = 0;
    static final int MSG_TYPE_RIGHT = 1;

    String recUid;
    FirebaseUser firebaseUser;

    public ChatsAdapter(String recUid, List<Chats> list, Context context) {
        if(!CloudinaryHelper.INSTANCE.getStarted()) CloudinaryHelper.INSTANCE.initializeConfig(context);
        this.list = list;
        this.context = context;
        this.recUid = recUid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_list, parent, false);
            return new ViewHolder1(view);
        } else if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_list_r, parent, false);
            return new ViewHolder2(view);
        } else if (viewType == 4) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_image_layout_sender, parent, false);
            return new ViewHolder3(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_layout_recv, parent, false);
            return new ViewHolder4(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Chats chat = list.get(position);
        String time = String.valueOf(chat.getDataTime());

        if (holder instanceof ViewHolder1) {
            ViewHolder1 v = (ViewHolder1) holder;
            v.textMessage.setText(list.get(position).textMessage);

        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 v = (ViewHolder2) holder;
            v.textMessage.setText(list.get(position).textMessage);
        } else if (holder instanceof ViewHolder3) {
            ViewHolder3 v = (ViewHolder3) holder;
            v.textMessage.setText(list.get(position).textMessage);
            CloudinaryHelper.INSTANCE.fetchThatImage(list.get(position).ImageName, v.img);

            v.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, View_chat_image.class);
                    intent.putExtra("viewimg",list.get(position).ImageName);
                    context.startActivity(intent);
                }
            });

        } else if (holder instanceof ViewHolder4) {
            ViewHolder4 v = (ViewHolder4) holder;
            v.textMessage.setText(list.get(position).textMessage);
            CloudinaryHelper.INSTANCE.fetchThatImage(list.get(position).ImageName, v.img);

            v.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, View_chat_image.class);
                    intent.putExtra("viewimg",list.get(position).ImageName);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getItemViewType(int position) {
        Chats chats = list.get(position);
        if (chats.ImageName != null) {
            if (chats.ImageName.startsWith(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return 3;
            } else {
                return 4;
            }
        } else {
            Log.d("error07","image null");
            if (chats.sender.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return MSG_TYPE_RIGHT;
            }else{
                return MSG_TYPE_LEFT;
            }
        }
    }
}

class ViewHolder1 extends RecyclerView.ViewHolder {
    public TextView textMessage;
    public TextView time;
    public TextView date;



    public ViewHolder1(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.txtLift);
        time = itemView.findViewById(R.id.chat_timeRecive);
        date = itemView.findViewById(R.id.dateReciver);
    }
}

class ViewHolder2 extends RecyclerView.ViewHolder {
    public TextView textMessage;
    public TextView time;
    public TextView date;

    public ViewHolder2(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.txtRight);
        time = itemView.findViewById(R.id.chat_timeSend);
        date = itemView.findViewById(R.id.dateSender);
    }
}

class ViewHolder3 extends RecyclerView.ViewHolder {
    public TextView textMessage;

    public ImageView img;
    public ViewHolder3(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.chat_img_text);
        img = itemView.findViewById(R.id.chat_img);
    }
}

class ViewHolder4 extends RecyclerView.ViewHolder {
    public TextView textMessage;

    public ImageView img;
    public ViewHolder4(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.chatr_img_text);
        img = itemView.findViewById(R.id.chatr_img);
    }
}