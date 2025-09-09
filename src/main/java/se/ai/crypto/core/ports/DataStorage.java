package se.ai.crypto.core.ports;

import se.ai.crypto.core.model.CryptoCurrency;
import se.ai.crypto.core.model.CryptoCurrencyStatistic;
import se.ai.crypto.core.model.CryptoCurrencyWithResultType;

import java.util.List;

public interface DataStorage {

    int databaseCheck() throws Exception;

    int insertCurrency(CryptoCurrency currency) throws Exception;

    CryptoCurrency findMinMax() throws Exception;

    List<CryptoCurrency> findMinMaxByCrypto(List<String> wantedCryptos) throws Exception;

    List<CryptoCurrencyWithResultType> findMinMaxOldestNewestByCrypto(String wantedCrypto) throws Exception;
}
