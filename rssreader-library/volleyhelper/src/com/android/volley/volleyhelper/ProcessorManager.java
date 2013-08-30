package com.android.volley.volleyhelper;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Handler;
import android.os.Looper;

/**
 * This manager is used to process the data from server response.<br>
 * 
 * @author chaobin
 * 
 */
public class ProcessorManager {
    private ExecutorService processExecutor;
    private Map<String, WeakReference<Future<?>>> processorMap;
    private static ProcessorManager instance;
    private final ProcessorDelivery processorDelivery;

    private ProcessorManager() {
        processorDelivery = new ProcessorDelivery(new Handler(
                Looper.getMainLooper()));
        processExecutor = Executors.newCachedThreadPool();
        processorMap = new HashMap<String, WeakReference<Future<?>>>();
    }

    public static ProcessorManager getInstance() {
        if (instance == null) {
            synchronized (ProcessorManager.class) {
                if (instance == null) {
                    instance = new ProcessorManager();
                }
            }
        }
        return instance;
    }

    public void excute(ResponseProcessor<?> processor) {
        Future<?> future = processExecutor.submit(processor);
        processorMap.put(processor.toString(), (new WeakReference<Future<?>>(
                future)));
    }

    public void post(Runnable runnable) {
        processorDelivery.deliver(runnable);
    }

    public void cancellAll(boolean mayInterruptIfRunning) {
        if (processorMap != null && !processorMap.isEmpty()) {
            for (Entry<String, WeakReference<Future<?>>> entry : processorMap
                    .entrySet()) {
                Future<?> future = entry.getValue().get();
                if (future != null) {
                    future.cancel(mayInterruptIfRunning);
                }
            }
            processorMap.clear();
        }
    }

    public void cancell(String key) {
        if (processorMap.containsKey(key)) {
            Future<?> future = processorMap.get(key).get();
            if (future != null) {
                future.cancel(true);
            }
            processorMap.remove(key);
        }
    }

    public void cancellAll() {
        cancellAll(true);
    }

    /**
     * Delivery the processor or database query callback to work on UI thread;
     * 
     * @author chaobin
     * 
     */
    public class ProcessorDelivery {
        private final Executor mResponsePoster;

        public ProcessorDelivery(final Handler handler) {
            mResponsePoster = new Executor() {
                @Override
                public void execute(Runnable command) {
                    handler.post(command);
                }
            };
        }

        public void deliver(Runnable runnable) {
            mResponsePoster.execute(runnable);
        }
    }
}
