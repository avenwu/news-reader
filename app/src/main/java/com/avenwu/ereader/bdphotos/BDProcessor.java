package com.avenwu.ereader.bdphotos;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.android.volley.volleyhelper.DataProvider;
import com.android.volley.volleyhelper.ResponseProcessor;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.avenwu.ereader.xmlparse.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;

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
