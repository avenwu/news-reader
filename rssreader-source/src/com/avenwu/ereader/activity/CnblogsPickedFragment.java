package com.avenwu.ereader.activity;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.avenwu.ereader.R;
import com.avenwu.ereader.adapter.CnblogPickedAdapter;
import com.avenwu.ereader.adapter.CnblogPickedAdapter.ArticalListener;
import com.avenwu.ereader.config.RssConfig;
import com.avenwu.ereader.dataprovider.DaoManager;
import com.avenwu.ereader.dataprovider.DataCenter;
import com.avenwu.ereader.model.PickedDetailItem;
import com.avenwu.ereader.model.QueryListener;
import com.avenwu.ereader.task.BaseListener;
import com.avenwu.ereader.task.BaseRequest;
import com.avenwu.ereader.task.BaseTask;
import com.avenwu.ereader.task.RssCnblogPickedRequest;
import com.avenwu.ereader.view.RefreshView.RefreshListener;

import java.sql.SQLException;
import java.util.ArrayList;

public class CnblogsPickedFragment extends SherlockFragment implements
        QueryListener {
    private FlipViewController flipview;
    private CnblogPickedAdapter pickedAdapter;
    private BaseTask task;
    @SuppressWarnings("rawtypes")
    private BaseRequest request;
    private CnblogPickedAdapter.ArticalListener listener;
    private DaoManager daoManager;
    private RefreshListener refreshListener;

    public static CnblogsPickedFragment newInstance() {
        CnblogsPickedFragment fragment = new CnblogsPickedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            daoManager = DaoManager.getInstance(getActivity());
            DataCenter.getInstance().replacePickedItems(
                    daoManager.getPickedEntryItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshListener = (CnblogsNewsFeedActivity) getActivity();
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
                intent.putExtra("url", DataCenter.getInstance().getPickedData()
                        .get(position).getId());
                v.getContext().startActivity(intent);
            }
        };
        pickedAdapter = new CnblogPickedAdapter(this.getActivity(), listener);
        flipview.setAdapter(pickedAdapter);
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
        if (pickedAdapter.getCount() != 0) {
            pickedAdapter.notifyDataSetChanged();
            refreshListener.onStopRefresh();
        } else {
            startTask();
        }
    }

    public void startTask() {
        if (request == null) {
            request = new RssCnblogPickedRequest<Void>(
                    new BaseListener<ArrayList<PickedDetailItem>>() {
                        @Override
                        public void onSuccess(ArrayList<PickedDetailItem> result) {
                            DataCenter.getInstance().replacePickedItems(result);
                            if (!CnblogsPickedFragment.this.isRemoving()) {
                                Toast.makeText(getActivity(), "success",
                                        Toast.LENGTH_SHORT).show();
                                pickedAdapter.notifyDataSetChanged();
                                try {
                                    daoManager.addPickedEntryItems(result);
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
                            if (CnblogsPickedFragment.this.isRemoving()) {
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
                            if (CnblogsPickedFragment.this.isRemoving()) {
                                return;
                            }
                            Toast.makeText(getActivity(),
                                    R.string.failed_to_get_content,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFinished() {
                            if (CnblogsPickedFragment.this.isRemoving()) {
                                return;
                            }
                            refreshListener.onStopRefresh();
                        }
                    });
        }
        task = new BaseTask(RssConfig.getInstance().getPickedUrl(), request);
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
        DataCenter.getInstance().getPickedData().clear();
        super.onDestroy();
    }
}
