//package com.yanhuanxy.multifunservice.ffmpegdemo;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.InputStream;
//import java.time.DayOfWeek;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class ImageWater {
//
//
//    /**
//     * 给图片添加水印（内部）
//     * @param inputStream 文件流
//     * @param watermarkName 水印名称
//     * @throws Exception
//     */
//    public BufferedImage doImageAddWatermark(InputStream inputStream, String watermarkName, String description, LocalDateTime localDateTime) throws Exception{
//        // 读取原始文件
//        BufferedImage sourceImage = ImageIO.read(inputStream);
//        // 计算边框尺寸
//        int width = sourceImage.getWidth();
//        int height = sourceImage.getHeight();
//        int borderWidth = Double.valueOf(width / 100.0).intValue();
//        int borderHeight = Double.valueOf(height / 100.0).intValue();
//        int fontMax, fontMin;
//        if(width > height){
//            fontMax = borderWidth;
//            fontMin = borderHeight;
//        }else{
//            fontMax = borderHeight;
//            fontMin = borderWidth;
//        }
//        // 构建新图片
//        BufferedImage changeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics = changeImage.createGraphics();
//        graphics.setColor(Color.WHITE);
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics.drawImage(sourceImage, 0, 0, width, height, null);
//        // 设置 时间
//        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
//        String weekDate = WEEK[dayOfWeek.getValue() - 1];
//        String dateFormat = localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
//        String[] dateFormats = dateFormat.split(" ");
//        String levelFirstTime = dateFormats[1];
//        String levelSecondDate = dateFormats[0].concat("星期".concat(weekDate));
//        // 加载时间字体
//        Font timeFont = FontManager.getFont("fonts/PingFang Bold.ttf", (fontMax * 2.5f * 1.33f));
//        timeFont.deriveFont(Font.BOLD);
//        graphics.setFont(timeFont);
//        graphics.setColor(Color.WHITE);
//        // 时间起始X坐标
//        FontMetrics timeFontMetrics = graphics.getFontMetrics();
//        int timeHeight = timeFontMetrics.getHeight();
////        int timeY = timeHeight + borderHeight;
//        int timeY = height - timeHeight - fontMax;
//        int marginWight = borderHeight * 3;
//        // 添加上蒙版
////        BufferedImage upImage = ImageManager.getImage("images/up.png");
////        graphics.drawImage(upImage, 0, 0, width, (timeY + timeHeight * 3), null);
//        // 添加 时间
//        graphics.drawString(levelFirstTime, marginWight, timeY);
//        // 加载日期字体
//        Font dateFont = FontManager.getFont("fonts/PingFang Bold.ttf", (fontMin * 2.2f * 1.33f));
//        dateFont.deriveFont(Font.BOLD);
//        graphics.setFont(dateFont);
//        graphics.setColor(Color.WHITE);
//        FontMetrics dateFontMetrics = graphics.getFontMetrics();
//        int dateHeight = dateFontMetrics.getHeight();
//        // 日期起始X坐标 Y坐标
//        int dateY = timeY + (int)(dateHeight * 1.2f);
//        graphics.drawString(levelSecondDate, marginWight, dateY);
//        // 添加小图标
//        BufferedImage juImage = ImageManager.getImage("images/juxing.png");
//        graphics.drawImage(juImage, (marginWight/2), (timeY- (int)(timeHeight * 0.5f)), (fontMin/3), (dateY - timeY + (int)(dateHeight * 0.8f)), null);
//
//
//        // 加载地址点字体
//        Font font = FontManager.getFont("fonts/PingFang Bold.ttf", (fontMax * 2.2f * 1.33f));
//        font.deriveFont(Font.BOLD);
//        graphics.setFont(font);
//        graphics.setColor(Color.WHITE);
//        // 文字起始X坐标 Y坐标
//        FontMetrics fontMetrics = graphics.getFontMetrics();
//        int textWidth = fontMetrics.stringWidth(watermarkName);
//        int textHeight = fontMetrics.getHeight();
//        int textX = width - textWidth - borderWidth * 2;
//        int textY = height - textHeight - fontMax;
//        // 添加下蒙版
//        BufferedImage downImage = ImageManager.getImage("images/down.png");
//        int downHeight = height - textY;
//        int downMargin = textHeight * 2;
//        graphics.drawImage(downImage, 0, (textY - downMargin), width, (downHeight + downMargin), null);
//        // 添加 地址点
//        graphics.drawString(watermarkName, textX, textY);
//        // 添加 地址点小图标
////        BufferedImage ganImage = ImageManager.getImage("images/address.png");
////        int ganWidth =  (int)(fontMax * 4f);
////        graphics.drawImage(ganImage, (textX - ganWidth - borderWidth), (textY - (int) (textHeight * 0.8f)), ganWidth, (int)(textHeight * 1.0f), null);
//        // 加载描述字体
//        if(ObjectUtils.isNotEmpty(description)){
//            Font fontDesc = FontManager.getFont("fonts/PingFang Bold.ttf", (fontMin * 2.3f * 1.33f));
//            fontDesc.deriveFont(Font.BOLD);
//            graphics.setFont(fontDesc);
//            graphics.setColor(Color.WHITE);
//            // 计算描述字体宽高
//            FontMetrics fontMetricsDesc = graphics.getFontMetrics();
//            int textDescWidth = fontMetricsDesc.stringWidth(description);
//            int textDescHeight = fontMetricsDesc.getHeight();
//            // 描述起始X坐标 Y坐标
//            int textDescX = width - textDescWidth - borderWidth * 2;
//            int textDescY = textY + (int) (textDescHeight * 1.2f);
//            graphics.drawString(description, textDescX, textDescY);
//            // 设置小图标
//            int infoWidth =  (int)(fontMin * 4f);
//            BufferedImage infoImage = ImageManager.getImage("images/info.png");
//            graphics.drawImage(infoImage, (textDescX - infoWidth - borderWidth), (textDescY - (int) (textDescHeight * 0.8f)) , infoWidth, (int)(textDescHeight * 1.0f), null);
//        }
//        // 关闭画板
//        graphics.dispose();
//
//        return changeImage;
//    }
//
//}


//public static void main(String[] args) {
//        try(FileReader fileInputStream = new FileReader(new File("C:\\Users\\yanhuan\\Desktop\\asset\\warn.log"))){
//        BufferedReader bufferedReader = new BufferedReader(fileInputStream);
//        String length;
//        List<String> result = new ArrayList<>();
//        while ((length = bufferedReader.readLine()) !=null) {
//        boolean contains = length.contains("tmpUrl->");
//        if(contains){
//        String[] split = length.split("tmpUrl->");
//        result.add(split[1]);
//        }
//        }
//        bufferedReader.close();
//
//        List<String> collect = result.stream().distinct().collect(Collectors.toList());
//
//        MinioConfig minio = new MinioConfig();
//        minio.setBucketName("bmdp-project-man41b28d303719dc37b9cc97a7c638d9f5");
//        minio.setAccessKey("qiyunadmin");
//        minio.setEndpoint("https://asset.kelven.cn:9300");
//        minio.setSecretKey("qiyun@2023");
//
//        MinioClient minioClient = MinioClient.builder().endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
//        for (String s : collect) {
//        try {
//        String replace = s.replace("\\\\", "/");
//        String url = replace.replace(minio.getEndpoint().concat("/").concat(minio.getBucketName()).concat("/"), "");
//        String lastUrl = url.replace("asset/", minio.getBucketName()+"/");
//        InputStream object = minioClient.getObject(GetObjectArgs.builder().bucket(minio.getBucketName()).object(lastUrl).build());
//        if(object != null){
//        object.close();
//        }
//        }catch (Exception e){
//        System.out.println(s);
//        }
//        }
//
//        }catch (Exception e){
//        e.printStackTrace();
//        }
//
//        }


//1、创建源
//        File dest = new File("C:\\Users\\yanhuan\\Downloads\\asset\\e68aa5e5918ae4b88be8bdbd2e7a6970");
//
//        //2、选择流
//        //SequenceInputStream 表示其他输入流的逻辑串联。它从输入流的有序集合开始，
//        //并从第一个输入流开始读取，直到到达文件末尾，接着从第二个输入流读取，依次类推，
//        //直到到达包含的最后一个输入流的文件末尾为止。
//
//        SequenceInputStream sis = null;//输入流
//        BufferedOutputStream bos = null;//输出源
//        try {
//            //创建一个容器
//            Vector<InputStream> vi = new Vector<InputStream>();
//            File[] files = dest.listFiles();
//            assert files != null;
//            List<File> collect = Arrays.stream(files).sorted(Comparator.comparing(item -> Integer.valueOf(item.getName()))).collect(Collectors.toList());
//            for (File file : collect) {
//                vi.add(new BufferedInputStream(new FileInputStream(file)));
//            }
//            bos = new BufferedOutputStream(new FileOutputStream("C:\\Users\\yanhuan\\Downloads\\asset\\报告下载1.zip",true));//表示追加
//            sis = new SequenceInputStream(vi.elements());
//
//            //缓冲区
//            byte[] flush = new byte[8192];
//            //接收长度
//            int len = 0;
//            while(-1 !=(len = sis.read(flush))) {
//                //打印到控制台
//                bos.write(flush,0,len);
//            }
//            bos.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if(bos != null){
//                bos.close();
//            }
//            if(sis != null){
//                sis.close();
//            }
//        }