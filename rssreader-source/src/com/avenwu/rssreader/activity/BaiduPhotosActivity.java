package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.PhotoFeedAdapter;
import com.avenwu.rssreader.config.Constant;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.rssreader.task.BaiduPhotoRequest;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseTask;

public class BaiduPhotosActivity extends Activity {
    private GridView photoFeedListView;
    private PhotoFeedAdapter photoFeedAdapter;
    private BaseListener<ArrayList<PhotoFeedItem>> listener;
    private BaiduPhotoRequest<Void> request;
    private BaseTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_feed_layout);
        photoFeedListView = (GridView) findViewById(R.id.lv_photo_feed);
        photoFeedAdapter = new PhotoFeedAdapter(this, DataCenter.getInstance()
                .getPhotoFeedsItems());
        photoFeedListView.setAdapter(photoFeedAdapter);
        listener = new BaseListener<ArrayList<PhotoFeedItem>>() {
            @Override
            public void onSuccess(ArrayList<PhotoFeedItem> result) {
                if (result != null && !result.isEmpty()) {
                    DataCenter.getInstance().addPhotoItems(result);
                    photoFeedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(Object result) {

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
            }
        };
        request = new BaiduPhotoRequest<Void>(listener);
        startTask();
    }

    void startTask() {
        if (task != null) {
            task.cancel();
        }
        task = new BaseTask(
                Constant.photo_request_predix
                        + "?fr=channel&tag1=%E7%BE%8E%E5%A5%B3&tag2=%E6%80%A7%E6%84%9F&sorttype=1&pn=0&rn=30&ie=utf8&oe=utf-8",
                request);
        task.start();
    }
}
