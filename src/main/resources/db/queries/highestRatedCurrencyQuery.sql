SELECT
    symbol,
    MIN(price) AS min_price,
    MAX(price) AS max_price,
    (MAX(price) - MIN(price)) / MIN(price) AS normalized_range
FROM piktiv_crypto_service.crypto
WHERE timestamp >= :start AND timestamp < :end
GROUP BY symbol
ORDER BY normalized_range DESC
LIMIT 1;