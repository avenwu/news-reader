package com.avenwu.rssreader.xmlparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.avenwu.rssreader.downloader.RssLoaderManager;
import com.avenwu.rssreader.model.EntryItem;

public class XMLParseManager {

    public static ArrayList<EntryItem> parseRssXML(String url) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        RssHandler handler = new RssHandler();
        InputStream inputStream = (InputStream) RssLoaderManager.getRssLoader().queryRssCotent(url, null);
        if (inputStream != null) {
            parser.parse(inputStream, handler);
            inputStream.close();
        }
        return handler.getEntryItems();
    }
}
