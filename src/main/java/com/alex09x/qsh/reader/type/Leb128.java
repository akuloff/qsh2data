package com.alex09x.qsh.reader.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by alexey on 21.01.15.
 */
public final class Leb128 {
    private Leb128() {
    }

    /**
     * Gets the number of bytes in the unsigned LEB128 encoding of the
     * given value.
     *
     * @param value the value in question
     * @return its write size, in bytes
     */
    public static int unsignedLeb128Size(int value) {
        // TODO: This could be much cleverer.

        int remaining = value >> 7;
        int count = 0;

        while (remaining != 0) {
            remaining >>= 7;
            count++;
        }

        return count + 1;
    }

    /**
     * Gets the number of bytes in the signed LEB128 encoding of the
     * given value.
     *
     * @param value the value in question
     * @return its write size, in bytes
     */
    public static int signedLeb128Size(int value) {
        // TODO: This could be much cleverer.

        int remaining = value >> 7;
        int count = 0;
        boolean hasMore = true;
        int end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                    || ((remaining & 1) != ((value >> 6) & 1));

            value = remaining;
            remaining >>= 7;
            count++;
        }

        return count;
    }

    /**
     * Reads an signed integer from {@code in}.
     */
    public static long readSignedLeb128(DataInput in) throws IOException {
        long value = 0;
        int shift = 0;

        for (; ; ) {
            int b = in.readUnsignedByte();

            if (b == -1)
                throw new IOException("EndOfStreamException");

            value |= (long) (b & 0x7f) << shift;
            shift += 7;

            if ((b & 0x80) == 0) {
                if (shift < 8 * 8 && (b & 0x40) != 0)
                    value |= -(1L << shift);

                return value;
            }
        }


//        int result = 0;
//        int cur;
//        int count = 0;
//        int signBits = -1;
//
//        do {
//            cur = in.readByte() & 0xff;
//            result |= (cur & 0x7f) << (count * 7);
//            signBits <<= 7;
//            count++;
//        } while (((cur & 0x80) == 0x80) && count < 5);
//
//        if ((cur & 0x80) == 0x80) {
//            throw new IOException("invalid LEB128 sequence");
//        }
//
//        // Sign extend if appropriate
//        if (((signBits >> 1) & result) != 0 ) {
//            result |= signBits;
//        }
//
//        return result;
    }

    /**
     * Reads an unsigned integer from {@code in}.
     */
    public static long readUnsignedLeb128(DataInput in) throws IOException {
      long value = 0;
      int shift = 0;

      for(; ; )
      {
        long b = (long)in.readUnsignedByte();

        if(b == 0xffffffff)
          throw new IOException("EndOfStreamException");

        value |= (b & 0x7f) << shift;

        if((b & 0x80) == 0)
          return value;

        shift += 7;
      }

//        int result = 0;
//        int cur;
//        int count = 0;
//
//        do {
//            cur = in.readByte() & 0xff;
//            result |= (cur & 0x7f) << (count * 7);
//            count++;
//        } while (((cur & 0x80) == 0x80) && count < 5);
//
//        if ((cur & 0x80) == 0x80) {
//            throw new IOException("invalid LEB128 sequence");
//        }
//
//        return result;
    }

    /**
     * Writes {@code value} as an unsigned integer to {@code out}, starting at
     * {@code offset}. Returns the number of bytes written.
     */
    public static void writeUnsignedLeb128(DataOutput out, int value) throws IOException {
        int remaining = value >>> 7;

        while (remaining != 0) {
            out.writeByte((byte) ((value & 0x7f) | 0x80));
            value = remaining;
            remaining >>>= 7;
        }

        out.writeByte((byte) (value & 0x7f));
    }

    /**
     * Writes {@code value} as a signed integer to {@code out}, starting at
     * {@code offset}. Returns the number of bytes written.
     */
    public static void writeSignedLeb128(DataOutput out, int value) throws IOException {
        int remaining = value >> 7;
        boolean hasMore = true;
        int end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                    || ((remaining & 1) != ((value >> 6) & 1));

            out.writeByte((byte) ((value & 0x7f) | (hasMore ? 0x80 : 0)));
            value = remaining;
            remaining >>= 7;
        }
    }
}