package com.mnus.gateway.filter;

import com.mnus.gateway.constance.MDCKey;
import com.mnus.gateway.constance.Order;
import com.mnus.gateway.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/10 22:03:46
 */
@Component
public class ReqLogFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(ReqLogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!StringUtils.hasText(MDC.get(MDCKey.TID))) {
            MDC.put(MDCKey.TID, UUID.randomUUID().toString());
        }
        ServerHttpRequest request = exchange.getRequest();
        String clientIp = IpUtil.getClientIp(request);
        String serverIp = IpUtil.getServerIp();
        // /127.0.0.1:13851 ---> 127.0.0.1:13851
        String localAddr = Objects.requireNonNull(request.getLocalAddress()).toString().substring(1);
        String path = request.getPath().pathWithinApplication().value();
        String method = request.getMethod().name();
        String url = localAddr + path;
        if (HttpMethod.POST.name().equals(method)) {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String inputArgs = new String(bytes, StandardCharsets.UTF_8);
                        LOG.info("[{}]→[{}]uri:{} {},input:{} ==>begin",
                                clientIp,
                                serverIp,
                                method, url,
                                strFormat(inputArgs));
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
        } else if (HttpMethod.GET.name().equals(method)) {
            MultiValueMap<String, String> inputArgs = request.getQueryParams();
            LOG.info("[{}]→[{}]uri:{} {},input:{} ==>begin",
                    clientIp,
                    serverIp,
                    method, url,
                    inputArgs);
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Order.REQUEST_PRECEDENCE;
    }

    /**
     * 去掉FormData 空格,换行和制表符
     */
    private static String strFormat(String str) {
        if (str != null && str.length() > 0) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            return m.replaceAll("");
        }
        return str;
    }

}
