package com.framgia.englishforkids.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.data.model.VideoModel;
import com.squareup.picasso.Picasso;

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

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
            mImageView.setOnClickListener(this);
            mTextView.setOnClickListener(this);
        }

        public void bindData(VideoModel videoModel) {
            if (videoModel == null) return;
            Picasso.with(mImageView.getContext()).load(videoModel.getImageUrl())
                .into(mImageView);
            mTextView.setText(videoModel.getName());
        }

        @Override
        public void onClick(View view) {
            // TODO: enter view video activity
        }
    }
}
