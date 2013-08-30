package com.android.volley.volleyhelper;

import java.util.ArrayList;

import com.android.volley.Response.Listener;

/**
 * processor base class, the response data from server need to be processed to
 * different data type T, then stored to locale;
 * 
 * @author chaobin
 * 
 * @param <T>
 *            data model such as {@link FriendInfo}
 */
public abstract class ResponseProcessor<T> extends BackgroundRunnable {
    private String result;
    private ParseResponse<T> response;
    private Listener<ArrayList<T>> listener;
    private boolean clearOld;

    public ResponseProcessor(Listener<ArrayList<T>> listener) {
        this.listener = listener;
    }

    public ResponseProcessor(Listener<ArrayList<T>> listener, boolean clearOld) {
        this.listener = listener;
        this.clearOld = clearOld;
    }

    /**
     * process the response data, insert all the data into database if
     * necessary;
     * 
     * @param result
     */
    public abstract ArrayList<T> parseJson(String result);

    /**
     * 
     * @return specific provider for different modules
     */
    public abstract DataProvider<T> getProvider();

    /**
     * TODO This method does not work in Main thread, so you should not refresh
     * UI element directly;
     * 
     * @param response
     */
    public void response(ParseResponse<?> response) {
    }

    @Override
    public void doInBackground() {
        response = new ParseResponse<T>();
        try {
            response.result = parseJson(result);
            response.state = ParseResponse.SUCCESS;
            // 1. notify the callback on UI thread
            ProcessorManager.getInstance().post(
                    new ResponseRunnable<T>(response, listener));
            // 2. store data into sqlite db
            getProvider().addAll(response.result, clearOld);
        } catch (Exception e) {
            response.exception = e;
            response.state = ParseResponse.FAILED;
            e.printStackTrace();
        }
        response(response);
    }

    public boolean isClearOld() {
        return clearOld;
    }

    public void setClearOld(boolean clearOld) {
        this.clearOld = clearOld;
    }

    public void start(String result) {
        this.result = result;
        ProcessorManager.getInstance().excute(this);
    }

    public void cancell() {
        ProcessorManager.getInstance().cancell(this.toString());
    }
}
