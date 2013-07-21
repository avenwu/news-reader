package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.NeteaseNewsItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.NeteaseRequest;

public class NeteaseActivity extends SherlockFragmentActivity {
    private static final String TAG = "NeteaseActivity";
    private ViewPager contentPager;
    private BaseTask task;
    private NeteaseRequest<Void> request;
    private BaseListener<ArrayList<NeteaseNewsItem>> listener;

    @Override
    protected void onCreate(Bundle arg0) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(arg0);
        setContentView(R.layout.net_ease_layout);
        contentPager = (ViewPager) findViewById(R.id.pager_contents);
        // AppConnect.getInstance(this).showPopAd(this);
        listener = new BaseListener<ArrayList<NeteaseNewsItem>>() {
            @Override
            public void onSuccess(ArrayList<NeteaseNewsItem> result) {
                Log.d(TAG, result.toString());
            }

            @Override
            public void onError(Exception e) {
                if (NeteaseActivity.this.isFinishing()) {
                    return;
                }
                Toast.makeText(NeteaseActivity.this,
                        R.string.failed_to_get_content, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailed(Object result) {
                if (NeteaseActivity.this.isFinishing()) {
                    return;
                }

                if (result instanceof Integer) {
                    Toast.makeText(NeteaseActivity.this, (Integer) result,
                            Toast.LENGTH_SHORT).show();
                } else if (result instanceof String) {
                    Toast.makeText(NeteaseActivity.this, (String) result,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NeteaseActivity.this,
                            R.string.failed_to_get_content, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                setSupportProgressBarIndeterminateVisibility(false);
            }
        };
        request = new NeteaseRequest<Void>(listener);
        task = new BaseTask("http://news.163.com/special/00011K6L/rss_newstop.xml", request);
        task.start();
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

    public void menuClick(View view) {
        Toast.makeText(this, "menu clicked," + ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
    }
}
