package id.my.hendisantika.webfluxsecurity.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.54
 * To change this template use File | Settings | File Templates.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        switch (parser.getCurrentToken()) {
            case VALUE_STRING:
                String rawDate = parser.getText();
                return FORMATTER.parse(rawDate, LocalDateTime::from);
        }
        throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected string.");
    }
}
