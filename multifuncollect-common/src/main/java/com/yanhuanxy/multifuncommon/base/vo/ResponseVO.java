package com.yanhuanxy.multifuncommon.base.vo;

/**
 * 成功响应体
 * @author hxl
 */
public class ResponseVO<T> extends ResponseStatusVO {

    /**
     * 数据
     */
    private T data;

    public ResponseVO() {
        super(true, "0", "成功");
    }

    public ResponseVO(T data) {
        this();
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
