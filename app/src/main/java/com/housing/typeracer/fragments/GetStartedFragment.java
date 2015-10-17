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
 * Created by gayathri_nair on 17/10/15.
 */
public class GetStartedFragment extends BaseFragment implements View.OnClickListener {

    public static GetStartedFragment newInstance() {
        return new GetStartedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.get_started, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        rootView.findViewById(R.id.started_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.started_button:
                getFragmentController().performOperation(Controller.OPEN_AVATAR_SCREEN, null);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivityReference()).hideToolbar();
    }
}
