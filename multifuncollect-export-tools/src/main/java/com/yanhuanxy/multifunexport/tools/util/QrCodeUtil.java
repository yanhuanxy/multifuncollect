package com.yanhuanxy.multifunexport.tools.util;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yanhuanxy.multifunexport.tools.exception.ToolsException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class QrCodeUtil {

    private static final String CHARSET = "utf-8";

    private static final int QRCODE_SIZE = 300;

    private static final String SUFFIX = "jpg";

    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;


//    public static void main(String[] args) throws Exception {
//        // 生成 二维码
//        Map<EncodeHintType, Object> hints = new HashMap<>();
//        //编码
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        //边距
//        hints.put(EncodeHintType.MARGIN, 2);
//        // 创建二维码写入器
//        QRCodeWriter writer = new QRCodeWriter();
//        int width = 300;
//        int height = 300;
//        String format = "png";
//        BitMatrix bitMatrix = writer.encode("memberCode", BarcodeFormat.QR_CODE, width, height, hints);
//        // 创建BufferedImage对象并绘制二维码
//        BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
//            }
//        }
//        // 保存二维码到文件
////        ImageIO.write(qrCodeImage, format, new File(filePath));
//
//        // 读取 背景图
//        BufferedImage banner = ImageIO.read(new File("F:\\Img\\store\\banner.png"));
//        //调整大小
////        Image resultingImage = qrCodeImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_AREA_AVERAGING);
////        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
////        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
//
//        if(banner == null || qrCodeImage == null ) {
//            System.out.println("生成图片失败");
//        }
//        // 往背景图写入二维码
//        Graphics2D g = banner.createGraphics();
//        int x = 345;    //x轴偏移
//        int y = 540 ;   //y轴偏移
//        g.drawImage(qrCodeImage, x, y, qrCodeImage.getWidth(), qrCodeImage.getHeight(), null);
//        g.dispose();
//
//    }

    /**
     *
     * @param content
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return image;
    }


    /**
     * 写到外置图片
     * @param banner
     * @param qrCodeImage
     * @param needCompress
     * @return
     * @throws Exception
     */
    private static BufferedImage bannerImage(BufferedImage banner, BufferedImage qrCodeImage, boolean needCompress) throws Exception{
        if (banner == null || qrCodeImage == null) {
            return qrCodeImage;
        }

        // 往背景图写入二维码
        Graphics2D g = banner.createGraphics();
        int x = 345;    //x轴偏移
        int y = 540 ;   //y轴偏移
        g.drawImage(qrCodeImage, x, y, qrCodeImage.getWidth(), qrCodeImage.getHeight(), null);
        g.dispose();

        return banner;
    }


    /**
     * 插入LOGO
     *
     * @param source
     *            二维码图片
     * @param srcImage
     *            LOGO图片
     * @param needCompress
     *            是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, Image srcImage, boolean needCompress) throws Exception {
        if(srcImage == null){
            throw new ToolsException("获取logo资源为空");
        }
        int width = srcImage.getWidth(null);
        int height = srcImage.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = srcImage.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            srcImage = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(srcImage, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 读取图片资源
     * @param file
     * @return
     * @throws IOException
     */
    private static Image readImage(File file) throws IOException {
        if (!file.exists()) {
            throw new ToolsException("文件不存在");
        }
        Image src = ImageIO.read(file);
        return src;
    }

    /**
     * 创建普通 二维码 文件地址
     * @param content
     * @param destPath
     * @throws Exception
     */
    public static void encode(String content,String destPath) throws Exception {
        //创建文件夹
        mkdirs(destPath);
        // 二维码名称
        String file = new Random().nextInt(99999999)+".jpg";
        try(FileOutputStream outputStream = new FileOutputStream(destPath.concat(File.separator).concat(file))) {
            QrCodeUtil.encode(content, outputStream);
        }
    }

    /**
     * 创建普通 二维码 文件
     * @param content
     * @param qrCodeFile
     * @throws Exception
     */
    public static void encode(String content,File qrCodeFile) throws Exception {
        // 写出二维码
        try(FileOutputStream outputStream = new FileOutputStream(qrCodeFile)) {
            QrCodeUtil.encode(content, outputStream);
        }
    }


    /**
     * 创建普通 二维码  流
     * @param content
     * @param output
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        // 绘画二维码
        BufferedImage image = QrCodeUtil.createImage(content);
        // 写出二维码
        ImageIO.write(image, SUFFIX, output);
    }

    /**
     * 创建 内嵌logo二维码 不压缩logo
     * @param content
     * @param imgPath
     * @param destPath
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, String imgPath, String destPath) throws Exception {
        if(imgPath == null || Objects.equals(imgPath, "")){
            throw new ToolsException("文件地址为空");
        }
        // 写出 二维码
        QrCodeUtil.encodeInnerLogo(content, new File(imgPath), destPath);
    }

    /**
     * 创建 内嵌logo二维码 不压缩logo
     * @param content
     * @param imgPath
     * @param destFile
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, String imgPath, File destFile) throws Exception {
        if(imgPath == null || Objects.equals(imgPath, "")){
            throw new ToolsException("文件地址为空");
        }
        // 写出 二维码
        QrCodeUtil.encodeInnerLogo(content, new File(imgPath), destFile);
    }

    /**
     * 创建 内嵌logo二维码 不压缩logo
     * @param content
     * @param imgPath
     * @param destOutPut
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, String imgPath, OutputStream destOutPut) throws Exception {
        if(imgPath == null || Objects.equals(imgPath, "")){
            throw new ToolsException("文件地址为空");
        }
        // 写出 二维码
        QrCodeUtil.encodeInnerLogo(content, new File(imgPath), destOutPut);
    }

    /**
     * 创建 内嵌logo二维码 不压缩logo
     * @param content
     * @param imgPath
     * @param destPath
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, File imgPath, String destPath) throws Exception {
        // 读取logo文件 转换image
        Image image = QrCodeUtil.readImage(imgPath);
        // 写出 二维码
        QrCodeUtil.encodeInnerLogo(content, image, destPath, Boolean.FALSE);
    }


    /**
     * 创建 内嵌logo二维码 不压缩logo
     * @param content
     * @param imgPath
     * @param destFile
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, File imgPath, File destFile) throws Exception {
        // 读取logo文件 转换image
        Image image = QrCodeUtil.readImage(imgPath);
        // 写出 二维码
        QrCodeUtil.encodeInnerLogo(content, image, destFile, Boolean.FALSE);
    }

    /**
     * 创建 内嵌logo二维码 不压缩logo
     * @param content
     * @param imgPath
     * @param destOutPut
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, File imgPath, OutputStream destOutPut) throws Exception {
        // 读取logo文件 转换image
        Image image = QrCodeUtil.readImage(imgPath);
        // 写出 二维码
        QrCodeUtil.encodeInnerLogo(content, image, destOutPut, Boolean.FALSE);
    }


    /**
     * 创建 内嵌logo 二维码
     * @param content
     * @param imgPath
     * @param destPath
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, Image imgPath, String destPath, boolean needCompress) throws Exception {
        if(destPath == null || Objects.equals(destPath, "")){
            throw new ToolsException("二维码存放地址为空");
        }
        //创建文件夹
        mkdirs(destPath);
        // 二维码名称
        String file = new Random().nextInt(99999999)+".jpg";
        // 写出二维码
        QrCodeUtil.encodeInnerLogo(content, imgPath, new File(destPath.concat(File.separator).concat(file)), needCompress);
    }

    /**
     * 创建 内嵌logo 二维码
     * @param content
     * @param imgPath
     * @param destFile
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, Image imgPath, File destFile, boolean needCompress) throws Exception {
        if(destFile == null){
            throw new ToolsException("二维码文件为空");
        }
        try(FileOutputStream outputStream = new FileOutputStream(destFile)) {
            QrCodeUtil.encodeInnerLogo(content, imgPath, outputStream, needCompress);
        }
    }

    /**
     * 创建 内嵌logo 二维码
     * @param content
     * @param imgPath
     * @param destOutPut
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    public static void encodeInnerLogo(String content, Image imgPath, OutputStream destOutPut, boolean needCompress) throws Exception {
        if(imgPath == null || destOutPut == null){
            throw new ToolsException("二维码或资源为空");
        }
        // 创建二维码
        BufferedImage image = QrCodeUtil.createImage(content);
        // 内嵌logo
        QrCodeUtil.insertImage(image, imgPath, needCompress);
        // 写出二维码
        ImageIO.write(image, SUFFIX, destOutPut);
    }


    /**
     * 创建 外置背景 二维码
     * @param content
     * @param bannerPath
     * @param destPath
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
    public static void encodeBannerImage(String content, String bannerPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QrCodeUtil.createImage(content);
        // 读取 背景图
        if(bannerPath != null && !Objects.equals(bannerPath, "")){
            BufferedImage banner = ImageIO.read(new File(bannerPath));
            // 内嵌logo
            QrCodeUtil.bannerImage(image, banner, needCompress);
        }

        //创建文件夹
        mkdirs(destPath);
        // 二维码名称
        String file = new Random().nextInt(99999999)+".jpg";
        // 写出二维码
        ImageIO.write(image, SUFFIX, new File(destPath.concat(File.separator).concat(file)));
    }


    /**
     * 创建 外置背景 二维码
     * @param content
     * @param bannerPath
     * @param destPath
     * @param needCompress 是否压缩logo
     * @throws Exception
     */
//    public static void encodeBannerImage(String content, File bannerFile, String destPath, boolean needCompress) throws Exception {
//        BufferedImage image = QrCodeUtil.createImage(content);
//        // 读取 背景图
//        if(bannerPath != null && !Objects.equals(bannerPath, "")){
//            BufferedImage banner = ImageIO.read(new File(bannerPath));
//            // 内嵌logo
//            QrCodeUtil.bannerImage(image, banner, needCompress);
//        }
//
//        //创建文件夹
//        mkdirs(destPath);
//        // 二维码名称
//        String file = new Random().nextInt(99999999)+".jpg";
//        // 写出二维码
//        ImageIO.write(image, SUFFIX, new File(destPath.concat(File.separator).concat(file)));
//    }



    /**
     * 创建文件夹
     * @param destPath
     */
    public static void mkdirs(String destPath) {
        File file =new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
        }
    }

    /**
     * 解析二维码
     * @param file 文件
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QrCodeUtil.decode(new File(path));
    }

    static class BufferedImageLuminanceSource extends LuminanceSource{
        private final BufferedImage image;
        private final int left;
        private final int top;

        public BufferedImageLuminanceSource(BufferedImage image) {
            this(image, 0, 0, image.getWidth(), image.getHeight());
        }

        public BufferedImageLuminanceSource(BufferedImage image, int left,
                                            int top, int width, int height) {
            super(width, height);

            int sourceWidth = image.getWidth();
            int sourceHeight = image.getHeight();
            if (left + width > sourceWidth || top + height > sourceHeight) {
                throw new IllegalArgumentException(
                        "Crop rectangle does not fit within image data.");
            }

            for (int y = top; y < top + height; y++) {
                for (int x = left; x < left + width; x++) {
                    if ((image.getRGB(x, y) & 0xFF000000) == 0) {
                        image.setRGB(x, y, 0xFFFFFFFF); // = white
                    }
                }
            }

            this.image = new BufferedImage(sourceWidth, sourceHeight,
                    BufferedImage.TYPE_BYTE_GRAY);
            this.image.getGraphics().drawImage(image, 0, 0, null);
            this.left = left;
            this.top = top;
        }


        public byte[] getRow(int y, byte[] row) {
            if (y < 0 || y >= getHeight()) {
                throw new IllegalArgumentException(
                        "Requested row is outside the image: " + y);
            }
            int width = getWidth();
            if (row == null || row.length < width) {
                row = new byte[width];
            }
            image.getRaster().getDataElements(left, top + y, width, 1, row);
            return row;
        }


        public byte[] getMatrix() {
            int width = getWidth();
            int height = getHeight();
            int area = width * height;
            byte[] matrix = new byte[area];
            image.getRaster().getDataElements(left, top, width, height, matrix);
            return matrix;
        }


        public boolean isCropSupported() {
            return true;
        }


        public LuminanceSource crop(int left, int top, int width, int height) {
            return new BufferedImageLuminanceSource(image, this.left + left,
                    this.top + top, width, height);
        }


        public boolean isRotateSupported() {
            return true;
        }


        public LuminanceSource rotateCounterClockwise() {
            int sourceWidth = image.getWidth();
            int sourceHeight = image.getHeight();
            AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0,
                    0.0, 0.0, sourceWidth);
            BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = rotatedImage.createGraphics();
            g.drawImage(image, transform, null);
            g.dispose();
            int width = getWidth();
            return new BufferedImageLuminanceSource(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
        }
    }

    public static void main(String[] args) {

//        1、创建源
        File dest = new File("D:\\asset\\asset_3512c29c3e12e1789bc0a73d6f6c16c6");

        //2、选择流
        //SequenceInputStream 表示其他输入流的逻辑串联。它从输入流的有序集合开始，
        //并从第一个输入流开始读取，直到到达文件末尾，接着从第二个输入流读取，依次类推，
        //直到到达包含的最后一个输入流的文件末尾为止。

        SequenceInputStream sis = null;//输入流
        BufferedOutputStream bos = null;//输出源
        try {
            //创建一个容器
            Vector<InputStream> vi = new Vector<InputStream>();
            File[] files = dest.listFiles();
            assert files != null;
            List<File> collect = Arrays.stream(files).sorted(Comparator.comparing(item -> Integer.valueOf(item.getName()))).collect(Collectors.toList());
            for (File file : collect) {
                vi.add(new BufferedInputStream(new FileInputStream(file)));
            }
            bos = new BufferedOutputStream(new FileOutputStream(new File("D:\\asset\\test1.zip"),true));//表示追加
            sis = new SequenceInputStream(vi.elements());

            //缓冲区
            byte[] flush = new byte[8192];
            //接收长度
            int len = 0;
            while(-1 !=(len = sis.read(flush))) {
                //打印到控制台
                bos.write(flush,0,len);
            }
            bos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(sis != null){
                try {
                    sis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
