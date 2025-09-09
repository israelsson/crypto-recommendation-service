SELECT timestamp, symbol, price FROM piktiv_crypto_service.crypto
WHERE price IN (
	(SELECT MAX (price) FROM piktiv_crypto_service.crypto WHERE symbol IN (:wantedCryptos)),
	(SELECT MIN (price) FROM piktiv_crypto_service.crypto WHERE symbol IN (:wantedCryptos))
)