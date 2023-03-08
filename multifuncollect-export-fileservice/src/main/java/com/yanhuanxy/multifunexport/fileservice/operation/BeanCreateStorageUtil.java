package com.yanhuanxy.multifunexport.fileservice.operation;

import com.yanhuanxy.multifunexport.fileservice.dto.enums.FileOperationType;
import com.yanhuanxy.multifunexport.fileservice.dto.enums.StorageTypeEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

public class BeanCreateStorageUtil<T> {
    private static final Logger logger = LoggerFactory.getLogger(BeanCreateStorageUtil.class);

    private final ApplicationContext context;

    public BeanCreateStorageUtil(ApplicationContext context) {
        this.context = context;
    }

    public T initCreateBeanByStorageType(StorageTypeEnums storageTypeEnums, FileOperationType fileOperationType){
        try {
            StringBuilder classNameBuf = new StringBuilder("com.kelven.oss.export.operation.");
            classNameBuf.append(fileOperationType.toString().toLowerCase()).append(".product.");
            if(Objects.isNull(storageTypeEnums)){
                storageTypeEnums = StorageTypeEnums.LOCAL;
            }
            classNameBuf.append(storageTypeEnums.getSuffix()).append(fileOperationType.getSuffix());
            Class<?> handler = Class.forName(classNameBuf.toString());
//                return (T) handler.newInstance();
            AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
            Object bean = autowireCapableBeanFactory.createBean(handler, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            return (T) bean;
        } catch (Exception e) {
            logger.error("策略模式创建实体类失败", e);
        }
        return null;
    }
}
