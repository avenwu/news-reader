package com.avenwu.ereader.bdphotos;

import android.content.Context;
import android.util.Log;

import com.android.volley.volleyhelper.DataProvider;
import com.avenwu.ereader.dataprovider.DaoManager;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BDProvider implements DataProvider<PhotoFeedItem> {
    private Context context;
    private String TAG = "BDProvider";

    public BDProvider(Context context) {
        this.context = context;
    }

    @Override
    public boolean add(PhotoFeedItem item) {
        return false;
    }

    @Override
    public boolean addAll(List<PhotoFeedItem> dataList, boolean clearOld) {
        Dao<PhotoFeedItem, Integer> photoDao;
        try {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            photoDao = DaoManager.getDbHelper(context).getPhotoDao();
            if (clearOld)
                clearAll();
            for (PhotoFeedItem photoItem : dataList) {
                photoDao.createIfNotExists(photoItem);
            }
            Log.d(TAG, "time consumed: "
                    + (System.currentTimeMillis() - timeStart));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<PhotoFeedItem> getAll() {
        ArrayList<PhotoFeedItem> items;
        try {
            Dao<PhotoFeedItem, Integer> photoDao = DaoManager.getDbHelper(
                    context).getPhotoDao();
            items = (ArrayList<PhotoFeedItem>) photoDao.queryForAll();
        } catch (SQLException e) {
            items = new ArrayList<PhotoFeedItem>();
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean delete(PhotoFeedItem item2Delete) {
        return false;
    }

    @Override
    public boolean clearAll() {
        try {
            DaoManager.getDbHelper(context).clearPhotos();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(PhotoFeedItem item2Update) {
        return false;
    }

}
