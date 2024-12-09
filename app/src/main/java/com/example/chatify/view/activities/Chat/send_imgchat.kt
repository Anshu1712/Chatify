package com.example.chatify.view.activities.Chat

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.chatify.Clouddinary.CloudinaryHelper.CloudinaryHelper
import com.example.chatify.R
import com.example.chatify.model.chat.Chats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class send_imgchat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_imgchat)

        if (!CloudinaryHelper.started) {
            CloudinaryHelper.initializeConfig(this)
        }

        val img: Uri? = intent.getParcelableExtra("img")
        val rec: String? = intent.getStringExtra("recuid")

        //Toast.makeText(this,"@"+rec, Toast.LENGTH_SHORT).show()
        findViewById<ImageView>(R.id.img_show).setImageURI(img)

        findViewById<ImageView>(R.id.send).setOnClickListener {
            val str: String = findViewById<EditText>(R.id.etd).text.toString()
            val name: String =
                FirebaseAuth.getInstance().currentUser?.uid.toString() + System.currentTimeMillis() + rec

            CloudinaryHelper.uploadImage(
                name,
                img?.let { uri -> CloudinaryHelper.getRealPathFromURI(uri, this) }.toString()
            ) { task ->
                runOnUiThread {
                    sendTextMessage(str, rec.toString(), name)
                }
            }

        }
    }

    private fun sendTextMessage(text: String, receiverID: String, imgName: String) {
        val date = Calendar.getInstance().time
        val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)

        val currentTime =
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)

        val chats = Chats(
            "$today, $currentTime",
            text,
            "text",
            FirebaseAuth.getInstance().uid ?: "",
            receiverID
        )

        chats.ImageName = imgName

        val senderRoom = (FirebaseAuth.getInstance().uid ?: "") + receiverID
        val receiverRoom = receiverID + (FirebaseAuth.getInstance().uid ?: "")

        // Add the message to the sender's and receiver's chat rooms
        val reference = FirebaseDatabase.getInstance().reference
        reference.child("Chats").child(senderRoom).child("msg").push()
            .setValue(chats)
            .addOnSuccessListener {
                reference.child("Chats").child(receiverRoom).child("msg").push().setValue(chats)
//                val intent = Intent(this, ChatActivity::class.java)
//                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.d("Send", "onFailure: ${e.message}")
            }

        // Update ChatList for sender
        val chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList")
            .child(FirebaseAuth.getInstance().uid ?: "").child(receiverID)
        chatRef1.child("chatid").setValue(receiverID)

        // Update ChatList for receiver
        val chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList")
            .child(receiverID).child(FirebaseAuth.getInstance().uid ?: "")
        chatRef2.child("chatid").setValue(FirebaseAuth.getInstance().uid)
    }
}