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
 *            return data type to delete
 */
public class DeleteDbTask<T> extends BackgroundRunnable {
    private DataProvider<T> dataProvider;
    private Listener<Boolean> listener;
    private T item2Delete;

    /**
     * call updateItem2Delete(T item) when item clicked to delete but only
     * excute() when API request returned response
     * 
     * @param dataProvider
     *            any subclass of DataProvider, to delete one data item
     * @param listener
     *            null if you do not care about result of delete operation;
     */
    public DeleteDbTask(DataProvider<T> dataProvider, Listener<Boolean> listener) {
        this.dataProvider = dataProvider;
        if (listener == null) {
            this.listener = new Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    if (response) {
                        Log.d("testormlite", "delete item successfully");
                    }
                }
            };
        }
    }

    @Override
    public void doInBackground() {
        listener.onResponse(dataProvider.delete(item2Delete));
    }

    public void excute() {
        if (item2Delete != null) {
            ProcessorManager.getInstance().post(this);
        }
    }

    /**
     * update the item to be deleted before excute() called;
     * 
     * @param item
     */
    public void updateItem2Delete(T item) {
        this.item2Delete = item;
    }

}
