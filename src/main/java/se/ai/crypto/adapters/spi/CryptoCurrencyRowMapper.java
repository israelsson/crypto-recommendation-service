package se.ai.crypto.adapters.spi;

import org.springframework.jdbc.core.RowMapper;
import se.ai.crypto.core.model.CryptoCurrency;
import se.ai.crypto.core.CryptoType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoCurrencyRowMapper implements RowMapper<CryptoCurrency> {

    @Override
    public CryptoCurrency mapRow(ResultSet rs, int rowNum) throws SQLException {

        return CryptoCurrency.builder()
                .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                .type(CryptoType.valueOf(rs.getString("symbol")))
                .price(rs.getDouble("price"))
                .build();
    }
}
