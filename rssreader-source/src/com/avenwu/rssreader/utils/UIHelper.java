package com.avenwu.rssreader.utils;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.avenwu.rssreader.activity.MenuActivity;

public class UIHelper {
    public static Intent back2Menu(Context context) {
        Intent i = new Intent(context, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    public static DisplayMetrics getScreenMetrix(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }
}
