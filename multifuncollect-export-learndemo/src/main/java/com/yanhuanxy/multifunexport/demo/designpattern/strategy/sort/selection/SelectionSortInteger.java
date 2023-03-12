package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.selection;

public class SelectionSortInteger<E> extends SelectionSort<E> {

    @Override
    public void sort(E[] e) {
        for (int i = 0; i < e.length - 1; i++) {
            // 获取需要交换的位置
            int index = compare(e, i);
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
    private int compare(E[] e, int index){
        int result = index;
        for (int j = index + 1; j < e.length; j++) {
            E tmpVal = e[j], afterVal = e[result];
            Integer tmpLongVal = objectMapper.convertValue(tmpVal, Integer.class);
            Integer afterLongVal = objectMapper.convertValue(afterVal, Integer.class);
            if(compareInteger(isMaxToMin, tmpLongVal, afterLongVal)){
                result = j;
            }
        }
        return result;
    }

    /**
     * 比较 integer 类型
     * @param isMaxToMin
     * @param beforeVal
     * @param afterVal
     */
    private boolean compareInteger(Boolean isMaxToMin, Integer beforeVal, Integer afterVal){
        return isMaxToMin ? beforeVal > afterVal : beforeVal < afterVal;
    }
}
