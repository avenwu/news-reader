package com.avenwu.ereader.netease;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.avenwu.ereader.dataprovider.DaoManager;
import com.avenwu.ereader.model.NeteaseNewsItem;
import com.avenwu.ereader.xmlparse.Element;
import com.android.volley.volleyhelper.DataProvider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

public class NeteaseProvider implements DataProvider<NeteaseNewsItem> {
    private Context context;
    private static final String TAG = "NeteaseProvider";
    private Dao<NeteaseNewsItem, Integer> dao;
    private String channel;

    public NeteaseProvider(Context context, String channel) {
        this.context = context;
        this.channel = channel;
    }

    @Override
    public boolean add(NeteaseNewsItem item) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(List<NeteaseNewsItem> dataList, boolean clearOld) {
        if (clearOld) {
            clearAll();
        }
        try {
            dao = DaoManager.getDbHelper(context).getEaseNewsDao();
            Log.d(TAG, "start insert");
            long timeStart = System.currentTimeMillis();
            for (NeteaseNewsItem item : dataList) {
                dao.create(item);
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
    public List<NeteaseNewsItem> getAll() {
        List<NeteaseNewsItem> datalist = new ArrayList<NeteaseNewsItem>();
        try {
            dao = DaoManager.getDbHelper(context).getEaseNewsDao();
            datalist.addAll(dao.queryForEq(Element.channel, channel));
        } catch (SQLException e) {
            Log.d(TAG, "failed to get cached netease news items");
            e.printStackTrace();
        }
        return datalist;
    }

    @Override
    public boolean delete(NeteaseNewsItem item2Delete) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean clearAll() {
        try {
//            DaoManager.getDbHelper(context).clearTable(NeteaseNewsItem.class);
            dao = DaoManager.getDbHelper(context).getEaseNewsDao();
            DeleteBuilder<NeteaseNewsItem, Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(Element.channel, channel);
            deleteBuilder.prepare();
            dao.deleteBuilder();
            Log.d(TAG, "clear netease table success");
            return true;
        } catch (SQLException e) {
            Log.d(TAG, "failed to clear netease table");
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public boolean update(NeteaseNewsItem item2Update) {
        // TODO Auto-generated method stub
        return false;
    }

}
