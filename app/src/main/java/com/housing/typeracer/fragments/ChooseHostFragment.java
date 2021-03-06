package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.housing.typeracer.Constants;
import com.housing.typeracer.MainActivity;
import com.housing.typeracer.MainApplication;
import com.housing.typeracer.R;
import com.housing.typeracer.Serializer;
import com.housing.typeracer.adapters.ChooseHostRecyclerAdapter;
import com.housing.typeracer.listeners.RecyclerItemClickListener;
import com.housing.typeracer.models.Host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class ChooseHostFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ChooseHostFragment";
    private List<Host> myDataset;
    private ChooseHostRecyclerAdapter mAdapter;
    private RelativeLayout confirmedlayout;
    private TextView confirmedHdr;
    private GoogleApiClient mGoogleApiClient;
    private String myDeviceId, myEndpointId;

    public static ChooseHostFragment newInstance() {
        return new ChooseHostFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataset = new ArrayList<>();
        mGoogleApiClient = ((MainActivity) getActivityReference()).mGoogleApiClient;
        myDeviceId = Nearby.Connections.getLocalDeviceId(mGoogleApiClient);
        myEndpointId = Nearby.Connections.getLocalEndpointId(mGoogleApiClient);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivityReference()).setToolbarTitle("Join a game");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivityReference()).startDiscovery();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_host_fragment, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        confirmedlayout = (RelativeLayout) rootView.findViewById(R.id.confirmed_layout);
        confirmedHdr = (TextView) rootView.findViewById(R.id.hdr);
        rootView.findViewById(R.id.nudge_host_button).setOnClickListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityReference()));
        mAdapter = new ChooseHostRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivityReference(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Host data = myDataset.get(position);
                        ((MainActivity) getActivityReference()).connectToHost(data.getEndpointId(), myDeviceId, data.getServiceId());
                    }
                })
        );
    }

    public void newHostFount(String endpointId, String deviceId, final String serviceId, String endpointName) {
        myDataset.add(new Host(endpointId, deviceId, serviceId, endpointName));
        mAdapter.notifyDataSetChanged();
    }

    public void connectedToHost(String endpointId) {
        confirmedlayout.setVisibility(View.VISIBLE);
        confirmedHdr.setText(endpointId + " accepted your request!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nudge_host_button:
                nudgeHost();
                break;
        }
    }

    private void nudgeHost() {
        try {
            byte[] data = Serializer.serialize(Constants.NUDGE_HOST);
            Nearby.Connections.sendReliableMessage(((MainActivity) getActivityReference()).mGoogleApiClient, ((MainActivity) getActivityReference()).mRemoteHostEndpoint, data);
        } catch (IOException e) {
            e.printStackTrace();
            MainApplication.showToast(R.string.something_went_wrong);
        }
    }
}
