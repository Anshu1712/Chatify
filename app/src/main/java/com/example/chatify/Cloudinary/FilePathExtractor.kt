package com.example.chatify.Cloudinary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

public class FilePathExtractor {

    private static final String TAG = "AuthUtils";

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        // For Android versions >= API 29 (Android 10)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getPathForApi29AndAbove(context, contentUri);
        } else {
            // For below API 29 (Android 9 and lower)
            return getPathForApiBelow29(context, contentUri);
        }
    }

    private static String getPathForApi29AndAbove(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        // Handle special case for Document URIs (like those from Google Photos, Downloads, etc.)
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            String type = split[0];

            if ("image".equals(type)) {
                return getImagePathForDocument(context, uri, docId);
            } else if ("video".equals(type)) {
                return getVideoPathForDocument(context, uri, docId);
            }
            // Add more cases as needed for other document types
        }

        // For general content URIs, use the ContentResolver
        return getContentUriPath(context, uri);
    }

    private static String getImagePathForDocument(Context context, Uri uri, String docId) {
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = new String[]{docId.split(":")[1]};

        try (Cursor cursor = context.getContentResolver().query(contentUri, new String[]{MediaStore.Images.Media.DATA}, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching image path: ", e);
        }
        return null;
    }

    private static String getVideoPathForDocument(Context context, Uri uri, String docId) {
        Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media._ID + "=?";
        String[] selectionArgs = new String[]{docId.split(":")[1]};

        try (Cursor cursor = context.getContentResolver().query(contentUri, new String[]{MediaStore.Video.Media.DATA}, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching video path: ", e);
        }
        return null;
    }

    private static String getContentUriPath(Context context, Uri uri) {
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("_data"));
                if (!TextUtils.isEmpty(path)) {
                    return path;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching content URI path: ", e);
        }
        return null;
    }

    private static String getPathForApiBelow29(Context context, Uri uri) {
        // Handle general content URIs for Android 9 and below
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching file path for below Android 10: ", e);
        }
        return null;
    }
}
