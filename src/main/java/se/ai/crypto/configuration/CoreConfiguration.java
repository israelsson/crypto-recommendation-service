package se.ai.crypto.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.ai.crypto.adapters.spi.PostgresDataStorage;
import se.ai.crypto.core.ports.DataStorage;

@Configuration
public class CoreConfiguration {

    @Bean
    DataStorage dataStorage(NamedParameterJdbcTemplate jdbcTemplate) {

        return new PostgresDataStorage(jdbcTemplate);
    }
}
