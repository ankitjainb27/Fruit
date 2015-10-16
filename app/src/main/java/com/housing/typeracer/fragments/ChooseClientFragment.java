package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.housing.typeracer.MainApplication;
import com.housing.typeracer.R;
import com.housing.typeracer.adapters.ChooseClientRecyclerAdapter;
import com.housing.typeracer.models.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class ChooseClientFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ChooseClientFragment";
    private RecyclerView mRecyclerView;
    private List<Client> myDataset;
    private ChooseClientRecyclerAdapter mAdapter;
    private Button startGameButton;

    public static ChooseClientFragment newInstance() {
        return new ChooseClientFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataset = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_client_fragment, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        startGameButton = (Button) rootView.findViewById(R.id.start_game_button);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityReference()));
        mAdapter = new ChooseClientRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void newClientFound(String remoteEndpointId, String remoteDeviceId, String remoteEndpointName, byte[] payload) {
        myDataset.add(new Client(remoteEndpointId, remoteDeviceId, remoteEndpointName, payload));
        mAdapter.notifyDataSetChanged();
        if (myDataset.size() > 0) {
            startGameButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_game_button:
                MainApplication.showToast("starting the game");
                break;
        }
    }
}
