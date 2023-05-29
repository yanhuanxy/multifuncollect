package com.yanhuanxy.multifunservice.socket;


import com.yanhuanxy.multifundomain.system.SysUser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前缓存池
 */
public class CurrentCachePool {

    public static Map<Integer, MultipleCompetitionWebsocket> webSockets = new ConcurrentHashMap();

    public static Map<Integer, List<Object>> sessionPool = new ConcurrentHashMap<>();

    public static Map<String, SysUser> curUserPool = new ConcurrentHashMap<>();
}
