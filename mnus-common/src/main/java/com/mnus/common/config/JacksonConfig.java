// package com.mnus.common.config;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.module.SimpleModule;
// import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
// /**
//  * 将 Long 转为 String
//  *
//  * @author: <a href="https://github.com/xkayah">xkayah</a>
//  * @date: 2024/3/13 20:07:05
//  */
// @Configuration
// public class JacksonConfig {
//     @Bean
//     public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//         ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//         SimpleModule simpleModule = new SimpleModule();
//         simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//         return objectMapper;
//     }
// }
