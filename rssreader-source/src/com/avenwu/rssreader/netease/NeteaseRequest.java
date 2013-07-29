package com.avenwu.rssreader.netease;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.NeteaseNewsItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseRequest;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.avenwu.rssreader.xmlparse.ParseManager;

public class NeteaseRequest implements BaseRequest<NeteaseNewsItem> {
    private BaseListener<?> listener;
    private NeteaseProvider provider;
    private boolean clearOld;

    public NeteaseRequest(NeteaseProvider provider, BaseListener<?> listener,
            boolean clearOld) {
        this.provider = provider;
        this.listener = listener;
        this.clearOld = clearOld;
    }

    @Override
    public NeteaseNewsItem doInbackground(String url) {
        if (!NetworkHelper.isNetworkActive()) {
            listener.sendResult(BaseListener.FAILED, R.string.network_lost);
            return null;
        }
        try {
            ArrayList<NeteaseNewsItem> dataList = ParseManager
                    .parseNeteaseNews(url);
            if (dataList == null) {
                listener.sendResult(BaseListener.FAILED, null);
            } else {
                listener.sendResult(BaseListener.SUCCESS, dataList);
                provider.addAll(dataList, clearOld);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            listener.sendResult(BaseListener.ERROR, e);
        } catch (SAXException e) {
            e.printStackTrace();
            listener.sendResult(BaseListener.ERROR, e);
        } catch (IOException e) {
            e.printStackTrace();
            listener.sendResult(BaseListener.ERROR, e);
        }
        return null;
    }
}
