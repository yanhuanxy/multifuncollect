package com.yanhuanxy.multifunexport.tools.base;

/**
 * @date 2021/3/30
 */

public class PageDTO {

    private Integer pageNo;

    private Integer current;

    private Integer pageSize;

    public PageDTO() {
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

    @Override
    public String toString() {
        return super.toString();
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}
