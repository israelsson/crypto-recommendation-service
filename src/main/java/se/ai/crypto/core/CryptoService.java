package se.ai.crypto.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import se.ai.crypto.configuration.properties.CryptoProperties;
import se.ai.crypto.core.exception.CryptoUnsupportedException;
import se.ai.crypto.core.exception.DatabaseException;
import se.ai.crypto.core.exception.StatisticTypeNotSupportedException;
import se.ai.crypto.core.model.*;
import se.ai.crypto.core.ports.DataStorage;

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

    public void insertCryptoCurrency(CryptoCurrency currency) throws Exception {

        try {
            dataStorage.insertCurrency(currency);
        } catch (Exception e) {
            log.error("Could not insert currency: {}", currency.getType().getName());
            throw e;
        }
    }

    public CryptoCurrencyStatistic findMinMaxOldestNewestForRequestedCrypto(String requestedCrypto) {

        requestedCrypto = requestedCrypto.toUpperCase();
        if (!cryptoProperties.getSupportedCryptos().contains(requestedCrypto)) {
            throw new CryptoUnsupportedException("Crypto: '" + requestedCrypto + "' not supported");
        }

        final var crypto = getCryptoCurrencyStatisticFromDatabaseResponse(requestedCrypto);
        if (crypto == null) {
            return null;
        }

        final var statistic = convertCryptoCurrenciesToStatistic(crypto);
        final var normalizedRange = calculateNormalizedRange(
                statistic.getMax(),
                statistic.getMin()
        );

        statistic.setNormalizedRange(normalizedRange);

        return statistic;

    }

    public List<CryptoCurrencyStatistic> findMinMaxOldestNewestForAllCryptos() {

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

    public List<MonthlyOverviewCryptoCurrency> calculateOldestNewestMinMaxForEachCryptoPerMonth() {

        try {

            final var result = dataStorage.calculateOldestNewestMinMaxForEachCryptoPerMonth();
        } catch (Exception e) {
            log.error("Could not calc monthly overview: {}", e.getMessage(), e);
        }

        return null;
    }

    public HighestRatedCryptoCurrency findHighestRatedCurrencyForDay(String day) {

        return getHighestRatedCryptoCurrencyForDay(day);
    }

    private HighestRatedCryptoCurrency getHighestRatedCryptoCurrencyForDay(String day) {

        try {
            return dataStorage.findHighestRatedCrypto(day);
        } catch (Exception e) {
            log.error(
                    "Something went wrong finding highest rated crypto currency for day: {}, reason {}",
                    day,
                    e.getMessage(),
                    e
            );
        }

        return null;
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
