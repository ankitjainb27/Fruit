package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.housing.typeracer.R;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class LeaderboardFragment extends BaseFragment {


    public static LeaderboardFragment newInstance() {
        return new LeaderboardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.leaderboard, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {

    }
}
