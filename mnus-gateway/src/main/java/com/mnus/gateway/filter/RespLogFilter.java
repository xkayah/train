package com.mnus.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.mnus.gateway.constance.MDCKey;
import com.mnus.gateway.constance.Order;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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

import java.net.URI;

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
                            // 释放掉内存
                            DataBufferUtils.release(buff);
                            // 排除Excel导出，不是application/json不打印。若请求是上传图片则在最上面判断。
                            MediaType contentType = originalResponse.getHeaders().getContentType();
                            if (!MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                                return bufferFactory.wrap(content);
                            }
                            // 构建返回日志
                            String joinData = new String(content);
                            String result = dateFormat(joinData);
                            int code = originalResponse.getStatusCode().value();
                            URI uri = exchange.getRequest().getURI();
                            LOG.info("code:{},result:{},uri:{} <==end",
                                    code,
                                    result,
                                    uri);

                            getDelegate().getHeaders().setContentLength(result.getBytes().length);
                            return bufferFactory.wrap(result.getBytes());
                        }));
                    } else {
                        LOG.error("{} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            LOG.error("Gateway log exception", e);
            return chain.filter(exchange);
        } finally {
            MDC.remove(MDCKey.TID);
        }
    }

    @Override
    public int getOrder() {
        return Order.RESPONSE_LOG;
    }

    /**
     * 日期格式化 eg 2024-02-23 11:00 注意: null --> ""
     */
    private String dateFormat(String jsonStr) {
        JSONObject json = JSON.parseObject(jsonStr, Feature.AllowISO8601DateFormat);
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
        return JSONObject.toJSONString(json, (ValueFilter) (object, name, value) -> value == null ? "" : value, SerializerFeature.WriteDateUseDateFormat);
    }
}