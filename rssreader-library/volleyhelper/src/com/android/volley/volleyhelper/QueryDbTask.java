package com.android.volley.volleyhelper;

import com.android.volley.Response.Listener;

import java.util.ArrayList;

/**
 * subclass of BackgroundRunnable, used to query the cached locale data, works
 * with {@link ProcessorManager}
 * 
 * @author chaobin
 * 
 * @param <T>
 *            return value for Listener.onResponse(T t);
 */
public class QueryDbTask<T> extends BackgroundRunnable {
    private DataProvider<T> dataProvider;
    private Listener<ArrayList<T>> listener;

    public QueryDbTask(DataProvider<T> dataProvider,
            Listener<ArrayList<T>> listener) {
        this.dataProvider = dataProvider;
        this.listener = listener;
    }

    @Override
    public void doInBackground() {
        listener.onResponse((ArrayList<T>) dataProvider.getAll());
    }

    public void excute() {
        ProcessorManager.getInstance().post(this);
    }

}
