package se.ai.crypto.core.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CryptoCurrencyStatistic {

    String symbol;
    double min;
    double max;
    LocalDateTime oldest;
    LocalDateTime newest;
    double normalizedRange;
}
