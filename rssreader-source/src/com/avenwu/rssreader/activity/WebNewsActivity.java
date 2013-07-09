package com.avenwu.rssreader.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.WebView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.utils.Toolkit;

public class WebNewsActivity extends SherlockActivity {
    private WebView webView;
    private CountDownTimer timer;
    private final int MILLS_IN_FUTURE = 2000;
    private final int COUNTDOWN_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.web_news_layout);
        webView = (WebView) findViewById(R.id.wv_news);
        String url = getIntent().getStringExtra("url");
        if (Toolkit.isUrlValid(url)) {
            getSupportActionBar().setTitle(url);
            webView.loadUrl(url);
            webView.getSettings().setJavaScriptEnabled(true);
        } else {
            Toast.makeText(this, R.string.url_invalid, Toast.LENGTH_SHORT)
                    .show();
            timer = new CountDownTimer(MILLS_IN_FUTURE, COUNTDOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    timer = null;
                    onBackPressed();
                }
            };
            timer.start();
        }
    }

    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            super.onBackPressed();
            return;
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
