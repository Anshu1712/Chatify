package com.example.chatify.call;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatify.R;
import com.example.chatify.databinding.ActivityVideoCallBinding;

public class videoCall extends AppCompatActivity {
    private ActivityVideoCallBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoCallBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_video_call);
    }
}