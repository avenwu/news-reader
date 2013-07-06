package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.CsdnNewsAdapter;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.CsdnNewsItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.CsdnNewsRequest;

public class CSDNNewsFeedActivity extends Activity {
    private String TAG = "CSDN";

    private ListView newsListView;
    private CsdnNewsAdapter newsAdapter;
    private CsdnNewsRequest<Void> request;
    private BaseTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csdn_feed_layout);
        newsListView = (ListView) findViewById(R.id.lv_csdn_news);
        newsAdapter = new CsdnNewsAdapter(this, DataCenter.getInstance()
                .getCsdnNewsData());
        newsListView.setAdapter(newsAdapter);
        startTask();
    }

    private void startTask() {
        if (request == null) {
            request = new CsdnNewsRequest<Void>(
                    new BaseListener<ArrayList<CsdnNewsItem>>() {
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
                                    R.string.failed_to_get_content,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(Object result) {
                            if (CSDNNewsFeedActivity.this.isFinishing()) {
                                return;
                            }

                            if (result instanceof Integer) {
                                Toast.makeText(CSDNNewsFeedActivity.this,
                                        (Integer) result, Toast.LENGTH_SHORT)
                                        .show();
                            } else if (result instanceof String) {
                                Toast.makeText(CSDNNewsFeedActivity.this,
                                        (String) result, Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(CSDNNewsFeedActivity.this,
                                        R.string.failed_to_get_content,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if (task != null)
            task.cancel();
        task = new BaseTask(getString(R.string.url_csdn_geek_news), request);
        task.start();
    }

    @Override
    protected void onDestroy() {
        task.cancel();
        super.onDestroy();
    }
}
