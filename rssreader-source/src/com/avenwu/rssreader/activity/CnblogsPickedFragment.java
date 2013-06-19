package com.avenwu.rssreader.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.avenwu.rssreader.R;
import com.avenwu.rssreader.adapter.CnblogPickedAdapter;
import com.avenwu.rssreader.adapter.CnblogPickedAdapter.ArticalListener;
import com.avenwu.rssreader.config.RssConfig;
import com.avenwu.rssreader.dataprovider.DataCenter;
import com.avenwu.rssreader.dataprovider.RssDaoManager;
import com.avenwu.rssreader.model.EntryItem;
import com.avenwu.rssreader.task.BaseListener;
import com.avenwu.rssreader.task.BaseRequest;
import com.avenwu.rssreader.task.BaseTask;
import com.avenwu.rssreader.task.RssCnblogRequest;
import com.avenwu.rssreader.view.RefreshView;
import com.avenwu.rssreader.view.RefreshView.RefreshListener;

public class CnblogsPickedFragment extends RoboFragment {
	@InjectView(R.id.flipview_rss)
	private FlipViewController flipview;
	@InjectView(R.id.refreshView1)
	private RefreshView refreshView;
	private CnblogPickedAdapter pickedAdapter;
	private BaseTask task;
	private BaseRequest request;
	private CnblogPickedAdapter.ArticalListener listener;
	private RssDaoManager daoManager;

	public static CnblogsPickedFragment newInstance(RssDaoManager rssDaoManager) {
		CnblogsPickedFragment fragment = new CnblogsPickedFragment();
		fragment.daoManager = rssDaoManager;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			DataCenter.getInstance().replacePickedItems(
					daoManager.getEntryItems());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.newsfeed_layout, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		flipview.setAnimationBitmapFormat(Config.RGB_565);
		listener = new ArticalListener() {
			@Override
			public void onClick(View v, int position) {
				Intent intent = new Intent(v.getContext(),
						BlogArticalActivity.class);
				intent.putExtra("content_id", position);
				intent.putExtra("content_type", RssConfig.getInstance()
						.getPickedUrl());
				v.getContext().startActivity(intent);
			}
		};
		pickedAdapter = new CnblogPickedAdapter(this.getActivity(), listener);
		flipview.setAdapter(pickedAdapter);
		flipview.setOnViewFlipListener(new ViewFlipListener() {
			@Override
			public void onViewFlipped(View view, int position) {
				listener.updatePosition(position);
			}
		});
		if (pickedAdapter.getCount() != 0) {
			pickedAdapter.notifyDataSetChanged();
		} else {
			startTask();
		}

		refreshView.setRefreshListener(new RefreshListener() {
			@Override
			public void onStartRefresh() {
				startTask();
			}
		});
	}

	private void startTask() {
		if (request == null) {
			request = new RssCnblogRequest<Void>(
					new BaseListener<ArrayList<EntryItem>>() {
						@Override
						public void onSuccess(ArrayList<EntryItem> result) {
							Toast.makeText(getActivity(), "success",
									Toast.LENGTH_SHORT).show();
							DataCenter.getInstance().addPickedItems(result);
							pickedAdapter.notifyDataSetChanged();
							try {
								daoManager.addEntryItems(result);
							} catch (SQLException e) {
								e.printStackTrace();
								Toast.makeText(getActivity(),
										"failed to insert tinto table",
										Toast.LENGTH_SHORT).show();
							}
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
										R.string.failed_to_get_content,
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onError(Exception e) {
							Toast.makeText(getActivity(),
									R.string.failed_to_get_content,
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFinished() {
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
		super.onDestroy();
	}
}
