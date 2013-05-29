package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.avenwu.rssreader.R;
import com.avenwu.rssreader.con.RssConfig;
import com.avenwu.rssreader.data.DataCenter;
import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseRequest;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.RssCnblogRequest;
import com.avenwu.rssreader.task.TaskManager;

public class MainActivity extends SActivity {

    private final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv1);
        RssConfig.getInstance().init(this);
        BaseRequest request = new RssCnblogRequest<Void>(new BaseListener<ArrayList<EntryItem>>() {
            @Override
            public void onSuccess(ArrayList<EntryItem> result) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                DataCenter.getInstance().addPickedItems(result);
                for (EntryItem entryItem : result) {
                    tv.append(entryItem.getId());
                    Log.d(TAG, entryItem.getId());
                }
            }

            @Override
            public void onFailed() {
                Toast.makeText(MainActivity.this, "failed to get rss content", Toast.LENGTH_SHORT).show();
            }
        });

        BaseTask task = new BaseTask(RssConfig.getInstance().getPickedUrl(), request);
        task.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManager.getInstance().cancellAll();
    }
}
