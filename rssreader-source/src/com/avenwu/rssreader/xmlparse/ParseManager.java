package com.avenwu.rssreader.xmlparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.DocumentException;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.xml.sax.SAXException;

import com.avenwu.rssreader.config.Constant;
import com.avenwu.rssreader.downloader.RssLoaderManager;
import com.avenwu.rssreader.model.CsdnNewsItem;
import com.avenwu.rssreader.model.HomeDetailItem;
import com.avenwu.rssreader.model.NeteaseNewsItem;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.rssreader.model.PickedDetailItem;
import com.avenwu.rssreader.task.HttpManager;

public class ParseManager {
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

    public static ArrayList<NeteaseNewsItem> parseNeteaseNews(String url)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        NeteaseNewsHandler handler = new NeteaseNewsHandler();
        InputStream inputStream = (InputStream) RssLoaderManager.getRssLoader()
                .queryRssCotent(url, null);
        if (inputStream != null) {
            parser.parse(inputStream, handler);
            inputStream.close();
        }
        return handler.getDataList();
    }

    public static String jsoupParse(String url) throws IOException {
        org.jsoup.nodes.Document doc1 = Jsoup.connect(url).get();
        Element element = doc1.select("a").get(Constant.TARGET_URL_INDEX);
        String deString = element.attr("abs:href");
        if (deString == null || deString.isEmpty()) {
            deString = url;
        }
        return deString;
    }

    public static ArrayList<PhotoFeedItem> parsePhotos(String url)
            throws JSONException {
        String result = HttpManager.getInstance().queryPhotos(url);
        return JsonParser.getPhotos(result);
    }
}
