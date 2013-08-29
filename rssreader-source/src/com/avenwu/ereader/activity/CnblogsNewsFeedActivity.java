package com.avenwu.ereader.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.avenwu.ereader.R;
import com.avenwu.ereader.config.Constant;
import com.avenwu.ereader.config.RssConfig;
import com.avenwu.ereader.model.QueryListener;
import com.avenwu.ereader.view.RefreshView;
import com.avenwu.ereader.view.RefreshView.RefreshListener;

public class CnblogsNewsFeedActivity extends SherlockFragmentActivity implements
        RefreshListener {
    private final String TAG = "MainActivity";
    private String[] catalogStrings;
    private int curent_catalog_index = -1;
    private RefreshView refreshView;
    private ImageView ivHomeBack;
    private TextView tvTitle;
    private String[] titleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        RssConfig.getInstance().init(this);
        titleArray = getResources().getStringArray(R.array.cnblogs_catalog);
        changeCatalog(Constant.CNBLOGS_HOME);

        refreshView.setRefreshListener(this);
        tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupButtonClick(v);
            }
        });
        ivHomeBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        catalogStrings = getResources().getStringArray(R.array.cnblogs_catalog);
        refreshView = (RefreshView) findViewById(R.id.refreshView1);
        ivHomeBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void changeCatalog(int which) {
        if (curent_catalog_index != which) {
            curent_catalog_index = which;
            refreshView.startRefresh();
            tvTitle.setText(titleArray[curent_catalog_index]);
            FragmentTransaction tr = getSupportFragmentManager()
                    .beginTransaction();
            switch (which) {
            case Constant.CNBLOGS_HOME:
                tr.replace(R.id.frame_content,
                        CnblogsHomeFragment.newInstance());
                break;
            case Constant.CNBLOGS_PICKED:
                tr.replace(R.id.frame_content,
                        CnblogsPickedFragment.newInstance());
                break;
            case Constant.CNBLOGS_CANDICATE:

                break;
            case Constant.CNBLOGS_NEWS:

                break;
            default:
                break;
            }
            tr.commit();
        }
    }

    public void onPopupButtonClick(View button) {
        PopupMenu popup = new PopupMenu(this, button);
        popup.getMenuInflater().inflate(R.menu.cnblog_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.menu_cnblog_home:
                    changeCatalog(Constant.CNBLOGS_HOME);
                    break;
                case R.id.menu_cnblog_picked:
                    changeCatalog(Constant.CNBLOGS_PICKED);
                default:
                    break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartRefresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(
                R.id.frame_content);
        if (fragment != null && fragment instanceof QueryListener) {
            ((QueryListener) fragment).startTask();
        }
    }

    @Override
    public void onStopRefresh() {
        refreshView.completeRefresh();
    }
}
