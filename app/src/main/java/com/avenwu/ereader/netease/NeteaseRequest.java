package com.avenwu.ereader.netease;

import android.content.Context;

import com.avenwu.ereader.R;
import com.avenwu.ereader.model.NeteaseNewsItem;
import com.avenwu.ereader.task.BaseListener;
import com.avenwu.ereader.task.BaseRequest;
import com.avenwu.ereader.utils.NetworkHelper;
import com.avenwu.ereader.xmlparse.ParseManager;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class NeteaseRequest implements BaseRequest<NeteaseNewsItem> {
    private Context context;
    private BaseListener<?> listener;
    private NeteaseProvider provider;
    private boolean clearOld;

    public NeteaseRequest(Context context, NeteaseProvider provider, BaseListener<?> listener,
            boolean clearOld) {
        this.context = context;
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
                    .parseNeteaseNews(context,url);
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
