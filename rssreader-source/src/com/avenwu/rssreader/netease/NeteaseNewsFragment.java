package com.avenwu.rssreader.netease;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.avenwu.ereader.R;
import com.avenwu.rssreader.model.NeteaseNewsItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseTask;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NeteaseNewsFragment extends SherlockFragment {
    private String TAG = "NeteaseNewsFragment";
    private PullToRefreshListView listView;
    private NeteaseNewsAdapter adapter;
    private ArrayList<NeteaseNewsItem> dataList = new ArrayList<NeteaseNewsItem>();
    private BaseTask task;
    private NeteaseRequest request;
    private BaseListener<ArrayList<NeteaseNewsItem>> listener;
    private String url;
    private NeteaseProvider provider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NeteaseNewsAdapter();
        url = getArguments().getString("url");
        provider = new NeteaseProvider(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netease_feed_layout, null);
        listView = (PullToRefreshListView) view
                .findViewById(R.id.lv_netease_news);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = new BaseListener<ArrayList<NeteaseNewsItem>>() {

            @Override
            public void onSuccess(ArrayList<NeteaseNewsItem> result) {
                Log.d(TAG, result.toString());
                dataList.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(), R.string.failed_to_get_content,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Object result) {

                if (result instanceof Integer) {
                    Toast.makeText(getActivity(), (Integer) result,
                            Toast.LENGTH_SHORT).show();
                } else if (result instanceof String) {
                    Toast.makeText(getActivity(), (String) result,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),
                            R.string.failed_to_get_content, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        };
        request = new NeteaseRequest(provider, listener, true);
        task = new BaseTask(url, request);
        task.start();
    }

    private class NeteaseNewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public NeteaseNewsItem getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            NeteaseNewsItem item = dataList.get(position);
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(getActivity(),
                        R.layout.netease_item_layout, null);
                holder.titleView = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.timeView = (TextView) convertView
                        .findViewById(R.id.tv_ts);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.titleView.setText(item.title);
            holder.timeView.setText(item.pubDate);
            return convertView;
        }

        class Holder {
            TextView titleView;
            TextView timeView;
        }
    }
}
