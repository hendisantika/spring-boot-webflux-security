package id.my.hendisantika.webfluxsecurity.security.jwt;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.47
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TokenProvider {

    private static final String SALT_KEY = "JpxM4e858rc673syopdZnMFb*ExeqJtUc0HJ_iOxu~jiSYu+yPdPw93OBBjF";
    private static final int TOKEN_VALIDITY = 86400; // Value in second
    private static final String AUTHORITIES_KEY = "auth";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final Base64.Encoder encoder = Base64.getEncoder();

    private String secretKey;

    private long tokenValidityInMilliseconds;

    @PostConstruct
    public void init() {
        this.secretKey = encoder.encodeToString(SALT_KEY.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInMilliseconds =
                1000 * TOKEN_VALIDITY;
    }
}
