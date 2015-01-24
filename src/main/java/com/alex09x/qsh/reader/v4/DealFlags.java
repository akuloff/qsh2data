package com.alex09x.qsh.reader.v4;

/**
 * Created by alex on 12.01.14.
 */
public enum DealFlags {
    TYPE(0x03),
    DATE_TIME(0x04),
    ID(0x08),
    ORDER_ID(0x10),
    PRICE(0x20),
    VOLUME(0x40),
    OI(0x80),
    NONE(0);

    private final int value;

    DealFlags(int intValue) {
        value = intValue;
    }

    public int getValue() {
        return value;
    }
}
