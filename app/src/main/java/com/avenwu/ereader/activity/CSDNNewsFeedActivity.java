package com.avenwu.ereader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.CsdnNewsAdapter;
import com.avenwu.ereader.dataprovider.DaoManager;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.CsdnNewsItem;
import com.avenwu.ereader.task.BaseListener;
import com.avenwu.ereader.task.BaseTask;
import com.avenwu.ereader.task.CsdnNewsRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;


public class CSDNNewsFeedActivity extends Activity {
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csdn_feed_layout);
        setProgressBarIndeterminateVisibility(true);
        initData();
        setListeners();
        if (newsAdapter.getCount() != 0) {
            newsAdapter.notifyDataSetChanged();
            setProgressBarIndeterminateVisibility(false);
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
                CsdnNewsItem dataItem = (CsdnNewsItem) parent.getAdapter().getItem(position);
                Message msgMessage = urlHandler.obtainMessage();
                msgMessage.obj = dataItem.link;
                msgMessage.sendToTarget();
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
        newsListView.getRefreshableView().addFooterView(footerView);
        urlHandler = new UrlHandler(this);
        listener = new BaseListener<ArrayList<CsdnNewsItem>>() {
            @Override
            public void onSuccess(ArrayList<CsdnNewsItem> result) {
                if (result.size() > 0)
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
                setProgressBarIndeterminateVisibility(false);
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
        setProgressBarIndeterminateVisibility(true);
        if (task != null)
            task.cancel();
        task = new BaseTask(getString(R.string.url_csdn_geek_news), request);
        task.start();
    }

    public class UrlHandler extends Handler {
        private WeakReference<CSDNNewsFeedActivity> activity;

        public UrlHandler(CSDNNewsFeedActivity activity) {
            this.activity = new WeakReference<CSDNNewsFeedActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (activity.get() != null) {
                Intent intent = new Intent();
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
