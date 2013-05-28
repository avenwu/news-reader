package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.avenwu.rssreader.R;
import com.avenwu.rssreader.con.RssConfig;
import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.rssparse.XMLParseManager;

public class MainActivity extends SActivity {
    private ArrayList<EntryItem> items;
    private final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv1);
        RssConfig.getInstance().init(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    items = XMLParseManager.parseRssXML(RssConfig.getInstance().getPickedUrl());
                    for (EntryItem item : items) {
                        Log.e(TAG, item.getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
