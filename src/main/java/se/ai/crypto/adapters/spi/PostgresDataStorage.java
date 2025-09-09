package se.ai.crypto.adapters.spi;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.ai.crypto.core.model.CryptoCurrency;
import se.ai.crypto.core.model.CryptoCurrencyStatistic;
import se.ai.crypto.core.model.CryptoCurrencyWithResultType;
import se.ai.crypto.core.ports.DataStorage;
import se.ai.crypto.utils.FileReaderUtil;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PostgresDataStorage implements DataStorage {

    private static final String DATABASE_CHECK_QUERY = "/db/queries/databaseCheck.sql";
    private static final String DATABASE_INSERT_CURRENCY_QUERY = "/db/queries/insertCurrency.sql";
    private static final String DATABASE_FIND_MIN_MAX_QUERY = "/db/queries/findMinMaxQuery.sql";
    private static final String DATABASE_FIND_MIN_MAX_BY_CRYPTO_QUERY = "/db/queries/findMinMaxByCryptoQuery.sql";
    private static final String DATABASE_FIND_MIN_MAX_OLDEST_NEWEST_BY_CRYPTO_QUERY = "/db/queries/findMinMaxOldestNewestCryptoQuery.sql";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int databaseCheck() throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_CHECK_QUERY);
        final var result = jdbcTemplate
                .getJdbcTemplate()
                .queryForObject(sql, Integer.class);

        assert result != null;
        return result;
    }

    @Override
    public int insertCurrency(CryptoCurrency currency) throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_INSERT_CURRENCY_QUERY);
        final var params = new MapSqlParameterSource();
        params.addValue("timestamp", currency.getTimestamp(), Types.TIMESTAMP)
                .addValue("symbol", currency.getType(), Types.OTHER)
                .addValue("price", currency.getPrice(), Types.DECIMAL);

        return jdbcTemplate.update(sql, params);
    }

    @Override
    public CryptoCurrency findMinMax() throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_FIND_MIN_MAX_QUERY);
        Map<String, String> params = new HashMap<>();

        return jdbcTemplate.queryForObject(sql, params, new CryptoCurrencyRowMapper());
    }

    @Override
    public List<CryptoCurrency> findMinMaxByCrypto(List<String> wantedCryptos) throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_FIND_MIN_MAX_BY_CRYPTO_QUERY);
        final var params = new MapSqlParameterSource();
        params.addValue("wantedCryptos", String.join(",", wantedCryptos), Types.ARRAY);

        final var resultList = jdbcTemplate.queryForList(sql, params, CryptoCurrency.class);

        return resultList;
    }

    @Override
    public List<CryptoCurrencyWithResultType> findMinMaxOldestNewestByCrypto(String wantedCrypto) throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_FIND_MIN_MAX_OLDEST_NEWEST_BY_CRYPTO_QUERY);
        final var params = new MapSqlParameterSource();
        params.addValue("symbol", wantedCrypto, Types.VARCHAR);

        return jdbcTemplate.query(sql, params, new CryptoCurrencyWithResultTypeRowMapper());
    }
}
