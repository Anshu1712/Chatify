package com.example.chatify.Cloudinary;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryHelper {

    private static boolean isInit = false;

    // Initializes the Cloudinary library (make sure to load credentials securely)
    public static void init(Context context) {
        if (!isInit) {
            Map<String, String> config = new HashMap<>();
            // Ideally, load from a more secure location like environment variables or encrypted storage
            config.put("cloud_name", "dbydr8c5p");
            config.put("api_key", "576661243913843");
            config.put("api_secret", "I1j72YDknkPgw2gcqdIE2JU2TSY");

            // Initialize the Cloudinary SDK
            MediaManager.init(context, config);
            isInit = true;
        }
    }

    // Upload a file to Cloudinary
    public static void uploadFile(
            String filePath,
            String fileType,
            Runnable onStart,
            ProgressCallback onProgress,
            SuccessCallback onSuccess,
            ErrorCallback onError
    ) {
        // Make sure that MediaManager is initialized before calling this
        if (!isInit) {
            throw new IllegalStateException("Cloudinary is not initialized. Call init() first.");
        }

        MediaManager.get()
                .upload(filePath)
                .option("resource_type", fileType)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // Run onStart callback on the main thread to ensure UI interaction
                        runOnUiThread(onStart);
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Calculate progress percentage
                        int progress = (int) ((bytes * 100) / totalBytes);
                        // Run onProgress callback on the main thread
                        runOnUiThread(() -> onProgress.onProgress(progress));
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Retrieve the secure URL from the result data
                        String url = (String) resultData.get("secure_url");
                        // Run onSuccess callback on the main thread
                        runOnUiThread(() -> onSuccess.onSuccess(url));
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        // Run onError callback on the main thread
                        runOnUiThread(() -> onError.onError(error.getDescription()));
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Optional: Handle reschedule scenarios if needed
                    }
                })
                .dispatch();
    }

    // Utility method to run actions on the main thread
    public static void runOnUiThread(Runnable action) {
        // Ensure that callbacks run on the main thread (important for UI updates)
        new Handler(Looper.getMainLooper()).post(action);
    }

    // Interface for reporting progress
    public interface ProgressCallback {
        void onProgress(int progress);
    }

    // Interface for successful upload result
    public interface SuccessCallback {
        void onSuccess(String url);
    }

    // Interface for handling errors
    public interface ErrorCallback {
        void onError(String error);
    }
}
