package com.yanhuanxy.multifunexport.demo.designpattern.strategy;

import com.google.gson.Gson;
import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.Sort;
import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.bubble.BubbleSort;
import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.selection.SelectionSort;

import java.util.logging.Logger;

/**
 * 策略模式
 *
 * comparator ->
 * comparable ->
 */
public class StrategyDemo {
    public static void main(String[] args) {
        Sort<Integer> sort = new BubbleSort<Integer>();
        Integer[] nums = new Integer[]{1,2,5,6,3,70,12};
        sort.sort(nums);
        Logger global = Logger.getGlobal();
        global.info(new Gson().toJson(nums));
        Sort<Integer> sort2 = new SelectionSort<Integer>();
        sort2.sort(nums, Boolean.TRUE);
        global.info(new Gson().toJson(nums));

        Sort<Cat> sortcat = new BubbleSort<Cat>();
        Cat[] cats = new Cat[]{new Cat(1),new Cat(11),new Cat(5)};
        sortcat.sort(cats, Boolean.TRUE);
        global.info(new Gson().toJson(cats));
    }
}
