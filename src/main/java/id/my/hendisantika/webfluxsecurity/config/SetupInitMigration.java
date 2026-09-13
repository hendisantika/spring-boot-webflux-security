package id.my.hendisantika.webfluxsecurity.config;

import id.my.hendisantika.webfluxsecurity.entity.AuthoritiesConstants;
import id.my.hendisantika.webfluxsecurity.entity.Authority;
import id.my.hendisantika.webfluxsecurity.entity.User;
import id.my.hendisantika.webfluxsecurity.repository.AuthorityRepository;
import id.my.hendisantika.webfluxsecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.52
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Component
public class SetupInitMigration implements ApplicationRunner {

    /**
     * BCrypt hash of "password".
     */
    private static final String DEFAULT_PASSWORD = "$2a$10$MYkP3aeSQy7DI.qgk4noreZ5uchb0i61OOeWu2tVHAO1yNSsGqCVG";

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    public SetupInitMigration(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Authority adminAuthority = authority(AuthoritiesConstants.ADMIN);
        Authority userAuthority = authority(AuthoritiesConstants.USER);

        authorityRepository.saveAll(List.of(adminAuthority, userAuthority))
                .thenMany(Flux.concat(
                        createIfMissing("admin", Set.of(adminAuthority, userAuthority)),
                        createIfMissing("user", Set.of(userAuthority))))
                .doOnNext(user -> log.info("Created default user '{}'", user.getLogin()))
                .blockLast();
    }

    private Mono<User> createIfMissing(String login, Set<Authority> authorities) {
        return userRepository.findByLogin(login)
                .hasElement()
                .filter(exists -> !exists)
                .flatMap(ignored ->
                        userRepository.save(new User(null, login, DEFAULT_PASSWORD, new HashSet<>(authorities))));
    }

    private Authority authority(String name) {
        Authority authority = new Authority();
        authority.setName(name);
        return authority;
    }
}
