package se.ai.crypto.adapters.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ai.crypto.adapters.api.dto.CryptoCurrencyStatisticDto;
import se.ai.crypto.adapters.api.dto.NormalizedResponseDto;
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
    public ResponseEntity<NormalizedResponseDto> getNormalizedRangeOfCryptos() {

        log.info("Incoming request to get normalized crypto");

        final var statisticDto = cryptoService.findMinMaxOldestNewestByCrypto()
                .stream()
                .map(CryptoCurrencyStatisticDto::fromCoreModel)
                .toList();

        return ResponseEntity.ok(NormalizedResponseDto.builder()
                        .statistics(statisticDto)
                .build()
        );
    }
}
