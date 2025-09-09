package se.ai.crypto.core.model;

import lombok.Getter;

@Getter
public enum CryptoType {
    BTC("btc"),
    DOGE("doge"),
    ETH("eth"),
    LTC("ltc"),
    XRP("xrp");

    private final String name;

    CryptoType(String name) {
        this.name = name;
    }
}
