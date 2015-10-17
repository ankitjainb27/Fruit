package com.housing.typeracer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class ConnectionUtils {

    public static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET};

    public static boolean isConnectedToNetwork() {
        ConnectivityManager connManager =
                (ConnectivityManager) MainApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    public static void startAdvertising(GoogleApiClient mGoogleApiClient, Connections.ConnectionRequestListener listener) {
        if (!isConnectedToNetwork()) {
            MainApplication.showToast(R.string.not_connected_to_network);
            return;
        }
        Nearby.Connections.stopAdvertising(mGoogleApiClient);
        // Identify that this device is the host
        MainApplication.mIsHost = true;

        // Advertising with an AppIdentifer lets other devices on the
        // network discover this application and prompt the user to
        // install the application.
        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(MainApplication.getContext().getPackageName()));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        // The advertising timeout is set to run indefinitely
        // Positive values represent timeout in milliseconds
        long NO_TIMEOUT = 0L;

        String name = null;
        Nearby.Connections.startAdvertising(mGoogleApiClient, name, appMetadata, NO_TIMEOUT, listener).setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
            @Override
            public void onResult(Connections.StartAdvertisingResult result) {
                if (result.getStatus().isSuccess()) {
                    MainApplication.showToast(R.string.advertising_now);
                } else {
                    MainApplication.showToast(R.string.something_went_wrong);
                }
            }
        });
    }


}
