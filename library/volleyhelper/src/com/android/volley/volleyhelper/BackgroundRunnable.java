package com.android.volley.volleyhelper;

/**
 * set the thread to a lower priority
 * 
 * @author chaobin
 * 
 */
public abstract class BackgroundRunnable implements Runnable {
    /**
     * subclass do not override it
     */
    @Override
    public void run() {
        // Moves the current Thread into the background, If you don't set the
        // thread to a lower priority this way, then the thread could still slow
        // down your app because it operates at the same priority as the UI
        // thread by default.
        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        doInBackground();
    }

    /**
     * subclass should override doInBackground() instead of run() so the thread
     * priority is lower than UI thread
     */
    public abstract void doInBackground();
}
