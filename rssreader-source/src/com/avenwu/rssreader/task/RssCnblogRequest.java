package com.avenwu.rssreader.task;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.rssparse.XMLParseManager;

public class RssCnblogRequest<Void> implements BaseRequest {
    private BaseListener listener;

    public RssCnblogRequest(BaseListener listener) {
        this.listener = listener;
    }

    @Override
    public Void doInbackground(String url) {
        try {
            ArrayList<EntryItem> dataList = XMLParseManager.parseRssXML(url);
            listener.sendResult(BaseListener.SUCCESS, dataList);
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
