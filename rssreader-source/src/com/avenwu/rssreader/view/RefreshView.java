package com.avenwu.rssreader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.avenwu.ereader.R;

public class RefreshView extends FrameLayout {
    private boolean refreshState = false;// false stands for need refreshing
    private ProgressBar progressBar;
    private ImageView refreshView;
    private RefreshListener listener;

    public RefreshView(Context context) {
        super(context);
        init(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RefreshView, 0, 0);
        try {
            refreshState = a.getBoolean(R.styleable.RefreshView_refreshState,
                    false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    public void init(Context context) {
        View view = inflate(context, R.layout.refreshview, null);
        addView(view);
        progressBar = (ProgressBar) view.findViewById(R.id.pr_refreshing);
        refreshView = (ImageView) view.findViewById(R.id.iv_refresh);
        progressBar.setVisibility(refreshState ? View.VISIBLE : View.GONE);
        refreshView.setVisibility(refreshState ? View.GONE : View.VISIBLE);
        refreshView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startRefresh();
                if (listener != null) {
                    listener.onStartRefresh();
                }
            }
        });
    }

    public boolean isRefreshState() {
        return refreshState;
    }

    public void setRefreshState(boolean refreshState) {
        this.refreshState = refreshState;
        invalidate();
        requestLayout();
    }

    public void setRefreshListener(RefreshListener listener) {
        this.listener = listener;
    }

    public void startRefresh() {
        refreshView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void completeRefresh() {
        refreshView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public static interface RefreshListener {
        public void onStartRefresh();

        public void onStopRefresh();
    }

}
