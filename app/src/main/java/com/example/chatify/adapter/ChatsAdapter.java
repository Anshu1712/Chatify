package com.example.chatify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.model.chat.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter {

    List<Chats> list;
    Context context;
    static final int MSG_TYPE_LEFT = 0;
    static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser firebaseUser;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_list, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_list_r, parent, false);
            return new ViewHolder2(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1) {
            ViewHolder1 v = (ViewHolder1) holder;
            v.textMessage.setText(list.get(position).textMessage);
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 v = (ViewHolder2) holder;
            v.textMessage.setText(list.get(position).textMessage);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getItemViewType(int position) {
        Chats chats = list.get(position);
        if (chats.sender.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return MSG_TYPE_RIGHT;
        }
        return MSG_TYPE_LEFT;

    }
}

class ViewHolder1 extends RecyclerView.ViewHolder {
    public TextView textMessage;

    public ViewHolder1(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.txtLift);
    }
}

class ViewHolder2 extends RecyclerView.ViewHolder {
    public TextView textMessage;

    public ViewHolder2(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.txtRight);
    }
}
