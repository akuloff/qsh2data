package com.alex09x.qsh.reader;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by alex on 09.01.14.
 */
public class DataReader {
    public static Timestamp readDateTime(DataInput dataInput, Timestamp baseDateTime) throws IOException {
        int offset = Utils.readUShort(dataInput);
        if (offset == Utils.USHORT_MAX_VALUE) {
            long time = Utils.readLong(dataInput);

            Timestamp timestamp = Utils.tick2date(time);
            baseDateTime.setTime(timestamp.getTime());
            return timestamp;
        } else {
            long time = baseDateTime.getTime() + offset;
            Timestamp timestamp = new Timestamp(time);
            return timestamp;
        }
    }

    public static int readRelative(DataInput dataInput, int[] baseValue) throws IOException {
        int offset = dataInput.readByte();

        if (offset == Byte.MIN_VALUE) {
            return baseValue[0] = Utils.readInt(dataInput);
        } else {
            return baseValue[0] + offset;
        }
    }

    public static int readPackInt(DataInput br) throws IOException {
        int prefix = br.readUnsignedByte();
        int bytes, value;

        if ((prefix & 0x78) == 0x78) {
            bytes = 4;
            value = (prefix & 0x7) << 32;
        } else if ((prefix & 0x70) == 0x70) {
            bytes = 3;
            value = (prefix & 0xf) << 24;
        } else if ((prefix & 0x60) == 0x60) {
            bytes = 2;
            value = (prefix & 0x1f) << 16;
        } else if ((prefix & 0x40) == 0x40) {
            bytes = 1;
            value = (prefix & 0x3f) << 8;
        } else {
            bytes = 0;
            value = prefix & 0x7f;
        }

        for (int i = 0; i < bytes; i++) {
            value |= br.readUnsignedByte() << (i << 3);
        }
        if ((prefix & 0x80) == 0x80)
            value = ~value;

        return value;
    }
}
