package com.example.chatify.view.activities.Chat

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper
import com.example.chatify.R
import com.squareup.picasso.Picasso

class View_chat_image : AppCompatActivity() {

    lateinit var img : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_chat_image)

        img = findViewById(R.id.view_img)
        val str = intent.getStringExtra("viewimg")
        CloudinaryHelper.fetchThatImage(str.toString(),img)
    }
}