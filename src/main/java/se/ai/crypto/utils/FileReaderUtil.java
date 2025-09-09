package se.ai.crypto.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class FileReaderUtil {

    private static final String RESOURCE_PATH = "/cvs/";

    public static String readSqlFilesFromResource(String resourcePath) throws Exception {

        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (
                InputStream inputStream = resource.getInputStream();
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)
        ) {

            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }

    }

    public static List<String> readLinesFromCvsResource(File file) throws Exception {

        ClassPathResource resource = new ClassPathResource(RESOURCE_PATH + file.getName());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        )) {

            return reader.lines()
                    .skip(1) // Skip the header
                    .collect(Collectors.toList());
        }
    }
}
