package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.housing.typeracer.ConnectionUtils;
import com.housing.typeracer.Controller;
import com.housing.typeracer.MainActivity;
import com.housing.typeracer.R;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class LaunchFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private RelativeLayout noWifiLayout;

    public static LaunchFragment newInstance() {
        return new LaunchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.launch_screen, container, false);
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.main_layout);
        noWifiLayout = (RelativeLayout) rootView.findViewById(R.id.no_wifi_layout);
        rootView.findViewById(R.id.host_game_button).setOnClickListener(this);
        rootView.findViewById(R.id.discover_game_button).setOnClickListener(this);
        rootView.findViewById(R.id.wifi_connect).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivityReference()).setToolbarTitle("Type Rush");
        if (!ConnectionUtils.isConnectedToNetwork()) {
            noWifiLayout.setVisibility(View.VISIBLE);
        } else {
            noWifiLayout.setVisibility(View.GONE);
        }
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
            case R.id.wifi_connect:
                openSettings();
                break;
        }
    }

    private void openSettings() {
        ((MainActivity) getActivityReference()).openWifiSettings();
    }
}
