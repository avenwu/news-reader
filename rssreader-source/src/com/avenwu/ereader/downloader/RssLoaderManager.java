package com.avenwu.ereader.downloader;

public class RssLoaderManager {
    public static final String PACKAGE_PREFIX = "com.avenwu.ereader.downloader.";
    public static final String HTTPCLIENT_RSS_LOADER = PACKAGE_PREFIX + "HttpClientRssLoader";
    public static final String URLCONNECTION_RSS_LOADER = PACKAGE_PREFIX + "UrlConnectionRssLoader";
    public static final String DEFAULT_LOADER = HTTPCLIENT_RSS_LOADER;

    public static BaseRssLoader getRssLoader() {
        return getRssLoader(DEFAULT_LOADER);
    }

    public static BaseRssLoader getRssLoader(String loaderType) {
        BaseRssLoader rssLoader = null;
        try {
            rssLoader = (BaseRssLoader) Class.forName(loaderType).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssLoader;
    }
}
