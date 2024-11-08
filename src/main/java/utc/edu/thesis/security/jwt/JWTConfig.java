package utc.edu.thesis.security.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.jwt")
public class JWTConfig {

    private String secretKey;
    private String tokenPrefix;
    private int expirationDateInMs;
    private int refreshExpirationDateInMs;

    public byte[] getByteSecretKey() {
        return secretKey.getBytes();
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(getByteSecretKey());
    }

}
