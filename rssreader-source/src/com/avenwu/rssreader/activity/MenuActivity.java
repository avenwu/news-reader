package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import cn.waps.AdView;
import cn.waps.AppConnect;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.ad.LoadActivity;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.MenuHelper;
import com.avenwu.rssreader.model.NewsMenuItem;
import com.avenwu.rssreader.service.NetworkReceiver;
import com.avenwu.rssreader.task.TaskManager;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.pakg.m.MediaManager;

public class MenuActivity extends BaseMenuActivity implements MenuHelper {
    private String[] menuTitles;
    private String[] menuDescriptions;
    private NetworkReceiver networkReceiver;
    private IntentFilter intentFilter;
    private final byte CNBLOG = 0;
    private final byte CSDN = 1;
    private final byte NET_EASE = 2;
    private final byte BAIDU = 3;
    private final String KUGUO_APP_ID = "13bb74a44903443ba53e76526086065c";
    private final String WAPS_APP_ID = "d2c72e069bde92b7e86f87c0bc9431e2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        menuTitles = getResources().getStringArray(R.array.menu_titles);
        menuDescriptions = getResources().getStringArray(
                R.array.menu_descriptions);
        super.onCreate(savedInstanceState);
        NetworkHelper.updateConnectionState(this);
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, intentFilter);
        // AppConnect.getInstance(WAPS_APP_ID, "WAPS", this);
        // AppConnect.getInstance(this).setAdViewClassName(
        // "com.avenwu.rssreader.activity.MyAdView");
        // AppConnect.getInstance(this).setCrashReport(false);
        LinearLayout container = (LinearLayout) findViewById(R.id.ll_ad);
        new AdView(this, container).DisplayAd();
        // AppConnect.getInstance(this).initPopAd(this);
    }

    @Override
    public ArrayList<NewsMenuItem> getMenuItems() {
        ArrayList<NewsMenuItem> menuItems = new ArrayList<NewsMenuItem>();
        for (int i = 0; i < menuTitles.length; i++) {
            NewsMenuItem item = new NewsMenuItem();
            item.setMenuTitle(menuTitles[i]);
            item.setMenuDescription(menuDescriptions[i]);
            menuItems.add(item);
        }
        DataCenter.getInstance().setMenuItems(menuItems);
        return DataCenter.getInstance().getMenuItems();
    }

    @Override
    public OnItemClickListener getMenuListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                    int position, long id) {

                Intent intent = new Intent();
                switch (position) {
                case CNBLOG:
                    intent.setClass(MenuActivity.this,
                            CnblogsNewsFeedActivity.class);
                    break;
                case CSDN:
                    intent.setClass(MenuActivity.this,
                            CSDNNewsFeedActivity.class);
                    break;
                case NET_EASE:
                    intent.setClass(MenuActivity.this, NeteaseActivity.class);
                    break;
                case BAIDU:
                    intent.setClass(MenuActivity.this,
                            BaiduPhotosActivity.class);
                    break;
                default:
                    break;
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.fade_out);
            }
        };
    }

    @Override
    protected void onDestroy() {
        AppConnect.getInstance(this).close();
        TaskManager.getInstance().cancellAll();
        unregisterReceiver(networkReceiver);
        DataCenter.getInstance().clear();
        super.onDestroy();
    }
}
