package se.ai.crypto.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HighestRatedCryptoCurrency {

    CryptoType symbol;
    double minPrice;
    double maxPrice;
    double normalizedRange;
}
