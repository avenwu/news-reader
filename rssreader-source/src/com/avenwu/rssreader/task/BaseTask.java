package com.avenwu.rssreader.task;

public class BaseTask implements Runnable {

    private String url;
    private BaseRequest<?> request;

    public BaseTask(String url, BaseRequest<?> request) {
        this.url = url;
        this.request = request;
    }

    @Override
    public void run() {
        request.doInbackground(url);
        finish();
    }

    public void start() {
        TaskManager.getInstance().excute(this);
    }

    public void cancel() {
        TaskManager.getInstance().cancelSingleTask(this.toString());
    }

    private void finish() {
        TaskManager.getInstance().removeFromRequest(this.toString());
    }
}
