package com.yanhuanxy.multifunservice.socket;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前缓存池
 */
public class CurrentCachePool {

    public static Map<Integer,Integer> webSockets = new ConcurrentHashMap();
}
