package com.avenwu.rssreader.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.adapter.MenuAdapter;
import com.avenwu.rssreader.model.MenuHelper;

public class BaseMenuActivity extends SherlockActivity {
    private GridView menubar;
    private MenuAdapter menuAdapter;
    private MenuHelper menuHelper;
    public int tempColumn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        SharedPreferences sp = getSharedPreferences("config", 0);
        tempColumn = sp.getInt("menu_column", 1);
        menubar = (GridView) findViewById(R.id.gv_menu);
        menubar.setNumColumns(tempColumn);
        menuHelper = (MenuHelper) this;
        menuAdapter = new MenuAdapter(this, menuHelper.getMenuItems());
        menubar.setAdapter(menuAdapter);
        menubar.setOnItemClickListener(menuHelper.getMenuListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        menu.getItem(0).setIcon(
                tempColumn == 1 ? R.drawable.ic_filter_grid_light
                        : R.drawable.ic_filter_list_light);
        return true;//39434c
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_list_filter:
            tempColumn = menubar.getNumColumns() == 1 ? 2 : 1;
            item.setIcon(tempColumn == 1 ? R.drawable.ic_filter_grid_light
                    : R.drawable.ic_filter_list_light);
            menubar.setNumColumns(tempColumn);
            menuAdapter.setColumnNumber(tempColumn);
            SharedPreferences sp = getSharedPreferences("config", 0);
            sp.edit().putInt("menu_column", tempColumn).commit();
            menubar.invalidate();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
