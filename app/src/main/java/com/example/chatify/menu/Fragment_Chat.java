package com.example.chatify.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.adapter.ChatListAdapter;
import com.example.chatify.model.ChatListModel;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Chat extends Fragment {


    public Fragment_Chat() {
        // Required empty public constructor
    }


    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__chat, container, false);
        List<ChatListModel> list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list.add(new ChatListModel("11", "Priyanka", "Hello Friends", "17/12/2002", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s"));
        list.add(new ChatListModel("11", "Priyanka", "Hello Friends", "17/12/2002", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s"));
        list.add(new ChatListModel("11", "Priyanka", "Hello Friends", "17/12/2002", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s"));
        list.add(new ChatListModel("11", "Priyanka", "Hello Friends", "17/12/2002", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s"));
        recyclerView.setAdapter(new ChatListAdapter(list, getContext()));
        return view;
    }
}