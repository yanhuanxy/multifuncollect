package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.bubble;

public class BubbleSortInteger<E> extends BubbleSort<E> {

    @Override
    public void sort(E[] e) {
        for (int i = e.length - 1; i > 0; i--) {
            compare(e, i);
        }
    }

    /**
     * 比较 两个值 大小
     * 1、从小到大
     * 再交换位置
     * @param e
     * @param index
     */
    private void compare(E[] e, int index){
        for (int j = 0; j < index; j++) {
            E tmpVal = e[j], afterVal = e[j+1];
            Integer tmpIntVal = objectMapper.convertValue(tmpVal, Integer.class);
            Integer afterIntVal = objectMapper.convertValue(afterVal, Integer.class);
            if(compareInteger(isMaxToMin, tmpIntVal, afterIntVal)) swap(e, j, j+1);
        }
    }

    /**
     * 比较 integer 类型
     * @param isMaxToMin
     * @param beforeVal
     * @param afterVal
     */
    private boolean compareInteger(Boolean isMaxToMin, Integer beforeVal, Integer afterVal){
        return isMaxToMin ? beforeVal< afterVal : beforeVal > afterVal;
    }
}
