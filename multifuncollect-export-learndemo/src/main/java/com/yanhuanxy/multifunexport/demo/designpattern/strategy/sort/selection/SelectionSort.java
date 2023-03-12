package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.selection;

import com.yanhuanxy.multifunexport.demo.designpattern.emuns.SortDataTypeEnum;
import com.yanhuanxy.multifunexport.demo.designpattern.emuns.SortStorageEnum;
import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.Sort;
import com.yanhuanxy.multifunexport.demo.designpattern.strategy.SortBeanStorageUtil;
import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.SortOrder;

import java.util.Optional;

public class SelectionSort<E> extends SortOrder implements Sort<E> {

    @Override
    public void sort(E[] e) {
        // 空指针
        Optional.ofNullable(e).orElseThrow(NullPointerException::new);
        if(e.length == 0) return;

        SortBeanStorageUtil<SelectionSort<E>> sortBeanStorageUtil = new SortBeanStorageUtil<>();
        SelectionSort<E> bubbleSort = sortBeanStorageUtil.initCreateBeanByStorageType(SortStorageEnum.SELECTION,
                SortDataTypeEnum.convertByClassName(getClassName(e[0])));
        bubbleSort.setMaxToMin(this.isMaxToMin);
        bubbleSort.sort(e);
    }

    @Override
    public void sort(E[] e, boolean isMaxToMin) {
        // 设置排序类型 升序 降序
        this.setMaxToMin(isMaxToMin);
        // 排序
        this.sort(e);
    }
}
