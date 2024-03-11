package com.mnus.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/10 22:07:56
 */
@Component
public class RespLogFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(RespLogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = (HttpStatus) originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);// 降级处理返回数据
            }
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buff = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[buff.readableByteCount()];
                            buff.read(content);
                            DataBufferUtils.release(buff);// 释放掉内存

                            // 排除Excel导出，不是application/json不打印。若请求是上传图片则在最上面判断。
                            MediaType contentType = originalResponse.getHeaders().getContentType();
                            if (!MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                                return bufferFactory.wrap(content);
                            }

                            // 构建返回日志
                            String joinData = new String(content);
                            String result = modifyBody(joinData);
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode().value());
                            rspArgs.add(exchange.getRequest().getURI());
                            rspArgs.add(result);
                            LOG.info("<-- {} {}\n{}", rspArgs.toArray());

                            getDelegate().getHeaders().setContentLength(result.getBytes().length);
                            return bufferFactory.wrap(result.getBytes());
                        }));
                    } else {
                        LOG.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            LOG.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    // 返回统一的JSON日期数据 2024-02-23 11:00， null转空字符串
    private String modifyBody(String jsonStr) {
        JSONObject json = JSON.parseObject(jsonStr, Feature.AllowISO8601DateFormat);
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
        return JSONObject.toJSONString(json, (ValueFilter) (object, name, value) -> value == null ? "" : value, SerializerFeature.WriteDateUseDateFormat);
    }
}


//    public void setJSONObjectWriteNullStringAsEmpty() {
//    JSON.toJSONString(data,SerializerFeature.WriteNullStringAsEmpty);
//    JSONObject jsonObj = JSON.parseObject(data, Feature.AllowISO8601DateFormat);
//    JSON.toJSONString(data,SerializerFeature.DisableCircularReferenceDetect);
//    JSONObject.DEFFAULT_DATE_FORMAT ="yyyy-MM-dd HH:mm";
//    JSONObject.toJSONString(jsonObject,SerializerFeature.WriteDateUseDateFormat);
//
//    String dataJson = JSON.toJSONString(data, (ValueFilter) (object, name, value) -> {
//        log.info("data:{} ", data);
//
//        log.info("object:{}, name:{}, value:{}", object, name, value);
//        if (value == null) {
//            return "";
//        }
//        return value;
//    });
//    JSONObject jsonObject = new JSONObject();
//    把json对象转换成字节数组
//    byte[] bits = data.getBytes(StandardCharsets.UTF_8);
//    DataBuffer buffer = originalResponse.bufferFactory().wrap(bits);
//    originalResponse.writeWith(Mono.just(buffer));
//    }

