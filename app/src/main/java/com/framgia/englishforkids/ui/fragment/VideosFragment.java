package com.framgia.englishforkids.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.data.model.VideoDataController;
import com.framgia.englishforkids.data.model.VideoModel;
import com.framgia.englishforkids.util.Constant;
import com.framgia.englishforkids.util.JsoupPaser;
import com.framgia.englishforkids.util.LoadItemsAdapter;
import com.framgia.englishforkids.util.ViewMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment {
    private static final String ARG_CATEGORY = "Category";
    private static final String ARG_NUMBER_OF_RANDOM_VIDEOS = "Number";
    private int mCategoryId;
    private int mNumberOfRandomVideos;
    private VideoDataController mDataSource;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ViewMode mMode = ViewMode.GRID;
    private List<VideoModel> mListVideoModel;
    private int mNumberOfColumnsGrid;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static VideosFragment newInstance(int category) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    public static VideosFragment newInstanceRandomVideos(int numberOfRandomVideos) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER_OF_RANDOM_VIDEOS, numberOfRandomVideos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(ARG_CATEGORY);
            mNumberOfRandomVideos = getArguments().getInt(ARG_NUMBER_OF_RANDOM_VIDEOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        mDataSource = new VideoDataController(getActivity());
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        if (mCategoryId != 0) {
            mListVideoModel = mDataSource.getVideoDataFromDatabase(mCategoryId);
            mNumberOfColumnsGrid = getResources().getInteger(R.integer.grid_number_of_columns);
            mLayoutManager = new GridLayoutManager(getActivity(), mNumberOfColumnsGrid);
            mAdapter = new LoadItemsAdapter(getActivity(), mListVideoModel, ViewMode.GRID);
            mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new PaserAsyncTask().execute();
                }
            });
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        } else if (mNumberOfRandomVideos != 0) {
            mListVideoModel = mDataSource.getRandomVideos(mNumberOfRandomVideos);
            mLayoutManager = new GridLayoutManager(getActivity(), 1);
            mAdapter = new LoadItemsAdapter(getActivity(), mListVideoModel, ViewMode.LIST);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void changeViewMode() {
        mMode = mMode == ViewMode.LIST ? ViewMode.GRID : ViewMode.LIST;
        mLayoutManager = new GridLayoutManager(getActivity(),
            mMode == ViewMode.LIST ? 1 : mNumberOfColumnsGrid);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LoadItemsAdapter(getActivity(), mListVideoModel, mMode);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void search(String searchString) {
        List<VideoModel> filteredList = new ArrayList<VideoModel>();
        for (VideoModel videoModel : mListVideoModel) {
            if (videoModel.getName().toLowerCase().contains(searchString))
                filteredList.add(videoModel);
        }
        ((LoadItemsAdapter) mAdapter).updateData(filteredList);
    }

    public class PaserAsyncTask extends AsyncTask<Void, Void, List<VideoModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<VideoModel> doInBackground(Void... voids) {
            List<VideoModel> listSong;
            List<VideoModel> listStory;
            try {
                listSong = JsoupPaser.getInstance().paserSongs();
                listStory = JsoupPaser.getInstance().paserSories();
                if (listSong != null && listSong.size() > 0) {
                    mDataSource.deleteAllRecord();
                    mDataSource.insertListVideoData(listSong);
                    mDataSource.insertListVideoData(listStory);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return mCategoryId == Constant.SONG_TYPE ? listSong : listStory;
        }

        @Override
        protected void onPostExecute(List<VideoModel> listVideoModel) {
            super.onPostExecute(listVideoModel);
            if (listVideoModel != null) {
                mListVideoModel.clear();
                mListVideoModel.addAll(listVideoModel);
                mAdapter.notifyDataSetChanged();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
