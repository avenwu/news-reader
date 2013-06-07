package com.avenwu.rssreader.utils;

import android.content.Context;
import android.content.Intent;

import com.avenwu.rssreader.activity.MenuActivity;

public class UIHelper {
	public static Intent back2Menu(Context context) {
		Intent i = new Intent(context, MenuActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}
	
}
