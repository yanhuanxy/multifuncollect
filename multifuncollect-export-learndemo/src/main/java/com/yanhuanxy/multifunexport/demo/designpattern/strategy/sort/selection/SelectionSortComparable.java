package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.selection;

import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.SortComparator;

public class SelectionSortComparable<E> extends SelectionSort<E> {

    @Override
    public void sort(E[] e) {
        global.warning("该类型不支持默认比较！请使用比较器模式");
    }

    @Override
    public void sort(E[] e, SortComparator<E> sortComparator) {
        for (int i = 0; i < e.length - 1; i++) {
            // 获取需要交换的位置
            int index = compare(e, i, sortComparator);
            // 交换位置
            if(index == i){
                continue;
            }
            swap(e, i, index);
        }
    }

    /**
     * 比较 两个值 大小
     * 1、从小到大
     * 再交换位置
     * @param e
     * @param index
     */
    private int compare(E[] e, int index, SortComparator<E> sortComparator){
        int result = index;
        for (int j = index + 1; j < e.length; j++) {
            E tmpVal = e[j], afterVal = e[result];
            boolean compareAble = isMaxToMin ? sortComparator.compare(tmpVal, afterVal) == 1 :
                    sortComparator.compare(tmpVal, afterVal) == -1;
            if(compareAble){
                result = j;
            }
        }
        return result;
    }
}
