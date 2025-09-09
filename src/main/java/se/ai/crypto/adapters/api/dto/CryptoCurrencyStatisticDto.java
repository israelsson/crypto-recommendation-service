package se.ai.crypto.adapters.api.dto;

import lombok.Builder;
import lombok.Data;
import se.ai.crypto.core.model.CryptoCurrencyStatistic;

import java.time.LocalDateTime;

@Data
@Builder
public class CryptoCurrencyStatisticDto {

    String symbol;
    double min;
    double max;
    LocalDateTime oldest;
    LocalDateTime newest;
    double normalizedRange;

    public static CryptoCurrencyStatisticDto fromCoreModel(CryptoCurrencyStatistic core) {

        return CryptoCurrencyStatisticDto.builder()
                .symbol(core.getSymbol())
                .min(core.getMin())
                .max(core.getMax())
                .oldest(core.getOldest())
                .newest(core.getNewest())
                .normalizedRange(core.getNormalizedRange())
                .build();

    }
}
