package se.ai.crypto.adapter.api;

import io.restassured.RestAssured;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.ai.crypto.adapters.api.dto.ErrorResponseDto;
import se.ai.crypto.configuration.ApplicationConstants;
import  org.testcontainers.junit.jupiter.Testcontainers;
import se.ai.crypto.core.CryptoService;
import se.ai.crypto.core.model.CryptoCurrency;
import se.ai.crypto.core.model.CryptoType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CryptoControllerIT {

    private static final String SCHEMA_NAME = "piktiv_crypto_service";
    private static final String TABLE_NAME = "crypto";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @LocalServerPort
    private int port;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private CryptoService cryptoService;

    @BeforeEach
    void setup() {
        truncateTable();
    }

    @Test
    void testWeGetErrorMessageIfWeSendUnsupportedCurrency() {

        final var response = RestAssured.given()
                .port(port)
                .get(ApplicationConstants.BASE_PATH + ApplicationConstants.NORMALIZED_PATH + "/abc");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

        final var body = response.getBody().as(ErrorResponseDto.class);
        Assertions.assertTrue(body.getMessage().contains("not supported"));
        Assertions.assertEquals("CryptoUnsupportedException", body.getType());
    }

    @Test
    @SneakyThrows
    void testWeCanGetStatisticFromAValidCrypto() {

        Assertions.assertTrue(cryptoService.isDatabaseEmpty());

        final var highestCurrency = CryptoCurrency.builder()
                .price(100.00)
                .type(CryptoType.BTC)
                .timestamp(NOW)
                .build();

        final var lowestCurrency = CryptoCurrency.builder()
                .price(50.00)
                .type(CryptoType.BTC)
                .timestamp(NOW.minusDays(10))
                .build();

        cryptoService.insertCryptoCurrency(highestCurrency);
        cryptoService.insertCryptoCurrency(lowestCurrency);
        Assertions.assertFalse(cryptoService.isDatabaseEmpty());

        final var result = cryptoService.findMinMaxOldestNewestForRequestedCrypto(CryptoType.BTC.getName());

        Assertions.assertEquals(100, result.getMax());
        Assertions.assertEquals(50, result.getMin());
        Assertions.assertEquals(CryptoType.BTC.getName(), result.getSymbol());
        Assertions.assertEquals(1.0, result.getNormalizedRange());
    }

    @Test
    @SneakyThrows
    void testWeCanFindTheHighestValue() {

        Assertions.assertTrue(cryptoService.isDatabaseEmpty());

        final var highestCurrencyBtc = CryptoCurrency.builder()
                .price(100.00)
                .type(CryptoType.BTC)
                .timestamp(NOW)
                .build();

        final var lowestCurrencyBtc = CryptoCurrency.builder()
                .price(50.00)
                .type(CryptoType.BTC)
                .timestamp(NOW.minusMinutes(1))
                .build();

        final var highestCurrencyEth = CryptoCurrency.builder()
                .price(100.00)
                .type(CryptoType.ETH)
                .timestamp(NOW)
                .build();

        final var lowestCurrencyEth = CryptoCurrency.builder()
                .price(90.00)
                .type(CryptoType.ETH)
                .timestamp(NOW.minusMinutes(1))
                .build();

        cryptoService.insertCryptoCurrency(highestCurrencyBtc);
        cryptoService.insertCryptoCurrency(lowestCurrencyBtc);
        cryptoService.insertCryptoCurrency(highestCurrencyEth);
        cryptoService.insertCryptoCurrency(lowestCurrencyEth);
        Assertions.assertFalse(cryptoService.isDatabaseEmpty());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final var date = formatter.format(NOW);

        final var response = cryptoService.findHighestRatedCurrencyForDay(date);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(CryptoType.BTC, response.getSymbol());
        Assertions.assertEquals(100, response.getMaxPrice());
        Assertions.assertEquals(50, response.getMinPrice());
        Assertions.assertEquals(1.0, response.getNormalizedRange());
    }


    private void truncateTable() {

        log.info("Table: {} will be truncating", TABLE_NAME);
        try {
            jdbcTemplate.getJdbcOperations().execute(
                    "TRUNCATE TABLE %s.%s RESTART IDENTITY CASCADE;".formatted(SCHEMA_NAME, TABLE_NAME));
        } catch (Exception ex) {
            log.error("Could not truncate table '%s'".formatted(TABLE_NAME), ex);
        }
    }

}
