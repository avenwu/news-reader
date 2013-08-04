package com.avenwu.rssreader.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.MenuAdapter;
import com.avenwu.rssreader.model.MenuHelper;

public class BaseMenuActivity extends SherlockActivity {
    public GridView menubar;
    public LinearLayout parentLayout;
    public MenuAdapter menuAdapter;
    public MenuHelper menuHelper;
    public int tempColumn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        SharedPreferences sp = getSharedPreferences("config", 0);
        tempColumn = sp.getInt("menu_column", 1);
        menubar = (GridView) findViewById(R.id.gv_menu);
        parentLayout = (LinearLayout) findViewById(R.id.parent_rl);
        menubar.setNumColumns(tempColumn);
        menuHelper = (MenuHelper) this;
        menuAdapter = new MenuAdapter(this, menuHelper.getMenuItems());
        menubar.setAdapter(menuAdapter);
        menubar.setOnItemClickListener(menuHelper.getMenuListener());

    }

}
