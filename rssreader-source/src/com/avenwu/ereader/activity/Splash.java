package com.avenwu.ereader.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.avenwu.ereader.R;

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
        scaleAnimation.setFillAfter(true);
        backgroundView.startAnimation(scaleAnimation);
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
