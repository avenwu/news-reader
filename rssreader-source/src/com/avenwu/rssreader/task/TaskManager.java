package com.avenwu.rssreader.task;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskManager {
    private ExecutorService executorService;
    private Map<String, WeakReference<Future<?>>> requestMap;
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
        requestMap = new HashMap<String, WeakReference<Future<?>>>();
    }

    public void excute(BaseTask task) {
        if (executorService == null) {
            init();
        }
        Future<?> future = executorService.submit(task);
        requestMap.put(task.toString(), (new WeakReference<Future<?>>(future)));
    }

    public void cancellAll(boolean mayInterruptIfRunning) {
        if (requestMap != null && !requestMap.isEmpty()) {
            for (Entry<String, WeakReference<Future<?>>> entry : requestMap.entrySet()) {
                Future<?> future = entry.getValue().get();
                if (future != null) {
                    future.cancel(mayInterruptIfRunning);
                }
            }
            requestMap.clear();
        }
    }

    public void cancelSingleTask(String key) {
        if (requestMap.containsKey(key)) {
            Future<?> future = requestMap.get(key).get();
            if (future != null) {
                future.cancel(true);
            }
            requestMap.remove(key);
        }
    }

    public void removeFromRequest(String key) {
        if (requestMap.containsKey(key)) {
            requestMap.remove(key);
        }
    }

    public void cancellAll() {
        cancellAll(true);
    }
}
