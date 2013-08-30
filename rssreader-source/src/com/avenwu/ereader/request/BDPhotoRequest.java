package com.avenwu.ereader.request;

import com.android.volley.Response.ErrorListener;
import com.android.volley.volleyhelper.MultiRequests;
import com.android.volley.volleyhelper.ResponseProcessor;

public class BDPhotoRequest extends MultiRequests {
    public BDPhotoRequest(BDPhotoParams params, ResponseProcessor<?> processor,
            ErrorListener errorListener) {
        super(params.getPhotoParams(), processor, errorListener);
    }

}
