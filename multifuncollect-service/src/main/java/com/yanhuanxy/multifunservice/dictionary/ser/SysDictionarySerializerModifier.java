package com.yanhuanxy.multifunservice.dictionary.ser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.yanhuanxy.multifundomain.annotation.DictionaryCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用于解析 DictionaryCode 注解
 * 给Bean实体添加字典编码对应的字典名称 字段
 * @author yym
 * @date 2023/3/14
 */
public class SysDictionarySerializerModifier extends BeanSerializerModifier {
    private static final Logger logger = LoggerFactory.getLogger(SysDictionarySerializerModifier.class);

    private final SysDictionaryContextualSerializer sysDictionarySerializer;

    public SysDictionarySerializerModifier(SysDictionaryContextualSerializer sysDictionarySerializer) {
        this.sysDictionarySerializer = sysDictionarySerializer;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

        List<BeanPropertyWriter> dictionaryCodeWriter = beanProperties.stream().filter(item->
                Objects.nonNull(item.getAnnotation(DictionaryCode.class))).map(beanProperty->{
            String fieldName = beanProperty.getName();
            DictionaryCode dictionaryCode = beanProperty.getAnnotation(DictionaryCode.class);
            fieldName = StringUtils.isNotBlank(dictionaryCode.name()) ? dictionaryCode.name() : fieldName.concat("Name");

            DefaultBeanPropertyWriter beanPropertyWriter = new DefaultBeanPropertyWriter(beanProperty, PropertyName.construct(fieldName));
            beanPropertyWriter.assignSerializer(sysDictionarySerializer);
            return beanPropertyWriter;
        }).collect(Collectors.toList());

        beanProperties.addAll(dictionaryCodeWriter);
        return beanProperties;
    }

    /**
     * DefaultBeanPropertyWriter
     */
    static class DefaultBeanPropertyWriter extends BeanPropertyWriter{

        public DefaultBeanPropertyWriter(BeanPropertyWriter base, PropertyName name) {
            super(base, name);
            _serializer = null;
        }

    }
}
