package com.avenwu.ereader.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.PhotoDetailAdapter;
import com.avenwu.ereader.dataprovider.DataCenter;

public class PhotoDetailActivity extends SherlockFragmentActivity {
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
        photoPager.setPageTransformer(true, new DepthPageTransformer());
        photoPager.setCurrentItem(position);
    }

    // http://developer.android.com/training/animation/screen-slide.html
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
