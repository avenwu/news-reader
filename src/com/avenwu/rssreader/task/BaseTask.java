package com.avenwu.rssreader.task;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.avenwu.rssreader.model.EntryItem;

public class BaseTask extends FutureTask<ArrayList<EntryItem>> {

    public BaseTask(Callable<ArrayList<EntryItem>> callable,) {
        super(callable);
    }

    
}
