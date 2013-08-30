package com.avenwu.ereader.xmlparse;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.avenwu.ereader.model.NeteaseNewsItem;

public class NeteaseNewsHandler extends DefaultHandler {
    private final String TAG = "NeteaseNewsHandler";
    private ArrayList<NeteaseNewsItem> itemList;
    private StringBuffer tempContent;
    private boolean itemStart;
    private NeteaseNewsItem newsItem;
    private String channel;

    /*
     * <item> 
     * <title>公务员享福利分房和购房补贴双福利被指不公平</title>
     * <link>http://rss.feedsportal.com/c/33390/f/628983/p/1/s/
     * 7edacf69/l/0Lnews0B1630N0C130C0A7190C10A0C944VKJ190A0A0A1124J0Bhtml/story01.htm</link>
     * <description>中国气象局昨日的“一纸”部门决acebook/?u=http%3A%2</description>
     * <pubDate>Fri, 19 Jul 2013 02:16:08 GMT</pubDate> <guid
     * isPermaLink="false"
     * >http://news.163.com/13/0719/10/944VKJ190001124J.html</guid> 
     * </item>
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        Log.d(TAG, "start parse document");
        itemList = new ArrayList<NeteaseNewsItem>();
        tempContent = new StringBuffer();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.d(TAG, "end parse document");
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals(Element.item)) {
            itemStart = true;
            newsItem = new NeteaseNewsItem();
            newsItem.channel = channel;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if (!itemStart) {
            if (qName.equals(Element.title))
                channel = tempContent.toString();
        } else {
            if (qName.equals(Element.item)) {
                itemList.add(newsItem);
            } else if (qName.equals(Element.title)) {
                newsItem.title = tempContent.toString();
            } else if (qName.equals(Element.link)) {
                newsItem.link = tempContent.toString();
            } else if (qName.equals(Element.description)) {
                newsItem.description = tempContent.toString();
            } else if (qName.equals(Element.pubDate)) {
                newsItem.pubDate = tempContent.toString();
            } else if (qName.equals(Element.guid)) {
                newsItem.guid = tempContent.toString();
            }
        }
        tempContent.delete(0, tempContent.length());
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        tempContent.append(ch, start, length);
    }

    public ArrayList<NeteaseNewsItem> getDataList() {
        return itemList;
    }
}
