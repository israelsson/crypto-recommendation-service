package se.ai.crypto.core;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CryptoCurrency {

    LocalDateTime timestamp;
    CryptoType type;
    double price;
}
