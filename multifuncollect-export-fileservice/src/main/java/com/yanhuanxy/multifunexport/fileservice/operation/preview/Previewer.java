package com.yanhuanxy.multifunexport.fileservice.operation.preview;

import com.yanhuanxy.multifunexport.fileservice.dto.ThumbImage;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.PreviewFile;
import com.yanhuanxy.multifunexport.fileservice.util.CharsetUtils;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author yym
 * @version 1.0
 */
public abstract class Previewer {
    protected static final Logger logger = LoggerFactory.getLogger(Previewer.class);

    protected ThumbImage thumbImage;

    protected void setThumbImage(ThumbImage thumbImage) {
        this.thumbImage = thumbImage;
    }

    protected abstract InputStream getInputStream(PreviewFile previewFile);

    public void imageThumbnailPreview(HttpServletResponse httpServletResponse, PreviewFile previewFile) {
        String fileUrl = previewFile.getFileUrl();
        boolean isVideo = OssUtils.isVideoFile(FilenameUtils.getExtension(fileUrl));
        String thumbnailImgUrl = previewFile.getFileUrl();
        if (isVideo) {
            thumbnailImgUrl = fileUrl.replace("." + FilenameUtils.getExtension(fileUrl), ".jpg");
        }

        File cacheFile = OssUtils.getCacheFile(thumbnailImgUrl);

        if (cacheFile.exists()) {
            FileInputStream fis = null;
            OutputStream outputStream = null;
            try {
                fis = new FileInputStream(cacheFile);
                outputStream = httpServletResponse.getOutputStream();
                IOUtils.copy(fis, outputStream);
            } catch (IOException e) {
                logger.error("图片缩略图预览异常", e);
            } finally {
                IOUtils.closeQuietly(fis);
                IOUtils.closeQuietly(outputStream);
            }
        }
//        else {
//            InputStream inputstream = null;
//            OutputStream outputStream = null;
//            InputStream in = null;
//            try {
//                inputstream = getInputStream(previewFile);
//                if (inputstream != null) {
//                    outputStream = httpServletResponse.getOutputStream();
//                    int thumbImageWidth = thumbImage.getWidth();
//                    int thumbImageHeight = thumbImage.getHeight();
//                    int width = thumbImageWidth == 0 ? 150 : thumbImageWidth;
//                    int height = thumbImageHeight == 0 ? 150 : thumbImageHeight;
//                    String type = FIleTypeUtil.getType(getInputStream(previewFile));
//                    boolean isImageFile = OssUtils.isImageFile(type);
//                    if (isVideo) {
//                        in = VideoOperation.thumbnailsImage(inputstream, cacheFile, width, height);
//                    } else if (isImageFile) {
//                        in = ImageOperation.thumbnailsImage(inputstream, cacheFile, width, height);
//                    } else {
//                        in = inputstream;
//                    }
//                    IOUtils.copy(in, outputStream);
//                }
//            } catch (IOException e) {
//                logger.error("图片或视频缩略图预览异常",e);
//            } finally {
//                IOUtils.closeQuietly(in);
//                IOUtils.closeQuietly(inputstream);
//                IOUtils.closeQuietly(outputStream);
//            }
//        }
    }

    public void imageOriginalPreview(HttpServletResponse httpServletResponse, PreviewFile previewFile) {

        InputStream inputStream = null;

        OutputStream outputStream = null;

        try {
            inputStream = getInputStream(previewFile);
            outputStream = httpServletResponse.getOutputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            bytes = CharsetUtils.convertTxtCharsetToUTF8(bytes, FilenameUtils.getExtension(previewFile.getFileUrl()));
            outputStream.write(bytes);
        } catch (IOException e) {
            logger.error("图片预览异常",e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

}
