package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.avenwu.rssreader.R;
import com.avenwu.rssreader.adapter.CnblogPickedAdapter;
import com.avenwu.rssreader.con.RssConfig;
import com.avenwu.rssreader.data.DataCenter;
import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseRequest;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.RssCnblogRequest;
import com.avenwu.rssreader.task.TaskManager;

public class RssMainActivity extends RoboActivity {

    private final String TAG = "MainActivity";
    @InjectView(R.id.flipview_rss)
    private FlipViewController flipview;
    private CnblogPickedAdapter pickedAdapter;
    private BaseTask task;
    @InjectView(R.id.progressbar)
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flipview.setAnimationBitmapFormat(Config.RGB_565);

        RssConfig.getInstance().init(this);
        startTask();
        pickedAdapter = new CnblogPickedAdapter(this);
        flipview.setAdapter(pickedAdapter);
    }

    private void startTask() {
        BaseRequest request = new RssCnblogRequest<Void>(new BaseListener<ArrayList<EntryItem>>() {
            @Override
            public void onSuccess(ArrayList<EntryItem> result) {
                Toast.makeText(RssMainActivity.this, "success", Toast.LENGTH_SHORT).show();
                DataCenter.getInstance().addPickedItems(result);
                pickedAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {
                Toast.makeText(RssMainActivity.this, "failed to get rss content", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                Toast.makeText(RssMainActivity.this, "failed to get rss content", Toast.LENGTH_SHORT).show();
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
    }
}
