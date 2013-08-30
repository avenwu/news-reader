package com.avenwu.ereader.ad;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.avenwu.ereader.activity.MenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import cn.waps.AppConnect;

public class LoadActivity extends Activity {
    private final String KUGUO_APP_ID = "13bb74a44903443ba53e76526086065c";
    private final String WAPS_APP_ID = "d2c72e069bde92b7e86f87c0bc9431e2";

    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);
    private final int time = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppConnect.getInstance(WAPS_APP_ID, "waps", this);
        AppConnect.getInstance(this).setAdViewClassName(
                "com.avenwu.ereader.ad.MyAdView");
        AppConnect.getInstance(this).initAdInfo();
        AppConnect.getInstance(this).initPopAd(this);

        View loadingAdView = LoadingPopAd.getInstance().getContentView(this,
                time);
        if (loadingAdView != null) {
            this.setContentView(loadingAdView);
        }

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadActivity.this,
                        MenuActivity.class);
                LoadActivity.this.startActivity(intent);
                LoadActivity.this.finish();
                AppConnect.getInstance(LoadActivity.this).close();
            }
        }, time, TimeUnit.SECONDS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        LoadingPopAd.release();
        super.onDestroy();
    }
}
