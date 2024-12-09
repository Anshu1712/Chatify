package com.example.chatify.view.activities.Chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper
import com.example.chatify.R

class View_chat_image : AppCompatActivity() {

    lateinit var img: ImageView
    lateinit var backButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_chat_image)

        img = findViewById(R.id.view_img)
        backButton = findViewById(R.id.backbtn1)

        val str = intent.getStringExtra("viewimg")
        CloudinaryHelper.fetchThatImage(str.toString(), img)

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Ends the activity and goes back to the previous screen
        }
    }
}
