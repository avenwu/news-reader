package com.avenwu.ereader.task;

public interface BaseRequest<M> {
    M doInbackground(String param);
}
