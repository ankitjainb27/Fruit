package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                ((MainActivity) getActivityReference()).startAdvertising();
                break;
            case R.id.discover_game_button:
                getFragmentController().performOperation(Controller.OPEN_CHOOSE_HOST_FRAGMENT, null);
                break;
        }
    }
}
