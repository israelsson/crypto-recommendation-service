package se.ai.crypto.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class FileReaderUtil {

    public static String readSqlFilesFromResource(String resourcePath) throws Exception {

        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (
                InputStream inputStream = resource.getInputStream();
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)
        ) {

            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }

    public static List<String> readLinesFromCvsResource(InputStream inputStream) throws RuntimeException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .skip(1) // Skip the header
                    .collect(Collectors.toList());
        } catch (IOException e) {

            throw new RuntimeException("Failed to read CSV file", e);
        }
    }
}
