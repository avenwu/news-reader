package com.avenwu.rssreader.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.PhotoFeedAdapter;
import com.avenwu.rssreader.config.Constant;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.dataprovider.RssDaoManager;
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
    private RssDaoManager daoManager;
    private Button refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_feed_layout);
        daoManager = RssDaoManager.getInstance(this);
        try {
            DataCenter.getInstance().replacePhotoItems(
                    daoManager.getPhotoFeedItems());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        photoFeedListView = (GridView) findViewById(R.id.lv_photo_feed);
        refreshBtn = (Button) findViewById(R.id.btn_refresh);
        refreshBtn.setVisibility(View.GONE);

        photoFeedAdapter = new PhotoFeedAdapter(this, DataCenter.getInstance()
                .getPhotoFeedsItems());
        photoFeedListView.setAdapter(photoFeedAdapter);
        listener = new BaseListener<ArrayList<PhotoFeedItem>>() {
            @Override
            public void onSuccess(ArrayList<PhotoFeedItem> result) {
                if (result != null && !result.isEmpty()) {
                    DataCenter.getInstance().addPhotoItems(result);
                    photoFeedAdapter.notifyDataSetChanged();
                    try {
                        daoManager.addPhotoItems(result);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(Object result) {
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                refreshBtn.setVisibility(View.VISIBLE);
            }
        };
        request = new BaiduPhotoRequest<Void>(listener);
        if (photoFeedAdapter.getCount() != 0) {
            photoFeedAdapter.notifyDataSetChanged();
            refreshBtn.setVisibility(View.VISIBLE);
        } else {
            startTask();
        }
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
