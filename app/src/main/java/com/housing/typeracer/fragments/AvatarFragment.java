package com.housing.typeracer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.housing.typeracer.Controller;
import com.housing.typeracer.MainActivity;
import com.housing.typeracer.MainApplication;
import com.housing.typeracer.R;
import com.housing.typeracer.adapters.GridViewAdapter;


/**
 * Created by gayathri_nair on 17/10/15.
 */
public class AvatarFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private GridView gridView;
    private EditText et;
    private boolean avatarSelected = false;
    private int selectedAvatar;
    private String selectedUserName;

    public static AvatarFragment newInstance() {
        return new AvatarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.avatar_screen, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        et = (EditText) rootView.findViewById(R.id.username);
        rootView.findViewById(R.id.proceed).setOnClickListener(this);
        GridViewAdapter adap = new GridViewAdapter(getActivityReference());
        gridView.setAdapter(adap);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed:
                validate();
                break;
        }
    }

    private void validate() {
        String userName = et.getEditableText().toString();
        if (TextUtils.isEmpty(userName)) {
            MainApplication.showToast("Yo! Type your username!");
            return;
        }
        if (!avatarSelected) {
            MainApplication.showToast("Yo! Choose a cool avatar!");
            return;
        }
        selectedUserName = userName;
        MainApplication.putInSharedPref(MainApplication.prof_key, true);
        MainApplication.putInSharedPref(MainApplication.username_key, selectedUserName);
        MainApplication.putInSharedPref(MainApplication.useravatar_key, selectedAvatar + 1);
        avatarSelected = false;
        getFragmentController().performOperation(Controller.OPEN_LAUNCH_FRAGMENT, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivityReference()).showToolbar();
        ((MainActivity) getActivityReference()).setToolbarTitle("Pick your Avatar");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            ImageView check = (ImageView) v.findViewById(R.id.grid_check);
            if (check.getVisibility() == View.VISIBLE) {
                check.setVisibility(View.GONE);
            }
        }
        view.findViewById(R.id.grid_check).setVisibility(View.VISIBLE);
        avatarSelected = true;
        selectedAvatar = position;
    }
}
