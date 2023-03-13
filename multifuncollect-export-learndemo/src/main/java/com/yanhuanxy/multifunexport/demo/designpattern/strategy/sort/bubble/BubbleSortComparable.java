package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.bubble;

import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.SortComparator;

public class BubbleSortComparable<E> extends BubbleSort<E> {

    @Override
    public void sort(E[] e) {
        global.warning("该类型不支持默认比较！请使用比较器模式");
    }

    @Override
    public void sort(E[] e, SortComparator<E> sortComparator) {
        for (int i = e.length - 1; i > 0; i--) {
            compare(e, i, sortComparator);
        }
    }

    /**
     * 比较 两个值 大小
     * 1、从小到大
     * 再交换位置
     * @param e
     * @param index
     */
    private void compare(E[] e, int index, SortComparator<E> sortComparator){
        for (int j = 0; j < index; j++) {
            E tmpVal = e[j], afterVal = e[j+1];
            boolean compareAble = isMaxToMin ? sortComparator.compare(tmpVal, afterVal) == -1 :
                    sortComparator.compare(tmpVal, afterVal) == 1;
            if(compareAble) swap(e, j, j+1);
        }
    }
}
