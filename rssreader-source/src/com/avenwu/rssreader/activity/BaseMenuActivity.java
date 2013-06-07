package com.avenwu.rssreader.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.GridView;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.adapter.MenuAdapter;
import com.avenwu.rssreader.model.MenuHelper;
import com.markupartist.android.widget.ActionBar;

public class BaseMenuActivity extends RoboActivity {
	@InjectView(R.id.gv_menu)
	private GridView menubar;
	private MenuAdapter menuAdapter;
	private MenuHelper menuHelper;
	@InjectView(R.id.actionbar)
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		actionBar.setTitle(R.string.app_name);
		menuHelper = (MenuHelper) this;
		menuAdapter = new MenuAdapter(this, menuHelper.getMenuItems());
		menubar.setAdapter(menuAdapter);
		menubar.setOnItemClickListener(menuHelper.getMenuListener());
	}

	public ActionBar getLibActionBar() {
		return actionBar;
	}
}
