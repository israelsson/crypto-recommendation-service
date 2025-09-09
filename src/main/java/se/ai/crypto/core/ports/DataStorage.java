package se.ai.crypto.core.ports;

import se.ai.crypto.core.CryptoCurrency;

public interface DataStorage {

    int databaseCheck() throws Exception;

    int insertCurrency(CryptoCurrency currency) throws Exception;
}
