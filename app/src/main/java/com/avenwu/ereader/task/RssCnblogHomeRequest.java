package com.avenwu.ereader.task;

import com.avenwu.ereader.R;
import com.avenwu.ereader.model.HomeDetailItem;
import com.avenwu.ereader.utils.NetworkHelper;
import com.avenwu.ereader.xmlparse.ParseManager;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class RssCnblogHomeRequest<Void> implements BaseRequest {
    private BaseListener<?> listener;

    public RssCnblogHomeRequest(BaseListener<?> listener) {
        this.listener = listener;
    }

    @Override
    public Void doInbackground(String url) {
        if (!NetworkHelper.isNetworkActive()) {
            listener.sendResult(BaseListener.FAILED, R.string.network_lost);
            return null;
        }
        try {
            ArrayList<HomeDetailItem> dataList = ParseManager.parseHomeXML(url);
            if (dataList == null) {
                listener.sendResult(BaseListener.FAILED, null);
            } else {
                listener.sendResult(BaseListener.SUCCESS, dataList);
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
