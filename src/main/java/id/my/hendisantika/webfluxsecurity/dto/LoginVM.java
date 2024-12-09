package id.my.hendisantika.webfluxsecurity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-security
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 09/12/24
 * Time: 11.41
 * To change this template use File | Settings | File Templates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVM {
    @NotEmpty
    @Size(min = 1, max = 50)
    private String username;

    @NotEmpty
    private String password;
}
