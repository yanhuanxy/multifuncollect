package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort;

import java.util.logging.Logger;

/**
 * 排序算法
 */
public interface Sort<E> {
    Logger global = Logger.getGlobal();

    default void sort(E[] e){
        global.warning("类型未实现");
    }

    default void sort(E[] e, boolean isMaxToMin){
        global.warning("类型未实现");
    }


    default void sort(E[] e, SortComparator<E> sortComparator){
        global.warning("类型未实现");
    }


    /**
     * 交换位置
     * @param e 数组对象
     * @param before 下标a
     * @param after  下标b
     */
    default void swap(E[] e, int before, int after){
        E tmpVal = e[before];
        e[before] = e[after];
        e[after] = tmpVal;
    }

    /**
     * 获取class 对象
     * @param e
     * @return
     */
    default Class<?> getClassName(E e){
        Class<?> className;
        if(e instanceof SortComparable){
            className = SortComparable.class;
        }else{
            className = e.getClass();
        }
        return className;
    }
}
