package se.ai.crypto.adapters.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ai.crypto.adapters.api.dto.CryptoCurrencyStatisticDto;
import se.ai.crypto.adapters.api.dto.HighestRatedCryptoCurrencyDto;
import se.ai.crypto.adapters.api.dto.NormalizedListResponseDto;
import se.ai.crypto.configuration.ApplicationConstants;
import se.ai.crypto.core.CryptoService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ApplicationConstants.BASE_PATH)
@Tag(name = "Crypto currency", description = "Will list all existing endpoints for crypto handling")
public class CryptoController {

    private final CryptoService cryptoService;

    @Operation(summary = "Returns a list of all supported currencies sorted descending after highest normalized value")
    @GetMapping(
            path = ApplicationConstants.NORMALIZED_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<NormalizedListResponseDto> getNormalizedRangeOfCryptos() {

        log.info("Incoming request to get normalized crypto");

        final var statisticDto = cryptoService.findMinMaxOldestNewestForAllCryptos()
                .stream()
                .map(CryptoCurrencyStatisticDto::fromCoreModel)
                .toList();

        return ResponseEntity.ok(NormalizedListResponseDto.builder()
                        .statistics(statisticDto)
                .build()
        );
    }

    @Operation(summary = "Returns info about requested currency")
    @GetMapping(
            path = ApplicationConstants.NORMALIZED_PATH + ApplicationConstants.CURRENCY_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CryptoCurrencyStatisticDto> getNormalizedRangeByCrypto(@PathVariable String currency) {

        log.info("Incoming request to get info about crypto: {}", currency);
        final var statistic = cryptoService.findMinMaxOldestNewestForRequestedCrypto(currency);
        return ResponseEntity.ok(CryptoCurrencyStatisticDto.fromCoreModel(statistic));
    }

    @Operation(summary = "Returns the highest rated, by normalized range, the specific date (YYYY-MM-DD)")
    @GetMapping(
            path = ApplicationConstants.HIGHEST_NORMALIZED_PATH + ApplicationConstants.DATE_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HighestRatedCryptoCurrencyDto> getHighestRatedNormalizedRange(@PathVariable String date) {

        log.info("Incoming request to get info about highest rated crypto the date: {}", date);

        final var highestRated = cryptoService.findHighestRatedCurrencyForDay(date);

        return ResponseEntity.ok(HighestRatedCryptoCurrencyDto.fromCoreModel(highestRated));
    }
}
