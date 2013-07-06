package com.avenwu.rssreader.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.config.Constant;
import com.avenwu.rssreader.config.RssConfig;
import com.avenwu.rssreader.dataprovider.RssDaoManager;
import com.avenwu.rssreader.model.QueryListener;
import com.avenwu.rssreader.service.NetworkReceiver;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.avenwu.rssreader.view.RefreshView;
import com.avenwu.rssreader.view.RefreshView.RefreshListener;
import com.google.inject.Inject;

public class CnblogsNewsFeedActivity extends RoboFragmentActivity implements
        RefreshListener {

    private final String TAG = "MainActivity";

    @Inject
    private NetworkReceiver networkReceiver;
    private IntentFilter intentFilter;
    private RssDaoManager daoManager;
    @InjectResource(R.array.cnblogs_catalog)
    private String[] catalogStrings;
    private int curent_catalog_index = -1;
    @InjectView(R.id.refreshView1)
    private RefreshView refreshView;
    @InjectView(R.id.iv_back)
    private ImageView ivHomeBack;
    @InjectView(R.id.tv_title)
    private TextView tvTitle;
    private String[] titleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RssConfig.getInstance().init(this);
        titleArray = getResources().getStringArray(R.array.cnblogs_catalog);
        NetworkHelper.updateConnectionState(this);
        daoManager = new RssDaoManager(this);
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, intentFilter);
        changeCatalog(Constant.CNBLOGS_HOME);

        refreshView.setRefreshListener(this);
        tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(v);
            }
        });
        ivHomeBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void changeCatalog(int which) {
        if (curent_catalog_index != which) {
            curent_catalog_index = which;
            refreshView.startRefresh();
            tvTitle.setText(titleArray[curent_catalog_index]);
            FragmentTransaction tr = getSupportFragmentManager()
                    .beginTransaction();
            switch (which) {
            case Constant.CNBLOGS_HOME:
                tr.replace(R.id.frame_content,
                        CnblogsHomeFragment.newInstance(daoManager));
                break;
            case Constant.CNBLOGS_PICKED:
                tr.replace(R.id.frame_content,
                        CnblogsPickedFragment.newInstance(daoManager));
                break;
            case Constant.CNBLOGS_CANDICATE:

                break;
            case Constant.CNBLOGS_NEWS:

                break;
            default:
                break;
            }
            tr.commit();
        }
    }

    public void onPopupButtonClick(View button) {
        PopupMenu popup = new PopupMenu(this, button);
        popup.getMenuInflater().inflate(R.menu.cnblog_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.menu_cnblog_home:
                    changeCatalog(Constant.CNBLOGS_HOME);
                    break;
                case R.id.menu_cnblog_picked:
                    changeCatalog(Constant.CNBLOGS_PICKED);
                default:
                    break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkReceiver);
    }

    @Override
    public void onStartRefresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(
                R.id.frame_content);
        if (fragment != null && fragment instanceof QueryListener) {
            ((QueryListener) fragment).startTask();
        }
    }

    @Override
    public void onStopRefresh() {
        refreshView.completeRefresh();
    }
}