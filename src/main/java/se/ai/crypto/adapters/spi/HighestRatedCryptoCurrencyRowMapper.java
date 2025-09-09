package se.ai.crypto.adapters.spi;

import org.springframework.jdbc.core.RowMapper;
import se.ai.crypto.core.model.CryptoType;
import se.ai.crypto.core.model.HighestRatedCryptoCurrency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HighestRatedCryptoCurrencyRowMapper implements RowMapper<HighestRatedCryptoCurrency> {

    @Override
    public HighestRatedCryptoCurrency mapRow(ResultSet rs, int rowNum) throws SQLException {

        return HighestRatedCryptoCurrency.builder()
                .symbol(CryptoType.valueOf(rs.getString("symbol")))
                .minPrice(rs.getDouble("min_price"))
                .maxPrice(rs.getDouble("max_price"))
                .normalizedRange(rs.getDouble("normalized_range"))
                .build();
    }
}
