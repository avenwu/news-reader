package com.avenwu.rssreader.activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.CsdnNewsAdapter;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.CsdnNewsItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.CsdnNewsRequest;
import com.avenwu.rssreader.xmlparse.ParseManager;

public class CSDNNewsFeedActivity extends Activity {
    private String TAG = "CSDN";

    private ListView newsListView;
    private CsdnNewsAdapter newsAdapter;
    private CsdnNewsRequest<Void> request;
    private BaseTask task;
    private UrlHandler urlHandler;
    private BaseListener<ArrayList<CsdnNewsItem>> listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csdn_feed_layout);
        initData();
        setListeners();
        startTask();
    }

    private void setListeners() {
        newsListView.setAdapter(newsAdapter);
        newsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message msgMessage = urlHandler.obtainMessage();
                            msgMessage.obj = ParseManager.jsoupParse(DataCenter
                                    .getInstance().getCsdnNewsData()
                                    .get(position).link);
                            msgMessage.sendToTarget();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void initData() {
        newsListView = (ListView) findViewById(R.id.lv_csdn_news);
        urlHandler = new UrlHandler(this);
        listener = new BaseListener<ArrayList<CsdnNewsItem>>() {
            @Override
            public void onSuccess(ArrayList<CsdnNewsItem> result) {
                DataCenter.getInstance().addCsdnNewsItems(result);
                if (CSDNNewsFeedActivity.this.isFinishing()) {
                    return;
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                if (CSDNNewsFeedActivity.this.isFinishing()) {
                    return;
                }
                Toast.makeText(CSDNNewsFeedActivity.this,
                        R.string.failed_to_get_content, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailed(Object result) {
                if (CSDNNewsFeedActivity.this.isFinishing()) {
                    return;
                }

                if (result instanceof Integer) {
                    Toast.makeText(CSDNNewsFeedActivity.this, (Integer) result,
                            Toast.LENGTH_SHORT).show();
                } else if (result instanceof String) {
                    Toast.makeText(CSDNNewsFeedActivity.this, (String) result,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CSDNNewsFeedActivity.this,
                            R.string.failed_to_get_content, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };
        request = new CsdnNewsRequest<Void>(listener);
        newsAdapter = new CsdnNewsAdapter(this, DataCenter.getInstance()
                .getCsdnNewsData());
    }

    private void startTask() {
        if (task != null)
            task.cancel();
        task = new BaseTask(getString(R.string.url_csdn_geek_news), request);
        task.start();
    }

    public static class UrlHandler extends Handler {
        private WeakReference<CSDNNewsFeedActivity> activity;

        public UrlHandler(CSDNNewsFeedActivity activity) {
            this.activity = new WeakReference<CSDNNewsFeedActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (activity.get() != null) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse((String) msg.obj);
                intent.setData(content_url);
                intent.setClassName("com.android.browser",
                        "com.android.browser.BrowserActivity");
                activity.get().startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        task.cancel();
        super.onDestroy();
    }
}
