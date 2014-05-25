package com.avenwu.ereader.request;

import com.avenwu.ereader.config.Constant;
import com.android.volley.volleyhelper.MultiApiParams;

public class BDPhotoParams extends MultiApiParams {
    private int stratIndex;
    private int endIndex;
    private int pageCount = 0;
    private final int countPerPage = 30;
    private String tag1 = MEI_NV;
    private String tag2 = XING_GAN;
    public static final String MEI_NV = "%E7%BE%8E%E5%A5%B3";
    public static final String XING_GAN = "%E6%80%A7%E6%84%9F";

    public BDPhotoParams(String tag1, String tag2) {
        setFinished(true);
        this.tag1 = tag1;
        this.tag2 = tag2;
    }

    @Override
    public void parse(int index, String response) throws Exception {

    }

    public int getCurrentPage() {
        return pageCount;
    }

    public void resetPage() {
        pageCount = 0;
    }

    public void updatePage() {
        pageCount++;
    }

    public String getPhotoParams() {
        stratIndex = pageCount * countPerPage;
        endIndex = stratIndex + countPerPage;
        // "http://image.baidu.com/channel/listjson?fr=channel&tag1=%E7%BE%8E%E5%A5%B3&tag2=%E6%80%A7%E6%84%9F&sorttype=1&pn=0&rn=30&ie=utf8&oe=utf-8"
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constant.PHOTO_REQUEST_PREFIX);
        stringBuffer
                .append("fr=channel&tag1=").append(tag1)
                .append("&tag2=").append(tag2)
                .append("&sorttype=1&pn=").append(stratIndex)
                .append("&rn=").append(endIndex)
                .append("&ie=utf8&oe=utf-8");
        return stringBuffer.toString();
    }
}
