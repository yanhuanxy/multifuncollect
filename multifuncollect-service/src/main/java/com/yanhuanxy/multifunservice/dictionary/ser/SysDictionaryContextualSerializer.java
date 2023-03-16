package com.yanhuanxy.multifunservice.dictionary.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yanhuanxy.multifundomain.annotation.DictionaryCode;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryHandle;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 用于解析 DictionaryCode 注解
 * 给Bean实体添加字典编码对应的字典名称 赋值
 * @author yym
 * @date 2023/3/14
 */
public class SysDictionaryContextualSerializer extends JsonSerializer<Object> implements ContextualSerializer {
    private static final Logger logger = LoggerFactory.getLogger(SysDictionaryContextualSerializer.class);

    private final ThreadLocal<String> dictCodeThreadLocal = new ThreadLocal<>();

    private final SysDictionaryHandle sysDictionaryHandle;

    public SysDictionaryContextualSerializer(SysDictionaryHandle sysDictionaryHandle) {
        this.sysDictionaryHandle = sysDictionaryHandle;
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        String dictCode = dictCodeThreadLocal.get();
        try{
            String dictItemName = sysDictionaryHandle.findDictItemName(dictCode, Objects.isNull(value) ? "" : String.valueOf(value));
            jsonGenerator.writeString(dictItemName);
        }catch (Exception e){
            logger.warn("字典编码自动转换失败! dictCode: {} value: {}", dictCode, value, e);
        }finally {
            dictCodeThreadLocal.remove();
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if(beanProperty != null){
            DictionaryCode annotation = beanProperty.getAnnotation(DictionaryCode.class);
            String dictCode = annotation.dictCode();
            if(ObjectUtils.isEmpty(dictCode)){
                return serializerProvider.findValueSerializer(beanProperty.getType(),beanProperty);
            }
            dictCodeThreadLocal.set(dictCode);
            return this;
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
