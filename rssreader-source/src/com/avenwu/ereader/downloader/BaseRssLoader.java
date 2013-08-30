package com.avenwu.ereader.downloader;

/**
 * 
 * @author AvenWu iamavenwu@gmail.com
 * @date 2013-5-28
 * 
 * @param <O>
 *            url
 * @param <P>
 *            params type
 * @param <K>
 *            return value type
 */
public interface BaseRssLoader<O, P, K> {
    /**
     * 
     * @param url
     * @param params
     * @return
     */
    public K queryRssCotent(O url, P params);
}
