package com.alex09x.qsh.reader.type;

/**
 * Created by alex on 12.01.14.
 */
public enum QuoteType {
    UNKNOWN(0), FREE(1), SPREAD(2), ASK(3), BID(4), BESTASK(5), BESTBID(6);
    private final int value;

    QuoteType(int intValue) {
        value = intValue;
    }

    public int getValue() {
        return value;
    }
}
