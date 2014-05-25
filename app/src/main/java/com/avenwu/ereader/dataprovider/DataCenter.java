package com.avenwu.ereader.dataprovider;

import com.avenwu.ereader.config.RssConfig;
import com.avenwu.ereader.model.BaseDetailItem;
import com.avenwu.ereader.model.CsdnNewsItem;
import com.avenwu.ereader.model.HomeDetailItem;
import com.avenwu.ereader.model.NewsMenuItem;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.avenwu.ereader.model.PickedDetailItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataCenter {
    private List<PickedDetailItem> pickedData = Collections
            .synchronizedList(new ArrayList<PickedDetailItem>());
    private List<HomeDetailItem> homeData = Collections
            .synchronizedList(new ArrayList<HomeDetailItem>());;
    private List<PickedDetailItem> candicateData = Collections
            .synchronizedList(new ArrayList<PickedDetailItem>());
    private List<PickedDetailItem> newsData = Collections
            .synchronizedList(new ArrayList<PickedDetailItem>());
    private List<NewsMenuItem> menuData;
    private List<CsdnNewsItem> csdnNewsData = Collections
            .synchronizedList(new ArrayList<CsdnNewsItem>());
    private static DataCenter instance;
    private List<PhotoFeedItem> photoFeedItems = Collections
            .synchronizedList(new ArrayList<PhotoFeedItem>());

    private DataCenter() {
    }

    public static DataCenter getInstance() {
        if (instance == null) {
            synchronized (DataCenter.class) {
                if (instance == null) {
                    instance = new DataCenter();
                }
            }
        }
        return instance;
    }

    public List<PickedDetailItem> getPickedData() {
        return pickedData;
    }

    public List<CsdnNewsItem> getCsdnNewsData() {
        return csdnNewsData;
    }

    public void setCsdnNewsData(List<CsdnNewsItem> csdnNewsData) {
        this.csdnNewsData = csdnNewsData;
    }

    public void setPickedData(List<PickedDetailItem> pickedData) {
        this.pickedData = pickedData;
    }

    public List<HomeDetailItem> getHomeData() {
        return homeData;
    }

    public void setHomeData(List<HomeDetailItem> homeData) {
        this.homeData = homeData;
    }

    public List<PickedDetailItem> getCandicateData() {
        return candicateData;
    }

    public void setCandicateData(List<PickedDetailItem> candicateData) {
        this.candicateData = candicateData;
    }

    public void addPickedItems(ArrayList<PickedDetailItem> collection) {
        pickedData.addAll(collection);
    }

    public void addHomeItems(ArrayList<HomeDetailItem> collection) {
        homeData.addAll(collection);
    }

    public void addPhotoItems(ArrayList<PhotoFeedItem> collection) {
        photoFeedItems.addAll(collection);
    }

    public List<PhotoFeedItem> getPhotoFeedsItems() {
        return photoFeedItems;
    }

    public void replacePickedItems(ArrayList<PickedDetailItem> collection) {
        pickedData.clear();
        addPickedItems(collection);
    }

    public void replacePhotoItems(ArrayList<PhotoFeedItem> collection) {
        photoFeedItems.clear();
        addPhotoItems(collection);
    }

    public void addCsdnNewsItems(ArrayList<CsdnNewsItem> collection) {
        csdnNewsData.addAll(collection);
    }

    public void replaceHomeItems(ArrayList<HomeDetailItem> collection) {
        homeData.clear();
        addHomeItems(collection);
    }

    public void replaceCsdnNewsItems(ArrayList<CsdnNewsItem> collection) {
        csdnNewsData.clear();
        addCsdnNewsItems(collection);
    }

    public String getArtical(int position, String url) {
        String content = "";
        try {
            if (url.equals(RssConfig.getInstance().getHomeUrl())) {
                content = getHomeData().get(position).getContent();
            } else if (url.equals(RssConfig.getInstance().getPickedUrl())) {
                content = getPickedData().get(position).getContent();
            } else if (url.equals(RssConfig.getInstance().getCandicateUrl())) {
                content = getCandicateData().get(position).getContent();
            } else if (url.equals(RssConfig.getInstance().getNewsUrl())) {
                content = getNewsData().get(position).getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public BaseDetailItem getDataItem(String url, int position) {
        BaseDetailItem item = null;
        try {
            if (url.equals(RssConfig.getInstance().getHomeUrl())) {
                item = getHomeData().get(position);
            } else if (url.equals(RssConfig.getInstance().getPickedUrl())) {
                item = getPickedData().get(position);
            } else if (url.equals(RssConfig.getInstance().getCandicateUrl())) {
                item = getCandicateData().get(position);
            } else if (url.equals(RssConfig.getInstance().getNewsUrl())) {
                item = getNewsData().get(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<PickedDetailItem> getNewsData() {
        return newsData;
    }

    public void setNewsData(List<PickedDetailItem> newsData) {
        this.newsData = newsData;
    }

    public void clear() {
        if (pickedData != null) {
            pickedData.clear();
        }
        if (homeData != null) {
            homeData.clear();
        }
        if (newsData != null) {
            newsData.clear();
        }
        if (candicateData != null) {
            candicateData.clear();
        }
        if (menuData != null) {
            menuData.clear();
        }
        instance = null;
    }
}
