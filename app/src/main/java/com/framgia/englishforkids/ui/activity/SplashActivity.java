package com.framgia.englishforkids.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.data.model.VideoDataController;
import com.framgia.englishforkids.data.model.VideoModel;
import com.framgia.englishforkids.util.JsoupPaser;

import java.io.IOException;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new PaserAsyncTask().execute();
    }

    public class PaserAsyncTask extends AsyncTask<Void, Void, Void> {
        public VideoDataController mDataSource;

        public PaserAsyncTask() {
            mDataSource = new VideoDataController(SplashActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<VideoModel> listSong = JsoupPaser.getInstance().paserSongs();
                List<VideoModel> listStory = JsoupPaser.getInstance().paserSories();
                if (listSong != null && listSong.size() > 0) {
                    mDataSource.deleteAllRecord();
                    mDataSource.insertListVideoData(listSong);
                    mDataSource.insertListVideoData(listStory);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}
