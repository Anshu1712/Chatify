package com.example.chatify.Clouddinary.CloudinaryHelper
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.ImageView
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.chatify.R
import com.google.protobuf.Internal.BooleanList
import com.squareup.picasso.Picasso

object CloudinaryHelper{
    val config : MutableMap<String,Any> = mutableMapOf()
    lateinit var publicId : String
    var started : Boolean = false


    fun initializeConfig(context : Context){
        config["cloud_name"] = "dzvjxl8ms"
        config["api_key"] = "712391915763792"
        config["api_secret"] = "algD8qAf1Y5HlKtbRBCJ6_QjwKw"
        MediaManager.init(context, config)
        started = true
    }

    fun uploadImage(name : String,filePath: String, onSuccess: (String) -> Unit) {
        MediaManager.get().upload(filePath)
            .option("resource_type", "image")
            .option("public_id",name)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    println("Upload started")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    val progress = (bytes * 100 / totalBytes).toInt()
                    println("Upload progress: $progress%")
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val imageUrl = resultData?.get("url").toString()
                    publicId = resultData?.get("public_id").toString()

                    println("Image uploaded successfully. URL: $imageUrl")
                    onSuccess(imageUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    println("Error uploading image: ${error?.description}")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                }
            })
            .dispatch()
    }
    fun fetchThatImage(name : String ,img : ImageView){
        val imageUrl = MediaManager.get().url().generate(name)
        Picasso.get()
            .load(imageUrl)
            .error(R.drawable.user)
            .into(img)
    }

    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        // Check if it's a document URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // Handle different document URIs
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    if ("primary" == type) {
                        return "${android.os.Environment.getExternalStorageDirectory()}/${split[1]}"
                    }
                }
                isDownloadsDocument(uri) -> {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = Uri.parse("content://downloads/public_downloads")
                    val uriContent = ContentUris.withAppendedId(contentUri, java.lang.Long.valueOf(id))
                    return getDataColumn(context, uriContent, null, null)
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    val contentUri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return contentUri?.let { getDataColumn(context, it, selection, selectionArgs) }
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

}