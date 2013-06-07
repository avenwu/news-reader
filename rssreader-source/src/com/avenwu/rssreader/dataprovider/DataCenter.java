package com.avenwu.rssreader.dataprovider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.avenwu.rssreader.config.RssConfig;
import com.avenwu.rssreader.model.EntryItem;

public class DataCenter {
	private List<EntryItem> pickedData = Collections
			.synchronizedList(new ArrayList<EntryItem>());
	private List<EntryItem> homeData = Collections
			.synchronizedList(new ArrayList<EntryItem>());;
	private List<EntryItem> candicateData = Collections
			.synchronizedList(new ArrayList<EntryItem>());
	private List<EntryItem> newsData = Collections
			.synchronizedList(new ArrayList<EntryItem>());
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

	public EntryItem getDataItem(String url, int position) {
		EntryItem item = null;
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

	public List<EntryItem> getNewsData() {
		return newsData;
	}

	public void setNewsData(List<EntryItem> newsData) {
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
		instance = null;
	}
}
