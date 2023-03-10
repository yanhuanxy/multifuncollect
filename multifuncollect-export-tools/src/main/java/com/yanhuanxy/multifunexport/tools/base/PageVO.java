package com.yanhuanxy.multifunexport.tools.base;

/**
 * @author yym
 * @date 2021/5/11
 */
public class PageVO {

    private Integer pageNo;
    private Integer pageSize;
    private Integer total;
    private String sort;

    public PageVO() {
        this.pageNo = 1;
        this.pageSize = 10;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
