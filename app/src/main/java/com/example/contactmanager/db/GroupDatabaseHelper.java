package com.example.contactmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactmanager.data.GroupContract;

public class GroupDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "group_db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_GROUP = "CREATE TABLE " +
            GroupContract.GroupEntry.TABLE_NAME + "(" +
            GroupContract.GroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GroupContract.GroupEntry.COLUMN_NAME + " TEXT," +
            GroupContract.GroupEntry.COLUMN_IMAGE + " INTEGER" +
            ")";

    public GroupDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GroupContract.GroupEntry.TABLE_NAME);
        onCreate(db);
    }
}
