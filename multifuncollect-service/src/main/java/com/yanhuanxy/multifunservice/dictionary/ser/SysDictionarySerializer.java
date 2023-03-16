package com.yanhuanxy.multifunservice.dictionary.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * 用于解析 DictionaryCode 注解
 * 给Bean实体添加字典编码对应的字典名称 赋值
 * @author yym
 * @date 2023/3/14
 */
public class SysDictionarySerializer extends JsonSerializer<Object> {
    private static final Logger logger = LoggerFactory.getLogger(SysDictionarySerializer.class);

    private final SysDictionaryHandle sysDictionaryHandle;

    private String dictCode;

    public SysDictionarySerializer(SysDictionaryHandle sysDictionaryHandle) {
        this.sysDictionaryHandle = sysDictionaryHandle;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try{
            String dictItemName = sysDictionaryHandle.findDictItemName(dictCode, Objects.isNull(value) ? "" : String.valueOf(value));
            jsonGenerator.writeString(dictItemName);
        }catch (Exception e){
            logger.warn("字典编码自动转换失败! dictCode: {} value: {}", dictCode, value, e);
            jsonGenerator.writeString(value.toString());
        }
    }
}
