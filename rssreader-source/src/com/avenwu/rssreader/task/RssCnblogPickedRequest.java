package com.avenwu.rssreader.task;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.PickedDetailItem;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.avenwu.rssreader.xmlparse.ParseManager;

public class RssCnblogPickedRequest<Void> implements BaseRequest {
    private BaseListener<?> listener;

    public RssCnblogPickedRequest(BaseListener<?> listener) {
        this.listener = listener;
    }

    @Override
    public Void doInbackground(String url) {
        if (!NetworkHelper.isNetworkActive()) {
            listener.sendResult(BaseListener.FAILED, R.string.network_lost);
            return null;
        }
        try {
            ArrayList<PickedDetailItem> dataList = ParseManager.parsePickedXML(url);
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
