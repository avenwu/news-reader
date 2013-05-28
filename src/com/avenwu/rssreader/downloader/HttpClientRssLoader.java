package com.avenwu.rssreader.downloader;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientRssLoader implements BaseRssLoader<String, Void, InputStream> {
    private static HttpClient client = new DefaultHttpClient();

    @Override
    public InputStream queryRssCotent(String url, Void p) {
        HttpGet request = new HttpGet(url);
        InputStream inputStream = null;
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
