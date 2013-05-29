package com.avenwu.rssreader.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.activity.BlogArticalActivity;
import com.avenwu.rssreader.con.RssConfig;
import com.avenwu.rssreader.data.DataCenter;
import com.avenwu.rssreader.model.EntryItem;

public class CnblogPickedAdapter extends BaseAdapter {
    private List<EntryItem> dataItems;
    private LayoutInflater inflater;
    private ArticalListener listener = new ArticalListener() {
        @Override
        public void onClick(View v, int position) {
            Intent intent = new Intent(v.getContext(), BlogArticalActivity.class);
            intent.putExtra("content_id", position);
            intent.putExtra("content_type", RssConfig.getInstance().getPickedUrl());
            v.getContext().startActivity(intent);
        }
    };

    public CnblogPickedAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.dataItems = DataCenter.getInstance().getPickedData();
    }

    @Override
    public int getCount() {
        return dataItems.size();
    }

    @Override
    public Object getItem(int position) {
        return dataItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EntryItem entryItem = dataItems.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.detail_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvSummary = (TextView) convertView.findViewById(R.id.tv_summary);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_author_name);
            viewHolder.tvPublished = (TextView) convertView.findViewById(R.id.tv_published);
            viewHolder.tvSummary.setOnClickListener(listener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(entryItem.getTitle());
        viewHolder.tvSummary.setText(entryItem.getSummary());
        listener.setPosition(position);
        viewHolder.tvUserName.setText(entryItem.getUser().getName());
        viewHolder.tvPublished.setText(entryItem.getPublised_time());
        return convertView;
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvSummary;
        public TextView tvUserName;
        public TextView tvPublished;
    }

    private static abstract class ArticalListener implements OnClickListener {
        private int position = 0;

        @Override
        public void onClick(View v) {
            onClick(v, position);
        }

        public abstract void onClick(View v, int position);

        public void setPosition(int position) {
            this.position = position;
        }

    }
}
