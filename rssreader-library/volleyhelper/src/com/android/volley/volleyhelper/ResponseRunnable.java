package com.android.volley.volleyhelper;

import java.util.ArrayList;

import com.android.volley.Response.Listener;

public class ResponseRunnable<T> implements Runnable {
    private ParseResponse<T> response;
    private Listener<ArrayList<T>> listener;

    public ResponseRunnable(ParseResponse<T> response,
            Listener<ArrayList<T>> listener) {
        this.response = response;
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.onResponse(response.result);
    }

}