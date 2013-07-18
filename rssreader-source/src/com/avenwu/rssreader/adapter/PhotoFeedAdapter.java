package com.avenwu.rssreader.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PhotoFeedAdapter extends BaseAdapter {
    private List<PhotoFeedItem> photoFeeds;
    private LayoutInflater inflater;

    public PhotoFeedAdapter(Context context, List<PhotoFeedItem> dataList) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.photoFeeds = dataList;
    }

    @Override
    public int getCount() {
        return photoFeeds.size();
    }

    @Override
    public PhotoFeedItem getItem(int position) {
        return photoFeeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoHolder holder = null;
        PhotoFeedItem dataItem = photoFeeds.get(position);
        if (convertView == null) {
            holder = new PhotoHolder();
            convertView = inflater.inflate(R.layout.photo_feed_item, null);
            holder.thumbnailView = (ImageView) convertView
                    .findViewById(R.id.iv_photo_thumb);
            holder.descriptionView = (TextView) convertView
                    .findViewById(R.id.tv_description);
            holder.timeStampView = (TextView) convertView
                    .findViewById(R.id.tv_photo_timestamp);
            convertView.setTag(holder);
        } else {
            holder = (PhotoHolder) convertView.getTag();
        }
        holder.thumbnailView.setAnimation(null);
        UrlImageViewHelper.setUrlDrawable(holder.thumbnailView,
                dataItem.thumbPhotoUrl, R.drawable.default_loading,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView,
                            Bitmap loadedBitmap, String url,
                            boolean loadedFromCache) {
                        if (!loadedFromCache) {
                            ScaleAnimation scale = new ScaleAnimation(0, 1, 0,
                                    1, ScaleAnimation.RELATIVE_TO_SELF, .5f,
                                    ScaleAnimation.RELATIVE_TO_SELF, .5f);
                            scale.setDuration(300);
                            scale.setInterpolator(new OvershootInterpolator());
                            imageView.startAnimation(scale);
                        }
                    }
                });
        holder.descriptionView.setText(dataItem.photoDescription);
        holder.timeStampView.setText(dataItem.timeStamp);
        return convertView;
    }

    static class PhotoHolder {
        public ImageView thumbnailView;
        public TextView descriptionView;
        public TextView timeStampView;
    }
}
