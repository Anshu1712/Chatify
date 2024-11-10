package com.example.chatify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MediaRepository {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public MediaRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open the database for writing
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }

    // Insert a new media record
    public long insertMedia(String name, String type, String filePath) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_TYPE, type);
        values.put(DatabaseHelper.COLUMN_FILE_PATH, filePath);

        return database.insert(DatabaseHelper.TABLE_MEDIA, null, values);
    }

    // Get all media records
    public Cursor getAllMedia() {
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_TYPE,
                DatabaseHelper.COLUMN_FILE_PATH
        };

        return database.query(DatabaseHelper.TABLE_MEDIA, columns, null, null, null, null, null);
    }

    // Get a specific media by ID
    public Cursor getMediaById(long id) {
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_TYPE,
                DatabaseHelper.COLUMN_FILE_PATH
        };

        return database.query(DatabaseHelper.TABLE_MEDIA,
                columns,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
    }
}
