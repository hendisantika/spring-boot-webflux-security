package id.my.hendisantika.webfluxsecurity.security.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.50
 * To change this template use File | Settings | File Templates.
 */
public class JWTHeadersExchangeMatcher implements ServerWebExchangeMatcher {
    @Override
    public Mono<MatchResult> matches(final ServerWebExchange exchange) {
        Mono<ServerHttpRequest> request = Mono.just(exchange).map(ServerWebExchange::getRequest);

        /* Check for header "Authorization" */
        return request.map(ServerHttpRequest::getHeaders)
                .filter(h -> h.containsKey(HttpHeaders.AUTHORIZATION))
                .flatMap($ -> MatchResult.match())
                .switchIfEmpty(MatchResult.notMatch());
    }
}
