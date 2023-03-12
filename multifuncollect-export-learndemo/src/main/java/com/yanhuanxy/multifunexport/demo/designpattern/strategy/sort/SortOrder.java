package com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class SortOrder {
    /**
     * 排序方式
     */
    protected Boolean isMaxToMin = false;

    /**
     * 类型转换对象
     */
    protected ObjectMapper objectMapper = new ObjectMapper();

    protected void setMaxToMin(Boolean maxToMin) {
        isMaxToMin = Optional.ofNullable(maxToMin).orElse(Boolean.FALSE);
    }

}
