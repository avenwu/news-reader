package com.avenwu.ereader.bdphotos;

import com.android.volley.Response.Listener;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.android.volley.volleyhelper.DataProvider;
import com.android.volley.volleyhelper.QueryDbTask;

import java.util.ArrayList;

public class PhotoCacheRequest extends QueryDbTask<PhotoFeedItem> {

    public PhotoCacheRequest(DataProvider<PhotoFeedItem> dataProvider,
            Listener<ArrayList<PhotoFeedItem>> listener) {
        super(dataProvider, listener);
    }

}
