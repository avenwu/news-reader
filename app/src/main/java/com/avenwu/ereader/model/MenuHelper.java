package com.avenwu.ereader.model;

import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

public interface MenuHelper {
	/**
	 * get the menu info to display
	 * 
	 * @return
	 */
	ArrayList<NewsMenuItem> getMenuItems();

	/**
	 * menu click event
	 * 
	 * @return
	 */
	OnItemClickListener getMenuListener();
}