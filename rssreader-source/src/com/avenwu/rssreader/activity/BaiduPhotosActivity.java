package com.avenwu.rssreader.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.waps.AdView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.PhotoFeedAdapter;
import com.avenwu.rssreader.dataprovider.DaoManager;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.rssreader.model.PhotoParams;
import com.avenwu.rssreader.task.BaiduPhotoRequest;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;

public class BaiduPhotosActivity extends SherlockActivity {
    private PullToRefreshGridView photoFeedListView;
    private PhotoFeedAdapter photoFeedAdapter;
    private BaseListener<ArrayList<PhotoFeedItem>> refreshListener,
            loadMoreListener;
    private BaiduPhotoRequest<Void> refreshRequest, loadmoreRequest;
    private BaseTask task;
    private DaoManager daoManager;
    private PhotoParams photoParams = new PhotoParams();
    private ImageView upTop;
    private Animation updownAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_feed_layout);
        setSupportProgressBarIndeterminateVisibility(true);
        initData();
        setListeners();
        if (photoFeedAdapter.getCount() != 0) {
            photoFeedAdapter.notifyDataSetChanged();
            setSupportProgressBarIndeterminateVisibility(false);
        } else {
            refreshTask();
        }
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

    private void initData() {
        daoManager = DaoManager.getInstance(this);
        try {
            DataCenter.getInstance().replacePhotoItems(
                    daoManager.getPhotoFeedItems());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        upTop = (ImageView) findViewById(R.id.iv_up_top);
        photoFeedListView = (PullToRefreshGridView) findViewById(R.id.lv_photo_feed);
        photoFeedAdapter = new PhotoFeedAdapter(this, DataCenter.getInstance()
                .getPhotoFeedsItems());
        photoFeedListView.setAdapter(photoFeedAdapter);
        refreshListener = new BaseListener<ArrayList<PhotoFeedItem>>() {
            @Override
            public void onSuccess(ArrayList<PhotoFeedItem> result) {
                if (result != null && !result.isEmpty()) {
                    DataCenter.getInstance().replacePhotoItems(result);
                    photoFeedAdapter.notifyDataSetChanged();
                    try {
                        daoManager.addPhotoItems(result);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(Object result) {
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                photoFeedListView.onRefreshComplete();
                setSupportProgressBarIndeterminateVisibility(false);
            }
        };
        loadMoreListener = new BaseListener<ArrayList<PhotoFeedItem>>() {
            @Override
            public void onSuccess(ArrayList<PhotoFeedItem> result) {
                if (result != null && !result.isEmpty()) {
                    DataCenter.getInstance().addPhotoItems(result);
                    photoFeedAdapter.notifyDataSetChanged();
                    try {
                        daoManager.addPhotoItems(result);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(Object result) {
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                photoFeedListView.onRefreshComplete();
                setSupportProgressBarIndeterminateVisibility(false);
            }
        };
        refreshRequest = new BaiduPhotoRequest<Void>(refreshListener);
        loadmoreRequest = new BaiduPhotoRequest<Void>(loadMoreListener);
        // set up animation
        updownAnimation = new TranslateAnimation(0.0f, upTop.getWidth()
                - upTop.getWidth() - upTop.getPaddingLeft()
                - upTop.getPaddingRight(), 0.0f, 0.0f);
        updownAnimation = new TranslateAnimation(upTop.getX(), upTop.getX(),
                upTop.getY(), -20);
        updownAnimation.setDuration(1000);
        updownAnimation.setStartOffset(300);
        updownAnimation.setRepeatMode(Animation.RESTART);
        updownAnimation.setRepeatCount(Animation.INFINITE);
        updownAnimation.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.anim.overshoot_interpolator));
    }

    private void setListeners() {
        photoFeedListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(BaiduPhotosActivity.this,
                        PhotoDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.fade_out);
            }
        });
        photoFeedListView.setMode(Mode.BOTH);
        photoFeedListView
                .setOnRefreshListener(new OnRefreshListener2<GridView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView) {
                        refreshTask();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView) {
                        loadMoreTask();
                    }
                });
        photoFeedListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                Log.d("testscoll", "firstVisibleItem=" + firstVisibleItem);
                if (firstVisibleItem <= 4) {
                    upTop.setVisibility(View.INVISIBLE);
                    upTop.clearAnimation();
                } else {
                    upTop.setVisibility(View.VISIBLE);
                    upTop.startAnimation(updownAnimation);
                }
            }
        });
        LoadingLayout header = (LoadingLayout) photoFeedListView.getChildAt(0);
        LinearLayout adlLayout = (LinearLayout) View.inflate(this,
                R.layout.ll_ad_layout, null);
        new AdView(this, adlLayout).DisplayAd();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = (int) (60 * metrics.density);
        header.addView(adlLayout, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, height);
        header.setLayoutParams(layoutParams);
        header.setPadding(0, 0, 0, 0);
        upTop.setAnimation(updownAnimation);
    }

    public void up2Top(View view) {
        photoFeedListView.getRefreshableView().smoothScrollToPositionFromTop(0,
                0, photoFeedListView.getBottom() / 2);

    }

    void refreshTask() {
        photoParams.pageCount = 1;
        setSupportProgressBarIndeterminateVisibility(true);
        if (task != null) {
            task.cancel();
        }
        task = new BaseTask(photoParams.getRequest(), refreshRequest);
        task.start();
    }

    void loadMoreTask() {
        photoParams.pageCount++;
        setSupportProgressBarIndeterminateVisibility(true);
        if (task != null) {
            task.cancel();
        }
        task = new BaseTask(photoParams.getRequest(), loadmoreRequest);
        task.start();
    }

    @Override
    protected void onDestroy() {
        if (task != null) {
            task.cancel();
        }
        DataCenter.getInstance().getPhotoFeedsItems().clear();
        super.onDestroy();
    }
}
