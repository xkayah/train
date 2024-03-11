package com.mnus.gateway.filter;

import cn.hutool.log.Log;
import com.mnus.gateway.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/10 22:03:46
 */
//@Component
public class ReqLogFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(ReqLogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String srcIp = IpUtil.getUserIpAddr(request);
        String srcUri = Objects.requireNonNull(request.getRemoteAddress()).toString();
        String destUri = Objects.requireNonNull(request.getLocalAddress()).toString();
        String path = request.getPath().pathWithinApplication().value();// 打印请求路径
        URI uri = request.getURI();
        String requestUrl = this.getOriginalRequestUrl(exchange);// 打印请求url
        String method = String.valueOf(request.getMethod());
        // cors
        HttpHeaders headers = request.getHeaders();

        LOG.info("--> method: {} url: {} header: {}", method, requestUrl, headers);
        if ("POST".equals(method)) {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyString = new String(bytes, StandardCharsets.UTF_8);
                        LOG.info("--> {}", bodyString);
                        // log.info("--> {}", formatStr(bodyString)); //formData
                        exchange.getAttributes().put("POST_BODY", bodyString);
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return Mono.just(buffer);
                        });

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        } else if ("GET".equals(method)) {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            LOG.info("请求参数：" + queryParams);
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    private String getOriginalRequestUrl(ServerWebExchange exchange) {
        ServerHttpRequest req = exchange.getRequest();
        String userIpAddr = IpUtil.getUserIpAddr(req);
        LinkedHashSet<URI> uris = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI requestUri = uris.stream().findFirst().orElse(req.getURI());
        MultiValueMap<String, String> queryParams = req.getQueryParams();
        // 打印 /api/rest/feign/order/detail
        // return UriComponentsBuilder.fromPath(requestUri.getRawPath()).queryParams(queryParams).build().toUriString();

        return requestUri.toString(); // http://localhost:8091/api/rest/feign/order/detail
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 去掉FormData 空格,换行和制表符
     */
    private static String formatStr(String str) {
        if (str != null && str.length() > 0) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            return m.replaceAll("");
        }
        return str;
    }

}
