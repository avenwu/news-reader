package com.avenwu.rssreader.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.con.Constant;
import com.avenwu.rssreader.data.DataCenter;
import com.avenwu.rssreader.model.EntryItem;

public class BlogArticalActivity extends RoboActivity {
    private int contentId;
    private String contentType;
    @InjectView(R.id.tv_artical_author)
    private TextView authorView;
    @InjectView(R.id.tv_artical_timestamp)
    private TextView timeView;
    @InjectView(R.id.tv_content)
    private WebView contentView;
    @InjectView(R.id.tv_artical_title)
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artical_layout);
        contentId = getIntent().getIntExtra("content_id", 0);
        contentType = getIntent().getStringExtra("content_type");
        EntryItem item = DataCenter.getInstance().getDataItem(contentType, contentId);
        authorView.setText(getString(R.string.author, item.getUser().getName()));
        titleView.setText(item.getTitle());
        timeView.setText(getString(R.string.timestamp, item.getPublised_time()));
        contentView.loadDataWithBaseURL(null, item.getContent(), Constant.TEXT_HTML, Constant.UTF_8, null);
        WebSettings webSettings = contentView.getSettings();
        webSettings.setBlockNetworkImage(true);
        webSettings.setBuiltInZoomControls(true);
    }
}
