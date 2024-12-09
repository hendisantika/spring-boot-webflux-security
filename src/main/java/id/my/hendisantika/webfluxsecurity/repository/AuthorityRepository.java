package id.my.hendisantika.webfluxsecurity.repository;

import id.my.hendisantika.webfluxsecurity.entity.Authority;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.40
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {
}
