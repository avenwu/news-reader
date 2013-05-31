package com.avenwu.rssreader.dataprovider;

import java.util.ArrayList;

import android.content.Context;

import com.avenwu.rssreader.model.EntryItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DaoManager {
    private XmlOrmDBHelper xmlOrmDBHelper;
    
    private DaoManager(Context context) {
        initHelper(context);
    }

    private void initHelper(Context context) {
        if (xmlOrmDBHelper == null) {
            xmlOrmDBHelper = OpenHelperManager.getHelper(context, XmlOrmDBHelper.class);
        }
    }
    public void addEntryItems(ArrayList<EntryItem> entryItems) {
        
    }
    public void release() {
        if (xmlOrmDBHelper != null) {
            OpenHelperManager.releaseHelper();
            xmlOrmDBHelper = null;
        }
    }
}
