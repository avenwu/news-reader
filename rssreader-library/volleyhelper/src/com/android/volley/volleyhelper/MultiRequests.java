package com.android.volley.volleyhelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * if you want to post API B which requires the result of API A. This class is
 * what's you need.
 * 
 * @author chaobin
 * 
 */
public abstract class MultiRequests extends Request<String> {
    private ResponseProcessor<?> processor;
    private MultiApiParams multiParams;

    /**
     * any self defined subclass of ResponseProcessor should has ability to
     * process response data
     * 
     * @param processor
     *            must not be null
     * @param errorListener
     */
    public MultiRequests(String url, ResponseProcessor<?> processor,
            ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.processor = processor;
    }

    public MultiRequests(int method, String url,
            ResponseProcessor<?> processor, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.processor = processor;
    }

    // @Override
    // public Map<String, String> getHeaders() throws AuthFailureError {
    // Map<String, String> map = new HashMap<String, String>();
    // // map.put("Cookie",
    // // "S={1};L={2};".replace("{1}", Login.appCtx.sessionToken)
    // // .replace("{2}", Login.appCtx.persistentToken));
    // // map.put("User-Agent", Login.UserAgent);
    // return map;
    // }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return multiParams.getCurrentParams();
    }

    @Override
    protected void deliverResponse(String response) {
        if (!isFinished()) {
            try {
                multiParams.parse(response);
            } catch (Exception e) {
                e.printStackTrace();
                deliverError(new VolleyError(e.getMessage()));
                return;
            }
            // post the next api parameters
            ApiManager.getInstance().post(this);
        } else {
            // all api requests have been done successfully, notify the
            // processor listener and callback listener
            if (processor != null) {
                processor.start(response);
            }
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        //check if Content-Type exists; 
        if (!response.headers.containsKey(HTTP.CONTENT_TYPE)) {
            response.headers.put(HTTP.CONTENT_TYPE,
                    response.headers.get("Content-type"));
        }
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * start the request
     * 
     * @param params
     */
    public void excute(MultiApiParams params) {
        multiParams = params;
        ApiManager.getInstance().post(this);
    }

    public void excute() {
        multiParams = new MultiApiParams() {
            @Override
            public void parse(int index, String response) throws Exception {
            }
        };
        multiParams.updateParams(new HashMap<String, String>());
        multiParams.setFinished(true);
        ApiManager.getInstance().post(this);
    }

    /**
     * check if there is any requests need to be done
     * 
     * @return true if no more requests
     */
    private boolean isFinished() {
        return multiParams == null
                || (multiParams != null && multiParams.isFinished());
    }

    /**
     * cancel the request and processor, no callback will be delivered
     */
    @Override
    public void cancel() {
        if (processor != null) {
            processor.cancell();
        }
        super.cancel();
    }
}