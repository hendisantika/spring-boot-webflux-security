package id.my.hendisantika.webfluxsecurity.controller;

import id.my.hendisantika.webfluxsecurity.dto.LoginVM;
import id.my.hendisantika.webfluxsecurity.security.jwt.JWTReactiveAuthenticationManager;
import id.my.hendisantika.webfluxsecurity.security.jwt.JWTToken;
import id.my.hendisantika.webfluxsecurity.security.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
public class AuthController {

    private final JWTReactiveAuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthController(JWTReactiveAuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/authorize")
    public Mono<ResponseEntity<JWTToken>> authorize(@Valid @RequestBody Mono<LoginVM> loginVM) {
        return loginVM
                .map(login -> new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                .flatMap(authenticationManager::authenticate)
                .map(tokenProvider::createToken)
                .map(jwt -> ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .body(new JWTToken(jwt)))
                .onErrorResume(BadCredentialsException.class, e -> {
                    log.info("Authentication failed: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }
}
