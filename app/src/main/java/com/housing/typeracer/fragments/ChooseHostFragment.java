package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.housing.typeracer.R;
import com.housing.typeracer.adapters.ChooseHostRecyclerAdapter;
import com.housing.typeracer.models.Host;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class ChooseHostFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private List<Host> myDataset;
    private ChooseHostRecyclerAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataset = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_host_fragment, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityReference()));
        mAdapter = new ChooseHostRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void newHostFount(String endpointId, String deviceId, final String serviceId, String endpointName) {
        myDataset.add(new Host(endpointId, deviceId, serviceId, endpointName));
        mAdapter.notifyDataSetChanged();
    }
}
