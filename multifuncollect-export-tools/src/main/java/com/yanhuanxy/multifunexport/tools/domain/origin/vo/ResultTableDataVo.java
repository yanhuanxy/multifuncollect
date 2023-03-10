package com.yanhuanxy.multifunexport.tools.domain.origin.vo;

import java.util.List;

/**
 * 根据表名、筛选条件分页查询数据 VO
 * @author yym
 * @date 2021/8/5
 */
public class ResultTableDataVo<T> {
    /**
     * 总数量
     */
    private Long total;

    /**
     * 数据集合
     */
    private List<T> records;

    public ResultTableDataVo() {
    }

    public ResultTableDataVo(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
