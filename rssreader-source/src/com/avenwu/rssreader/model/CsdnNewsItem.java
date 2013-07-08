package com.avenwu.rssreader.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "csdn_news")
public class CsdnNewsItem {
    @DatabaseField
    public String title;
    @DatabaseField(id = true)
    public String link;
    @DatabaseField
    public String pubDate;

}
