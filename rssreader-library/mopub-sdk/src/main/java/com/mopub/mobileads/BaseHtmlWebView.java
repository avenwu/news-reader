package com.mopub.mobileads;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static com.mopub.mobileads.util.VersionCode.ICE_CREAM_SANDWICH;
import static com.mopub.mobileads.util.VersionCode.currentApiLevel;

public class BaseHtmlWebView extends BaseWebView {
    public BaseHtmlWebView(Context context) {
        super(context);

        disableScrollingAndZoom();
        getSettings().setJavaScriptEnabled(true);

        if (currentApiLevel().isAtLeast(ICE_CREAM_SANDWICH)) {
            enablePlugins(true);
        }
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void init(boolean isScrollable) {
        setWebViewScrollingEnabled(isScrollable);
    }

    @Override
    public void loadUrl(String url) {
        if (url == null) return;

        Log.d("MoPub", "Loading url: " + url);
        if (url.startsWith("javascript:")) {
            super.loadUrl(url);
        }
    }

    private void disableScrollingAndZoom() {
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        getSettings().setSupportZoom(false);
    }

    void loadHtmlResponse(String htmlResponse) {
        loadDataWithBaseURL("http://ads.mopub.com/", htmlResponse, "text/html", "utf-8", null);
    }

    void setWebViewScrollingEnabled(boolean isScrollable) {
        if (isScrollable) {
            setOnTouchListener(null);
        } else {
            setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });
        }
    }
}
