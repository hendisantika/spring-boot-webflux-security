package id.my.hendisantika.webfluxsecurity.config;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.36
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Configuration
public class DatasourceMongobeeConfig {
//    @Bean
//    public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
//        log.debug("Configuring MongoBee");
//        Mongobee mongobee = new Mongobee(mongoClient);
//        mongobee.setDbName(mongoProperties.getMongoClientDatabase());
//        mongobee.setMongoTemplate(mongoTemplate);
//        // package to scan for migrations
//        mongobee.setChangeLogsScanPackage("id.my.hendisantika.webfluxsecurity.config");
//        mongobee.setEnabled(true);
//        return mongobee;
//    }

    @Bean
    public MongoClient mongoClient(MongoProperties mongoProperties) {
        return new com.mongodb.MongoClient(new MongoClientURI(mongoProperties.getUri()));
    }

    @Bean
    public MongoTemplate mongoTemplate(com.mongodb.MongoClient mongoClient, MongoProperties mongoProperties) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getDatabase()));
    }

    @Bean
    public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
        log.debug("Configuring MongoBee");
        Mongobee mongobee = new Mongobee(mongoClient);
        mongobee.setDbName(mongoProperties.getDatabase());
        mongobee.setMongoTemplate(mongoTemplate);
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage("id.my.hendisantika.webfluxsecurity.config");
        mongobee.setEnabled(true);
        return mongobee;
    }
}
