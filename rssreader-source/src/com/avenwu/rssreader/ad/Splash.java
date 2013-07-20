package com.avenwu.rssreader.ad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import cn.waps.AppConnect;

import com.avenwu.ereader.R;
import com.avenwu.rssreader.activity.MenuActivity;

public class Splash extends Activity {
    private final String KUGUO_APP_ID = "13bb74a44903443ba53e76526086065c";
    private final String WAPS_APP_ID = "d2c72e069bde92b7e86f87c0bc9431e2";
    private CountDownTimer timer;
    private final int MILLIS = 4000;
    private final int INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // MediaManager.startAd(MenuActivity.this,
        // MediaManager.LEFT_TOP, KUGUO_APP_ID, "m-appchina");
        AppConnect.getInstance(WAPS_APP_ID, "WAPS", this);
        AppConnect.getInstance(this).setAdViewClassName(
                "com.avenwu.rssreader.activity.MyAdView");
        AppConnect.getInstance(this).setCrashReport(false);
        // 初始化插屏广告数据
        AppConnect.getInstance(this).initPopAd(this);
        AppConnect.getInstance(this).showPopAd(this);
        timer = new CountDownTimer(MILLIS, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Splash.this, MenuActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.left_out,
//                        android.R.anim.fade_out);
                finish();
            }
        };
        timer.start();
    }

    public void startMenu(View view) {
        Intent intent = new Intent(Splash.this, MenuActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.left_out,
//                android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        super.onBackPressed();
    }
}
