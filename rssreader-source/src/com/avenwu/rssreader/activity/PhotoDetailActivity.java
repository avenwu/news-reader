package com.avenwu.rssreader.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.PhotoDetailAdapter;
import com.avenwu.rssreader.dataprovider.DataCenter;

public class PhotoDetailActivity extends FragmentActivity {
    private ViewPager photoPager;
    private PhotoDetailAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail_layout);
        int position = getIntent().getIntExtra("position", 0);
        photoPager = (ViewPager) findViewById(R.id.vp_photos);
        photoAdapter = new PhotoDetailAdapter(getSupportFragmentManager(),
                DataCenter.getInstance().getPhotoFeedsItems());
        photoPager.setAdapter(photoAdapter);
        photoPager.setCurrentItem(position);
    }
}
