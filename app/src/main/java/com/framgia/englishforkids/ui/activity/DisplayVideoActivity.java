package com.framgia.englishforkids.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.data.model.VideoDataController;
import com.framgia.englishforkids.data.model.VideoModel;
import com.framgia.englishforkids.ui.fragment.VideosFragment;
import com.framgia.englishforkids.util.Constant;
import com.framgia.englishforkids.util.Convert;

public class DisplayVideoActivity extends AppCompatActivity implements View.OnTouchListener, SeekBar
    .OnSeekBarChangeListener, View.OnClickListener {
    private static final String PREF_POSITION = "PREF_POSITION";
    private Toolbar mToolbar;
    private VideoView mVideoView;
    private LinearLayout mLLStatus;
    private SeekBar mSeekBar;
    private ImageView mImgvStatus;
    private ImageView mImgvFullScreen;
    private TextView mTvTime;
    private boolean mIsFullScreen;
    private Handler mHandler = new Handler();
    private VideoDataController mVideoDataController;
    private VideoModel mVideoModel;
    private String mVideoUrl;
    private RelativeLayout mRlSuggestVideo;
    private ProgressDialog mProgressDialog;
    private Runnable mUpdateSeekBarTask = new Runnable() {
        @Override
        public void run() {
            if (!mVideoView.isPlaying()) return;
            mTvTime.setText(
                Convert.convertTime(mVideoView.getCurrentPosition() / 1000) + "/" +
                    Convert.convertTime
                        (mVideoView.getDuration() / 1000));
            mSeekBar.setProgress(mVideoView.getCurrentPosition());
            mHandler.postDelayed(this, Constant.DELAY_TIME);
        }
    };

    public static Intent getDisplayVideoIntent(Context context, VideoModel videoModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.VIDEO_DATA, videoModel);
        Intent intent = new Intent(context, DisplayVideoActivity.class);
        intent.putExtra(Constant.BUNDLE_DATA, bundle);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);
        getData();
        initPosition();
        initViews();
        initVideo();
        checkOrientation();
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.BUNDLE_DATA);
        mVideoModel = (VideoModel) bundle.getSerializable(Constant.VIDEO_DATA);
        mVideoDataController = new VideoDataController(this);
        mVideoDataController.updateVideo(mVideoModel);
        mVideoUrl = mVideoModel.getVideoUrl();
    }

    private void initPosition() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(PREF_POSITION,
            100).apply();
    }

    private void savePosition() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(PREF_POSITION,
            mVideoView.getCurrentPosition()).apply();
        mVideoView.pause();
        mImgvStatus.setImageResource(R.drawable.ic_play);
    }

    private void restorePosition() {
        final int position =
            PreferenceManager.getDefaultSharedPreferences(this).getInt(PREF_POSITION, 0);
        preparedVideo(position);
    }

    private void preparedVideo(final int position) {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnBufferingUpdateListener(
                    new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                            mSeekBar.setSecondaryProgress(i * mVideoView.getDuration() / 100);
                        }
                    });
                setupSeekBar();
                mVideoView.seekTo(position);
                updateSeekBar();
                mProgressDialog.dismiss();
                mVideoView.pause();
            }
        });
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLLStatus = (LinearLayout) findViewById(R.id.ll_status_bar);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mImgvStatus = (ImageView) findViewById(R.id.imv_status);
        mImgvFullScreen = (ImageView) findViewById(R.id.imgv_fullscreen);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mVideoView.setOnTouchListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
        mImgvStatus.setOnClickListener(this);
        mImgvFullScreen.setOnClickListener(this);
        setupToolBar();
        mRlSuggestVideo = (RelativeLayout) findViewById(R.id.relative_suggest_video);
        initSuggestVideoLayout();
        initProgressDialog();
    }

    private void initVideo() {
        mVideoView.setVideoURI(Uri.parse(String.format(Constant.URL_VIDEO, mVideoUrl)));
        mVideoView.requestFocus();
    }

    private void setupSeekBar() {
        mSeekBar.setMax(mVideoView.getDuration());
        mSeekBar.setProgress(0);
    }

    private void updateSeekBar() {
        mHandler.postDelayed(mUpdateSeekBarTask, Constant.DELAY_TIME);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        checkOrientation();
    }

    private void setFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setVisibility(View.GONE);
        mRlSuggestVideo.setVisibility(View.GONE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        mVideoView.setLayoutParams(params);
        mIsFullScreen = true;
    }

    private void setToDefaultLayout() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setVisibility(View.VISIBLE);
        mRlSuggestVideo.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = getResources().getDimensionPixelSize(R
            .dimen.dp_300);
        params.leftMargin = 0;
        mVideoView.setLayoutParams(params);
        mIsFullScreen = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mLLStatus
            .setVisibility(mLLStatus.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        return false;
    }

    public void toggle() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mImgvStatus.setImageResource(R.drawable.ic_play);
        } else {
            updateSeekBar();
            mVideoView.start();
            mImgvStatus.setImageResource(R.drawable.ic_pause);
        }
    }

    public void doFullScreen() {
        if (mIsFullScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mImgvFullScreen.setImageResource(R.drawable.ic_fullscreen);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mImgvFullScreen.setImageResource(R.drawable.ic_restore);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePosition();
        mImgvStatus.setImageResource(R.drawable.ic_play);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restorePosition();
        mImgvStatus.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mVideoView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_status:
                toggle();
                break;
            case R.id.imgv_fullscreen:
                doFullScreen();
                break;
            default:
                break;
        }
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(mVideoModel.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initSuggestVideoLayout() {
        VideosFragment videosFragment = VideosFragment.newInstanceRandomVideos(Constant
            .NUMBER_OF_RANDOM_VIDEOS);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
            .replace(R.id.relative_suggest_video, videosFragment,
                videosFragment.getTag())
            .commit();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this, R.style.CustomDialogTheme);
        mProgressDialog.setTitle(this.getResources().getString(R.string.title_wait));
        mProgressDialog
            .setMessage(this.getResources().getString(R.string.title_loading));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    public void checkOrientation() {
        if (this.getResources().getConfiguration().orientation ==
            Configuration.ORIENTATION_LANDSCAPE) {
            setFullScreen();
        } else {
            setToDefaultLayout();
        }
    }
}

