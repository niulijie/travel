package com.niulijie.mdm.dto.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 14328
 */
public class DeptUserCache {

    public static Map<Integer, Integer> deptUserCache = new ConcurrentHashMap<>(4);
}
