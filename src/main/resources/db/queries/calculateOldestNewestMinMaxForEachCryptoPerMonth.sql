WITH prices_by_month AS (
    SELECT
        symbol,
        DATE_TRUNC('month', timestamp) AS month,
        price,
        timestamp,
        ROW_NUMBER() OVER (PARTITION BY symbol, DATE_TRUNC('month', timestamp) ORDER BY timestamp ASC) AS rn_oldest,
        ROW_NUMBER() OVER (PARTITION BY symbol, DATE_TRUNC('month', timestamp) ORDER BY timestamp DESC) AS rn_newest
    FROM piktiv_crypto_service.crypto
),
aggregated_prices AS (
    SELECT
        symbol,
        month,
        MAX(CASE WHEN rn_oldest = 1 THEN price END) AS oldest_price,
        MAX(CASE WHEN rn_newest = 1 THEN price END) AS newest_price,
        MIN(price) AS min_price,
        MAX(price) AS max_price
    FROM prices_by_month
    GROUP BY symbol, month
)
SELECT symbol, month, oldest_price, newest_price, min_price, max_price
FROM aggregated_prices
ORDER BY symbol, month;