package id.my.hendisantika.webfluxsecurity.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.49
 * To change this template use File | Settings | File Templates.
 */
public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public JWTReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return Mono.just(authentication);
        }
        return Mono.just(authentication)
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(this::authenticateToken)
                .publishOn(Schedulers.parallel())
                .onErrorResume(e -> raiseBadCredentials())
                .filter(u -> passwordEncoder.matches((String) authentication.getCredentials(), u.getPassword()))
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .map(u -> new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), u.getAuthorities()));
    }

    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new BadCredentialsException("Invalid Credentials"));
    }

}
