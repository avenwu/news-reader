package com.avenwu.ereader.dataprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.avenwu.ereader.model.AuthorInfo;
import com.avenwu.ereader.model.CsdnNewsItem;
import com.avenwu.ereader.model.HomeDetailItem;
import com.avenwu.ereader.model.NeteaseNewsItem;
import com.avenwu.ereader.model.PhotoFeedItem;
import com.avenwu.ereader.model.PickedDetailItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmDBHelper extends OrmLiteSqliteOpenHelper {
    private final static String DB_NAME = "cnblogrss.db";
    private final static int DB_VERSION = 1;
    private Dao<PickedDetailItem, Integer> pickedEntryDao;
    private Dao<HomeDetailItem, Integer> homeEntryDao;
    private Dao<AuthorInfo, Integer> authorDao;
    private Dao<PhotoFeedItem, Integer> photoDao;
    private Dao<CsdnNewsItem, Integer> geekNewsDao;
    private Dao<NeteaseNewsItem, Integer> neteaseNewsDao;

    public OrmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        createTables(connectionSource);
    }

    private void createTables(ConnectionSource connectionSource) {
        try {
            Log.i(OrmDBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, AuthorInfo.class);
            TableUtils.createTable(connectionSource, PickedDetailItem.class);
            TableUtils.createTable(connectionSource, HomeDetailItem.class);
            TableUtils.createTable(connectionSource, PhotoFeedItem.class);
            TableUtils.createTable(connectionSource, CsdnNewsItem.class);
            TableUtils.createTable(connectionSource, NeteaseNewsItem.class);
        } catch (SQLException e) {
            Log.e(OrmDBHelper.class.getName(), "Can't create database", e);
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

    public Dao<PhotoFeedItem, Integer> getPhotoDao() throws SQLException {
        if (photoDao == null) {
            photoDao = getDao(PhotoFeedItem.class);
        }
        return photoDao;
    }

    public void clearPhotos() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), PhotoFeedItem.class);
    }

    public Dao<CsdnNewsItem, Integer> getGeekNewsDao() throws SQLException {
        if (geekNewsDao == null) {
            geekNewsDao = getDao(CsdnNewsItem.class);
        }
        return geekNewsDao;
    }

    public Dao<NeteaseNewsItem, Integer> getEaseNewsDao() throws SQLException {
        if (neteaseNewsDao == null) {
            neteaseNewsDao = getDao(NeteaseNewsItem.class);
        }
        return neteaseNewsDao;
    }

    public void clearTable(Class cl) throws SQLException {
        TableUtils.clearTable(connectionSource, cl);
    }

    public void clearCache() throws SQLException {
        Log.i(OrmDBHelper.class.getName(), "onUpgrade");
        TableUtils.dropTable(getConnectionSource(), AuthorInfo.class, true);
        TableUtils
                .dropTable(getConnectionSource(), PickedDetailItem.class, true);
        TableUtils.dropTable(getConnectionSource(), HomeDetailItem.class, true);
        TableUtils.dropTable(getConnectionSource(), PhotoFeedItem.class, true);
        TableUtils.dropTable(getConnectionSource(), CsdnNewsItem.class, true);
        TableUtils.dropTable(getConnectionSource(), NeteaseNewsItem.class, true);
        createTables(getConnectionSource());
    }

    @Override
    public synchronized void close() {
        super.close();
        photoDao = null;
        homeEntryDao = null;
        pickedEntryDao = null;
        authorDao = null;
        geekNewsDao = null;
        neteaseNewsDao = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            Log.i(OrmDBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, AuthorInfo.class, true);
            TableUtils
                    .dropTable(connectionSource, PickedDetailItem.class, true);
            TableUtils.dropTable(connectionSource, HomeDetailItem.class, true);
            TableUtils.dropTable(connectionSource, PhotoFeedItem.class, true);
            TableUtils.dropTable(connectionSource, CsdnNewsItem.class, true);
            TableUtils.dropTable(connectionSource, NeteaseNewsItem.class, true);
            createTables(connectionSource);
        } catch (SQLException e) {
            Log.e(OrmDBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

}
