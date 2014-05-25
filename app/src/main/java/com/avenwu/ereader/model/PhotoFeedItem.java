package com.avenwu.ereader.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "baidu_photos")
public class PhotoFeedItem {
    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public String photoDescription;
    @DatabaseField
    public String largePhotoUrl;
    @DatabaseField
    public String thumbPhotoUrl;
    @DatabaseField
    public String timeStamp;
    @DatabaseField
    public int largeWidth;
    @DatabaseField
    public int largeHeight;
    @DatabaseField
    public String tag;
}
