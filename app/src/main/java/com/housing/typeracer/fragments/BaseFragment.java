package com.housing.typeracer.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.housing.typeracer.Controller;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class BaseFragment extends Fragment implements UiFragment {

    private Controller fragmentControllerActivity;
    protected FragmentActivity activity;

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }

    public Controller getFragmentController() {
        if (fragmentControllerActivity == null) {
            fragmentControllerActivity = (Controller) getActivity();
        }
        return fragmentControllerActivity;
    }

    protected FragmentActivity getActivityReference() {
        if (activity == null) {
            activity = getActivity();
        }
        return activity;
    }
}
