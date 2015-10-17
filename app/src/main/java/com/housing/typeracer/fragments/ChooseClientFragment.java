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
import com.housing.typeracer.Controller;
import com.housing.typeracer.MainActivity;
import com.housing.typeracer.MainApplication;
import com.housing.typeracer.R;
import com.housing.typeracer.Serializer;
import com.housing.typeracer.adapters.ChooseClientRecyclerAdapter;
import com.housing.typeracer.models.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class ChooseClientFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ChooseClientFragment";
    private List<Client> myDataset;
    private ChooseClientRecyclerAdapter mAdapter;
    private GoogleApiClient googleApiClient;
    private String myDeviceId;
    private String myRemoteId;
    private RelativeLayout noClients, recyclerHolder;

    public static ChooseClientFragment newInstance() {
        return new ChooseClientFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataset = new ArrayList<>();
        googleApiClient = ((MainActivity) getActivityReference()).mGoogleApiClient;
        myDeviceId = Nearby.Connections.getLocalDeviceId(googleApiClient);
        myRemoteId = Nearby.Connections.getLocalEndpointId(googleApiClient);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_client_fragment, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivityReference()).setToolbarTitle("Host a game");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivityReference()).startAdvertising();
    }

    private void initViews(View rootView) {
        rootView.findViewById(R.id.start_game_button).setOnClickListener(this);
        rootView.findViewById(R.id.dismiss_game_button).setOnClickListener(this);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        noClients = (RelativeLayout) rootView.findViewById(R.id.rl_no_client);
        recyclerHolder = (RelativeLayout) rootView.findViewById(R.id.rl_recycler_content);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityReference()));
        mAdapter = new ChooseClientRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        MainApplication.resetGameUsers();
        addToGameUsers(myDeviceId, MainApplication.getSharedPreferences().getString(Constants.USER_NAME, "Host"), myRemoteId);

        ((TextView) rootView.findViewById(R.id.games_name)).setText(MainApplication.getSharedPreferences().getString(Constants.USER_NAME, "Host") + "'s Game");
        ((TextView) rootView.findViewById(R.id.games_name_init)).setText(MainApplication.getSharedPreferences().getString(Constants.USER_NAME, "Host") + "'s Game Created");
    }

    public void newClientFound(String remoteEndpointId, String remoteDeviceId, String remoteEndpointName, byte[] payload) {
        myDataset.add(new Client(remoteEndpointId, remoteDeviceId, remoteEndpointName, payload));
        mAdapter.notifyDataSetChanged();
        addToGameUsers(remoteDeviceId, remoteEndpointName, remoteEndpointId);
        try {
            byte[] fileByteArray = Serializer.serialize(MainApplication.USER_NAME);
            for (String device :
                    MainApplication.USER_NAME.keySet()) {
                if (!device.equalsIgnoreCase(myDeviceId)) {
                    Nearby.Connections.sendReliableMessage(googleApiClient, MainApplication.USER_REMOTE_ENDPOINT.get(device), fileByteArray);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (myDataset.size() > 0) {
            noClients.setVisibility(View.GONE);
            recyclerHolder.setVisibility(View.VISIBLE);
        } else {
            noClients.setVisibility(View.VISIBLE);
            recyclerHolder.setVisibility(View.GONE);
        }
    }


    private void addToGameUsers(String deviceId, String name, String remoteEndPoint) {
        MainApplication.USER_NAME.put(deviceId, name);
        MainApplication.USER_SCORE.put(deviceId, 0);
        MainApplication.USER_REMOTE_ENDPOINT.put(deviceId, remoteEndPoint);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_game_button:
                startGame();
                break;
            case R.id.dismiss_game_button:
                ((MainActivity) getActivityReference()).onBackPressed();
                break;
        }
    }

    private void startGame() {
        try {
            byte[] startgame = Serializer.serialize(Constants.START_GAME);
            for (String device :
                    MainApplication.USER_NAME.keySet()) {
                if (!device.equalsIgnoreCase(myDeviceId)) {
                    Nearby.Connections.sendReliableMessage(googleApiClient, MainApplication.USER_REMOTE_ENDPOINT.get(device), startgame);
                }
            }
            getFragmentController().performOperation(Controller.OPEN_GAME_FRAGMENT, null);
        } catch (IOException e) {
            e.printStackTrace();
            MainApplication.showToast(R.string.something_went_wrong);
        }
    }
}
