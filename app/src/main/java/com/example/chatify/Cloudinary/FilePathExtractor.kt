package com.example.chatify.Cloudinary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object AuthUtils {


    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        // For Android versions >= API 29 (Android 10)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getPathForApi29AndAbove(context, contentUri)
        } else {
            // For below API 29 (Android 9 and lower)
            return getPathForApiBelow29(context, contentUri)
        }
    }

    private fun getPathForApi29AndAbove(context: Context, uri: Uri): String? {
        // Handle special case for Document URIs (like those from Google Photos or Downloads)
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":")
            val type = split[0]
            if ("image" == type) {
                return getImagePathForDocument(context, uri, docId)
            }
        }
        return null
    }

    private fun getImagePathForDocument(context: Context, uri: Uri, docId: String): String? {
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = "${MediaStore.Images.Media._ID}=?"
        val selectionArgs = arrayOf(docId.split(":")[1])

        val cursor = context.contentResolver.query(contentUri, arrayOf(MediaStore.Images.Media.DATA), selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    return it.getString(columnIndex)
                }
            }
        }
        return null
    }

    private fun getPathForApiBelow29(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    return it.getString(columnIndex)
                }
            }
        }
        return null
    }




}