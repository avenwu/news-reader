package com.android.volley.volleyhelper;

import java.util.Map;

import android.util.Log;

/**
 * This class can be used for single/multiple API requests, for single request,
 * please setFinished(true) in the constructor, eg:
 * {@link com.taggedapp.newsfeed.NFFeedParams}, but for multiple API requests,
 * just override the parse() method, and setFinished(true) when the last request
 * is going to excute(), eg: {@link com.taggedapp.browse.BSMultiParams};<br>
 * 
 * @author chaobin
 * 
 */
public abstract class MultiApiParams {
    private final String TAG = "MultiApiParams";
    public int curentIndex = 0;
    private boolean isFinished = false;
    private Map<String, String> params;

    public Map<String, String> getCurrentParams() {
        return params;
    }

    public void parse(String response) throws Exception {
        Log.d(TAG, response);
        parse(curentIndex, response);
        curentIndex++;
    }

    /**
     * Only multiple API requests need to override this method to parse the
     * previous response here, then add the parameters for next API request;
     * 
     * @param index
     * @param response
     *            result of the previous api;
     * @return
     * @throws Exception
     */
    public abstract void parse(int index, String response) throws Exception;

    public int getParamsIndex() {
        return curentIndex;
    }

    public void updateParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isFinished() {
        return isFinished;
    }

    /**
     * set true when the the last API is to excute(), for single API request,
     * set true in the constructor
     * 
     * @param isFinished
     */
    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

}
