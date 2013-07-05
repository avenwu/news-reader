package com.avenwu.rssreader.dataprovider;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.avenwu.rssreader.model.AuthorInfo;
import com.avenwu.rssreader.model.HomeDetailItem;
import com.avenwu.rssreader.model.PickedDetailItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class XmlOrmDBHelper extends OrmLiteSqliteOpenHelper {
    private final static String DB_NAME = "cnblogrss.db";
    private final static int DB_VERSION = 1;
    private Dao<PickedDetailItem, Integer> pickedEntryDao;
    private Dao<HomeDetailItem, Integer> homeEntryDao;
    private Dao<AuthorInfo, Integer> authorDao;

    public XmlOrmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        createTables(connectionSource);
    }

    private void createTables(ConnectionSource connectionSource) {
        try {
            Log.i(XmlOrmDBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, AuthorInfo.class);
            TableUtils.createTable(connectionSource, PickedDetailItem.class);
            TableUtils.createTable(connectionSource, HomeDetailItem.class);
        } catch (SQLException e) {
            Log.e(XmlOrmDBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<PickedDetailItem, Integer> getPickedEntryItemDao()
            throws SQLException {
        if (pickedEntryDao == null) {
            pickedEntryDao = getDao(PickedDetailItem.class);
        }
        return pickedEntryDao;
    }

    public Dao<HomeDetailItem, Integer> getHomeEntryItemDao()
            throws SQLException {
        if (homeEntryDao == null) {
            homeEntryDao = getDao(HomeDetailItem.class);
        }
        return homeEntryDao;
    }

    public Dao<AuthorInfo, Integer> getAuthorDao() throws SQLException {
        if (authorDao == null) {
            authorDao = getDao(AuthorInfo.class);
        }
        return authorDao;
    }

    @Override
    public synchronized void close() {
        super.close();
        pickedEntryDao = null;
        authorDao = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
            int oldVersion, int newVersion) {
        try {
            Log.i(XmlOrmDBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, AuthorInfo.class, true);
            TableUtils
                    .dropTable(connectionSource, PickedDetailItem.class, true);
            createTables(connectionSource);
        } catch (SQLException e) {
            Log.e(XmlOrmDBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

}
