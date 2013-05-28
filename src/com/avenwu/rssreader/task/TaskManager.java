package com.avenwu.rssreader.task;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import android.content.pm.FeatureInfo;

public class TaskManager {
    private ExecutorService executorService;
    private List<WeakReference<Future<?>>> requestMap;
    private static TaskManager instance;

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }

    private void init() {
        executorService = Executors.newCachedThreadPool();
        requestMap = new ArrayList<WeakReference<Future<?>>>();
    }

    public void excute(FutureTask<?> task) {
        if (executorService == null) {
            init();
        }
        Future<?> future = executorService.submit(task);
        requestMap.add(new WeakReference<Future<?>>(future));
    }

    public void cancellAll(boolean mayInterruptIfRunning) {
        if (requestMap != null) {
            for (WeakReference<Future<?>> futre : requestMap) {
                Future<?> p = futre.get();
                p.cancel(mayInterruptIfRunning);
            }
        }
    }

    public void cancellAll() {
        cancellAll(true);
    }
}
