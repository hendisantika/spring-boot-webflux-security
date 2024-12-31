package id.my.hendisantika.webfluxsecurity.config;

import id.my.hendisantika.webfluxsecurity.entity.AuthoritiesConstants;
import id.my.hendisantika.webfluxsecurity.exception.UnauthorizedAuthenticationEntryPoint;
import id.my.hendisantika.webfluxsecurity.security.ReactiveUserDetailsServiceImpl;
import id.my.hendisantika.webfluxsecurity.security.TokenAuthenticationConverter;
import id.my.hendisantika.webfluxsecurity.security.jwt.JWTHeadersExchangeMatcher;
import id.my.hendisantika.webfluxsecurity.security.jwt.JWTReactiveAuthenticationManager;
import id.my.hendisantika.webfluxsecurity.security.jwt.TokenProvider;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.51
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {
    private static final String[] AUTH_WHITELIST = {
            "/resources/**",
            "/webjars/**",
            "/authorize/**",
            "/favicon.ico",
    };
    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final TokenProvider tokenProvider;

    public SecurityConfiguration(ReactiveUserDetailsServiceImpl reactiveUserDetailsService,
                                 TokenProvider tokenProvider) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    //    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {
//        http.httpBasic().disable()
//                .formLogin().disable()
//                .csrf().disable()
//                .logout().disable();
//
//        http
//                .exceptionHandling()
//                .authenticationEntryPoint(entryPoint)
//                .and()
//                .authorizeExchange()
//                .matchers(EndpointRequest.to("health", "info"))
//                .permitAll()
//                .and()
//                .authorizeExchange()
//                .pathMatchers(HttpMethod.OPTIONS)
//                .permitAll()
//                .and()
//                .authorizeExchange()
//                .matchers(EndpointRequest.toAnyEndpoint())
//                .hasAuthority(AuthoritiesConstants.ADMIN)
//                .and()
//                .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
//                .authorizeExchange()
//                .pathMatchers(AUTH_WHITELIST).permitAll()
//                .anyExchange().authenticated();
//
//        return http.build();
//    }
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {
        http.securityContextRepository(new WebSessionServerSecurityContextRepository())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable);

        http
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(entryPoint))
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .matchers(EndpointRequest.to("health", "info")).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .matchers(EndpointRequest.toAnyEndpoint()).hasAuthority(AuthoritiesConstants.ADMIN)
                        .pathMatchers(AUTH_WHITELIST).permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION);

        return http.build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(repositoryReactiveAuthenticationManager());
        authenticationWebFilter.setAuthenticationConverter(new TokenAuthenticationConverter(tokenProvider));
        authenticationWebFilter.setRequiresAuthenticationMatcher(new JWTHeadersExchangeMatcher());
        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        return authenticationWebFilter;
    }

    @Bean
    public JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager() {
        JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager = new JWTReactiveAuthenticationManager(reactiveUserDetailsService, passwordEncoder());
        return repositoryReactiveAuthenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
