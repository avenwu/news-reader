package com.avenwu.rssreader.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.avenwu.rssreader.R;
import com.avenwu.rssreader.adapter.CnblogPickedAdapter;
import com.avenwu.rssreader.adapter.CnblogPickedAdapter.ArticalListener;
import com.avenwu.rssreader.con.RssConfig;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.dataprovider.RssDaoManager;
import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.service.NetworkReceiver;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseRequest;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.RssCnblogRequest;
import com.avenwu.rssreader.task.TaskManager;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.google.inject.Inject;

public class RssMainActivity extends RoboActivity {

    private final String TAG = "MainActivity";
    @InjectView(R.id.flipview_rss)
    private FlipViewController flipview;
    private CnblogPickedAdapter pickedAdapter;
    private BaseTask task;
    @InjectView(R.id.progressbar)
    private ProgressBar progressBar;
    private CnblogPickedAdapter.ArticalListener listener;
    @Inject
    private NetworkReceiver networkReceiver;
    private IntentFilter intentFilter;
    private RssDaoManager daoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flipview.setAnimationBitmapFormat(Config.RGB_565);
        RssConfig.getInstance().init(this);
        NetworkHelper.updateConnectionState(this);
        daoManager = new RssDaoManager(this);
        
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, intentFilter);
        startTask();
        listener = new ArticalListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(v.getContext(), BlogArticalActivity.class);
                intent.putExtra("content_id", position);
                intent.putExtra("content_type", RssConfig.getInstance().getPickedUrl());
                v.getContext().startActivity(intent);
            }
        };
        pickedAdapter = new CnblogPickedAdapter(this, listener);
        flipview.setAdapter(pickedAdapter);
        flipview.setOnViewFlipListener(new ViewFlipListener() {
            @Override
            public void onViewFlipped(View view, int position) {
                listener.updatePosition(position);
            }
        });
    }

    private void startTask() {
        BaseRequest request = new RssCnblogRequest<Void>(new BaseListener<ArrayList<EntryItem>>() {
            @Override
            public void onSuccess(ArrayList<EntryItem> result) {
                Toast.makeText(RssMainActivity.this, "success", Toast.LENGTH_SHORT).show();
                DataCenter.getInstance().addPickedItems(result);
                pickedAdapter.notifyDataSetChanged();
                try {
                    daoManager.addEntryItems(result);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "failed to insert tinto table", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Object result) {
                if (result instanceof Integer) {
                    Toast.makeText(RssMainActivity.this, (Integer) result, Toast.LENGTH_SHORT).show();
                } else if (result instanceof String) {
                    Toast.makeText(RssMainActivity.this, (String) result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RssMainActivity.this, R.string.failed_to_get_content, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(RssMainActivity.this, R.string.failed_to_get_content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                progressBar.setVisibility(View.GONE);
            }

        });

        task = new BaseTask(RssConfig.getInstance().getPickedUrl(), request);
        task.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flipview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flipview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManager.getInstance().cancellAll();
        this.unregisterReceiver(networkReceiver);
    }
}
