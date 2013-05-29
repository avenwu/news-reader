package com.avenwu.rssreader.downloader;

import java.io.InputStream;
import java.net.URL;

public class UrlConnectionRssLoader implements BaseRssLoader<String, Void, InputStream> {

    @Override
    public InputStream queryRssCotent(String url, Void params) {
        URL url2 = null;
        InputStream inputStream = null;
        try {
            url2 = new URL(url);
            inputStream = url2.openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
