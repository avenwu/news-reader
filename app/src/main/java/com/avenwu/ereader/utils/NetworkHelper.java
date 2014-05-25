package com.avenwu.ereader.utils;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.avenwu.ereader.receiver.NetworkReceiver;

public class NetworkHelper {
    private static NetworkHelper sInstance = new NetworkHelper();
    public static String WIFI = "wifi";
    public static String ANY = "any";
    public static String CURENT_NETWORK_TYPE;
    public static boolean REFRESH_CONFIG = false;
    public static boolean WIFI_CONNECTED;
    public static boolean MOBILE_CONNECTED;
    private NetworkReceiver mReceiver;

    public static NetworkHelper getInstance() {
        return sInstance;
    }

    public void register(Context context) {
        mReceiver = new NetworkReceiver();
        context.registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        updateConnectionState(context);
    }

    public void unregister(Context context) {
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }

    private NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
    }

    private void updateConnectionState(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null && networkInfo.isConnected()) {
            WIFI_CONNECTED = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            MOBILE_CONNECTED = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            WIFI_CONNECTED = false;
            MOBILE_CONNECTED = false;
        }
        CURENT_NETWORK_TYPE = WIFI_CONNECTED ? WIFI : ANY;
    }

    public static boolean isNetworkActive() {
        return WIFI_CONNECTED || MOBILE_CONNECTED;

    }
}
