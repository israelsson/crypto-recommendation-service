package se.ai.crypto.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.ai.crypto.core.exception.DatabaseException;
import se.ai.crypto.core.ports.DataStorage;

@Slf4j
@Service
public class CryptoService {

    private DataStorage dataStorage;

    public boolean isDatabaseEmpty() {

        try {
            return (dataStorage.databaseCheck() == 0);
        } catch (Exception e) {
            log.error("Could not do a database check: {}", e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
