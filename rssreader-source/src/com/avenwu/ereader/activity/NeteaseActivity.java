package com.avenwu.ereader.activity;

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
import com.avenwu.ereader.model.NetEaseChannel;
import com.avenwu.ereader.netease.NeteaseNewsFragment;

import java.util.ArrayList;

public class NeteaseActivity extends SherlockFragmentActivity {
    private static final String TAG = "NeteaseActivity";
    private ViewPager contentPager;
    private ArrayList<NetEaseChannel> channels = new ArrayList<NetEaseChannel>();

    @Override
    protected void onCreate(Bundle arg0) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(arg0);
        setContentView(R.layout.net_ease_layout);
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
}
