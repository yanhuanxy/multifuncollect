package com.yanhuanxy.multifunexport.fileservice.dto.operation;


/**
 * @author yym
 * @version 1.0
 */
public class RangeFile {
    /**
     * 开始分片数
     */
    private int start;

    /**
     * 长度
     */
    private int length;

    public RangeFile() {
        super();
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
