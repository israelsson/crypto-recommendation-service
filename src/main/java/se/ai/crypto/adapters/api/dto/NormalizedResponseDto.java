package se.ai.crypto.adapters.api.dto;

import lombok.Builder;
import lombok.Data;
import se.ai.crypto.core.model.CryptoCurrencyStatistic;

import java.util.List;

@Data
@Builder
public class NormalizedResponseDto {

    List<CryptoCurrencyStatisticDto> statistics;

}
