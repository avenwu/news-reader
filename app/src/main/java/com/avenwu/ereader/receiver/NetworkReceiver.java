package com.avenwu.ereader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.avenwu.ereader.R;
import com.avenwu.ereader.utils.NetworkHelper;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (NetworkHelper.WIFI.equals(NetworkHelper.CURENT_NETWORK_TYPE)
                && networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            NetworkHelper.REFRESH_CONFIG = true;
            Log.d("NetworkReceiver", "wifi connected");
        } else if (NetworkHelper.ANY.equals(NetworkHelper.CURENT_NETWORK_TYPE)
                && networkInfo != null) {
            NetworkHelper.REFRESH_CONFIG = true;
        } else if (networkInfo == null) {
            NetworkHelper.REFRESH_CONFIG = false;
            Toast.makeText(context, R.string.network_lost, Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
