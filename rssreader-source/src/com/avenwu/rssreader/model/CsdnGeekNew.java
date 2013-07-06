package com.avenwu.rssreader.model;

import java.util.ArrayList;

public class CsdnGeekNew {
    public String title;
    public String link;
    public String description;
    public String lastBuildDate;
    private ArrayList<CsdnNewsItem> itemList = new ArrayList<CsdnNewsItem>();

    public void addItem(CsdnNewsItem item) {
        itemList.add(item);
    }
}
