package com.framgia.englishforkids.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.data.model.VideoModel;
import com.framgia.englishforkids.ui.activity.DisplayVideoActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by hoang on 12/27/2016.
 */
public class LoadItemsAdapter extends RecyclerView.Adapter<LoadItemsAdapter.ViewHolder> {
    private Context mContext;
    private List<VideoModel> mListVideoModel;
    private ViewMode mViewMode;
    private LayoutInflater mInflater;

    public LoadItemsAdapter(Context context, List<VideoModel> listVideoModel, ViewMode viewMode) {
        mContext = context;
        mListVideoModel = listVideoModel;
        mViewMode = viewMode;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater
            .inflate(mViewMode == ViewMode.GRID ? R.layout.card_item_grid : R.layout.card_item_list,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mListVideoModel.get(position));
    }

    @Override
    public int getItemCount() {
        return mListVideoModel != null ? mListVideoModel.size() : 0;
    }

    public void updateData(List<VideoModel> listVideoModel) {
        mListVideoModel = listVideoModel;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextView;
        private VideoModel mVideoModel;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
            view.setOnClickListener(this);
        }

        public void bindData(VideoModel videoModel) {
            if (videoModel == null) return;
            mVideoModel = videoModel;
            Picasso.with(mImageView.getContext()).load(videoModel.getImageUrl())
                .into(mImageView);
            mTextView.setText(videoModel.getName());
        }

        @Override
        public void onClick(View view) {
            // TODO: enter view video activity
            new ParseUrlVideo(view.getContext())
                .execute(mVideoModel.getVideoAddress());
        }

        private void startDisplayActivity(Context context, String video_url) {
            mVideoModel.setVideoUrl(video_url);
            context.startActivity(DisplayVideoActivity.getDisplayVideoIntent(context, mVideoModel));
        }

        public class ParseUrlVideo extends AsyncTask<String, Void, String> {
            private Context mContext;

            public ParseUrlVideo(Context context) {
                mContext = context;
            }

            @Override
            protected String doInBackground(String... strings) {
                String videoUrl = null;
                try {
                    videoUrl = JsoupPaser.getInstance().paserVideoId(strings[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return videoUrl;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    startDisplayActivity(mContext, s);
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.warning),
                        Toast
                            .LENGTH_LONG).show();
                }
            }
        }
    }
}
