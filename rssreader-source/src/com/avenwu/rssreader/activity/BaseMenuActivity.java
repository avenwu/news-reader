package com.avenwu.rssreader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.MenuAdapter;
import com.avenwu.rssreader.model.MenuHelper;

public class BaseMenuActivity extends Activity {
    private GridView menubar;
    private MenuAdapter menuAdapter;
    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        menubar = (GridView) findViewById(R.id.gv_menu);
        menuHelper = (MenuHelper) this;
        menuAdapter = new MenuAdapter(this, menuHelper.getMenuItems());
        menubar.setAdapter(menuAdapter);
        menubar.setOnItemClickListener(menuHelper.getMenuListener());
    }
}
