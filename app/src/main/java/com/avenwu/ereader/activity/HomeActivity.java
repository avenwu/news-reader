package com.avenwu.ereader.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.MenuAdapter;
import com.avenwu.ereader.model.NewsMenuItem;
import com.avenwu.ereader.utils.NetworkHelper;
import com.avenwu.ereader.widget.GridviewWrapper;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NetworkHelper.getInstance().register(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        private final byte CNBLOG = 0;
        private final byte CSDN = 1;
        private final byte NET_EASE = 2;
        private final byte BAIDU = 3;
        public GridviewWrapper gvWrapper;
        public MenuAdapter adapter;
        public int tempColumn = 1;
        private View settingBtn;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.menu_layout, container, false);
            SharedPreferences sp = getActivity().getSharedPreferences("config", 0);
            tempColumn = sp.getInt("menu_column", 2);
            gvWrapper = new GridviewWrapper((GridView) rootView.findViewById(R.id.gv_menu));
            settingBtn = rootView.findViewById(R.id.btn_setting);
            settingBtn.setOnClickListener(this);
            setVersion((TextView) rootView.findViewById(R.id.tv_version));
            gvWrapper.addFooterView(View.inflate(getActivity(), R.layout.menu_footer, null));
            gvWrapper.setNumColumns(tempColumn);
            adapter = new MenuAdapter(getActivity(), getMenuItems());
            gvWrapper.setAdapter(adapter);
            gvWrapper.setOnItemClickListener(getMenuListener());
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            settingBtn.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.setting_rotate));

        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            settingBtn.clearAnimation();
        }

        public ArrayList<NewsMenuItem> getMenuItems() {
            ArrayList<NewsMenuItem> menuItems = new ArrayList<NewsMenuItem>();
            String[] menuTitles = getResources().getStringArray(R.array.menu_titles);
            String[] menuDescriptions = getResources().getStringArray(
                    R.array.menu_descriptions);
            int[] backgroundIds = new int[]{
                    R.drawable.blue_bg,
                    R.drawable.purple_bg,
                    R.drawable.green_bg,
                    R.drawable.orange_bg,
                    R.drawable.red_bg};
            for (int i = 0; i < menuTitles.length; i++) {
                NewsMenuItem item = new NewsMenuItem();
                item.layutBackground = backgroundIds[i];
                item.menuTitle = menuTitles[i];
                item.menuDescription = menuDescriptions[i];
                menuItems.add(item);
            }
            return menuItems;
        }

        public AdapterView.OnItemClickListener getMenuListener() {
            return new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view,
                                        int position, long id) {
                    Intent intent = new Intent();
                    switch (position) {
                        case CNBLOG:
                            intent.setClass(getActivity(),
                                    CnblogsNewsFeedActivity.class);
                            break;
                        case CSDN:
                            intent.setClass(getActivity(),
                                    CSDNNewsFeedActivity.class);
                            break;
                        case NET_EASE:
                            intent.setClass(getActivity(), NeteaseActivity.class);
                            break;
                        case BAIDU:
                            intent.setClass(getActivity(),
                                    BaiduPhotosActivity.class);
                            break;
                        default:
                            break;
                    }
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.fade_out);
                }
            };
        }

        private void setVersion(TextView view) {

            try {
                PackageInfo info = getActivity().getPackageManager().getPackageInfo
                        (getActivity().getPackageName(), 0);
                view.setText(getResources().getString(R.string.version, info.versionName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            View popupView = View.inflate(getActivity(), R.layout.about_us, null);
            new AlertDialog.Builder(getActivity()).setTitle(R.string
                    .about_us).setView(popupView).show();
            setVersion(((TextView) popupView.findViewById(R.id.tv_version)));
        }
    }

    @Override
    protected void onDestroy() {
        NetworkHelper.getInstance().unregister(this);
        super.onDestroy();
    }

    public void onBottomTabClicked(View view) {

    }
}
