package com.avenwu.rssreader.dataprovider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.avenwu.rssreader.model.AuthorInfo;
import com.avenwu.rssreader.model.CsdnNewsItem;
import com.avenwu.rssreader.model.HomeDetailItem;
import com.avenwu.rssreader.model.PhotoFeedItem;
import com.avenwu.rssreader.model.PickedDetailItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class DaoManager {
    private static OrmDBHelper ormDBHelper;
    private final String TAG = "DaoManager";
    private static DaoManager instance;

    private DaoManager(Context context) {
        initHelper(context);
    }

    public static DaoManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager(context);
                }
            }
        }
        return instance;
    }

    public static OrmDBHelper getDbHelper(Context context) {
        if (ormDBHelper == null) {
            synchronized (DaoManager.class) {
                if (ormDBHelper == null) {
                    ormDBHelper = OpenHelperManager.getHelper(context,
                            OrmDBHelper.class);
                }
            }
        }
        return ormDBHelper;
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
                entryDao.createIfNotExists(entryItem);
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

    public void addPhotoItems(List<PhotoFeedItem> photoFeedItems)
            throws SQLException {
        if (ormDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<PhotoFeedItem, Integer> photoDao = ormDBHelper.getPhotoDao();
            for (PhotoFeedItem photoItem : photoFeedItems) {
                photoDao.createIfNotExists(photoItem);
            }
            Log.d(TAG, "time consumed: "
                    + (System.currentTimeMillis() - timeStart));
        }
    }

    public ArrayList<CsdnNewsItem> getCsdnNewsItems() throws SQLException {
        ArrayList<CsdnNewsItem> items;
        Dao<CsdnNewsItem, Integer> newsDao = ormDBHelper.getGeekNewsDao();
        items = (ArrayList<CsdnNewsItem>) newsDao.queryForAll();
        return items;

    }

    public void addCsdnNewsItemse(ArrayList<CsdnNewsItem> newsItems)
            throws SQLException {
        if (ormDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<CsdnNewsItem, Integer> newsDao = ormDBHelper.getGeekNewsDao();
            for (CsdnNewsItem item : newsItems) {
                newsDao.createIfNotExists(item);
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
