package com.avenwu.ereader.model;

import java.util.ArrayList;

import android.widget.AdapterView.OnItemClickListener;

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