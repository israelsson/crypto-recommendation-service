package se.ai.crypto.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import se.ai.crypto.adapters.spi.CryptoCurrencyStatisticRowMapper;
import se.ai.crypto.configuration.properties.CryptoProperties;
import se.ai.crypto.core.exception.DatabaseException;
import se.ai.crypto.core.exception.StatisticTypeNotSupportedException;
import se.ai.crypto.core.model.CryptoCurrencyStatistic;
import se.ai.crypto.core.model.CryptoCurrencyWithResultType;
import se.ai.crypto.core.ports.DataStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoService {

    private CryptoProperties cryptoProperties;

    private DataStorage dataStorage;

    public boolean isDatabaseEmpty() {

        try {
            return (dataStorage.databaseCheck() == 0);
        } catch (Exception e) {
            log.error("Could not do a database check: {}", e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    public List<CryptoCurrencyStatistic> findMinMaxOldestNewestByCrypto() {

        List<CryptoCurrencyStatistic> resultList = new ArrayList<>();

        for (String supportedCrypto : cryptoProperties.getSupportedCryptos()) {

            final var cryptoCurrencyWithResultType = getCryptoCurrencyStatisticFromDatabaseResponse(supportedCrypto);
            if (cryptoCurrencyWithResultType == null) {
                continue;
            }

            final var cryptoCurrencyStatistic = convertCryptoCurrenciesToStatistic(cryptoCurrencyWithResultType);

            final var normalizedRange = calculateNormalizedRange(
                    cryptoCurrencyStatistic.getMax(),
                    cryptoCurrencyStatistic.getMin()
            );
            cryptoCurrencyStatistic.setNormalizedRange(normalizedRange);
            resultList.add(cryptoCurrencyStatistic);

        }

        // Sort the list before returning it
        resultList.sort(Comparator.comparingDouble(CryptoCurrencyStatistic::getNormalizedRange).reversed());

        return resultList;
    }

    private double calculateNormalizedRange(double max, double min) {

        return ((max - min) / min);

    }

    private CryptoCurrencyStatistic convertCryptoCurrenciesToStatistic(List<CryptoCurrencyWithResultType> currencies) {

        final var result = CryptoCurrencyStatistic.builder();
        for (CryptoCurrencyWithResultType currency : currencies) {

            final var typeOfResult = currency.getResultType();
            switch (typeOfResult) {
                case "HIGHEST_PRICE" -> {
                    result.max(currency.getPrice());
                    result.symbol(currency.getType().getName());
                }
                case "LOWEST_PRICE" -> result.min(currency.getPrice());
                case "OLDEST_TIMESTAMP" -> result.oldest(currency.getTimestamp());
                case "NEWEST_TIMESTAMP" -> result.newest(currency.getTimestamp());
                case null, default -> throw new StatisticTypeNotSupportedException("Unsupported type");
            }
        }

        return result.build();
    }

    private List<CryptoCurrencyWithResultType> getCryptoCurrencyStatisticFromDatabaseResponse(String supportedCrypto) {

        try {
            return dataStorage.findMinMaxOldestNewestByCrypto(supportedCrypto);
        } catch (Exception e) {
            log.error("Something went wrong collecting crypto currency statistic: {}", e.getMessage(), e);
        }

        return null;
    }
}
