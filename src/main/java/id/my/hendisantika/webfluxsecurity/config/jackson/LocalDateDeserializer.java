package id.my.hendisantika.webfluxsecurity.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.53
 * To change this template use File | Settings | File Templates.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.getCurrentToken() == JsonToken.VALUE_STRING) {
            String rawDate = parser.getText();
            try {
                return LocalDate.parse(rawDate, FORMATTER);
            } catch (Exception e) {
                throw context.weirdStringException(rawDate, LocalDate.class,
                        "Failed to parse date with format " + FORMATTER);
            }
        }

        // Handle unexpected token
        throw context.wrongTokenException(parser, LocalDate.class, START_ARRAY,
                "Expected a string for LocalDate deserialization.");
    }
}
