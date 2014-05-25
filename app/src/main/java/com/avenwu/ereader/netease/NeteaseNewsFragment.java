package com.avenwu.ereader.netease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.volleyhelper.QueryDbTask;
import com.avenwu.ereader.R;
import com.avenwu.ereader.activity.WebNewsActivity;
import com.avenwu.ereader.model.NeteaseNewsItem;
import com.avenwu.ereader.task.BaseListener;
import com.avenwu.ereader.task.BaseTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


public class NeteaseNewsFragment extends Fragment {
    private String TAG = "NeteaseNewsFragment";
    private PullToRefreshListView listView;
    private NeteaseNewsAdapter adapter;
    private ArrayList<NeteaseNewsItem> dataList = new ArrayList<NeteaseNewsItem>();
    private BaseTask task;
    private NeteaseRequest request;
    private BaseListener<ArrayList<NeteaseNewsItem>> listener;
    private String url;
    private NeteaseProvider provider;
    private String channel;
    private QueryDbTask<NeteaseNewsItem> cacheTask;
    private LinearLayout footerView;
    private View emptyView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NeteaseNewsAdapter();
        url = getArguments().getString("url");
        channel = getArguments().getString("channel");
        provider = new NeteaseProvider(getActivity(), channel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netease_feed_layout, null);
        listView = (PullToRefreshListView) view.findViewById(R.id.lv_netease_news);
        listView.setAdapter(adapter);
        footerView = (LinearLayout) View.inflate(getActivity(), R.layout.ll_ad_layout, null);
        listView.getRefreshableView().addFooterView(footerView);
        emptyView = View.inflate(getActivity(), R.layout.loading_empty, null);
        listView.setEmptyView(emptyView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cacheTask = new QueryDbTask<NeteaseNewsItem>(provider, new Response.Listener<ArrayList<NeteaseNewsItem>>() {
            @Override
            public void onResponse(ArrayList<NeteaseNewsItem> neteaseNewsItems) {
                if (neteaseNewsItems == null || neteaseNewsItems.isEmpty()) {
                    task.start();
                } else {
                    Log.d(TAG, "cache received");
                    dataList.addAll(neteaseNewsItems);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        cacheTask.excute();
        listener = new BaseListener<ArrayList<NeteaseNewsItem>>() {
            @Override
            public void onSuccess(ArrayList<NeteaseNewsItem> result) {
                if (isRemoving())
                    return;
                Log.d(TAG, result.toString());
                if (result.size() > 0)
                    dataList.clear();
                dataList.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                if (isRemoving())
                    return;
                Toast.makeText(getActivity(), R.string.failed_to_get_content,
                        Toast.LENGTH_SHORT).show();
                if (emptyView != null) {
                    emptyView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    emptyView.findViewById(R.id.tv_error_message).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(Object result) {
                if (isRemoving())
                    return;
                if (emptyView != null) {
                    emptyView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    emptyView.findViewById(R.id.tv_error_message).setVisibility(View.VISIBLE);
                }
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
                if (isRemoving())
                    return;
                super.onFinished();
                if (listView != null)
                    listView.onRefreshComplete();
            }
        };
        request = new NeteaseRequest(getActivity(), provider, listener, true);
        task = new BaseTask(url, request);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebNewsActivity.class);
                intent.putExtra("url", dataList.get((int) l).link);
                startActivity(intent);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (task != null) {
                    task.cancel();
                    task.start();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
    }

    @Override
    public void onDestroy() {
        if (task != null)
            task.cancel();
        task = null;
        super.onDestroy();
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
