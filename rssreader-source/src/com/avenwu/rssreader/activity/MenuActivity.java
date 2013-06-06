package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.R.anim;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.adapter.MenuAdapter;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.NewsMenuItem;

public class MenuActivity extends RoboActivity {
	@InjectView(R.id.gv_menu)
	private GridView menubar;
	private MenuAdapter menuAdapter;
	@InjectResource(R.array.menu_titles)
	private String[] menuTitles;
	@InjectResource(R.array.menu_descriptions)
	private String[] menuDescriptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		ArrayList<NewsMenuItem> menuItems = new ArrayList<NewsMenuItem>();
		for (int i = 0; i < menuTitles.length; i++) {
			NewsMenuItem item = new NewsMenuItem();
			item.setMenuTitle(menuTitles[i]);
			item.setMenuDescription(menuDescriptions[i]);
			menuItems.add(item);
		}
		DataCenter.getInstance().setMenuItems(menuItems);
		menuAdapter = new MenuAdapter(this);
		menubar.setAdapter(menuAdapter);
		menubar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (position == 0) {
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this, RssMainActivity.class);
					startActivity(intent);
//					overridePendingTransition(android.R.anim.slide_out_right,
//							android.R.anim.slide_in_left);
				}
			}
		});
	}

}
