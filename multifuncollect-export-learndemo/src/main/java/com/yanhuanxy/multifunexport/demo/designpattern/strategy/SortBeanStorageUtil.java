package com.yanhuanxy.multifunexport.demo.designpattern.strategy;

import com.yanhuanxy.multifunexport.demo.designpattern.emuns.SortDataTypeEnum;
import com.yanhuanxy.multifunexport.demo.designpattern.emuns.SortStorageEnum;
import com.yanhuanxy.multifunexport.demo.designpattern.emuns.StoragePackageEnum;

import java.util.Optional;
import java.util.logging.Logger;

public class SortBeanStorageUtil<T> {
    private static final Logger logger = Logger.getLogger(SortBeanStorageUtil.class.getName());

    public T initCreateBeanByStorageType(StoragePackageEnum storagePackageEnum, SortStorageEnum storageTypeEnums, SortDataTypeEnum sortDataTypeEnum){
        try {
            StringBuilder classNameBuf = new StringBuilder("com.yanhuanxy.multifunexport.demo.designpattern.strategy.");

            Optional.ofNullable(storageTypeEnums).orElseThrow(NullPointerException::new);
            Optional.ofNullable(sortDataTypeEnum).orElseThrow(NullPointerException::new);

            classNameBuf.append(storagePackageEnum.getCode().toLowerCase()).append(".");
            classNameBuf.append(storageTypeEnums.getCode().toLowerCase()).append(".");
            classNameBuf.append(storageTypeEnums.getCode()).append("Sort").append(sortDataTypeEnum.getCode());

            Class<?> handler = Class.forName(classNameBuf.toString());
            return (T) handler.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.info(String.format("策略模式创建实体类失败-> %s" , e.getMessage()));
        }
        return null;
    }

    /**
     * 默认排序 调用
     * @param storageTypeEnums
     * @param sortDataTypeEnum
     * @return
     */
    public T initCreateBeanByStorageType(SortStorageEnum storageTypeEnums, SortDataTypeEnum sortDataTypeEnum){
        return initCreateBeanByStorageType(StoragePackageEnum.SORT, storageTypeEnums, sortDataTypeEnum);
    }
}
