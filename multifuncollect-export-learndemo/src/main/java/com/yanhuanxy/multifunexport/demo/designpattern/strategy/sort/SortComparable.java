package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort;

public interface SortComparable<T> {

    /**
     * 对象比较 接口
     */
    int compareTo(T t);
}
