package com.yanhuanxy.multifunexport.fileservice.dto;

/**
 * @author yym
 * @version 1.0
 */
public class ThumbImage {
    /**
     * 缩略图 宽
     */
    private int width;

    /**
     * 缩略图 高
     */
    private int height;

    public ThumbImage() {
        super();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
