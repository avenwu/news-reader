package com.avenwu.ereader.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.avenwu.ereader.R;
import com.avenwu.ereader.config.Constant;

import cn.waps.AppConnect;

public class Splash extends Activity {
    private CountDownTimer timer;
    private final int MILLIS = 2000;
    private final int INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // MediaManager.startAd(MenuActivity.this,
        // MediaManager.LEFT_TOP, KUGUO_APP_ID, "m-appchina");
        AppConnect.getInstance(this).setCrashReport(true);
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

//    public void startMenu(View view) {
//        Intent intent = new Intent(Splash.this, MenuActivity.class);
//        startActivity(intent);
////        overridePendingTransition(R.anim.left_out,
////                android.R.anim.fade_out);
//        finish();
//    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Dialog dialog = AppConnect.getInstance(this).getPopAdDialog();
        if (dialog!=null)
            dialog.dismiss();
        super.onDestroy();
    }
}
