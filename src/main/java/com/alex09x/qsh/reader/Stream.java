package com.alex09x.qsh.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by alex on 12.01.14.
 */
public abstract class Stream<T> {
    private static final Logger logger = LoggerFactory.getLogger(Stream.class);

    protected final DataInput dataInput;
    protected final String symbol;
    protected final double stepPrice;

    public Stream(DataInput dataInput) throws IOException {
        this.dataInput = dataInput;
        String data = Utils.readString(dataInput);
        String[] split = data.split(":");
        this.symbol = split[1];
        this.stepPrice = Double.valueOf(split[4]);
        logger.debug(String.format("Instrument = %s%n", symbol));
    }

    public abstract T read(Timestamp currentDateTime) throws IOException;

}
