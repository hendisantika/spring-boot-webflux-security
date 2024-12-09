package id.my.hendisantika.webfluxsecurity.config;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import id.my.hendisantika.webfluxsecurity.entity.AuthoritiesConstants;
import id.my.hendisantika.webfluxsecurity.entity.Authority;
import org.springframework.data.mongodb.core.MongoTemplate;

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
@ChangeLog(order = "001")
public class SetupInitMigration {
    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
    }
}
