package com.avenwu.rssreader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestClick extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText("hello world");
		setContentView(textView);
	}
}
