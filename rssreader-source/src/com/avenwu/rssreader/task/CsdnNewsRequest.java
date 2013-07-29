package com.avenwu.rssreader.task;

import java.util.ArrayList;

import org.dom4j.DocumentException;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.CsdnNewsItem;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.avenwu.rssreader.xmlparse.ParseManager;

public class CsdnNewsRequest implements BaseRequest<Void> {
    private BaseListener<?> listener;

    public CsdnNewsRequest(BaseListener<?> listener) {
        this.listener = listener;
    }

    @Override
    public Void doInbackground(String url) {
        if (!NetworkHelper.isNetworkActive()) {
            listener.sendResult(BaseListener.FAILED, R.string.network_lost);
            return null;
        }
        ArrayList<CsdnNewsItem> dataList;
        try {
            dataList = ParseManager.parseCsdnNews(url);
            if (dataList == null) {
                listener.sendResult(BaseListener.FAILED, null);
            } else {
                listener.sendResult(BaseListener.SUCCESS, dataList);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            listener.sendResult(BaseListener.ERROR, e);
        }
        return null;
    }

}
