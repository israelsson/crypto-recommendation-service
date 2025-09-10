package se.ai.crypto.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "se.ai.crypto")
public class CryptoProperties {

    private List<String> supportedCryptos;

    private List<String> blockedIps;
}
