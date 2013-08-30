package com.avenwu.ereader.model;

import com.avenwu.ereader.config.Constant;

public class PhotoParams {
    private int stratIndex;
    private int endIndex;
    private String request;
    public int pageCount = 0;
    private final int countPerPage = 30;

    public String getRequest() {
        stratIndex = pageCount * countPerPage;
        endIndex = stratIndex + countPerPage;
        request = Constant.PHOTO_REQUEST_PREFIX
                + "?fr=channel&tag1=%E7%BE%8E%E5%A5%B3&tag2=%E6%80%A7%E6%84%9F&sorttype=1&pn=%1&rn=%2&ie=utf8&oe=utf-8"
                        .replace("%1", stratIndex + "").replace("%2",
                                endIndex + "");
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getPageNumber() {
        return stratIndex;
    }

    public void setPageNumber(int pageNumber) {
        this.stratIndex = pageNumber;
    }

    public int getResultNumber() {
        return endIndex;
    }

    public void setResultNumber(int resultNumber) {
        this.endIndex = resultNumber;
    }

}
