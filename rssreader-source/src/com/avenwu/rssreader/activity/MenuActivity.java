package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import roboguice.inject.InjectResource;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.MenuHelper;
import com.avenwu.rssreader.model.NewsMenuItem;
import com.avenwu.rssreader.task.TaskManager;

public class MenuActivity extends BaseMenuActivity implements MenuHelper {
	@InjectResource(R.array.menu_titles)
	private String[] menuTitles;
	@InjectResource(R.array.menu_descriptions)
	private String[] menuDescriptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TaskManager.getInstance().cancellAll();
		DataCenter.getInstance().clear();
	}

	@Override
	public ArrayList<NewsMenuItem> getMenuItems() {
		ArrayList<NewsMenuItem> menuItems = new ArrayList<NewsMenuItem>();
		for (int i = 0; i < menuTitles.length; i++) {
			NewsMenuItem item = new NewsMenuItem();
			item.setMenuTitle(menuTitles[i]);
			item.setMenuDescription(menuDescriptions[i]);
			menuItems.add(item);
		}
		DataCenter.getInstance().setMenuItems(menuItems);
		return DataCenter.getInstance().getMenuItems();
	}

	@Override
	public OnItemClickListener getMenuListener() {
		return new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (position == 0) {
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this,
							CnblogsNewsFeedActivity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.fade_out);
				} else {
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this, TestClick.class);
					startActivity(intent);
				}
			}
		};
	}
}
