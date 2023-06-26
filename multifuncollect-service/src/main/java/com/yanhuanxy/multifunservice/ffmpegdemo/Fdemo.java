package com.yanhuanxy.multifunservice.ffmpegdemo;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Fdemo {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("java.library.path"));

        String videoFilePath = "C:\\Users\\yanhuan\\Desktop\\b1a88d0e6836253d1e582666133236e1.mp4"; // 视频文件路径
        String outputFolderPath = "C:\\Users\\yanhuan\\Desktop\\output_folder"; // 输出文件夹路径

        // 创建输出文件夹
        File outputFolder = new File(outputFolderPath);
        outputFolder.mkdirs();
//
//        // 打开视频文件
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFilePath);
        try {
            grabber.start();

            int frameNumber = 0;
            Frame frame;
            while ((frame = grabber.grab()) != null) {
                // 将帧转换为BufferedImage
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.getBufferedImage(frame);
                if(image == null){
                    continue;
                }
                // 生成输出文件路径
                String outputFilePath = outputFolderPath + File.separator + "frame_" + frameNumber + ".jpg";

                // 保存帧为图片文件
                ImageIO.write(image, "jpg", new File(outputFilePath));

                frameNumber++;
            }

            grabber.stop();
            System.out.println("视频帧提取完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
