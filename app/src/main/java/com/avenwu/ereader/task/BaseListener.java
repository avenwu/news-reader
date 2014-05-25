package com.avenwu.ereader.task;

import android.os.Handler;
import android.os.Message;

public abstract class BaseListener<O> extends Handler {
    public static final int SUCCESS = 1;
    public static final int FAILED = 0;
    public static final int ERROR = 2;

    public abstract void onSuccess(O result);

    /**
     * deal with all the try catch exception;
     * 
     * @param e
     */
    public void onError(Exception e) {

    }

    /**
     * do some clean job here, eg: set progress bar invisible
     */
    public void onFinished() {

    }

    /**
     * failed to get data
     */
    public abstract void onFailed(Object result);

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
        case SUCCESS:
            onSuccess((O) msg.obj);
            break;
        case FAILED:
            onFailed(msg.obj);
            break;
        case ERROR:
            onError((Exception) msg.obj);
            break;
        default:
            break;
        }
        onFinished();
    }

    public void sendResult(int type, Object obj) {
        obtainMessage(type, obj).sendToTarget();
    }
}
