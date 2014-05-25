package com.avenwu.ereader.task;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpManager {
    private final String TAG = "HttpManager";
    private HttpClient client = new DefaultHttpClient();
    private static HttpManager instance = new HttpManager();

    public static HttpManager getInstance() {
        return instance;
    }

    public String queryPhotos(String url) {
        Log.d(TAG, url);
        HttpGet request = new HttpGet(url);
        String strResult = "";
        try {
            HttpResponse response = client.execute(request);
            strResult = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, strResult);
        return strResult;
    }
}
