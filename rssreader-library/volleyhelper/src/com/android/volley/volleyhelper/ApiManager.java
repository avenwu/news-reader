package com.android.volley.volleyhelper;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * It's singleton, any operation should after init(); <br>
 * <br>
 * <b>Tip:</b>
 * 
 * <pre>
 * {@code
 * ApiManager.init(context);
 * }
 * <br>
 * @author chaobin
 * 
 */
public class ApiManager {
    private RequestQueue requestQueue;
    private static ApiManager instance;

    private ApiManager(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static ApiManager init(Context context) {
        return init(context, false);
    }

    public static ApiManager init(Context context, boolean cancellOldRequests) {
        if (instance != null) {
            if (cancellOldRequests) {
                instance.requestQueue.stop();// TODO
            }
        } else {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager(context);
                }
            }
        }
        return instance;
    }

    public static ApiManager getInstance() {
        if (instance == null)
            throw new RuntimeException("ApiManager is not init()");
        return instance;
    }

    public void post(StringRequest request) {
        requestQueue.add(request);
    }

    /**
     * This method is only for multi apis request
     * 
     * @param request
     */
    public void post(MultiRequests request) {
        if (requestQueue != null) {
            requestQueue.add(request);
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.requestQueue.stop();
        }
    }
}
