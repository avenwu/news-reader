package com.avenwu.rssreader.task;

public interface BaseRequest<M> {
    M doInbackground(String param);
}
