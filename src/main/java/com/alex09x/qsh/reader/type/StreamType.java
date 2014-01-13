package com.alex09x.qsh.reader.type;

/**
 * Created by alex on 12.01.14.
 */
public enum StreamType {
    STOCK(0X10),
    DEALS(0X20),
    ORDERS(0X30),
    TRADES(0X40),
    MESSAGES(0X50),
    AUXINFO(0X60),
    NONE(0);
    private final int value;

    StreamType(int intValue) {
        value = intValue;
    }

    public int getValue() {
        return value;
    }
}
