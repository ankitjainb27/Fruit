package com.housing.typeracer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.housing.typeracer.fragments.BaseFragment;
import com.housing.typeracer.fragments.LaunchFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener, Controller {

    // Identify if the device is the host
    private boolean mIsHost = false;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsConnected;
    private String mRemoteHostEndpoint;
    private List<String> mRemotePeerEndpoints = new ArrayList<String>();
    private FrameLayout frameLayout;
    private FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        setSupportActionBar(toolbar);
    }

    private void initLaunchFragment() {
        replaceFragmentInDefaultLayout(LaunchFragment.newInstance());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionRequest(final String remoteEndpointId, final String remoteDeviceId, final String remoteEndpointName, byte[] payload) {
        if (mIsHost) {
            Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, remoteEndpointId, payload, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        if (!mRemotePeerEndpoints.contains(remoteEndpointId)) {
                            mRemotePeerEndpoints.add(remoteEndpointId);
                        }

                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        MainApplication.showToast(remoteDeviceId + " connected!");
                    }
                }
            });
        } else {
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
        }
    }

    @Override
    public void onEndpointFound(String endpointId, String deviceId, final String serviceId, String endpointName) {
        MainApplication.showToast("endpoint found callback called");
        byte[] payload = null;

        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, deviceId,
                endpointId, payload, new Connections.ConnectionResponseCallback() {

                    @Override
                    public void onConnectionResponse(String endpointId, Status status, byte[] bytes) {
                        if (status.isSuccess()) {
                            MainApplication.showToast("Connected to: " + endpointId);
                            Nearby.Connections.stopDiscovery(mGoogleApiClient, serviceId);
                            mRemoteHostEndpoint = endpointId;

                            if (!mIsHost) {
                                mIsConnected = true;
                            }
                        } else {
                            MainApplication.showToast("Connection to " + endpointId + " failed");
                            if (!mIsHost) {
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
    public void onMessageReceived(String s, byte[] bytes, boolean b) {

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
                            MainApplication.showToast(R.string.hosts_found);
                        } else {
                            int statusCode = status.getStatusCode();
                            MainApplication.showToast(R.string.something_went_wrong);
                            Log.d("startDiscovery", status.getStatusMessage());
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
    public void onClick(View v) {

    }

    public void startAdvertising() {
        ConnectionUtils.startAdvertising(mGoogleApiClient, MainActivity.this);
    }
}
