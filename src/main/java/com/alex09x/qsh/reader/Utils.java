package com.alex09x.qsh.reader;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by alex on 10.01.14.
 */
public class Utils {
    public static final int USHORT_MAX_VALUE = 65535;
    public static final long TICKS_AT_EPOCH = 621355968000000000L;
    public static final long TICKS_PER_MILLISECOND = 10000L;

    public static String readString(DataInput br) throws IOException {
        byte b = br.readByte();
        byte[] bytes = new byte[b];
        br.readFully(bytes);
        return new String(bytes);
    }

    public static long readLong(DataInput dataInput) throws IOException {
        byte[] bytes = new byte[8];
        dataInput.readFully(bytes);
        return (((long) bytes[7] << 56) +
                ((long) (bytes[6] & 255) << 48) +
                ((long) (bytes[5] & 255) << 40) +
                ((long) (bytes[4] & 255) << 32) +
                ((long) (bytes[3] & 255) << 24) +
                ((bytes[2] & 255) << 16) +
                ((bytes[1] & 255) << 8) +
                ((bytes[0] & 255) << 0));
    }

    public static int readInt(DataInput dataInput) throws IOException {
        int ch1 = dataInput.readUnsignedByte();
        int ch2 = dataInput.readUnsignedByte();
        int ch3 = dataInput.readUnsignedByte();
        int ch4 = dataInput.readUnsignedByte();
        if ((ch4 | ch3 | ch2 | ch1) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    public static int readUShort(DataInput dataInput) throws IOException {
        int b1 = dataInput.readUnsignedByte();
        int b2 = dataInput.readUnsignedByte();
        return (b2 << 8) | b1;
    }

    public static Timestamp tick2date(long time) {
        return new Timestamp((time - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND);
    }

}
