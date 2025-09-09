SELECT timestamp, symbol, price FROM piktiv_crypto_service.crypto
WHERE price IN (
	(SELECT MAX (price) FROM piktiv_crypto_service.crypto),
	(SELECT MIN (price) FROM piktiv_crypto_service.crypto)
)