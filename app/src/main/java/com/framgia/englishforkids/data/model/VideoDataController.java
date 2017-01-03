package com.framgia.englishforkids.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.framgia.englishforkids.util.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GIAKHANH on 12/28/2016.
 */
public class VideoDataController extends DatabaseManager {
    private SQLiteDatabase mDatabase;

    public VideoDataController(Context context) {
        super(context);
    }

    public void open() {
        mDatabase = this.getWritableDatabase();
    }

    /*
    insert video data to database
     */
    public boolean insertOneVideoData(VideoModel myVideo) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.COLUMN_IMG_URL, myVideo.getImageUrl());
        cv.put(DatabaseManager.COLUMN_NAME, myVideo.getName());
        cv.put(DatabaseManager.COLUMN_VIDEO_ADDRESS, myVideo.getVideoAddress());
        cv.put(DatabaseManager.COLUMN_TYPE, myVideo.getType());
        return mDatabase.insert(DatabaseManager.TABLE_VIDEO, null, cv) != -1;
    }

    public List<VideoModel> getVideoDataFromDatabase(int type) {
        List<VideoModel> listVideo = new ArrayList<>();
        try {
            open();
            VideoModel newVideo;
            Cursor cursor;
            cursor = mDatabase
                .query(DatabaseManager.TABLE_VIDEO, null, COLUMN_TYPE + " = " + type, null, null,
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    newVideo = new VideoModel(cursor);
                    listVideo.add(newVideo);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return listVideo;
    }

    public List<VideoModel> getRandomVideos(int numberOfVideos) {
        List<VideoModel> listVideo = new ArrayList<>();
        try {
            open();
            VideoModel newVideo;
            Cursor cursor;
            cursor = mDatabase
                .query(DatabaseManager.TABLE_VIDEO, null, null, null, null, null, "RANDOM()",
                    numberOfVideos + "");
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    newVideo = new VideoModel(cursor);
                    listVideo.add(newVideo);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return listVideo;
    }

    //update video data
    public boolean updateVideo(VideoModel newVideo) {
        try {
            open();
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.COLUMN_VIDEO_URL, newVideo.getVideoUrl());
            return mDatabase
                .update(DatabaseManager.TABLE_VIDEO, values, COLUMN_ID + " = " + newVideo.getId(),
                    null) != 0;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    //delete all record
    public void deleteAllRecord() {
        try {
            open();
            mDatabase.execSQL(String.format("delete from %s", DatabaseManager.TABLE_VIDEO));
            mDatabase.execSQL(
                String.format("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '%s'", TABLE_VIDEO));
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void insertListVideoData(List<VideoModel> listVideo) {
        if (listVideo == null || listVideo.size() == 0) return;
        try {
            open();
            for (VideoModel item : listVideo) {
                insertOneVideoData(item);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
