package com.avenwu.ereader.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.ereader.model.NetEaseChannel;
import com.avenwu.ereader.netease.NeteaseNewsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NeteaseActivity extends FragmentActivity {
    private static final String TAG = "NeteaseActivity";
    private ViewPager contentPager;
    private ArrayList<NetEaseChannel> channels = new ArrayList<NetEaseChannel>();
    private TabTracker tabTracker;
    @Override
    protected void onCreate(Bundle arg0) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(arg0);
        setContentView(R.layout.net_ease_layout);
        tabTracker = new TabTracker(R.id.lr_tab_container);
        String[] links = getResources().getStringArray(R.array.netease_channel_links);
        String[] names = getResources().getStringArray(R.array.netease_channel_names);
        for (int i = 0; i < links.length; i++) {
            NetEaseChannel item = new NetEaseChannel();
            item.channelLink = links[i];
            item.channelName = names[i];
            channels.add(item);
        }
        contentPager = (ViewPager) findViewById(R.id.pager_contents);
        contentPager.setAdapter(new NeteasePagerAdapter(
                getSupportFragmentManager()));
        contentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (tabTracker!=null)
                    tabTracker.updateTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
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

    public void menuClick(View view) {
        int index = tabTracker.updateTab(view);
        contentPager.setCurrentItem(index);
    }

    public class NeteasePagerAdapter extends FragmentStatePagerAdapter {

        public NeteasePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new NeteaseNewsFragment();
            Bundle bundle = new Bundle();
            NetEaseChannel channel = channels.get(position % channels.size());
            bundle.putCharSequence("url", channel.channelLink);
            bundle.putCharSequence("channel", channel.channelName);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return channels.size();
        }

    }

    /**
     * Helper class to change the status of the Tab at bottom
     */
    private class TabTracker{
        private HorizontalScrollView hView;
        private Rect rect = new Rect();
        public ViewGroup tabContainer;
        public TextView previousTab;
        private final int DEFAULT_TAB_INDEX = 0;
        private Map<Integer,Integer> indexMap = new HashMap<Integer, Integer>();// id -- index mapping
        private int FOCUS_STATUS = View.FOCUS_RIGHT;
        private int tabTextPressedColor = getResources().getColor(R.color.light_green_669900);
        private int tabTextDefaultColor = getResources().getColor(android.R.color.white);
        public TabTracker(int containerId){
            this.hView = (HorizontalScrollView)findViewById(R.id.hs_menus);
            this.tabContainer = (ViewGroup)findViewById(containerId);
            this.previousTab = (TextView)this.tabContainer.getChildAt(DEFAULT_TAB_INDEX);
            for (int index = 0; index < tabContainer.getChildCount(); index++){
                indexMap.put(tabContainer.getChildAt(index).getId(),index);
            }
        }

        /**
         * Update the tab background after clicked event
         * @param view tab being clicked
         * @return index of the tab being clicked
         */
        public int updateTab(View view) {
            previousTab.setBackgroundResource(R.drawable.tab_selector);
            previousTab.setTextColor(tabTextDefaultColor);
            view.setBackgroundResource(R.drawable.tab_pressed);
            FOCUS_STATUS = indexMap.get(view.getId()) > indexMap.get(previousTab.getId()) ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
            previousTab = (TextView)view;
            previousTab.setTextColor(tabTextPressedColor);
            return indexMap.get(view.getId());
        }

        /**
         * Update the tab background after scrolled viewpager
         * @param index
         */
        public void updateTab(int index) {
            previousTab.setBackgroundResource(R.drawable.tab_selector);
            previousTab.setTextColor(tabTextDefaultColor);
            FOCUS_STATUS = index > indexMap.get(previousTab.getId()) ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
            previousTab = (TextView)this.tabContainer.getChildAt(index);
            previousTab.setBackgroundResource(R.drawable.tab_pressed);
            previousTab.setTextColor(tabTextPressedColor);
            hView.getHitRect(rect);
            //TODO scroll popup_left_unpressed needs to be checked later
            if (!previousTab.getLocalVisibleRect(rect)) {
                hView.pageScroll(FOCUS_STATUS);
            }
        }
    }

}
