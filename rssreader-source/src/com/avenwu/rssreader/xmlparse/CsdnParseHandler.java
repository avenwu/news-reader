package com.avenwu.rssreader.xmlparse;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import android.util.Log;

import com.avenwu.rssreader.model.CsdnGeekNew;
import com.avenwu.rssreader.model.CsdnNewsItem;

public class CsdnParseHandler {
    private String TAG = "CsdnParseHandler";
    private CsdnGeekNew csdnData = new CsdnGeekNew();
    private CsdnNewsItem currentItem = new CsdnNewsItem();

    public void parse(String url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        readXml(document);
    }

    public void readXml(Document document) {
        readXml(document.getRootElement());
    }

    public void readXml(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if ("title".equals(node.getName())) {
                Log.d(TAG, node.getStringValue());
                if (csdnData.title == null) {
                    csdnData.title = node.getStringValue();
                } else if (currentItem.title == null) {
                    currentItem.title = node.getStringValue();
                } else {
                    currentItem = new CsdnNewsItem();
                    currentItem.title = node.getStringValue();
                }
            } else if ("link".equals(node.getName())) {
                Log.d(TAG, node.getStringValue());
                if (csdnData.link == null) {
                    csdnData.link = node.getStringValue();
                } else if (currentItem.link == null) {
                    currentItem.link = node.getStringValue();
                }
            } else if ("description".equals(node.getStringValue())) {
                Log.d(TAG, node.getStringValue());
                if (csdnData.description == null) {
                    csdnData.description = node.getStringValue();
                }
            } else if ("lastBuildDate".equals(node.getName())) {
                Log.d(TAG, node.getStringValue());
                csdnData.lastBuildDate = node.getStringValue();
            } else if ("pubDate".equals(node.getName())) {
                Log.d(TAG, node.getStringValue());
                if (currentItem.pubDate == null) {
                    currentItem.pubDate = node.getStringValue();
                    csdnData.addItem(currentItem);
                }
            }
            if (node instanceof Element) {
                readXml((Element) node);
            }
        }
    }

    public ArrayList<CsdnNewsItem> getEntryItems() {
        return csdnData.itemList;
    }
}
