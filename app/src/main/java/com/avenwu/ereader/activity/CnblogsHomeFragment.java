package com.avenwu.ereader.activity;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.CnblogHomeAdapter;
import com.avenwu.ereader.adapter.CnblogHomeAdapter.ArticalListener;
import com.avenwu.ereader.config.RssConfig;
import com.avenwu.ereader.dataprovider.DaoManager;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.HomeDetailItem;
import com.avenwu.ereader.model.QueryListener;
import com.avenwu.ereader.model.RefreshListener;
import com.avenwu.ereader.task.BaseListener;
import com.avenwu.ereader.task.BaseRequest;
import com.avenwu.ereader.task.BaseTask;
import com.avenwu.ereader.task.RssCnblogHomeRequest;

import java.sql.SQLException;
import java.util.ArrayList;

public class CnblogsHomeFragment extends Fragment implements
        QueryListener {
    private FlipViewController flipview;
    private CnblogHomeAdapter homeAdapter;
    private BaseTask task;
    @SuppressWarnings("rawtypes")
    private BaseRequest request;
    private ArticalListener listener;
    private DaoManager daoManager;
    private RefreshListener refreshListener;

    public static CnblogsHomeFragment newInstance() {
        CnblogsHomeFragment fragment = new CnblogsHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            daoManager = DaoManager.getInstance(getActivity());
            DataCenter.getInstance().replaceHomeItems(
                    daoManager.getHomeEntryItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshListener = (RefreshListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.newsfeed_layout, null);
        flipview = (FlipViewController) view.findViewById(R.id.flipview_rss);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flipview.setAnimationBitmapFormat(Config.RGB_565);
        listener = new ArticalListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), WebNewsActivity.class);
                intent.putExtra("url", DataCenter.getInstance().getHomeData()
                        .get(position).getId());
                v.getContext().startActivity(intent);
            }
        };
        homeAdapter = new CnblogHomeAdapter(getActivity(), listener);
        flipview.setAdapter(homeAdapter);
        flipview.setOnViewFlipListener(new ViewFlipListener() {
            @Override
            public void onViewFlipped(View view, int position) {
                listener.updatePosition(position);
                if (position == 0) {
                    refreshListener.onStartRefresh();
                    startTask();
                }
            }
        });
        if (homeAdapter.getCount() != 0) {
            homeAdapter.notifyDataSetChanged();
            refreshListener.onStopRefresh();
        } else {
            startTask();
        }
    }

    public void startTask() {
        if (request == null) {
            request = new RssCnblogHomeRequest<Void>(
                    new BaseListener<ArrayList<HomeDetailItem>>() {
                        @Override
                        public void onSuccess(ArrayList<HomeDetailItem> result) {
                            DataCenter.getInstance().replaceHomeItems(result);
                            if (!CnblogsHomeFragment.this.isRemoving()) {
                                Toast.makeText(getActivity(), "success",
                                        Toast.LENGTH_SHORT).show();
                                homeAdapter.notifyDataSetChanged();
                                try {
                                    daoManager.addHomeEntryItems(result);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),
                                            "failed to insert tinto table",
                                            Toast.LENGTH_SHORT).show();
                                }
                                flipview.setSelection(0);
                            }
                        }

                        @Override
                        public void onFailed(Object result) {
                            if (getActivity() == null || CnblogsHomeFragment.this.isRemoving()) {
                                return;
                            }
                            if (result instanceof Integer) {
                                Toast.makeText(getActivity(), (Integer) result,
                                        Toast.LENGTH_SHORT).show();
                            } else if (result instanceof String) {
                                Toast.makeText(getActivity(), (String) result,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),
                                        R.string.failed_to_get_content,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if (CnblogsHomeFragment.this.isRemoving()) {
                                return;
                            }
                            Toast.makeText(getActivity(),
                                    R.string.failed_to_get_content,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFinished() {
                            if (CnblogsHomeFragment.this.isRemoving()) {
                                return;
                            }
                            refreshListener.onStopRefresh();
                        }
                    });
        }
        task = new BaseTask(RssConfig.getInstance().getHomeUrl(), request);
        task.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        flipview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        flipview.onPause();
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.cancel();
        }
        DataCenter.getInstance().getHomeData().clear();
        super.onDestroy();
    }
}
