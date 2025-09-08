package se.ai.crypto.adapters.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ai.crypto.adapters.api.dto.NormalizedResponse;
import se.ai.crypto.configuration.ApplicationConstants;
import se.ai.crypto.core.CryptoService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ApplicationConstants.BASE_PATH)
public class CryptoController {

    private final CryptoService cryptoService;

    @GetMapping(
            path = ApplicationConstants.NORMALIZED_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<NormalizedResponse> getNormalizedRangeOfCryptos() {

        log.info("Incoming request to get normalized crypto");

        cryptoService.

        return ResponseEntity.ok(NormalizedResponse.builder().build());
    }

}
