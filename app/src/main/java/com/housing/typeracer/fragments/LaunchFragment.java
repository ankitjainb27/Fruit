package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.nearby.Nearby;
import com.housing.typeracer.Controller;
import com.housing.typeracer.MainActivity;
import com.housing.typeracer.R;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class LaunchFragment extends BaseFragment implements View.OnClickListener {

    public static LaunchFragment newInstance() {
        return new LaunchFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        Nearby.Connections.stopAdvertising(((MainActivity) getActivityReference()).mGoogleApiClient);
        Nearby.Connections.disconnectFromEndpoint(((MainActivity) getActivityReference()).mGoogleApiClient, "Back pressed");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.launch_screen, container, false);
        rootView.findViewById(R.id.host_game_button).setOnClickListener(this);
        rootView.findViewById(R.id.discover_game_button).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivityReference()).setToolbarTitle("Type Rush");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.host_game_button:
                getFragmentController().performOperation(Controller.OPEN_CHOOSE_CLIENT_FRAGMENT, null);
                break;
            case R.id.discover_game_button:
                getFragmentController().performOperation(Controller.OPEN_CHOOSE_HOST_FRAGMENT, null);
                break;
        }
    }
}
