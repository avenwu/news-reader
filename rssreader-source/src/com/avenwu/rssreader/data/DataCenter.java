package com.avenwu.rssreader.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.avenwu.rssreader.model.EntryItem;

public class DataCenter {
    private List<EntryItem> pickedData = Collections.synchronizedList(new ArrayList<EntryItem>());
    private List<EntryItem> homeData = Collections.synchronizedList(new ArrayList<EntryItem>());;
    private List<EntryItem> candicateData = Collections.synchronizedList(new ArrayList<EntryItem>());
    private static DataCenter instance;

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

    public List<EntryItem> getPickedData() {
        return pickedData;
    }

    public void setPickedData(List<EntryItem> pickedData) {
        this.pickedData = pickedData;
    }

    public List<EntryItem> getHomeData() {
        return homeData;
    }

    public void setHomeData(List<EntryItem> homeData) {
        this.homeData = homeData;
    }

    public List<EntryItem> getCandicateData() {
        return candicateData;
    }

    public void setCandicateData(List<EntryItem> candicateData) {
        this.candicateData = candicateData;
    }

    public void addPickedItems(ArrayList<EntryItem> collection) {
        pickedData.addAll(collection);
    }

    public void replacePickedItems(ArrayList<EntryItem> collection) {
        pickedData.clear();
        addPickedItems(collection);
    }
}
