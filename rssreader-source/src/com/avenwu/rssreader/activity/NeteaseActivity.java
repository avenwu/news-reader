package com.avenwu.rssreader.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.avenwu.ereader.R;

public class NeteaseActivity extends SherlockFragmentActivity {
    private ViewPager contentPager;

    @Override
    protected void onCreate(Bundle arg0) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(arg0);
        setContentView(R.layout.net_ease_layout);
        contentPager = (ViewPager) findViewById(R.id.pager_contents);
        AppConnect.getInstance(this).showPopAd(this);
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
}
