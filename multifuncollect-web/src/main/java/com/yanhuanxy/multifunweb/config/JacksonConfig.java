package com.yanhuanxy.multifunweb.config;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.yanhuanxy.multifundomain.annotation.DictionaryCode;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryHandle;
import com.yanhuanxy.multifunservice.dictionary.ser.SysDictionaryContextualSerializer;
import com.yanhuanxy.multifunservice.dictionary.ser.SysDictionarySerializer;
import com.yanhuanxy.multifunservice.dictionary.ser.SysDictionarySerializerModifier;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Resource
    SysDictionaryHandle sysDictionaryHandle;

    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder, SysDictionaryContextualSerializer sysDictionarySerializer, DictionaryFieldAnnotationIntrospector introspector){
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 添加字段
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new SysDictionarySerializerModifier(sysDictionarySerializer)));

        // 转换值
        AnnotationIntrospector annotationIntrospector = objectMapper.getSerializationConfig().getAnnotationIntrospector();
        AnnotationIntrospector pair = AnnotationIntrospector.pair(annotationIntrospector, introspector);
        objectMapper.setAnnotationIntrospector(pair);

        return objectMapper;
    }

    @Bean
    public SysDictionaryContextualSerializer sysDictionaryContextualSerializer(){

        return new SysDictionaryContextualSerializer(sysDictionaryHandle);
    }

    @Bean
    public DictionaryFieldAnnotationIntrospector dictionaryFieldAnnotationIntrospector(){

        return new DictionaryFieldAnnotationIntrospector();
    }

    @Bean
    public SysDictionarySerializer sysDictionarySerializer(){

        return new SysDictionarySerializer(sysDictionaryHandle);
    }

   static class DictionaryFieldAnnotationIntrospector extends NopAnnotationIntrospector {

       @Override
       public Object findSerializer(Annotated am) {
           DictionaryCode annotation = am.getAnnotation(DictionaryCode.class);
           if(annotation != null){
                return SysDictionaryContextualSerializer.class;
           }

           return super.findSerializer(am);
       }
   }
}
