package com.avenwu.rssreader.activity;

import uk.co.senab.photoview.PhotoView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.avenwu.ereader.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PhotoFragment extends SherlockFragment {
    private PhotoView photoView;
    private String photoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoUrl = getArguments().getString("photo_url");
    }

    public static PhotoFragment getPhotoFragment(String url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence("photo_url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_photo, null);
        photoView = (PhotoView) view.findViewById(R.id.iv_single_photo);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UrlImageViewHelper.setUrlDrawable(photoView, photoUrl,
                R.drawable.default_loading, new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView,
                            Bitmap loadedBitmap, String url,
                            boolean loadedFromCache) {
                        float s = loadedBitmap.getHeight()
                                / loadedBitmap.getWidth();
                        if (s >= 2 || s <= 0.5) {
                            ((PhotoView) imageView).setZoomable(false);
                        }
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
    }
}
