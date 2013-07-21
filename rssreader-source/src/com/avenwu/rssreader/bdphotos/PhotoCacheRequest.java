package com.avenwu.rssreader.bdphotos;

import java.util.ArrayList;

import com.android.volley.Response.Listener;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.volleyhelper.DataProvider;
import com.avenwu.volleyhelper.QueryDbTask;

public class PhotoCacheRequest extends QueryDbTask<PhotoFeedItem> {

    public PhotoCacheRequest(DataProvider<PhotoFeedItem> dataProvider,
            Listener<ArrayList<PhotoFeedItem>> listener) {
        super(dataProvider, listener);
    }

}
