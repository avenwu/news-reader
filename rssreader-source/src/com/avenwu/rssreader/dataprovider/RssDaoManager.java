package com.avenwu.rssreader.dataprovider;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.avenwu.rssreader.model.AuthorInfo;
import com.avenwu.rssreader.model.HomeDetailItem;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.rssreader.model.PickedDetailItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class RssDaoManager {
    private OrmDBHelper ormDBHelper;
    private final String TAG = "DaoManager";
    private static RssDaoManager instance;

    private RssDaoManager(Context context) {
        initHelper(context);
    }

    public static RssDaoManager getInstance(Context context) {
        if (instance == null) {
            synchronized (RssDaoManager.class) {
                if (instance == null) {
                    instance = new RssDaoManager(context);
                }
            }
        }
        return instance;
    }

    private void initHelper(Context context) {
        if (ormDBHelper == null) {
            ormDBHelper = OpenHelperManager.getHelper(context,
                    OrmDBHelper.class);
        }
    }

    public void addPickedEntryItems(ArrayList<PickedDetailItem> entryItems)
            throws SQLException {
        if (ormDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<AuthorInfo, Integer> authorDao = ormDBHelper.getAuthorDao();
            for (PickedDetailItem entryItem : entryItems) {
                authorDao.create(entryItem.getUser());
            }
            Dao<PickedDetailItem, Integer> entryDao = ormDBHelper
                    .getPickedEntryItemDao();
            for (PickedDetailItem entryItem : entryItems) {
                entryDao.create(entryItem);
            }
            Log.d(TAG, "time consumed: "
                    + (System.currentTimeMillis() - timeStart));
        }
    }

    public ArrayList<PickedDetailItem> getPickedEntryItems()
            throws SQLException {
        ArrayList<PickedDetailItem> entryItems;
        Dao<PickedDetailItem, Integer> entryDao = ormDBHelper
                .getPickedEntryItemDao();
        entryItems = (ArrayList<PickedDetailItem>) entryDao.queryForAll();
        return entryItems;

    }

    public void addHomeEntryItems(ArrayList<HomeDetailItem> entryItems)
            throws SQLException {
        if (ormDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<AuthorInfo, Integer> authorDao = ormDBHelper.getAuthorDao();
            for (HomeDetailItem entryItem : entryItems) {
                authorDao.create(entryItem.getUser());
            }
            Dao<HomeDetailItem, Integer> entryDao = ormDBHelper
                    .getHomeEntryItemDao();
            for (HomeDetailItem entryItem : entryItems) {
                entryDao.create(entryItem);
            }
            Log.d(TAG, "time consumed: "
                    + (System.currentTimeMillis() - timeStart));
        }
    }

    public ArrayList<HomeDetailItem> getHomeEntryItems() throws SQLException {
        ArrayList<HomeDetailItem> entryItems;
        Dao<HomeDetailItem, Integer> entryDao = ormDBHelper
                .getHomeEntryItemDao();
        entryItems = (ArrayList<HomeDetailItem>) entryDao.queryForAll();
        return entryItems;

    }

    public ArrayList<PhotoFeedItem> getPhotoFeedItems() throws SQLException {
        ArrayList<PhotoFeedItem> items;
        Dao<PhotoFeedItem, Integer> photoDao = ormDBHelper.getPhotoDao();
        items = (ArrayList<PhotoFeedItem>) photoDao.queryForAll();
        return items;
    }

    public void addPhotoItems(ArrayList<PhotoFeedItem> photoFeedItems)
            throws SQLException {
        if (ormDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<PhotoFeedItem, Integer> photoDao = ormDBHelper.getPhotoDao();
            for (PhotoFeedItem photoItem : photoFeedItems) {
                photoDao.create(photoItem);
            }
            Log.d(TAG, "time consumed: "
                    + (System.currentTimeMillis() - timeStart));
        }
    }

    public void release() {
        if (ormDBHelper != null) {
            OpenHelperManager.releaseHelper();
            ormDBHelper = null;
        }
    }
}
