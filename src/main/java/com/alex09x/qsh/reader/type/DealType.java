package com.alex09x.qsh.reader.type;

/**
 * Created by alex on 12.01.14.
 */
public enum DealType {
    UNKNOWN(0), BUY(1), SELL(2);
    private final int value;

    DealType(int intValue) {
        value = intValue;
    }

    public int getValue() {
        return value;
    }
}
