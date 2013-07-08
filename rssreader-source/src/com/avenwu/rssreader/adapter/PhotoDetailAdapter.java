package com.avenwu.rssreader.adapter;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.avenwu.rssreader.activity.PhotoFragment;
import com.avenwu.rssreader.model.PhotoFeedItem;

public class PhotoDetailAdapter extends FragmentStatePagerAdapter {
    private List<PhotoFeedItem> photos;

    public PhotoDetailAdapter(FragmentManager fm,
            List<PhotoFeedItem> photoFeedItems) {
        super(fm);
        this.photos = photoFeedItems;
    }

    @Override
    public PhotoFragment getItem(int position) {

        return PhotoFragment
                .getPhotoFragment(photos.get(position).largePhotoUrl);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

}
