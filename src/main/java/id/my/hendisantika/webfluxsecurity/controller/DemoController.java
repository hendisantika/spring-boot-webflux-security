package id.my.hendisantika.webfluxsecurity.controller;

import id.my.hendisantika.webfluxsecurity.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.56
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api")
public class DemoController {
    @GetMapping(value = "/hello")
    public Mono<String> hello(ServerWebExchange serverWebExchange, @RequestParam String name) {
        log.info("Hello {}", name);
        return SecurityUtils.getUserFromRequest(serverWebExchange);
    }
}
