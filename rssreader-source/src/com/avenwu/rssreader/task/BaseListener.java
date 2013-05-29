package com.avenwu.rssreader.task;

import android.os.Handler;
import android.os.Message;

public abstract class BaseListener<O> extends Handler {
    public static final int SUCCESS = 1;
    public static final int FAILED = 0;
    public static final int ERROR = 2;
    public static final int FINISH = 3;

    public abstract void onSuccess(O result);

    public void onError(Exception e) {

    }

    public void onStart() {

    }

    public void onFinished() {

    }

    public abstract void onFailed();

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
        case SUCCESS:
            onSuccess((O) msg.obj);
            break;
        case FAILED:
            onFailed();
            break;
        case ERROR:
            onError((Exception) msg.obj);
            break;
        case FINISH:
            onFinished();
            break;
        default:
            break;
        }
    }

    public void sendResult(int type, Object obj) {
        obtainMessage(type, obj).sendToTarget();
    }
}
