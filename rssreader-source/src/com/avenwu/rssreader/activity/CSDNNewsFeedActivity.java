package com.avenwu.rssreader.activity;

import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.model.CsdnGeekNew;
import com.avenwu.rssreader.model.CsdnNewsItem;

public class CSDNNewsFeedActivity extends Activity {
    private URL url = null;
    private String TAG = "CSDN";
    private CsdnGeekNew csdnData;
    private CsdnNewsItem currentItem = new CsdnNewsItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csdn_feed_layout);
        csdnData = new CsdnGeekNew();
        try {
            url = new URL(getString(R.string.url_csdn_geek_news));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = CSDNNewsFeedActivity.this.parse(url);
                    treeWalk(document);
                    Log.d(TAG, csdnData.toString());
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }

    public void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    public void treeWalk(Element element) {
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
                treeWalk((Element) node);
            }
        }
    }
}
