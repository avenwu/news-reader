package com.avenwu.ereader.activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.waps.AdView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.CsdnNewsAdapter;
import com.avenwu.ereader.dataprovider.DaoManager;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.CsdnNewsItem;
import com.avenwu.ereader.task.BaseListener;
import com.avenwu.ereader.task.BaseTask;
import com.avenwu.ereader.task.CsdnNewsRequest;
import com.avenwu.ereader.xmlparse.ParseManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CSDNNewsFeedActivity extends SherlockActivity {
    private String TAG = "CSDN";
    private PullToRefreshListView newsListView;
    private CsdnNewsAdapter newsAdapter;
    private CsdnNewsRequest request;
    private BaseTask task;
    private UrlHandler urlHandler;
    private BaseListener<ArrayList<CsdnNewsItem>> listener;
    private DaoManager daoManager;
    private View footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csdn_feed_layout);
        setSupportProgressBarIndeterminateVisibility(true);
        initData();
        setListeners();
        if (newsAdapter.getCount() != 0) {
            newsAdapter.notifyDataSetChanged();
            setSupportProgressBarIndeterminateVisibility(false);
        } else {
            startTask();
        }
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
        newsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startTask();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        daoManager = DaoManager.getInstance(this);
        newsListView = (PullToRefreshListView) findViewById(R.id.lv_csdn_news);
        footerView = View.inflate(this, R.layout.ll_ad_layout, null);
        newsListView.getRefreshableView().addHeaderView(footerView);
        new AdView(this, (LinearLayout) footerView).DisplayAd();
        urlHandler = new UrlHandler(this);
        listener = new BaseListener<ArrayList<CsdnNewsItem>>() {
            @Override
            public void onSuccess(ArrayList<CsdnNewsItem> result) {
                DataCenter.getInstance().replaceCsdnNewsItems(result);
                if (CSDNNewsFeedActivity.this.isFinishing()) {
                    return;
                }
                newsAdapter.notifyDataSetChanged();
                try {
                    daoManager.addCsdnNewsItemse(result);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

            @Override
            public void onFinished() {
                super.onFinished();
                newsListView.onRefreshComplete();
                setSupportProgressBarIndeterminateVisibility(false);
            }
        };
        request = new CsdnNewsRequest(listener);
        try {
            DataCenter.getInstance().replaceCsdnNewsItems(
                    daoManager.getCsdnNewsItems());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        newsAdapter = new CsdnNewsAdapter(this, DataCenter.getInstance()
                .getCsdnNewsData());
    }

    private void startTask() {
        setSupportProgressBarIndeterminateVisibility(true);
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
                // intent.setAction("android.intent.action.VIEW");
                // Uri content_url = Uri.parse((String) msg.obj);
                // intent.setData(content_url);
                // intent.setClassName("com.android.browser",
                // "com.android.browser.BrowserActivity");
                // activity.get().startActivity(intent);
                intent.setClass(activity.get(), WebNewsActivity.class);
                intent.putExtra("url", (String) msg.obj);
                activity.get().startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (task != null) {
            task.cancel();
        }
        DataCenter.getInstance().getCsdnNewsData().clear();
        super.onDestroy();
    }
}
