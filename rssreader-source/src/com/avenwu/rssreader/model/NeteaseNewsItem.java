package com.avenwu.rssreader.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "netease_news")
public class NeteaseNewsItem {
    @DatabaseField
    public String title;
    @DatabaseField(id = true)
    public String link;
    @DatabaseField
    public String description;
    @DatabaseField
    public String pubDate;
    @DatabaseField
    public String guid;
    @DatabaseField
    public String channel;

}
