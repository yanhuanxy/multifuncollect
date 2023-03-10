package com.yanhuanxy.multifunexport.tools.domain.emuns.excel;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * excel类文件类型
 * @author yym
 * @date 20220831 18:07
 */
public enum ExcelTypeEnums {
    // excel类型
    EXCEL_2003L,EXCEL_2007U,CSV,OTHER;
    /**
     * excel 2003
     */
    private final static Pattern IS_EXCEL2003 = Pattern.compile("^.+\\.(?i)(xls)$");

    /**
     * excel 2007
     */
    private final static Pattern IS_EXCEL2007 = Pattern.compile("^.+\\.(?i)(xlsx)$");

    /**
     * excel csv
     */
    private final static Pattern IS_CSV = Pattern.compile("^.+\\.(?i)(csv)$");

    public static ExcelTypeEnums getExcelTypeEnum(String fileName){
        if(isExcel2003(fileName)){
            return EXCEL_2003L;
        }else if(isExcel2007(fileName)){
            return EXCEL_2007U;
        }else if(isExcelCsv(fileName)){
            return CSV;
        }else {
            return OTHER;
        }
    }

    private static boolean isExcel2003(String filePath) {
        return IS_EXCEL2003.matcher(filePath).find();
    }

    private static boolean isExcel2007(String filePath) {
        return IS_EXCEL2007.matcher(filePath).find();
    }

    private static boolean isExcelCsv(String filePath) {
        return IS_CSV.matcher(filePath).find();
    }

    /**
     * 检查文件是否存在
     *
     * @param exclFile  文件
     * @throws IOException
     */
    public static ExcelTypeEnums checkFile(File exclFile, String fileName) throws IOException {
        if (!exclFile.exists()) {
            throw new FileNotFoundException("文件不存在！");
        }
        // 校验excel 类型
        return checkExcelType(fileName);
    }

    /**
     * 检查文件是否存在
     *
     * @param file  文件
     * @throws IOException
     */
    public static ExcelTypeEnums checkFile(MultipartFile file, String fileName) throws IOException {
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        // 校验excel 类型
        return checkExcelType(fileName);
    }

    /**
     * 检查文件是否存在
     *
     * @param exclFile  文件
     * @throws IOException
     */
    public static ExcelTypeEnums checkFile(InputStream exclFile, String fileName) throws IOException {
        if (exclFile == null) {
            throw new FileNotFoundException("输入流为空！");
        }
        // 校验excel 类型
        return checkExcelType(fileName);
    }

    /**
     * 校验excel 类型
     * @throws IOException
     */
    private static ExcelTypeEnums checkExcelType(String fileName) throws IOException{
        // 设置类型
        ExcelTypeEnums excelType = getExcelTypeEnum(fileName);
        if (ExcelTypeEnums.OTHER.equals(excelType)) {
            throw new IOException(fileName + "不是excel文件");
        }
        return excelType;
    }
}
