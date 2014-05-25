package com.avenwu.ereader.bdphotos;

import com.android.volley.Response;
import com.android.volley.volleyhelper.DataProvider;
import com.android.volley.volleyhelper.QueryDbTask;
import com.avenwu.ereader.model.PhotoFeedItem;

import java.util.ArrayList;

public class PhotoCacheRequest extends QueryDbTask<PhotoFeedItem> {

    public PhotoCacheRequest(DataProvider<PhotoFeedItem> dataProvider,
            Response.Listener<ArrayList<PhotoFeedItem>> listener) {
        super(dataProvider, listener);
    }

}
