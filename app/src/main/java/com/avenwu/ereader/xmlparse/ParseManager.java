package com.avenwu.ereader.xmlparse;

import android.content.Context;

import com.avenwu.ereader.downloader.RssLoaderManager;
import com.avenwu.ereader.model.CsdnNewsItem;
import com.avenwu.ereader.model.HomeDetailItem;
import com.avenwu.ereader.model.NeteaseNewsItem;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.avenwu.ereader.model.PickedDetailItem;
import com.avenwu.ereader.task.HttpManager;
import com.avenwu.ereader.utils.Toolkit;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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

    public static ArrayList<NeteaseNewsItem> parseNeteaseNews(Context context, String url)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        NeteaseNewsHandler handler = new NeteaseNewsHandler();
        File file = Toolkit.createFile(context, url);
        URL filePath = new URL(url);
        FileUtils.copyURLToFile(filePath,file);
        if (file != null) {
            parser.parse(file, handler);
        }
        return handler.getDataList();
    }

    public static ArrayList<PhotoFeedItem> parsePhotos(String url)
            throws JSONException {
        String result = HttpManager.getInstance().queryPhotos(url);
        return JsonParser.getPhotos(result);
    }
}
