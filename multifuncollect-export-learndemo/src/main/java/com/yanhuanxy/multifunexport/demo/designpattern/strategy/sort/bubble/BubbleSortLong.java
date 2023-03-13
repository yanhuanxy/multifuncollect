package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.bubble;

public class BubbleSortLong<E> extends BubbleSort<E> {

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
            Long tmpLongVal = objectMapper.convertValue(tmpVal, Long.class);
            Long afterLongVal = objectMapper.convertValue(afterVal, Long.class);
            if(compareLong(isMaxToMin, tmpLongVal, afterLongVal)) swap(e, j, j+1);
        }
    }

    /**
     * 比较 Long 类型
     * @param isMaxToMin
     * @param beforeVal
     * @param afterVal
     */
    private boolean compareLong(Boolean isMaxToMin, Long beforeVal, Long afterVal){
        return isMaxToMin ? beforeVal< afterVal : beforeVal > afterVal;
    }
}
