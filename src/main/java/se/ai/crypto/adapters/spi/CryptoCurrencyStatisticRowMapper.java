package se.ai.crypto.adapters.spi;

import org.springframework.jdbc.core.RowMapper;
import se.ai.crypto.core.exception.StatisticTypeNotSupportedException;
import se.ai.crypto.core.model.CryptoCurrencyStatistic;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoCurrencyStatisticRowMapper implements RowMapper<CryptoCurrencyStatistic> {

    @Override
    public CryptoCurrencyStatistic mapRow(ResultSet rs, int rowNum) throws SQLException {

        final var result = CryptoCurrencyStatistic.builder();
        final var typeOfResult = rs.getString("result_type");

        if ("HIGHEST_PRICE".equals(typeOfResult)) {
            result.max(rs.getDouble("price"));
        } else if ("LOWEST_PRICE".equals(typeOfResult)) {
            result.min(rs.getDouble("price"));
        } else if ("OLDEST_TIMESTAMP".equals(typeOfResult)) {
            result.oldest(rs.getTimestamp("timestamp").toLocalDateTime());
        } else if ("NEWEST_TIMESTAMP".equals(typeOfResult)) {
            result.newest(rs.getTimestamp("timestamp").toLocalDateTime());
        } else {
            throw new StatisticTypeNotSupportedException("Unsupported type");
        }

        result.symbol(rs.getString("symbol"));

        return result.build();
    }
}
