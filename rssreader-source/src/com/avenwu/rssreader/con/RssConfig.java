package com.avenwu.rssreader.con;

import android.content.Context;

import com.avenwu.rssreader.R;

public class RssConfig {
	private String homeUrl;
	private String pickedUrl;
	private String candicateUrl;
	private String newsUrl;
	private static RssConfig config;

	private RssConfig() {
	}

	/**
	 * initialize the config instance;
	 * 
	 * @param c
	 * @return
	 */
	public static RssConfig getInstance() {
		if (config == null) {
			synchronized (RssConfig.class) {
				if (config == null) {
					config = new RssConfig();
				}
			}
		}
		return config;
	}

	public void init(Context c) {
		config.homeUrl = c.getString(R.string.url_home_page);
		config.pickedUrl = c.getString(R.string.url_picked_page);
		config.newsUrl = c.getString(R.string.url_news_page);
		config.candicateUrl = c.getString(R.string.url_candidate_page);
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public String getPickedUrl() {
		return pickedUrl;
	}

	public String getCandicateUrl() {
		return candicateUrl;
	}

	public String getNewsUrl() {
		return newsUrl;
	}
}
