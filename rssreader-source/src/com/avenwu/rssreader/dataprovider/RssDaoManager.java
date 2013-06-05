package com.avenwu.rssreader.dataprovider;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.avenwu.rssreader.model.AuthorInfo;
import com.avenwu.rssreader.model.EntryItem;
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
            xmlOrmDBHelper = OpenHelperManager.getHelper(context, XmlOrmDBHelper.class);
        }
    }

    public void addEntryItems(ArrayList<EntryItem> entryItems) throws SQLException {
        if (xmlOrmDBHelper != null) {
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            Dao<AuthorInfo, Integer> authorDao = xmlOrmDBHelper.getAuthorDao();
            for (EntryItem entryItem : entryItems) {
                authorDao.create(entryItem.getUser());
            }
            Dao<EntryItem, Integer> entryDao = xmlOrmDBHelper.getEntryItemDao();
            for (EntryItem entryItem : entryItems) {
                entryDao.create(entryItem);
            }
            Log.d(TAG, "time consumed: " + (System.currentTimeMillis() - timeStart));
        }
    }

    public ArrayList<EntryItem> getEntryItems() throws SQLException {
        ArrayList<EntryItem> entryItems;
        Dao<AuthorInfo, Integer> authorDao = xmlOrmDBHelper.getAuthorDao();
        Dao<EntryItem, Integer> entryDao = xmlOrmDBHelper.getEntryItemDao();
        entryItems = (ArrayList<EntryItem>) entryDao.queryForAll();
        return entryItems;

    }

    public void release() {
        if (xmlOrmDBHelper != null) {
            OpenHelperManager.releaseHelper();
            xmlOrmDBHelper = null;
        }
    }
}
