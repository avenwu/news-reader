package com.avenwu.ereader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.PhotoFeedAdapter;
import com.avenwu.ereader.bdphotos.BDProcessor;
import com.avenwu.ereader.bdphotos.BDProvider;
import com.avenwu.ereader.bdphotos.PhotoCacheRequest;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.avenwu.ereader.request.BDPhotoParams;
import com.avenwu.ereader.request.BDPhotoRequest;
import com.android.volley.volleyhelper.ApiManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;

import java.util.ArrayList;

import cn.waps.AdView;

public class BaiduPhotosActivity extends SherlockActivity {
    private PullToRefreshGridView photoFeedListView;
    private PhotoFeedAdapter photoFeedAdapter;
    private ImageView upTop;
    private Animation updownAnimation;
    private BDPhotoParams params;
    private BDPhotoRequest photoRequest;
    private BDProcessor processor;
    private PhotoCacheRequest cacheRequest;
    private ErrorListener errorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_feed_layout);
        setSupportProgressBarIndeterminateVisibility(true);
        initData();
        setListeners();
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
        ApiManager.init(this);
        upTop = (ImageView) findViewById(R.id.iv_up_top);
        photoFeedListView = (PullToRefreshGridView) findViewById(R.id.lv_photo_feed);
        photoFeedAdapter = new PhotoFeedAdapter(this, DataCenter.getInstance()
                .getPhotoFeedsItems());
        photoFeedListView.setAdapter(photoFeedAdapter);

        params = new BDPhotoParams(BDPhotoParams.MEI_NV, BDPhotoParams.XING_GAN);
        processor = new BDProcessor(this,
                new Listener<ArrayList<PhotoFeedItem>>() {
                    @Override
                    public void onResponse(ArrayList<PhotoFeedItem> response) {
                        Log.d("test", response.toString());
                        if (params.getCurrentPage() == 0) {
                            DataCenter.getInstance().getPhotoFeedsItems()
                                    .clear();
                        }
                        DataCenter.getInstance().addPhotoItems(response);
                        photoFeedAdapter.notifyDataSetChanged();
                        photoFeedListView.onRefreshComplete();
                        setSupportProgressBarIndeterminateVisibility(false);
                    }
                }, true);
        errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test", error.toString());
                photoFeedListView.onRefreshComplete();
                setSupportProgressBarIndeterminateVisibility(false);
            }
        };
        // try to get cached photos
        cacheRequest = new PhotoCacheRequest(new BDProvider(this),
                new Listener<ArrayList<PhotoFeedItem>>() {
                    @Override
                    public void onResponse(ArrayList<PhotoFeedItem> response) {
                        if (response.isEmpty()) {
                            refreshTask();
                        } else {
                            Log.d("test", response.toString());
                            DataCenter.getInstance()
                                    .replacePhotoItems(response);
                            photoFeedAdapter.notifyDataSetChanged();
                            photoFeedListView.onRefreshComplete();
                            setSupportProgressBarIndeterminateVisibility(false);
                        }
                    }
                });
        cacheRequest.excute();

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
        params.resetPage();
        if (photoRequest != null) {
            photoRequest.cancel();
        }
        processor.setClearOld(true);
        photoRequest = new BDPhotoRequest(params, processor, errorListener);
        photoRequest.excute();
    }

    void loadMoreTask() {
        params.updatePage();
        if (photoRequest != null) {
            photoRequest.cancel();
        }
        processor.setClearOld(false);
        photoRequest = new BDPhotoRequest(params, processor, errorListener);
        photoRequest.excute();
    }

    @Override
    protected void onDestroy() {
        if (photoRequest != null) {
            photoRequest.cancel();
        }
        DataCenter.getInstance().getPhotoFeedsItems().clear();
        super.onDestroy();
    }
}
