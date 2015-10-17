package com.housing.typeracer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Connections;
import com.housing.typeracer.fragments.AvatarFragment;
import com.housing.typeracer.fragments.BaseFragment;
import com.housing.typeracer.fragments.ChooseClientFragment;
import com.housing.typeracer.fragments.ChooseHostFragment;
import com.housing.typeracer.fragments.GetStartedFragment;
import com.housing.typeracer.fragments.LaunchFragment;
import com.housing.typeracer.fragments.LeaderboardFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener, Controller {

    // Identify if the device is the host
//    public boolean mIsHost = false;
    public GoogleApiClient mGoogleApiClient;
    public boolean mIsConnected;
    public String mRemoteHostEndpoint;
    private List<String> mRemotePeerEndpoints = new ArrayList<String>();
    private FrameLayout frameLayout;
    private FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;
    private int playersCount = 0;
    public String myDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();
        initViews();
    }

    private void initViews() {
        frameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
        initToolbar();
        initLaunchFragment();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setNavigationIcon(R.drawable.default_nav_icon_back);
        toolbar.setTitleTextAppearance(this, R.style.toolBarTitleStyle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setGravity(toolbar.getChildAt(0));
    }

    private void setGravity(View viewInToolBar) {
        if (viewInToolBar != null) {
            Toolbar.LayoutParams viewInToolBarLayoutParams = (Toolbar.LayoutParams) viewInToolBar
                    .getLayoutParams();
            if (viewInToolBarLayoutParams != null) {
                viewInToolBarLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                viewInToolBar.setLayoutParams(viewInToolBarLayoutParams);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initLaunchFragment() {
        if (MainApplication.getSharedPreferences().contains(MainApplication.prof_key)) {
            boolean isProfilePresent = MainApplication.getSharedPreferences().getBoolean(MainApplication.prof_key, false);
            if (isProfilePresent) {
                String userName = MainApplication.getSharedPreferences().getString(MainApplication.username_key, "Anon");
                int avatarId = MainApplication.getSharedPreferences().getInt(MainApplication.useravatar_key, 100);
                ((MainApplication) getApplication()).setUserName(userName);
                ((MainApplication) getApplication()).setAvatarId(avatarId);
                replaceFragmentInDefaultLayout(LaunchFragment.newInstance());
            } else {
                replaceFragmentInDefaultLayout(GetStartedFragment.newInstance());
            }
        } else {
            replaceFragmentInDefaultLayout(GetStartedFragment.newInstance());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        myDeviceId = Nearby.Connections.getLocalDeviceId(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionRequest(final String remoteEndpointId, final String remoteDeviceId, final String remoteEndpointName, final byte[] payload) {
        if (MainApplication.mIsHost && playersCount < Constants.TOTAL_PLAYERS) {
            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, remoteEndpointId, payload, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        if (!mRemotePeerEndpoints.contains(remoteEndpointId)) {
                            mRemotePeerEndpoints.add(remoteEndpointId);
                            addToUI(remoteEndpointId, remoteDeviceId, remoteEndpointName, payload);
                            playersCount++;
                        }

                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        } else {
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
        }
    }

    private void addToUI(String remoteEndpointId, String remoteDeviceId, String remoteEndpointName, byte[] payload) {
        BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        if (baseFragment instanceof ChooseClientFragment) {
            ((ChooseClientFragment) baseFragment).newClientFound(remoteEndpointId, remoteDeviceId, remoteEndpointName, payload);
        }
    }

    @Override
    public void onEndpointFound(String endpointId, String deviceId, final String serviceId, String endpointName) {

        BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        if (baseFragment instanceof ChooseHostFragment) {
            ((ChooseHostFragment) baseFragment).newHostFount(endpointId, deviceId, serviceId, endpointName);
        }

    }

    public void connectToHost(final String deviceId, String endpointId, final String serviceId) {
        byte[] payload = null;
        String name = "client1";
        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, name,
                endpointId, payload, new Connections.ConnectionResponseCallback() {

                    @Override
                    public void onConnectionResponse(String endpointId, Status status, byte[] bytes) {
                        if (status.isSuccess()) {
                            MainApplication.showToast("Connected to: " + endpointId);
                            Nearby.Connections.stopDiscovery(mGoogleApiClient, serviceId);
                            mRemoteHostEndpoint = endpointId;

                            if (!MainApplication.mIsHost) {
                                mIsConnected = true;
                                BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
                                if (baseFragment instanceof ChooseHostFragment) {
                                    ((ChooseHostFragment) baseFragment).connectedToHost(endpointId);
                                }
                            }
                        } else {
                            Log.d("ERROR", "connection failed");
                            MainApplication.showToast("Connection to " + endpointId + " failed");
                            if (!MainApplication.mIsHost) {
                                mIsConnected = false;
                            }
                        }
                    }
                }, this);
    }

    @Override
    public void onEndpointLost(String s) {
        MainApplication.showToast("endpoint lost");
    }

    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        if (isReliable && !MainApplication.mIsHost) {
            try {
                Object obj = Serializer.deserialize(payload);
                if (obj instanceof HashMap) {
                    Map<String, String> userName = (HashMap<String, String>) obj;
                    showToastToUser(userName);
                    MainApplication.USER_NAME.putAll(userName);
                    for (String key : userName.keySet()) {
                        MainApplication.USER_SCORE.put(key, 0);
                        MainApplication.showToast("RELIABLE DATA : " + key);
                    }
                } else if (obj instanceof String) {
                    String data = (String) obj;
                    if (data.equalsIgnoreCase(Constants.START_GAME)) {
                        openGameScreen();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isReliable && MainApplication.mIsHost) {
            try {
                Object obj = Serializer.deserialize(payload);
                if (obj instanceof String) {
                    String data = (String) obj;
                    if (data.equalsIgnoreCase(Constants.NUDGE_HOST)) {
                        showNudgeMessage(endpointId);
                    }
                }
            } catch (Exception p) {

            }
        } else if (!isReliable && MainApplication.mIsHost) {
            try {
                Object obj = Serializer.deserialize(payload);
                if (obj instanceof Integer) {
                    String deviceId = getDeviceIdFromEndpoint(endpointId);
                    if (deviceId != null) {
                        MainApplication.USER_SCORE.put(deviceId, (Integer) obj);

                    }
                }
            } catch (Exception p) {
            }
        } else if (!isReliable && !MainApplication.mIsHost) {
            try {
                Object obj = Serializer.deserialize(payload);
                if (obj instanceof Map) {
                    Map<String, Integer> data = (HashMap<String, Integer>) obj;
                    MainApplication.USER_SCORE.putAll(data);
                }
            } catch (Exception p) {

            }
        }
    }

    private String getDeviceIdFromEndpoint(String endpoint) {
        for (String key : MainApplication.USER_REMOTE_ENDPOINT.keySet()) {
            if (null != (MainApplication.USER_REMOTE_ENDPOINT.get(key)) && (MainApplication.USER_REMOTE_ENDPOINT.get(key)).equalsIgnoreCase(endpoint)) {
                return MainApplication.USER_NAME.get(key);
            }
        }
        return null;
    }

    private void showNudgeMessage(String endpointId) {
        String name = getDeviceIdFromEndpoint(endpointId);
        if (name != null) {
            MainApplication.showToast(name + " wants to start the game.");
        }
    }

    private void showToastToUser(Map<String, String> userName) {
        for (String key : userName.keySet()) {
            String name = MainApplication.USER_NAME.get(key);
            if (name != null && myDeviceId != null && !key.equalsIgnoreCase(myDeviceId)) {
                MainApplication.showToast(name + " also joined!");
            }
        }
    }

    @Override
    public void onDisconnected(String s) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void startDiscovery() {
        if (!ConnectionUtils.isConnectedToNetwork()) {
            MainApplication.showToast(R.string.not_connected_to_network);
            return;
        }

        // Identify that this device is NOT the host
        MainApplication.mIsHost = false;
        String serviceId = MainApplication.getContext().getString(R.string.service_id);

        // Set an appropriate timeout length in milliseconds
        long DISCOVER_TIMEOUT = 1000L;

        Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, DISCOVER_TIMEOUT, MainActivity.this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                        } else {
                            MainApplication.showToast(R.string.something_went_wrong);
                        }
                    }
                });
    }

    public void replaceFragmentInDefaultLayout(BaseFragment fragmentToBeLoaded) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, fragmentToBeLoaded,
                fragmentToBeLoaded.getName());
        fragmentTransaction.addToBackStack(fragmentToBeLoaded.getName());
        fragmentTransaction.commit();

    }

    @Override
    public void performOperation(int operation, Object input) {
        switch (operation) {
            case OPEN_LAUNCH_FRAGMENT:
                replaceFragmentInDefaultLayout(LaunchFragment.newInstance());
                break;
            case OPEN_CHOOSE_HOST_FRAGMENT:
                replaceFragmentInDefaultLayout(ChooseHostFragment.newInstance());
                break;
            case OPEN_CHOOSE_CLIENT_FRAGMENT:
                replaceFragmentInDefaultLayout(ChooseClientFragment.newInstance());
                break;
            case OPEN_AVATAR_SCREEN:
                replaceFragmentInDefaultLayout(AvatarFragment.newInstance());
            case OPEN_GAME_FRAGMENT:
                openGameScreen();
                break;
            case OPEN_LEADERBOARD:
                replaceFragmentInDefaultLayout(LeaderboardFragment.newInstance());
                break;

        }
    }

    @Override
    public Fragment getCurrentFragment() {
        return null;
    }

    @Override
    public Fragment getTopFragmentInBackStack() {
        return null;
    }

    @Override
    public ViewGroup getFragmentContainer() {
        return null;
    }

    @Override
    public void popBackStackIfForeground() {

    }

    @Override
    public void popBackStack() {

    }

    @Override
    public void clearBackStack(boolean isInclusive) {

    }

    @Override
    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }

    @Override
    public void onClick(View v) {

    }

    public void startAdvertising() {
        ConnectionUtils.startAdvertising(mGoogleApiClient, MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            manager.popBackStack();
        } else {
            finish();
        }
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void openGameScreen() {
        replaceFragmentInDefaultLayout(GameFragment.newInstance());
    }

    public void openWifiSettings() {
        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

}
