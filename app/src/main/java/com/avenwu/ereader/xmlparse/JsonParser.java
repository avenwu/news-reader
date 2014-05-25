package com.avenwu.ereader.xmlparse;

import com.avenwu.ereader.model.PhotoFeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public static ArrayList<PhotoFeedItem> getPhotos(String result)
            throws JSONException {
        ArrayList<PhotoFeedItem> feedItems = new ArrayList<PhotoFeedItem>();
        JSONObject feed = new JSONObject(result);
        JSONArray array = feed.getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            if (item.isNull("id")) {
                break;
            }
            PhotoFeedItem photoItem = new PhotoFeedItem();
            photoItem.id = item.getString("id");
            photoItem.photoDescription = item.getString("abs");
            photoItem.largePhotoUrl = item.getString("image_url");
            photoItem.largeHeight = item.getInt("image_height");
            photoItem.largeWidth = item.getInt("image_width");
            photoItem.timeStamp = item.getString("date");
            photoItem.thumbPhotoUrl = item.getString("thumbnail_url");
            photoItem.tag = item.getString("tag");
            feedItems.add(photoItem);
        }
        return feedItems;
    }
}
