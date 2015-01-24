package com.alex09x.qsh.reader.v3;

import com.alex09x.qsh.reader.Stream;
import com.alex09x.qsh.reader.Utils;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by alex on 12.01.14.
 */
public abstract class Stream3<T> extends Stream<T> {

    protected final int[] basePrice = new int[1];

    public Stream3(DataInput dataInput) throws IOException {
        super(dataInput);
    }
}
