package com.avenwu.rssreader.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.CsdnNewsItem;

public class CsdnNewsAdapter extends BaseAdapter {
    private List<CsdnNewsItem> dataList;
    private LayoutInflater inflater;
    private final int[] colorArray;

    public CsdnNewsAdapter(Context context, List<CsdnNewsItem> datalist) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = datalist;

        colorArray = new int[] {
                context.getResources().getColor(R.color.blue_2cb1e1),
                context.getResources().getColor(R.color.blue_50c0e9) };
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public CsdnNewsItem getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsHolder viewHolder = null;
        CsdnNewsItem dataItem = dataList.get(position);
        if (convertView == null) {
            viewHolder = new NewsHolder();
            convertView = inflater.inflate(R.layout.csdn_new_item, null);
            viewHolder.titleView = (TextView) convertView
                    .findViewById(R.id.tv_csdn_news_title);
            viewHolder.timestampView = (TextView) convertView
                    .findViewById(R.id.tv_csdn_news_timestamp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsHolder) convertView.getTag();
        }
        convertView
                .setBackgroundColor(colorArray[position % colorArray.length]);
        viewHolder.titleView.setText(dataItem.title);
        viewHolder.timestampView.setText(dataItem.pubDate);
        return convertView;
    }

    static class NewsHolder {
        public TextView titleView;
        public TextView contentView;
        public TextView timestampView;
    }
}
