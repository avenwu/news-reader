package com.avenwu.rssreader.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.model.PickedDetailItem;

public class CnblogPickedAdapter extends BaseAdapter {
    private String TAG = "CnblogPickedAdapter";
    private List<PickedDetailItem> dataItems;
    private LayoutInflater inflater;
    private ArticalListener listener;

    public CnblogPickedAdapter(Context context, ArticalListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.dataItems = DataCenter.getInstance().getPickedData();
        this.listener = listener;
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
        Log.d(TAG, "getview, position=" + position);
        PickedDetailItem entryItem = dataItems.get(position);
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

    public static abstract class ArticalListener implements OnClickListener {
        private int position = 0;

        @Override
        public void onClick(View v) {
            onClick(v, position);
        }

        public abstract void onClick(View v, int position);

        public void updatePosition(int position) {
            this.position = position;
        }

    }
}
