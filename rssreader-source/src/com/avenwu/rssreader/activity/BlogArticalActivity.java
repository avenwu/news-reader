package com.avenwu.rssreader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.con.Constant;
import com.avenwu.rssreader.data.DataCenter;
import com.avenwu.rssreader.model.EntryItem;

public class BlogArticalActivity extends Activity {
    private int contentId;
    private String contentType;
    private TextView titleView;
    private TextView authorView;
    private TextView timeView;
    private WebView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artical_layout);
        contentView = (WebView) findViewById(R.id.tv_content);
        timeView = (TextView) findViewById(R.id.tv_artical_timestamp);
        titleView = (TextView) findViewById(R.id.tv_artical_title);
        authorView = (TextView) findViewById(R.id.tv_artical_author);
        contentId = getIntent().getIntExtra("content_id", 0);
        contentType = getIntent().getStringExtra("content_type");
        EntryItem item = DataCenter.getInstance().getDataItem(contentType, contentId);
        authorView.setText(getString(R.string.author, item.getUser().getName()));
        titleView.setText(item.getTitle());
        timeView.setText(getString(R.string.timestamp, item.getPublised_time()));
        contentView.loadDataWithBaseURL(null, item.getContent(), Constant.TEXT_HTML, Constant.UTF_8, null);
    }
}
