package com.yanhuanxy.multifuncommon.enums;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum FileTypeEnum {
    XLS("xls"),XLSX("xlsx"),CSV("csv"),ZIP("zip");

    private static final String SYMBOL = ".";

    private final String suffix;

    public String getSuffix() {
        return suffix;
    }

    FileTypeEnum(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 判断是否符合规则
     * @param fileName 文件名
     */
    public static boolean exist(String fileName){
        if(ObjectUtils.isEmpty(fileName)){
            return false;
        }
        String[] fileSuffixs = Arrays.stream(FileTypeEnum.values()).map(item-> SYMBOL + item.getSuffix()).toArray(String[]::new);
        return StringUtils.endsWithAny(fileName.toLowerCase(), fileSuffixs);
    }

    /**
     * 判断是否zip
     * @param fileName 文件名
     */
    public static boolean isZip(String fileName){
        if(ObjectUtils.isEmpty(fileName)){
            return false;
        }
        return StringUtils.endsWith(fileName.toLowerCase(), SYMBOL + ZIP.getSuffix());
    }

    public static String getFileSuffixType(String fileName){
        if(ObjectUtils.isEmpty(fileName)){
            return null;
        }
        for(FileTypeEnum fileTypeEnum : FileTypeEnum.values()){
            if(StringUtils.endsWith(fileName.toLowerCase(), SYMBOL + fileTypeEnum.getSuffix())){
                return fileTypeEnum.getSuffix();
            }
        }

        return "";
    }
}
