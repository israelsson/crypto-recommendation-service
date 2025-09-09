package se.ai.crypto.core.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CryptoCurrencyWithResultType {

    LocalDateTime timestamp;
    CryptoType type;
    double price;
    String resultType;
}
