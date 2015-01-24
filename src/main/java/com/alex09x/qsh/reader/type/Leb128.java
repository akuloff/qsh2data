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

    }

}