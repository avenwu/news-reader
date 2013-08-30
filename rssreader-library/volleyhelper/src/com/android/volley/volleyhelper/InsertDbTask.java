package com.android.volley.volleyhelper;

import android.util.Log;

import com.android.volley.Response.Listener;

/**
 * subclass of BackgroundRunnable, used to delete data item, works with
 * {@link ProcessorManager}
 * 
 * @author chaobin
 * 
 * @param <T>
 *            data type of the item to insert
 */
public class InsertDbTask<T> extends BackgroundRunnable {
    private DataProvider<T> dataProvider;
    private Listener<Boolean> listener;
    private T item2add;

    /**
     * call excute(T item) to start the task to insert data;
     * 
     * @param dataProvider
     *            any subclass of DataProvider, to insert one data item
     * @param listener
     *            null if you do not care about result of insert operation;
     */
    public InsertDbTask(DataProvider<T> dataProvider, Listener<Boolean> listener) {
        this.dataProvider = dataProvider;
        if (listener == null) {
            this.listener = new Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    if (response) {
                        Log.d("testormlite", "insert data item successfully");
                    }
                }
            };
        }

    }

    @Override
    public void doInBackground() {
        listener.onResponse(dataProvider.add(item2add));
    }

    /**
     * start the task to insert data;
     * 
     * @param item
     *            the data item to insert
     */
    public void excute(T item) {
        this.item2add = item;
        ProcessorManager.getInstance().post(this);
    }
}
