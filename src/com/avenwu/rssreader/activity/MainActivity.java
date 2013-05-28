package com.avenwu.rssreader.activity;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.os.Bundle;
import android.widget.TextView;

import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.avenwu.rssreader.R;
import com.avenwu.rssreader.con.RssConfig;
import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.rssparse.XMLParseManager;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.TaskManager;

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
        BaseTask task = new BaseTask(new Callable<ArrayList<EntryItem>>() {
            @Override
            public ArrayList<EntryItem> call() throws Exception {
                return XMLParseManager.parseRssXML(RssConfig.getInstance().getPickedUrl());
            }
        });
        task.cancel(true);
        TaskManager.getInstance().excute(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManager.getInstance().cancellAll();
    }
}
