package com.avenwu.ereader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.avenwu.ereader.R;
import com.avenwu.ereader.utils.Toolkit;

public class WebNewsActivity extends SherlockActivity {
    private WebView webView;
    private CountDownTimer timer;
    private final int MILLS_IN_FUTURE = 2000;
    private final int COUNTDOWN_INTERVAL = 1000;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.web_news_layout);
        webView = (WebView) findViewById(R.id.wv_news);
        url = getIntent().getStringExtra("url");
        if (Toolkit.isUrlValid(url)) {
            getSupportActionBar().setTitle(url);
            WebViewClient client = new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    setSupportProgressBarIndeterminateVisibility(false);
                    super.onPageFinished(view, url);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    setSupportProgressBarIndeterminateVisibility(true);
                    super.onLoadResource(view, url);
                }

            };
            webView.setWebViewClient(client);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(R.string.web_client);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_browse);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        // intent.setClassName("com.android.browser",
        // "com.android.browser.BrowserActivity");
        item.setIntent(intent);
        return super.onCreateOptionsMenu(menu);
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
