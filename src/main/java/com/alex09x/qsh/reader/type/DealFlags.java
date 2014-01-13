package com.alex09x.qsh.reader.type;

/**
 * Created by alex on 12.01.14.
 */
public enum DealFlags {
    TYPE(0x03),
    DATE_TIME(0x04),
    PRICE(0x08),
    VOLUME(0x10),
    NONE(0);
    private final int value;

    DealFlags(int intValue) {
        value = intValue;
    }

    public int getValue() {
        return value;
    }
}
