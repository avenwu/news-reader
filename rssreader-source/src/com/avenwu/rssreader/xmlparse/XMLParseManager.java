package com.avenwu.rssreader.xmlparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.avenwu.rssreader.activity.CSDNNewsFeedActivity;
import com.avenwu.rssreader.downloader.RssLoaderManager;
import com.avenwu.rssreader.model.CsdnNewsItem;
import com.avenwu.rssreader.model.HomeDetailItem;
import com.avenwu.rssreader.model.PickedDetailItem;

public class XMLParseManager {
    public static ArrayList<HomeDetailItem> parseHomeXML(String url)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        HomeRssHandler handler = new HomeRssHandler();
        InputStream inputStream = (InputStream) RssLoaderManager.getRssLoader()
                .queryRssCotent(url, null);
        if (inputStream != null) {
            parser.parse(inputStream, handler);
            inputStream.close();
        }
        return handler.getEntryItems();
    }

    public static ArrayList<PickedDetailItem> parsePickedXML(String url)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        PickedRssHandler handler = new PickedRssHandler();
        InputStream inputStream = (InputStream) RssLoaderManager.getRssLoader()
                .queryRssCotent(url, null);
        if (inputStream != null) {
            parser.parse(inputStream, handler);
            inputStream.close();
        }
        return handler.getEntryItems();
    }

    public static ArrayList<CsdnNewsItem> parseCsdnNews(String url)
            throws DocumentException {
        CsdnParseHandler handler = new CsdnParseHandler();
        handler.parse(url);
        return handler.getEntryItems();
    }
}
