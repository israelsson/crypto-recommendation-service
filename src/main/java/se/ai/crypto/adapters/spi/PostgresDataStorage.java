package se.ai.crypto.adapters.spi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.ai.crypto.core.model.CryptoCurrency;
import se.ai.crypto.core.model.CryptoCurrencyWithResultType;
import se.ai.crypto.core.model.HighestRatedCryptoCurrency;
import se.ai.crypto.core.model.MonthlyOverviewCryptoCurrency;
import se.ai.crypto.core.ports.DataStorage;
import se.ai.crypto.utils.FileReaderUtil;

import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class PostgresDataStorage implements DataStorage {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    private static final String DATABASE_CHECK_QUERY = "/db/queries/databaseCheck.sql";
    private static final String DATABASE_INSERT_CURRENCY_QUERY = "/db/queries/insertCurrency.sql";
    private static final String DATABASE_FIND_MIN_MAX_QUERY = "/db/queries/findMinMaxQuery.sql";
    private static final String DATABASE_FIND_MIN_MAX_BY_CRYPTO_QUERY = "/db/queries/findMinMaxByCryptoQuery.sql";
    private static final String DATABASE_FIND_MIN_MAX_OLDEST_NEWEST_BY_CRYPTO_QUERY = "/db/queries/findMinMaxOldestNewestCryptoQuery.sql";
    private static final String DATABASE_FIND_HIGHEST_RATED_CURRENCY_QUERY = "/db/queries/highestRatedCurrencyQuery.sql";
    private static final String DATABASE_CALC_MONTHLY_OVERVIEW_QUERY = "/db/queries/calculateOldestNewestMinMaxForEachCryptoPerMonth.sql";

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

        return jdbcTemplate.queryForList(sql, params, CryptoCurrency.class);
    }

    @Override
    public List<CryptoCurrencyWithResultType> findMinMaxOldestNewestByCrypto(String wantedCrypto) throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_FIND_MIN_MAX_OLDEST_NEWEST_BY_CRYPTO_QUERY);
        final var params = new MapSqlParameterSource();
        params.addValue("symbol", wantedCrypto, Types.VARCHAR);

        return jdbcTemplate.query(sql, params, new CryptoCurrencyWithResultTypeRowMapper());
    }

    public HighestRatedCryptoCurrency findHighestRatedCrypto(String day) throws Exception {

        final var date = dateFormat.parse(day);
        final var dayAsLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_FIND_HIGHEST_RATED_CURRENCY_QUERY);
        final var params = new MapSqlParameterSource();

        final var start = dayAsLocalDate.atStartOfDay();
        final var end = start.plusDays(1);
        params.addValue("start", Timestamp.valueOf(start), Types.TIMESTAMP);
        params.addValue("end", Timestamp.valueOf(end), Types.TIMESTAMP);

        return jdbcTemplate.queryForObject(sql, params, new HighestRatedCryptoCurrencyRowMapper());
    }

    public List<MonthlyOverviewCryptoCurrency> calculateOldestNewestMinMaxForEachCryptoPerMonth() throws Exception {

        final var sql = FileReaderUtil.readSqlFilesFromResource(DATABASE_CALC_MONTHLY_OVERVIEW_QUERY);
        final var params = new MapSqlParameterSource();

        return jdbcTemplate.queryForList(sql, params, MonthlyOverviewCryptoCurrency.class);
    }
}
