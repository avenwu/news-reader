package com.avenwu.rssreader.bdphotos;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.rssreader.xmlparse.JsonParser;
import com.avenwu.volleyhelper.DataProvider;
import com.avenwu.volleyhelper.ResponseProcessor;

public class BDProcessor extends ResponseProcessor<PhotoFeedItem> {
    private Context context;

    public BDProcessor(Context context,
            Listener<ArrayList<PhotoFeedItem>> listener, boolean clearOld) {
        super(listener, clearOld);
        this.context = context;
    }

    @Override
    public ArrayList<PhotoFeedItem> parseJson(String result) {
        ArrayList<PhotoFeedItem> dataItems = null;
        try {
            dataItems = JsonParser.getPhotos(result);
        } catch (JSONException e) {
            dataItems = new ArrayList<PhotoFeedItem>();
            e.printStackTrace();
        }
        return dataItems;
    }

    @Override
    public DataProvider<PhotoFeedItem> getProvider() {
        return new BDProvider(context);
    }

}
