package com.avenwu.rssreader.netease;

import java.util.List;

import android.content.Context;

import com.avenwu.rssreader.model.NeteaseNewsItem;
import com.avenwu.volleyhelper.DataProvider;

public class NeteaseProvider implements DataProvider<NeteaseNewsItem> {
    private Context context;

    public NeteaseProvider(Context context) {
        this.context = context;
    }

    @Override
    public boolean add(NeteaseNewsItem item) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(List<NeteaseNewsItem> dataList, boolean clearOld) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<NeteaseNewsItem> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(NeteaseNewsItem item2Delete) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean clearAll() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(NeteaseNewsItem item2Update) {
        // TODO Auto-generated method stub
        return false;
    }

}
