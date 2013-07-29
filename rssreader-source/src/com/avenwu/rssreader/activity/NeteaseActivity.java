package com.avenwu.rssreader.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.netease.NeteaseNewsFragment;
import com.avenwu.volleyhelper.ApiManager;

public class NeteaseActivity extends SherlockFragmentActivity {
    private static final String TAG = "NeteaseActivity";
    private ViewPager contentPager;
    private String[] channels;

    @Override
    protected void onCreate(Bundle arg0) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(arg0);
        setContentView(R.layout.net_ease_layout);
        channels = getResources().getStringArray(R.array.netease_channels);
        ApiManager.init(this);
        contentPager = (ViewPager) findViewById(R.id.pager_contents);
        contentPager.setAdapter(new NeteasePagerAdapter(
                getSupportFragmentManager()));

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
        Toast.makeText(this, "menu clicked," + ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
    }

    public class NeteasePagerAdapter extends FragmentStatePagerAdapter {

        public NeteasePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new NeteaseNewsFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("url", channels[position % channels.length]);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return channels.length;
        }

    }
}
