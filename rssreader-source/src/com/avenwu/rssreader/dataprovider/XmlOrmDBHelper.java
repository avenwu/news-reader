package com.avenwu.rssreader.dataprovider;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.avenwu.rssreader.model.AuthorInfo;
import com.avenwu.rssreader.model.EntryItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class XmlOrmDBHelper extends OrmLiteSqliteOpenHelper {
    private final static String DB_NAME = "cnblogrss.db";
    private final static int DB_VERSION = 1;
    private Dao<EntryItem, Integer> entryDao;
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
            TableUtils.createTable(connectionSource, EntryItem.class);
        } catch (SQLException e) {
            Log.e(XmlOrmDBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<EntryItem, Integer> getEntryItemDao() throws SQLException {
        if (entryDao == null) {
            entryDao = getDao(EntryItem.class);
        }
        return entryDao;
    }

    public Dao<AuthorInfo, Integer> getAuthorDao() throws SQLException {
        if (authorDao == null) {
            authorDao = getDao(EntryItem.class);
        }
        return authorDao;
    }

    @Override
    public synchronized void close() {
        super.close();
        entryDao = null;
        authorDao = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(XmlOrmDBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, AuthorInfo.class, true);
            TableUtils.dropTable(connectionSource, EntryItem.class, true);
            createTables(connectionSource);
        } catch (SQLException e) {
            Log.e(XmlOrmDBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

}
