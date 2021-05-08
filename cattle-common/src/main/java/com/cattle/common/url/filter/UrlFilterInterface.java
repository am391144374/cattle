package com.cattle.common.url.filter;

/**
 * url 过滤接口
 */
public interface UrlFilterInterface {

    int[] createHash(String str);

    /**
     * 不存在则添加
     * @param str
     * @return
     */
    boolean add(String str,String key);

    /**
     *
     * @param str
     * @return true - 存在,false - 不存在
     */
    boolean exist(String str,String key);
}
