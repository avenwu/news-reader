package com.avenwu.rssreader.activity;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
//            int temp = menubar.getNumColumns() == 1 ? 2 : 1;
//            menubar.setNumColumns(temp);
//            menuAdapter.columnNumber = temp;
//            menubar.invalidate();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
