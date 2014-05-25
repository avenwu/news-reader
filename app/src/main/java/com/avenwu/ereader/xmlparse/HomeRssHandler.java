package com.avenwu.ereader.xmlparse;

import android.util.Log;

import com.avenwu.ereader.model.AuthorInfo;
import com.avenwu.ereader.model.HomeDetailItem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class HomeRssHandler extends DefaultHandler {
    private ArrayList<HomeDetailItem> itemList;
    private final String TAG = "RssHandler";
    private HomeDetailItem entryItem;
    private AuthorInfo authorInfo;
    private StringBuffer tempContent;
    private boolean isEntryStarted = false;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        Log.d(TAG, "start parse document");
        itemList = new ArrayList<HomeDetailItem>();
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
        if (qName.equals(Element.entry)) {
            isEntryStarted = true;
            entryItem = new HomeDetailItem();
        } else if (qName.equals(Element.author)) {
            authorInfo = new AuthorInfo();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if (!isEntryStarted) {
            return;
        }
        if (qName.equals(Element.entry)) {
            itemList.add(entryItem);
        } else if (qName.equals(Element.author)) {
            entryItem.setUser(authorInfo);
        } else if (qName.equals(Element.id)) {
            entryItem.setId(tempContent.toString());
        } else if (qName.equals(Element.title)) {
            entryItem.setTitle(tempContent.toString());
        } else if (qName.equals(Element.summary)) {
            entryItem.setSummary(tempContent.toString());
        } else if (qName.equals(Element.published)) {
            entryItem.setPublised_time(tempContent.toString());
        } else if (qName.equals(Element.updated)) {
            entryItem.setUpdated_time(tempContent.toString());
        } else if (qName.equals(Element.name)) {
            authorInfo.setName(tempContent.toString());
        } else if (qName.equals(Element.uri)) {
            authorInfo.setAvatar_url(tempContent.toString());
        } else if (qName.equals(Element.link)) {

        } else if (qName.equals(Element.content)) {
            entryItem.setContent(tempContent.toString());
        }
        tempContent.delete(0, tempContent.length());
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        if (!isEntryStarted) {
            return;
        }
        tempContent.append(ch, start, length);
    }

    public ArrayList<HomeDetailItem> getEntryItems() {
        return itemList;
    }
}
