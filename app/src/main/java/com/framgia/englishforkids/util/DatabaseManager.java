package com.framgia.englishforkids.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by GIAKHANH on 12/28/2016.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "video_url.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_VIDEO = "tbl_videos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMG_URL = "img_url";
    public static final String COLUMN_VIDEO_ADDRESS = "video_address";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_TYPE = "type";

    //database creation sql statement
    private static final String CREATE_TABLE_VIDEO_SQL = "CREATE TABLE [tbl_videos] (\n" +
            "[id] INTEGER PRIMARY KEY autoincrement ,\n" +
            "[name] TEXT NULL  ,\n" +
            "[img_url] INTEGER NULL  ,\n" +
            "[video_address] TEXT NULL,\n" +
            "[video_url] TEXT  NULL,\n" +
            "[type] INTEGER  NULL\n" +
            ")";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create image table
        db.execSQL(CREATE_TABLE_VIDEO_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(getClass().getSimpleName(), "upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
        onCreate(db);
    }
}
