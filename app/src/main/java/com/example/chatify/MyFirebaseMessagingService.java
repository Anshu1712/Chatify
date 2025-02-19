package com.example.chatify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.chatify.view.activities.Chat.ChatActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if the message contains notification payload
        if (remoteMessage.getData().size() > 0) {
            String senderName = remoteMessage.getData().get("senderName");
            String message = remoteMessage.getData().get("message");
            String senderPhoto = remoteMessage.getData().get("senderPhoto");

            // Create a notification with the sender's name, photo, and message
            showNotification(senderName, message, senderPhoto);
        }
    }

    private void showNotification(String senderName, String message, String senderPhoto) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, ChatActivity.class); // Open chat when notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Load the sender's photo asynchronously using Glide
        Glide.with(this)
                .asBitmap()
                .load(senderPhoto)  // URL of the sender's profile photo
                .into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        // Once the image is loaded, create and show the notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "chat_notification_channel")
                               // .setSmallIcon(R.drawable.notification_icon)  // Make sure to add an icon in your drawable resources
                                .setContentTitle(senderName)
                                .setContentText(message)
                                .setLargeIcon(resource)  // Set the sender's photo as the large icon
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);

                        notificationManager.notify(0, builder.build()); // Show notification
                    }
                });
    }

    // Helper method to load image from URL if needed
    private Bitmap getSenderPhoto(String photoUrl) {
        try {
            URL url = new URL(photoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if image can't be fetched
        }
    }
}
