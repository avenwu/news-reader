package com.avenwu.rssreader.request;

import com.android.volley.Response.ErrorListener;
import com.avenwu.volleyhelper.MultiRequests;
import com.avenwu.volleyhelper.ResponseProcessor;

public class BDPhotoRequest extends MultiRequests {
    public BDPhotoRequest(BDPhotoParams params, ResponseProcessor<?> processor,
            ErrorListener errorListener) {
        super(params.getPhotoParams(), processor, errorListener);
    }

}
