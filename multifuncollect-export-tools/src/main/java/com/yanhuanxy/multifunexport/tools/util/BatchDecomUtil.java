package com.yanhuanxy.multifunexport.tools.util;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件解压工具
 * @author yym
 * @date 20190916 18:07
 */
public class BatchDecomUtil {
    private static final Logger logger = LoggerFactory.getLogger(BatchDecomUtil.class);

    /**
     * 文件类型
     * @author yym
     * @date 20190916 18:07
     */
    public enum BatchCompressorEnums {
        //未知类型
        UNKNOWN("未知"),
        //压缩文件
        ZIP("zip"), RAR("rar"), SEVERNZ("7z"), TAR("tar"), GZ("gzip"),
        TAR_GZ("tar.gz"), BZ2("bzip2"), TAR_BZ2("tar.bz2"),
        // 位图文件
        BMP("bmp"),PNG("png"), JPG("jpg"), JPEG("jpeg"),
        // 矢量图文件
        SVG("svg"),
        // 影音文件
        AVI("avi"), MP4("mp4"), MP3("mp3"), AAR("aar"), OGG("ogg"), WAV("wav"), WAVE("wave");

        public final String value;

        private BatchCompressorEnums(String value) {
            this.value = value;
        }
    }

    /**缓冲字节*/
    public static final int BUFFER = 1024;

    public static void compress(File srcFile, File filepath, BatchCompressorEnums filetype, boolean delete) throws IOException{
        if(srcFile == null || filepath == null){
            return;
        }
        String parentPath = filepath.getParent();
        String fileName = filepath.getName();
        String lastfilename;
        if(filepath.isDirectory()){
            lastfilename = fileName;
        }else{
            lastfilename = fileName.substring(0,fileName.lastIndexOf("."));
        }
        compress(srcFile, parentPath, lastfilename, filetype, delete);
    }

    public static void compress(File srcFile, String filesavepath, BatchCompressorEnums filetype, boolean delete) throws IOException{
        if(srcFile == null){
            return;
        }
        String lastfilename;
        String filename = srcFile.getName();
        if(srcFile.isDirectory()){
            lastfilename = filename;
        }else{
            lastfilename = filename.substring(0,filename.lastIndexOf("."));
        }
        fileProber(new File(filesavepath));
        compress(srcFile, filesavepath, lastfilename, filetype, delete);
    }

    /**
     *
     * @param srcFile
     * @param filesavepath
     * @param filetype
     * @throws IOException
     */
    private static void compress(File srcFile, String filesavepath, String lastfilename, BatchCompressorEnums filetype, boolean delete) throws IOException{
        String savefilename;
        FileOutputStream fos = null;
        try {
            switch (filetype){
                case TAR:
                    savefilename = filesavepath + File.separator + lastfilename +".tar";
                    fos = new FileOutputStream(savefilename);
                    try(TarArchiveOutputStream taros = new TarArchiveOutputStream(fos)) {
                        setTarCompressfile(srcFile,taros,filesavepath);

                        taros.finish();
                        taros.flush();
                    }
                    break;
                case ZIP:
                    savefilename = filesavepath + File.separator + lastfilename +".zip";
                    fos = new FileOutputStream(savefilename);

                    try(ZipArchiveOutputStream zipos = new ZipArchiveOutputStream(fos)) {
                        setZipCompressfile(srcFile,zipos,filesavepath);

                        zipos.finish();
                        zipos.flush();
                    }
                    break;
                case GZ:
                    savefilename = filesavepath + File.separator + lastfilename +".gz";
                    fos = new FileOutputStream(savefilename);
                    try(GzipCompressorOutputStream gzipos = new GzipCompressorOutputStream(fos)){
                        setGZipCompressfile(srcFile,gzipos);

                        gzipos.finish();
                        gzipos.flush();
                    }
                    break;
                case BZ2:
                    savefilename = filesavepath + File.separator + lastfilename +".bz2";
                    fos = new FileOutputStream(savefilename);

                    try(BZip2CompressorOutputStream bzipos = new BZip2CompressorOutputStream(fos)) {
                        setGZipCompressfile(srcFile,bzipos);

                        bzipos.finish();
                        bzipos.flush();
                    }
                    break;
                case SEVERNZ:
                    savefilename = filesavepath + File.separator + lastfilename +".7z";
                    File savefile = new File(savefilename);
                    try(SevenZOutputFile sevenzosfile = new SevenZOutputFile(savefile)) {
                        setServenZCompressfile(srcFile,sevenzosfile,filesavepath);

                        sevenzosfile.finish();
                    }
                    break;
                default:
                    break;
            }
            if(fos != null){
                fos.flush();
            }
        }finally {
            if(fos != null){
                fos.close();
            }
            if (delete) {
                if(!srcFile.delete()){
                    logger.warn("原始文件删除失败!");
                }
            }
        }
    }

    /**
     * ar 压缩
     * @param srcFile
     * @param aros
     * @param filesavepath
     * @throws IOException
     */
    private static void setARCompressfile(File srcFile,ArArchiveOutputStream aros,String filesavepath) throws IOException{
        String path = srcFile.getPath();
        String lastpath = path.substring(filesavepath.length());
        if (srcFile.isDirectory()) {
            ArchiveEntry arentry = aros.createArchiveEntry(srcFile,lastpath);
            aros.putArchiveEntry(arentry);
            File[] files= srcFile.listFiles();
            if(files != null){
                for (File file : files) {
                    setARCompressfile(file, aros, filesavepath);
                }
            }
        }else{
            try (FileInputStream fis = new FileInputStream(srcFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
                ArArchiveEntry entry = (ArArchiveEntry) aros.createArchiveEntry(srcFile, lastpath);
                aros.putArchiveEntry(entry);
                int count;
                byte[] data = new byte[BUFFER];
                while ((count = bis.read(data, 0, BUFFER)) != -1) {
                    aros.write(data, 0, count);
                }
                aros.closeArchiveEntry();
            }
        }
    }

    /**
     *  tar 压缩
     * @param srcFile
     * @param taros
     * @param filesavepath
     * @throws IOException
     */
    private static void setTarCompressfile(File srcFile,TarArchiveOutputStream taros,String filesavepath) throws IOException{
        String path = srcFile.getPath();
        String lastpath = path.substring(filesavepath.length());
        if (srcFile.isDirectory()) {
            ArchiveEntry arentry = taros.createArchiveEntry(srcFile,lastpath);
            taros.putArchiveEntry(arentry);
            File[] files= srcFile.listFiles();
            if(files != null){
                for (File file : files) {
                    setTarCompressfile(file, taros, filesavepath);
                }
            }
        }else{
            try (FileInputStream fis = new FileInputStream(srcFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
                TarArchiveEntry entry = (TarArchiveEntry) taros.createArchiveEntry(srcFile, lastpath);
                taros.putArchiveEntry(entry);
                int count;
                byte[] data = new byte[BUFFER];
                while ((count = bis.read(data,0, BUFFER)) != -1) {
                    taros.write(data, 0, count);
                }
                taros.closeArchiveEntry();
            }
        }
    }

    /**
     * zip 压缩
     * @param srcFile
     * @param zipos
     * @param filesavepath
     * @throws IOException
     */
    private static void setZipCompressfile(File srcFile,ZipArchiveOutputStream zipos,String filesavepath) throws IOException{
        String path = srcFile.getPath();
        String lastpath = path.substring(filesavepath.length());
        if (srcFile.isDirectory()) {
            ZipArchiveEntry arentry = (ZipArchiveEntry) zipos.createArchiveEntry(srcFile,lastpath);
            zipos.putArchiveEntry(arentry);
            File[] files= srcFile.listFiles();
            if(files != null){
                for (File file : files) {
                    setZipCompressfile(file, zipos, filesavepath);
                }
            }
        }else{
            try (FileInputStream fis = new FileInputStream(srcFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
                ZipArchiveEntry entry = (ZipArchiveEntry) zipos.createArchiveEntry(srcFile, lastpath);
                zipos.putArchiveEntry(entry);
                int count;
                byte[] data = new byte[BUFFER];
                while ((count = bis.read(data,0,BUFFER)) != -1) {
                    zipos.write(data, 0, count);
                }
                zipos.closeArchiveEntry();
            }
        }
    }

    /**
     *  gzip 压缩
     * @param srcFile
     * @param gzipos
     * @throws IOException
     */
    private static void setGZipCompressfile(File srcFile,OutputStream gzipos) throws IOException{
        try (FileInputStream fis = new FileInputStream(srcFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data,0,BUFFER)) != -1) {
                gzipos.write(data, 0, count);
            }
        }
    }

    /**
     *  7z 压缩
     * @param srcFile
     * @param sevenzosfile
     * @param filesavepath
     * @throws IOException
     */
    private static void setServenZCompressfile(File srcFile,SevenZOutputFile sevenzosfile,String filesavepath)throws IOException {
        String path = srcFile.getPath();
        String lastpath = path.substring(filesavepath.length());
        if (srcFile.isDirectory()) {
            SevenZArchiveEntry entry = sevenzosfile.createArchiveEntry(srcFile, lastpath);
            sevenzosfile.putArchiveEntry(entry);
            File[] files= srcFile.listFiles();
            if(files != null){
                for (File file : files) {
                    setServenZCompressfile(file, sevenzosfile, filesavepath);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(srcFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
                SevenZArchiveEntry entry = sevenzosfile.createArchiveEntry(srcFile, lastpath);
                sevenzosfile.putArchiveEntry(entry);
                int count;
                byte[] data = new byte[BUFFER];
                while ((count = bis.read(data,0,BUFFER)) != -1) {
                    sevenzosfile.write(data, 0, count);
                }
                sevenzosfile.closeArchiveEntry();
            }
        }
    }

    /**
     * 文件解压缩
     * @param srcFile 文件
     * @param delete 是否删除源文件
     * @throws IOException
     */
    public static void deCompress(File srcFile,String filesavepath, boolean delete) throws IOException{
        BatchCompressorEnums filetype = null;
        String filename = srcFile.getName();
        String outcataloguename ="";

        filetype = getFileType(srcFile);
        if (filename.lastIndexOf(".tar")!=-1) {
            outcataloguename = filesavepath + File.separator + filename.replace(".tar","");
        } else if (filename.lastIndexOf(".zip")!=-1) {
            outcataloguename = filesavepath + File.separator + filename.replace(".zip","");
        } else if (filename.lastIndexOf(".gz")!=-1) {
            outcataloguename = filesavepath + File.separator + filename.replace(".gz","");
        } else if (filename.lastIndexOf(".bz2")!=-1) {
            outcataloguename = filesavepath + File.separator + filename.replace(".bz2","");
        } else if (filename.lastIndexOf(".7z")!=-1) {
            outcataloguename = filesavepath + File.separator + filename.replace(".bz2","");
        } else if(filename.lastIndexOf(".rar")!=-1){
            outcataloguename = filesavepath + File.separator + filename.replace(".rar","");
        }

        File destFile = new File(outcataloguename);
        fileProber(destFile);

        deCompress(srcFile, filetype, destFile);

        if (delete) {
            if(!srcFile.delete()){
                logger.warn("压缩包删除失败！");
            }
        }
    }

    /**
     * 解压缩
     * @param srcFile 输入文件
     * @throws IOException
     */
    public static void deCompress(File srcFile,BatchCompressorEnums filetype,File destFile) throws IOException {
        byte[] data = new byte[BUFFER];
        String dir;
        File dirFile;
        try (FileInputStream srcis = new FileInputStream(srcFile)) {
            switch (filetype) {
                case TAR:
                    String fileCode = fileCodeString(srcFile);
                    try (TarArchiveInputStream taris = new TarArchiveInputStream(srcis, fileCode)) {
                        TarArchiveEntry tarentry = null;
                        while ((tarentry = taris.getNextTarEntry()) != null) {
                            // 文件
                            dir = destFile.getPath() + File.separator + tarentry.getName();
                            dirFile = new File(dir);
                            fileProber(dirFile);
                            if (tarentry.isDirectory()) {
                                dirFile.mkdirs();
                            } else {
                                setBufferedOutputStream(taris, dirFile, data);
                            }
                        }
                    }
                    break;
                case ZIP:
                    String fileCodes = fileCodeString(srcFile);
                    try (ZipArchiveInputStream zipis = new ZipArchiveInputStream(srcis, fileCodes)) {
                        ZipArchiveEntry zipentry = null;
                        while ((zipentry = zipis.getNextZipEntry()) != null) {
                            // 文件
                            String tempFileName = zipentry.getName();
                            dir = destFile.getPath() + File.separator + tempFileName;
                            dirFile = new File(dir);
                            fileProber(dirFile);
                            if (zipentry.isDirectory()) {
                                dirFile.mkdirs();
                            } else {
//                            三个方法都可以
//                            try(OutputStream zipoutput = Files.newOutputStream(dirFile.toPath())){
//                                IOUtils.copy(zipis,zipoutput);
//                            }
                                IOUtils.copy(zipis, FileUtils.openOutputStream(dirFile));
//                            setBufferedOutputStream(zipis,dirFile,data);
                            }
                        }
                    }
                    break;
                case GZ:
                    try (GzipCompressorInputStream gzipis = new GzipCompressorInputStream(srcis)) {
                        dir = destFile.getPath() + File.separator + destFile.getName() + ".gz";
                        dirFile = new File(dir);
                        setBufferedOutputStream(gzipis, dirFile, data);
                    }
                    break;
                case BZ2:
                    try (BZip2CompressorInputStream bzipis = new BZip2CompressorInputStream(srcis)) {
                        dir = destFile.getPath() + File.separator + destFile.getName() + ".bz2";
                        dirFile = new File(dir);
                        setBufferedOutputStream(bzipis, dirFile, data);
                    }
                    break;
                case SEVERNZ:
                    try (SevenZFile sevenz = new SevenZFile(srcFile)) {
                        SevenZArchiveEntry sevenzEntry = null;
                        while ((sevenzEntry = sevenz.getNextEntry()) != null) {
                            // 文件
                            dir = destFile.getPath() + File.separator + sevenzEntry.getName();
                            dirFile = new File(dir);
                            fileProber(dirFile);
                            if (sevenzEntry.isDirectory()) {
                                dirFile.mkdirs();
                            } else {
                                try (SevenZOutputFile sevenZOutputFile = new SevenZOutputFile(dirFile)) {
                                    int count;
                                    while ((count = sevenz.read(data, 0, BUFFER)) != -1) {
                                        sevenZOutputFile.write(data, 0, count);
                                    }
                                }
                            }
                        }
                    }
                    break;
                case RAR:
                    try (Archive archive = new Archive(srcis)) {
                        FileHeader fileHeader = null;
                        while ((fileHeader = archive.nextFileHeader()) != null) {
                            // 文件
                            dir = destFile.getPath() + File.separator + fileHeader.getFileNameString();
                            dirFile = new File(dir);
                            fileProber(dirFile);
                            if (fileHeader.isDirectory()) {
                                dirFile.mkdirs();
                            } else {
                                FileOutputStream os = new FileOutputStream(dirFile);
                                archive.extractFile(fileHeader, os);
                                os.close();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (RarException e) {
            logger.error("rar格式压缩文件解压异常！", e);
        } catch (Exception e) {
            logger.error("压缩文件解压失败！", e);
        }
    }

    /**
     *
     * @param is
     * @param dirFile
     * @param data
     * @throws IOException
     */
    private static void setBufferedOutputStream(InputStream is,File dirFile,byte[] data) throws IOException{
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirFile))){
            int count;
            while ((count = is.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
        }
    }


    /**
     * 文件探针
     *
     * <pre>
     * 当父目录不存在时，创建目录！
     * </pre>
     *
     * @param dirFile
     */
    private static void fileProber(File dirFile) {

        File parentFile = dirFile.getParentFile();
        if (parentFile !=null && !parentFile.exists()) {
            // 递归寻找上级目录
            fileProber(parentFile);

            parentFile.mkdir();
        }

    }

    /**
     * 获取文件真实类型
     * @param file 要获取类型的文件。
     * @return 文件类型枚举。
     */
    private static BatchCompressorEnums getFileType(File file){
        FileInputStream inputStream =null;
        try{
            inputStream = new FileInputStream(file);
            byte[] head = new byte[4];
            if (-1 == inputStream.read(head)) {
                return BatchCompressorEnums.UNKNOWN;
            }
            int headHex = 0;
            for (byte b : head) {
                headHex <<= 8;
                headHex |= b;
            }
            return switch (headHex) {
                case 0x504B0304 -> BatchCompressorEnums.ZIP;
                case 0x776f7264 -> BatchCompressorEnums.TAR;
                case -0x51 -> BatchCompressorEnums.SEVERNZ;
                case 0x425a6839 -> BatchCompressorEnums.BZ2;
                case -0x74f7f8 -> BatchCompressorEnums.GZ;
                case 0x52617221 -> BatchCompressorEnums.RAR;
                default -> BatchCompressorEnums.UNKNOWN;
            };
        }catch (Exception e){
            logger.error("探嗅文件类型失败！", e);
        }finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("探嗅文件类型时文件关闭失败！", e);
            }
        }
        return BatchCompressorEnums.UNKNOWN;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 判断string 是否是乱码
     * @param strName
     * @return
     * @author yym
     * @date 20191218 10:55
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");//去重为空的情况
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    /**
     *判断字符是否是中文
     * @param c
     * @return
     * @author yym
     * @date 20191218 10:55
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件编码
     * @param file
     * @return
     * @throws Exception
     * @author yym
     * @date 20191218 10:55
     */
    public static String fileCodeString(File file) throws Exception {
        String code = null;
        try(BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file))){
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        }
        return code;
    }

    public static void main(String[] args) throws Exception {
//        File srcFile = new File("C:\\Users\\yuany\\Desktop\\20190911-101705\\20190911-101705.zip");
//        BatchCompressorType filetype = BatchCompressorType.SEVERNZ;
        String filesavepath = "C:\\Users\\yuany\\Desktop\\test";
        boolean delete = false;
//        compress(srcFile,filesavepath,filetype,delete);

        File file = new File("C:\\Users\\yuany\\Desktop\\20190911-101705.zip");
        Long starttime = System.currentTimeMillis();
        deCompress(file,filesavepath,delete);
        Long endtime = System.currentTimeMillis();
        System.out.println((endtime-starttime)/1000);
//        InputStreamReader isr = new InputStreamReader(
//                new FileInputStream(srcFile), "GBK");
//        BufferedReader br = new BufferedReader(isr);
//        String lineTxt = null;
//        int count = 0;
//        int oldlength = 0;
//
//        File outFile = new File("C:\\Users\\yuany\\Desktop\\20190911-10170511112.txt");
//        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
//        if(!outFile.exists()){
//            outFile.createNewFile();
//        }
//        while ((lineTxt = br.readLine()) != null) {
//            out.write(lineTxt+"\n");
//            out.flush();
//            count ++;
////            if(count >= 232 && count <= 239){
////                continue;
////            }
////            if(oldlength!=0 && oldlength != arrStrings.length){
////                System.out.println(arrStrings.length);
////                System.out.println(lineTxt);
////                break;
////            }
////            oldlength = arrStrings.length;
////            if(count == 233){
////                System.out.println(lineTxt);
////            }
//            if(count == 1000000){
////                String[] arrStrings = lineTxt.split("\t");
//                System.out.println(lineTxt);
//                break;
//            }
//        }
//        System.out.println(count);
//        //out.close();
//
//        br.close();
//        isr.close();


//        FileInputStream inputStream = null;
//        Scanner sc = null;
//        try {
//            Long starttime1 = System.currentTimeMillis();
//            inputStream = new FileInputStream(srcFile);
//            sc = new Scanner(inputStream, "utf-8");
//            count =0;
//            while (sc.hasNextLine()) {
//                count++;
//                String line = sc.nextLine();
//            }
//            System.out.println(count);
//            Long endtime2 = System.currentTimeMillis();
//            System.out.println((endtime2-starttime1)/1000);
//        }catch(IOException e){
//            e.printStackTrace();
//        }finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            if (sc != null) {
//                sc.close();
//            }
//        }
    }
}