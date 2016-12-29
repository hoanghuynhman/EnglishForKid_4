package com.framgia.englishforkids.data.model;

import android.database.Cursor;

import com.framgia.englishforkids.util.DatabaseManager;

/**
 * Created by GIAKHANH on 12/28/2016.
 */

public class VideoModel {
    private int mId;
    private String mName;
    private String mImageUrl;
    private String mVideoAddress;
    private String mVideoUrl;
    private int mType;

    public VideoModel() {
    }

    public VideoModel(String name, String imageUrl, String videoAddress, int type) {
        mName = name;
        mImageUrl = imageUrl;
        mVideoAddress = videoAddress;
        mType = type;
    }

    public VideoModel(int id, String name, String imageUrl, String videoAddress, String videoUrl, int type) {
        mId = id;
        mName = name;
        mImageUrl = imageUrl;
        mVideoAddress = videoAddress;
        mVideoUrl = videoUrl;
        mType = type;
    }

    public VideoModel(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_ID));
        mName = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_NAME));
        mImageUrl = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_IMG_URL));
        mVideoAddress = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_VIDEO_ADDRESS));
        mType = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COLUMN_TYPE));
        mVideoUrl = cursor.getString(cursor.getColumnIndex(DatabaseManager.COLUMN_VIDEO_URL));
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getVideoAddress() {
        return mVideoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        mVideoAddress = videoAddress;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public String toString() {
        return mId + "- " + mName + " - " + mVideoAddress + " - " + mImageUrl;
    }
}
