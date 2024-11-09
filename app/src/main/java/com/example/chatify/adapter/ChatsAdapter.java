package com.example.chatify.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.model.chat.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter{

    private List<Chats> list;
    private Context context;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser firebaseUser;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_list, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_list_r, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder v = (ViewHolder) holder;
            v.textMessage.setText(list.get(position).textMessage);
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 v = (ViewHolder2) holder;
            v.textMessage.setText(list.get(position).textMessage);
        }
    }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.txtLift);
        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private TextView textMessage;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.txtRight);
        }
    }
    public int getItemViewType(int position){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String  current_user_uid =list.get(position).sender == null ? "" : list.get(position).sender;
        String owner = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        if(owner == null){
            Log.d("empty_user","firebase auth missing");
            owner = "";
        }

        if(owner.equals(current_user_uid)){
            return MSG_TYPE_RIGHT;
        }
        return MSG_TYPE_LEFT;

    }
}
