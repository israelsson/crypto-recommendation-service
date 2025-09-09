package se.ai.crypto.adapters.spi;

import org.springframework.jdbc.core.RowMapper;
import se.ai.crypto.core.model.CryptoType;
import se.ai.crypto.core.model.CryptoCurrencyWithResultType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoCurrencyWithResultTypeRowMapper implements RowMapper<CryptoCurrencyWithResultType> {

    @Override
    public CryptoCurrencyWithResultType mapRow(ResultSet rs, int rowNum) throws SQLException {

        return CryptoCurrencyWithResultType.builder()
                .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                .type(CryptoType.valueOf(rs.getString("symbol")))
                .price(rs.getDouble("price"))
                .resultType(rs.getString("result_type"))
                .build();
    }
}
