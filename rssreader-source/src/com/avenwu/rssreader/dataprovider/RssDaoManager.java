package com.avenwu.rssreader.dataprovider;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.avenwu.rssreader.model.AuthorInfo;
import com.avenwu.rssreader.model.HomeDetailItem;
import com.avenwu.rssreader.model.PickedDetailItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class RssDaoManager {
    private XmlOrmDBHelper xmlOrmDBHelper;
    private final String TAG = "DaoManager";

    public RssDaoManager(Context context) {
        initHelper(context);
    }

    private void initHelper(Context context) {
        if (xmlOrmDBHelper == null) {
            xmlOrmDBHelper = OpenHelperManager.getHelper(context,
                    XmlOrmDBHelper.class);
        }
    }

    public void addPickedEntryItems(ArrayList<PickedDetailItem> entryItems)
            throws SQLException {
        if (xmlOrmDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<AuthorInfo, Integer> authorDao = xmlOrmDBHelper.getAuthorDao();
            for (PickedDetailItem entryItem : entryItems) {
                authorDao.create(entryItem.getUser());
            }
            Dao<PickedDetailItem, Integer> entryDao = xmlOrmDBHelper
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
        Dao<AuthorInfo, Integer> authorDao = xmlOrmDBHelper.getAuthorDao();
        Dao<PickedDetailItem, Integer> entryDao = xmlOrmDBHelper
                .getPickedEntryItemDao();
        entryItems = (ArrayList<PickedDetailItem>) entryDao.queryForAll();
        return entryItems;

    }

    public void addHomeEntryItems(ArrayList<HomeDetailItem> entryItems)
            throws SQLException {
        if (xmlOrmDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<AuthorInfo, Integer> authorDao = xmlOrmDBHelper.getAuthorDao();
            for (HomeDetailItem entryItem : entryItems) {
                authorDao.create(entryItem.getUser());
            }
            Dao<HomeDetailItem, Integer> entryDao = xmlOrmDBHelper
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
        Dao<AuthorInfo, Integer> authorDao = xmlOrmDBHelper.getAuthorDao();
        Dao<HomeDetailItem, Integer> entryDao = xmlOrmDBHelper
                .getHomeEntryItemDao();
        entryItems = (ArrayList<HomeDetailItem>) entryDao.queryForAll();
        return entryItems;

    }

    public void release() {
        if (xmlOrmDBHelper != null) {
            OpenHelperManager.releaseHelper();
            xmlOrmDBHelper = null;
        }
    }
}
