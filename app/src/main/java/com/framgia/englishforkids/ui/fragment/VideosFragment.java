package com.framgia.englishforkids.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.data.model.VideoDataController;
import com.framgia.englishforkids.data.model.VideoModel;
import com.framgia.englishforkids.util.LoadItemsAdapter;
import com.framgia.englishforkids.util.ViewMode;

import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment {
    private static final String ARG_CATEGORY = "Category";
    private int mCategory;
    private VideoDataController mDataSource;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ViewMode mMode = ViewMode.GRID;
    private List<VideoModel> mListVideoModel;
    private int mNumberOfColumnsGrid;

    public static VideosFragment newInstance(int category) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt(ARG_CATEGORY);
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
        mListVideoModel = mDataSource.getVideoDataFromDatabase(mCategory);
        mNumberOfColumnsGrid = getResources().getInteger(R.integer.grid_number_of_columns);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), mNumberOfColumnsGrid);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LoadItemsAdapter(getActivity(), mListVideoModel, ViewMode.GRID);
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
        ((LoadItemsAdapter)mAdapter).updateData(filteredList);
    }
}
