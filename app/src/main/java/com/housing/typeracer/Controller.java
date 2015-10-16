package com.housing.typeracer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

public interface Controller {

    int OPEN_LAUNCH_FRAGMENT = 101;

    void performOperation(final int operation, Object input);

    Fragment getCurrentFragment();

    FragmentManager getSupportFragmentManager();

    void onBackPressed();

    Fragment getTopFragmentInBackStack();

    ViewGroup getFragmentContainer();

    void popBackStackIfForeground();

    void popBackStack();

    void clearBackStack(boolean isInclusive);

    int getStatusBarHeight();
}
