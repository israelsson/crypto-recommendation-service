package se.ai.crypto.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import se.ai.crypto.core.exception.ResourceNotFoundException;
import se.ai.crypto.core.ports.DataStorage;
import se.ai.crypto.utils.FileReaderUtil;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/*
    Service that are used for migrating data from CSV file to a database
 */
@Slf4j
@Service
@AllArgsConstructor
public class MigrationService implements CommandLineRunner {

    private DataStorage dataStorage;

    @Override
    public void run(String... args) throws Exception {

        // The database is empty, we need to migrate the data
        if (dataStorage.databaseCheck() == 0) {
            doMigration();
        }
    }

    private void doMigration() {

        final var cvsFolder = new ClassPathResource("cvs");
        log.info("cvs:s can be loaded: " + cvsFolder.exists());
        if (cvsFolder.exists()) {

            int counter = 0;
            final var filesInFolder = getFilesInFolder(cvsFolder);
            for (File file : filesInFolder) {

                log.info("File: {} loaded", file.getName());
                final var cvsEntries = getCvsFileContentAsList(file);

                for (String entry : cvsEntries) {
                    insertCvsEntryInDatabase(entry);
                    counter++;
                }
            }

            log.info("Migration job is done, {} entries has been added to database", counter);
        }
    }

    private void insertCvsEntryInDatabase(String entry) {

        String[] parts = entry.split(",");
        long timestamp = Long.parseLong(parts[0]);
        String symbol = parts[1];
        double price = Double.parseDouble(parts[2]);

        final var currency = CryptoCurrency.builder()
                .price(price)
                .type(CryptoType.valueOf(symbol))
                .timestamp(convertMillisToLocalDateTime(timestamp))
                .build();

        try {

            dataStorage.insertCurrency(currency);
        } catch (Exception e) {

            log.error("Could not insert currency: {}", e.getMessage(), e);
        }

    }

    private static LocalDateTime convertMillisToLocalDateTime(long millis) {

        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private List<String> getCvsFileContentAsList(File file) {
        try {

            return FileReaderUtil.readLinesFromCvsResource(file);
        } catch (Exception e) {

            log.error("Could not load cvs file: {} with path: {}", file, file.getAbsolutePath());
            throw new ResourceNotFoundException(e.getMessage(), e);
        }

    }

    private List<File> getFilesInFolder(ClassPathResource folder) {

        try {
            return Arrays.stream(Objects.requireNonNull(folder.getFile().listFiles())).toList();
        } catch (IOException ioe) {
            log.error("Could not get files from resource folder: {}", ioe.getMessage(), ioe);
            throw new ResourceNotFoundException(ioe.getMessage(), ioe);
        }
    }
}
