package com.avenwu.rssreader.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.NewsMenuItem;

public class MenuAdapter extends BaseAdapter {
    private List<NewsMenuItem> menuDataList;
    private LayoutInflater inflater;
    private int[] backgroundIds;
    private int[] siteIcons;
    public int columnNumber = 1;

    public MenuAdapter(Context context, ArrayList<NewsMenuItem> menuItems) {
        menuDataList = menuItems;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        backgroundIds = new int[] { R.drawable.blue_bg, R.drawable.purple_bg,
                R.drawable.green_bg, R.drawable.orange_bg, R.drawable.red_bg };
        siteIcons = new int[] { R.drawable.icon_cnblog, R.drawable.icon_csdn,
                R.drawable.ic_home, R.drawable.icon_baidu };
    }

    @Override
    public int getCount() {
        return menuDataList.size();
    }

    @Override
    public NewsMenuItem getItem(int position) {
        return menuDataList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        NewsMenuItem item = menuDataList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(
                    columnNumber == 1 ? R.layout.menu_item_detail_list
                            : R.layout.menu_item_detail_grid, null);
            holder.parentLayout = convertView.findViewById(R.id.ll_menu_item);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_menu_title);
            holder.tvDescription = (TextView) convertView
                    .findViewById(R.id.tv_menu_description);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.parentLayout.setBackgroundResource(backgroundIds[position]);
        holder.tvTitle.setText(item.getMenuTitle());
        holder.tvDescription.setText(item.getMenuDescription());
        holder.ivIcon.setImageResource(siteIcons[position]);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDescription;
        public ImageView ivIcon;
        public View parentLayout;
    }
}
