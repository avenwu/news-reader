package com.avenwu.rssreader.activity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectResource;
import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.avenwu.rssreader.R;
import com.avenwu.rssreader.config.RssConfig;
import com.avenwu.rssreader.dataprovider.RssDaoManager;
import com.avenwu.rssreader.service.NetworkReceiver;
import com.avenwu.rssreader.utils.NetworkHelper;
import com.google.inject.Inject;

public class CnblogsNewsFeedActivity extends RoboFragmentActivity {

	private final String TAG = "MainActivity";
	private final int CNBLOGS_HOME = 0;
	private final int CNBLOGS_PICKED = 1;
	private final int CNBLOGS_CANDICATE = 2;
	private final int CNBLOGS_NEWS = 3;
	@Inject
	private NetworkReceiver networkReceiver;
	private IntentFilter intentFilter;
	private RssDaoManager daoManager;
	private Dialog catalogDialog;
	@InjectResource(R.array.cnblogs_catalog)
	private String[] catalogStrings;
	private int curent_catalog_index = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RssConfig.getInstance().init(this);
		NetworkHelper.updateConnectionState(this);
		daoManager = new RssDaoManager(this);
		intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(networkReceiver, intentFilter);
		changeCatalog(CNBLOGS_PICKED);
	}

	// private void initTitleBar() {
	// updateTitle(R.string.title_cnblogs);
	// actionBar.setHomeAction(new IntentAction(this,
	// UIHelper.back2Menu(this), R.drawable.ic_launcher));
	// actionBar.setProgressBarVisibility(View.VISIBLE);
	// actionBar.addAction(new Action() {
	// @Override
	// public void performAction(View view) {
	// if (catalogDialog == null) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// CnblogsNewsFeedActivity.this);
	// builder.setSingleChoiceItems(R.array.cnblogs_catalog, 0,
	// new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// updateTitle(catalogStrings[which]);
	// changeCatalog(which);
	// }
	// }).setPositiveButton(R.string.confirm, null);
	// catalogDialog = builder.create();
	// }
	// catalogDialog.show();
	// }
	//
	// @Override
	// public int getDrawable() {
	// return 0;
	// }
	//
	// @Override
	// public int getContent() {
	// return R.string.hello;
	// }
	// });
	// }

	// private void updateTitle(int titleId) {
	// actionBar.setTitle(titleId);
	// }

	// private void updateTitle(String title) {
	// actionBar.setTitle(title);
	// }

	private void changeCatalog(int which) {
		if (curent_catalog_index != which) {
			curent_catalog_index = which;
			switch (which) {
			case CNBLOGS_HOME:

				break;
			case CNBLOGS_PICKED:
				FragmentTransaction tr = getSupportFragmentManager()
						.beginTransaction();
				tr.replace(R.id.frame_content,
						CnblogsPickedFragment.newInstance(daoManager));
				tr.commit();
				break;
			case CNBLOGS_CANDICATE:

				break;
			case CNBLOGS_NEWS:

				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(networkReceiver);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
