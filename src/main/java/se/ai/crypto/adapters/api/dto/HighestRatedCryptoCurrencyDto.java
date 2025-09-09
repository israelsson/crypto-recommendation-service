package se.ai.crypto.adapters.api.dto;

import lombok.Builder;
import lombok.Data;
import se.ai.crypto.core.model.HighestRatedCryptoCurrency;

@Data
@Builder
public class HighestRatedCryptoCurrencyDto {

    String symbol;
    double minPrice;
    double maxPrice;
    double normalizedRange;

    public static HighestRatedCryptoCurrencyDto fromCoreModel(HighestRatedCryptoCurrency highest) {

        return HighestRatedCryptoCurrencyDto.builder()
                .symbol(highest.getSymbol().getName())
                .maxPrice(highest.getMaxPrice())
                .minPrice(highest.getMinPrice())
                .normalizedRange(highest.getNormalizedRange())
                .build();
    }
}
