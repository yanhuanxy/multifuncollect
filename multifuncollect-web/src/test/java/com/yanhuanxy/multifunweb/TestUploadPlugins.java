package com.yanhuanxy.multifunweb;

import com.yanhuanxy.multifunexport.fileservice.dto.enums.StorageTypeEnums;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.UploadFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.UploadException;
import com.yanhuanxy.multifunexport.fileservice.factory.OssFactory;
import com.yanhuanxy.multifunexport.fileservice.operation.upload.Uploader;
import jakarta.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@SpringBootTest
public class TestUploadPlugins {

    @Resource
    private OssFactory ossFactory;

    public void test(){
//        Copier copier = ossFactory.getCopier(StorageTypeEnums.MINIO);
//        try (InputStream fileInputStream = new FileInputStream(new File("C:\\Users\\yanhuan\\Desktop\\ip切换.bat"))){
//            CopyFile copyFile = new CopyFile();
//            copyFile.setExtendName("bat");
//            copier.copy(fileInputStream, copyFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Deleter deleter = ossFactory.getDeleter(StorageTypeEnums.MINIO);
//        DeleteFile deleteFile = new DeleteFile();
//        deleteFile.setFileUrl("bmdp-project-man/20230210/4a1f381d-8adb-4c37-8e21-2bc8f6dd7b5e.bat");
//        deleter.delete(deleteFile);

//          Downloader downloader = ossFactory.getDownloader(StorageTypeEnums.MINIO);
//          DownloadFile downloadFile = new DownloadFile();
//          downloadFile.setFileUrl("/cm_admin/一季度报表 季模板16753208801761675737145767.xlsx");
//
//          try(InputStream inputStream = downloader.getInputStream(downloadFile);
//              FileOutputStream fileOutputStream = new FileOutputStream("D:\\IDEAProjectGitlibWork\\demo\\target\\test-classes\\static\\bmdp-project-man\\一季度报表 季模板16753208801761675737145767.xlsx")) {
//              byte[] buf = new byte[8192];
//              int len;
//              while ((len = inputStream.read(buf, 0, 8192)) != -1){
//                  fileOutputStream.write(buf, 0, len);
//              }
//          } catch (FileNotFoundException e) {
//              e.printStackTrace();
//          } catch (IOException e) {
//              e.printStackTrace();
//          }

//        Reader copier = ossFactory.getReader(StorageTypeEnums.MINIO);
//        ReadFile readFile = new ReadFile();
////        readFile.setFileUrl("ip切换.txt");
//        readFile.setFileUrl("/market/员工部门联系方式1675750765386.xlsx");
//        String read = copier.read(readFile);
//        Downloader downloader = ossFactory.getDownloader(StorageTypeEnums.MINIO);
//        DownloadFile downloadFile = new DownloadFile();
//        downloadFile.setFileUrl("/market/员工部门联系方式1675750765386.xlsx");
//        ServletRequestAttributes servletRequestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletResponse response = servletRequestAttributes.getResponse();
//        downloader.download(response, downloadFile);
//        System.out.println(response.getBufferSize());
//        try {
//            ServletOutputStream outputStream = response.getOutputStream();
//            outputStream.println();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Writer downloader = ossFactory.getWriter(StorageTypeEnums.MINIO);
//        WriteFile downloadFile = new WriteFile();
//        downloadFile.setFileUrl("/bmdp-project-man/一季度报表 季模板.xlsx");
//
//        try(InputStream inputStream =new FileInputStream("D:\\IDEAProjectGitlibWork\\demo\\target\\test-classes\\static\\bmdp-project-man\\一季度报表 季模板16753208801761675737145767.xlsx")) {
//            downloader.write(inputStream, downloadFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Uploader uploader = ossFactory.getUploader(StorageTypeEnums.LOCAL);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
//        MockMultipartHttpServletRequest mockMultipartHttpServletRequest = new MockMultipartHttpServletRequest();
//        mockMultipartHttpServletRequest.addFile(fileToMulti(new File("C:\\Users\\yanhuan\\Desktop\\tmpfile.xlsx")));
        mockHttpServletRequest.addParameter("test1","file1");
        mockHttpServletRequest.setMethod("POST");
        mockHttpServletRequest.setContentType("multipart/form-data");
        try {
            byte[] bytes = org.apache.commons.compress.utils.IOUtils.toByteArray(new FileInputStream("C:\\Users\\yanhuan\\Desktop\\tmpfile.xlsx"));
            MockPart mockPart = new MockPart("test", "test.xlsx", bytes );
            mockHttpServletRequest.addPart(mockPart);

//            UploadFile uploadFile = new UploadFile();
//            uploadFile.setChunkNumber(1);
//            uploadFile.setChunkSize(0);
//            uploadFile.setTotalChunks(1);
//            writeByteDataToFile(bytes, new File("D:\\IDEAProjectGitlibWork\\demo\\target\\test-classes\\static\\temp\\bmdp-project-man\\20230216\\23feed02-e080-4e3a-953a-974fc2fdce7c.xlsx"), uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MockMultipartHttpServletRequest mockMultipartHttpServletRequest = new MockMultipartHttpServletRequest();
//        mockMultipartHttpServletRequest.addFile(fileToMulti(new File("C:\\Users\\yanhuan\\Desktop\\tmpfile.xlsx")));

        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest = new StandardMultipartHttpServletRequest(mockHttpServletRequest, false);
//        try {
//            Class<?> hander = StandardMultipartHttpServletRequest.class;
//            Method setMultipartFiles = hander.getDeclaredMethod("setMultipartFiles", MultiValueMap.class);
//            setMultipartFiles.setAccessible(true);
//            MultiValueMap<String, MultipartFile> tmp = new LinkedMultiValueMap<>();
//            tmp.add("tmpfile", fileToMulti(new File("C:\\Users\\yanhuan\\Desktop\\tmpfile.xlsx")));
//            setMultipartFiles.invoke(standardMultipartHttpServletRequest, tmp);
//            Field declaredField = hander.getDeclaredField("multipartFiles");
//            declaredField.setAccessible(true);
//            HashSet<String> filenames = new HashSet<>();
//            filenames.add("tmpfile");
//            declaredField.set(standardMultipartHttpServletRequest, filenames);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        uploader.upload(standardMultipartHttpServletRequest);
    }

    public MultipartFile fileToMulti(File file){
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            MultipartFile cMultiFile = new MockMultipartFile("file", file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, fileInputStream);
            return cMultiFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void writeByteDataToFile(byte[] fileData, File file, UploadFile uploadFile) {
        //第一步 打开将要写入的文件
        RandomAccessFile raf;
        try {
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }

            raf = new RandomAccessFile(file, "rw");
            //第二步 打开通道
            FileChannel fileChannel = raf.getChannel();
            //第三步 计算偏移量
            long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
            //第四步 获取分片数据
//            byte[] fileData = OssMultipartFile.getUploadBytes();
            //第五步 写入数据
            fileChannel.position(position);
            fileChannel.write(ByteBuffer.wrap(fileData));
            fileChannel.force(true);
            fileChannel.close();
            raf.close();
        } catch (IOException e) {
            throw new UploadException(e);
        }

    }
}
