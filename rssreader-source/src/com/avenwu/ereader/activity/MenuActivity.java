package com.avenwu.ereader.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.MenuAdapter;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.NewsMenuItem;
import com.avenwu.ereader.service.NetworkReceiver;
import com.avenwu.ereader.task.TaskManager;
import com.avenwu.ereader.utils.NetworkHelper;
import com.avenwu.ereader.view.GridviewWrapper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import java.util.ArrayList;

import cn.waps.AppConnect;

public class MenuActivity extends SherlockActivity {
    private NetworkReceiver networkReceiver;
    private IntentFilter intentFilter;
    private final byte CNBLOG = 0;
    private final byte CSDN = 1;
    private final byte NET_EASE = 2;
    private final byte BAIDU = 3;
    public GridviewWrapper gvWrapper;
    public MenuAdapter adapter;
    public int tempColumn = 1;
    private View settingBtn;
    private Dialog popupDialog;
    private FeedbackAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        registerNetwork();
        init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("config", 0);
        tempColumn = sp.getInt("menu_column", 1);
        gvWrapper = new GridviewWrapper((GridView) findViewById(R.id.gv_menu));
        settingBtn = findViewById(R.id.btn_setting);
        gvWrapper.addFooterView(View.inflate(this, R.layout.menu_footer, null));
        gvWrapper.setNumColumns(tempColumn);
        adapter = new MenuAdapter(this, getMenuItems());
        gvWrapper.setAdapter(adapter);
        gvWrapper.setOnItemClickListener(getMenuListener());
        // MediaManager.startAd(MenuActivity.this,
        // MediaManager.LEFT_TOP, KUGUO_APP_ID, "m-appchina");
        AppConnect.getInstance(this).setCrashReport(true);
        AppConnect.getInstance(this).initPopAd(this);
        // AppConnect.getInstance(this).showPopAd(this);
        MobclickAgent.setDebugMode(true);
        agent = new FeedbackAgent(MenuActivity.this);
        agent.sync();

    }

    private void registerNetwork() {
        NetworkHelper.updateConnectionState(this);
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, intentFilter);
    }

    public ArrayList<NewsMenuItem> getMenuItems() {
        ArrayList<NewsMenuItem> menuItems = new ArrayList<NewsMenuItem>();
        String[] menuTitles = getResources().getStringArray(R.array.menu_titles);
        String[] menuDescriptions = getResources().getStringArray(
                R.array.menu_descriptions);
        int[] stickerBg = {R.drawable.ic_sticker_blue, R.drawable.ic_sticker_purple, R.drawable.ic_sticker_grenn, R.drawable.ic_sticker_oriange};
        int[] backgroundIds = new int[]{R.drawable.blue_bg, R.drawable.purple_bg,
                R.drawable.green_bg, R.drawable.orange_bg, R.drawable.red_bg};
        for (int i = 0; i < menuTitles.length; i++) {
            NewsMenuItem item = new NewsMenuItem();
            item.layutBackground = backgroundIds[i];
            item.stickerIndex = getResources().getString(R.string.No_x, i + 1);
            item.menuTitle = menuTitles[i];
            item.menuDescription = menuDescriptions[i];
            item.stickerBackground = stickerBg[i];
            menuItems.add(item);
        }
        return menuItems;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        menu.getItem(0).setIcon(
                tempColumn == 1 ? R.drawable.ic_filter_grid_light
                        : R.drawable.ic_filter_list_light);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list_filter:
                tempColumn = gvWrapper.getNumColumns() == 1 ? 2 : 1;
                item.setIcon(tempColumn == 1 ? R.drawable.ic_filter_grid_light
                        : R.drawable.ic_filter_list_light);
                gvWrapper.setNumColumns(tempColumn);
                adapter.setColumnNumber(tempColumn);
                SharedPreferences sp = getSharedPreferences("config", 0);
                sp.edit().putInt("menu_column", tempColumn).commit();
                gvWrapper.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSettingClick(View view) {
//        if (popupDialog == null) {
//            popupDialog = new Dialog(this);
//            popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            View popupView = View.inflate(this, R.layout.popup_setting_layout, null);
//            popupDialog.setContentView(popupView);
//            ExpandableListView lv = (ExpandableListView)popupView.findViewById(R.id.expandableListView);
//            SettingAdapter settingAdapter = new SettingAdapter();
//            lv.setAdapter(settingAdapter);
//        }
//        if (popupDialog.isShowing())
//            popupDialog.dismiss();
//        else
//            popupDialog.show();
        agent.startFeedbackActivity();
    }
    private class SettingAdapter extends BaseExpandableListAdapter{
        private String[] settingGroup = getResources().getStringArray(R.array.setting_group);
        @Override
        public int getGroupCount() {
            return settingGroup.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return settingGroup[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
    public void onBottomTabClicked(View view) {
        //do nothing here

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
