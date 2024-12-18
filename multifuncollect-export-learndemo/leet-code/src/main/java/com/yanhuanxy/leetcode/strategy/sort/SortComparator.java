package com.yanhuanxy.leetcode.strategy.sort;

@FunctionalInterface
public interface SortComparator<T> {

    /**
     * 对象比较 接口
     */
    int compare(T ta, T tb);
}
