package se.ai.crypto.configuration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {

    public static final String BASE_PATH = "/api/v1";
    public static final String NORMALIZED_PATH = "/normalized";
    public static final String HIGHEST_NORMALIZED_PATH = "/highest";
    public static final String CURRENCY_PATH = "/{currency}";
    public static final String DATE_PATH = "/{date}";
}
