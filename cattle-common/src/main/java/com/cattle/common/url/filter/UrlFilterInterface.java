package com.cattle.common.url.filter;

/**
 * url 过滤接口
 */
public interface UrlFilterInterface {

    int[] createHash(String str);

    boolean add(String str);

    boolean exist(String str);
}
