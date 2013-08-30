package com.avenwu.ereader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.ereader.activity.MenuActivity;
import com.avenwu.ereader.model.NewsMenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private List<NewsMenuItem> menuDataList;
    private LayoutInflater inflater;
    private int[] backgroundIds;
    private int columnNumber;
    private boolean columnChanged;
    private ScaleAnimation scaleAnimation;

    public void setColumnNumber(int columnNumber) {
        if (columnNumber != this.columnNumber) {
            columnChanged = true;
        }
        this.columnNumber = columnNumber;
    }

    public MenuAdapter(Context context, ArrayList<NewsMenuItem> menuItems) {
        columnNumber = ((MenuActivity) context).tempColumn;
        menuDataList = menuItems;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        backgroundIds = new int[]{R.drawable.blue_bg, R.drawable.purple_bg,
                R.drawable.green_bg, R.drawable.orange_bg, R.drawable.red_bg};
        scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                ScaleAnimation.RELATIVE_TO_SELF, .5f,
                ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
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
        if (convertView == null || columnChanged) {
            holder = new ViewHolder();
            convertView = inflater.inflate(
                    columnNumber == 1 ? R.layout.menu_item_detail_list
                            : R.layout.menu_item_detail_grid, null);
            holder.parentLayout = convertView.findViewById(R.id.ll_menu_item);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_menu_title);
            holder.tvDescription = (TextView) convertView
                    .findViewById(R.id.tv_menu_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.parentLayout.setBackgroundResource(backgroundIds[position]);
        holder.tvTitle.setText(item.getMenuTitle());
        holder.tvDescription.setText(item.getMenuDescription());
        if (position == menuDataList.size() - 1) {
            columnChanged = false;
        }
        convertView.startAnimation(scaleAnimation);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDescription;
        public ImageView ivIcon;
        public View parentLayout;
    }
}
