package com.avenwu.rssreader.activity;

import java.util.ArrayList;

import roboguice.inject.InjectResource;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.MenuHelper;
import com.avenwu.rssreader.model.NewsMenuItem;
import com.avenwu.rssreader.service.NetworkReceiver;
import com.avenwu.rssreader.task.TaskManager;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.google.inject.Inject;

public class MenuActivity extends BaseMenuActivity implements MenuHelper {
    @InjectResource(R.array.menu_titles)
    private String[] menuTitles;
    @InjectResource(R.array.menu_descriptions)
    private String[] menuDescriptions;
    @Inject
    private NetworkReceiver networkReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkHelper.updateConnectionState(this);

        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, intentFilter);
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
                case 0:
                    intent.setClass(MenuActivity.this,
                            CnblogsNewsFeedActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.fade_out);
                    break;
                case 1:
                    intent.setClass(MenuActivity.this,
                            CSDNNewsFeedActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.fade_out);
                    break;
                case 3:
                    intent.setClass(MenuActivity.this,
                            BaiduPhotosActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.fade_out);
                    break;
                default:
                    break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        TaskManager.getInstance().cancellAll();
        unregisterReceiver(networkReceiver);
        DataCenter.getInstance().clear();
        super.onDestroy();
    }
}
