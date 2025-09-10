package se.ai.crypto.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import se.ai.crypto.core.exception.ResourceNotFoundException;
import se.ai.crypto.core.model.CryptoCurrency;
import se.ai.crypto.core.model.CryptoType;
import se.ai.crypto.utils.FileReaderUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
    Service that are used for migrating data from CSV file to a database
 */
@Slf4j
@Service
@AllArgsConstructor
public class MigrationService implements CommandLineRunner {

    private CryptoService cryptoService;

    @Override
    public void run(String... args) throws Exception {

        // The database is empty, we need to migrate the data
        if (cryptoService.isDatabaseEmpty()) {
            doMigration();
        } else {
            log.info("Data in database already exist, no need for migration");
        }
    }

    private void doMigration() throws IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/cvs/*.csv");

        log.info("Found {} CSV files in /cvs/", resources.length);

        int counter = 0;
        for (Resource resource : resources) {
            log.info("File: {} loaded", resource.getFilename());

            final var cvsEntries = getCvsFileContentAsList(resource.getInputStream());
            for (String entry : cvsEntries) {
                insertCvsEntryInDatabase(entry);
                counter++;
            }
        }

        log.info("Migration job is done, {} entries has been added to database", counter);
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

            cryptoService.insertCryptoCurrency(currency);
        } catch (Exception e) {

            log.error("Could not insert currency: {}", e.getMessage(), e);
        }
    }

    private static LocalDateTime convertMillisToLocalDateTime(long millis) {

        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private List<String> getCvsFileContentAsList(InputStream inputStream) {
        try {

            return FileReaderUtil.readLinesFromCvsResource(inputStream);
        } catch (Exception e) {

            log.error("Could not load cvs file");
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
