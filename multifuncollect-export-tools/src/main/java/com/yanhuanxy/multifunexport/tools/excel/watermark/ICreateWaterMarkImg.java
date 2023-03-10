package com.yanhuanxy.multifunexport.tools.excel.watermark;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author yym
 */
public interface ICreateWaterMarkImg {

    /**
     * 自定以水印图片（默认版）
     * @param width 图片宽度
     * @param height 图片高度
     * @param waterText 图片内容
     * @return
     */
    public BufferedImage getWaterImage(int width, int height, String waterText);

    /**
     * 自定以水印图片（纯手动版）
     * @param width 图片宽度
     * @param height 图片高度
     * @param waterText 图片内容
     * @param color 字体颜色
     * @param font 字体类型（windows 字体库基本都可以）
     * @param degree 字体倾斜角度
     * @param alpha 字体透明度
     * @return
     */
    public BufferedImage getWaterImage(int width, int height, String waterText, Color color, Font font, Double degree, float alpha);
}
