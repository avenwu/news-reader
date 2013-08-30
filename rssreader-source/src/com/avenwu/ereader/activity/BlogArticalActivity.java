package com.avenwu.ereader.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.avenwu.ereader.R;
import com.avenwu.ereader.config.Constant;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.BaseDetailItem;

public class BlogArticalActivity extends SherlockActivity {
    private int contentId;
    private String contentType;
    private TextView authorView;
    private TextView timeView;
    private WebView contentView;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artical_layout);
        intiViews();
        contentId = getIntent().getIntExtra("content_id", 0);
        contentType = getIntent().getStringExtra("content_type");
        BaseDetailItem item = DataCenter.getInstance().getDataItem(contentType,
                contentId);
        authorView
                .setText(getString(R.string.author, item.getUser().getName()));
        titleView.setText(item.getTitle());
        timeView.setText(getString(R.string.timestamp, item.getPublised_time()));
        contentView.loadDataWithBaseURL(null, item.getContent(),
                Constant.TEXT_HTML, Constant.UTF_8, null);
        WebSettings webSettings = contentView.getSettings();
        webSettings.setBlockNetworkImage(true);
        webSettings.setBuiltInZoomControls(true);
    }

    private void intiViews() {
        authorView = (TextView) findViewById(R.id.tv_artical_author);
        timeView = (TextView) findViewById(R.id.tv_artical_timestamp);
        contentView = (WebView) findViewById(R.id.tv_content);
        titleView = (TextView) findViewById(R.id.tv_artical_title);
    }
}
