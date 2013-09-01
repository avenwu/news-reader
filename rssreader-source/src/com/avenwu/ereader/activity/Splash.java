package com.avenwu.ereader.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.avenwu.ereader.R;
import com.avenwu.ereader.config.Constant;

import cn.waps.AppConnect;

public class Splash extends Activity {
    private CountDownTimer timer;
    private final int MILLIS = 3000;
    private final int INTERVAL = 1000;
    private View backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        backgroundView = findViewById(R.id.iv_background);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        backgroundView.startAnimation(scaleAnimation);
        // MediaManager.startAd(MenuActivity.this,
        // MediaManager.LEFT_TOP, KUGUO_APP_ID, "m-appchina");
        AppConnect.getInstance(this).setCrashReport(true);
        AppConnect.getInstance(this).initPopAd(this);
//        AppConnect.getInstance(this).showPopAd(this);
        timer = new CountDownTimer(MILLIS, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Splash.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.start();
    }

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
