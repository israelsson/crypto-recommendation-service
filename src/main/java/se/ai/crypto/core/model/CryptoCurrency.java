package se.ai.crypto.core.model;

import lombok.Builder;
import lombok.Data;
import se.ai.crypto.core.CryptoType;

import java.time.LocalDateTime;

@Data
@Builder
public class CryptoCurrency {

    LocalDateTime timestamp;
    CryptoType type;
    double price;
}
