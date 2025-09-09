INSERT INTO piktiv_crypto_service.crypto (timestamp, symbol, price)
VALUES (:timestamp, :symbol, :price)
ON CONFLICT (timestamp, symbol) DO NOTHING;
