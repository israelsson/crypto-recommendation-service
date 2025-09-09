package se.ai.crypto.adapters.spi;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.ai.crypto.core.CryptoCurrency;
import se.ai.crypto.core.ports.DataStorage;
import se.ai.crypto.utils.FileReaderUtil;

import java.sql.Types;

@AllArgsConstructor
public class PostgresDataStorage implements DataStorage {

    private static final String DATABASE_CHECK_QUERY = "/db/queries/databaseCheck.sql";
    private static final String DATABASE_INSERT_CURRENCY_QUERY = "/db/queries/insertCurrency.sql";

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
}
