package com.example.chatify.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.adapter.CallListAdapter;
import com.example.chatify.model.CallList;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Call extends Fragment {

    public Fragment_Call() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__call, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CallList> list = new ArrayList<>();

        list.add(new CallList("001", "Priyanka", "17/12/2002, 9:00 AM", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s", "missed"));
        list.add(new CallList("001", "Priyanka", "17/12/2002, 9:00 AM", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s", "income"));
        list.add(new CallList("001", "Priyanka", "17/12/2002, 9:00 AM", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s", "missed"));
        list.add(new CallList("001", "Priyanka", "17/12/2002, 9:00 AM", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFJLOojUO72L-cMidjn2LWKlmuUYHBNtR5PA&s", "received"));

        recyclerView.setAdapter(new CallListAdapter(list, getContext())); 
        return view;

    }
}
