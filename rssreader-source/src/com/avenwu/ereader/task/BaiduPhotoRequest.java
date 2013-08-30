package com.avenwu.ereader.task;

import java.util.ArrayList;

import org.json.JSONException;

import com.avenwu.ereader.R;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.avenwu.ereader.utils.NetworkHelper;
import com.avenwu.ereader.xmlparse.ParseManager;

public class BaiduPhotoRequest<Void> implements BaseRequest {
    private BaseListener<?> listener;

    public BaiduPhotoRequest(BaseListener<?> listener) {
        this.listener = listener;
    }

    @Override
    public Void doInbackground(String url) {
        if (!NetworkHelper.isNetworkActive()) {
            listener.sendResult(BaseListener.FAILED, R.string.network_lost);
            return null;
        }
        ArrayList<PhotoFeedItem> dataList = null;
        try {
            dataList = ParseManager.parsePhotos(url);
            if (dataList == null || dataList.isEmpty()) {
                listener.sendResult(BaseListener.FAILED, null);
            } else {
                listener.sendResult(BaseListener.SUCCESS, dataList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.sendResult(BaseListener.ERROR, e);
        }
        return null;
    }

}
