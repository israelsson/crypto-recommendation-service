(
    SELECT
        'HIGHEST_PRICE' AS result_type,
        timestamp,
        symbol,
        price
    FROM piktiv_crypto_service.crypto
    WHERE symbol = :symbol
      AND price = (SELECT MAX(price) FROM piktiv_crypto_service.crypto WHERE symbol = :symbol)
    LIMIT 1
)
UNION ALL
(
    SELECT
        'LOWEST_PRICE' AS result_type,
        timestamp,
        symbol,
        price
    FROM piktiv_crypto_service.crypto
    WHERE symbol = :symbol
      AND price = (SELECT MIN(price) FROM piktiv_crypto_service.crypto WHERE symbol = :symbol)
    LIMIT 1
)
UNION ALL
(
    SELECT
        'OLDEST_TIMESTAMP' AS result_type,
        timestamp,
        symbol,
        price
    FROM piktiv_crypto_service.crypto
    WHERE symbol = :symbol
    ORDER BY timestamp ASC
    LIMIT 1
)
UNION ALL
(
    SELECT
        'NEWEST_TIMESTAMP' AS result_type,
        timestamp,
        symbol,
        price
    FROM piktiv_crypto_service.crypto
    WHERE symbol = :symbol
    ORDER BY timestamp DESC
    LIMIT 1
);