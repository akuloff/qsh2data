package com.alex09x.qsh.reader;

import com.alex09x.qsh.reader.type.StreamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;

/**
 * Created by alexey on 24.01.15.
 */
public abstract class QshReader<T> implements Iterator<T> {
    private static final Logger logger = LoggerFactory.getLogger(Stream.class);

    protected final DataInputStream dataInput;
    protected Timestamp baseDateTime;
    protected Timestamp currentDateTime;
    private final Stream<T> stream;
    private boolean isRead = true;

    public QshReader(DataInputStream dataInput) throws IOException {
        this.dataInput = dataInput;
        logger.debug("Application name = " + Utils.readString(dataInput));
        logger.debug("Comment = " + Utils.readString(dataInput));
        baseDateTime = new Timestamp((Utils.readLong(dataInput) - 621355968000000000L) / 10000L);
        logger.debug(String.format("Base Time = %s%n", baseDateTime));
        currentDateTime = baseDateTime;


        logger.debug(String.format("Stream count = %d%n", dataInput.readUnsignedByte()));
        int streamType = dataInput.readByte();
        if (StreamType.STOCK.getValue() == streamType) {
            stream = createQuotesStream();
            logger.debug(String.format("Stream type = %s%n", StreamType.STOCK));
        } else if (StreamType.DEALS.getValue() == streamType) {
            stream = createDealStream();
            logger.debug(String.format("Stream type = %s%n", StreamType.DEALS));
        } else if (StreamType.FULLORDERSLOG.getValue() == streamType) {
            stream = createFullOrdersLogStream();
            logger.debug(String.format("Stream type = %s%n", StreamType.FULLORDERSLOG));
        } else {
            throw new RuntimeException("Unknown stream type " + streamType);
        }
    }

    protected abstract Stream<T> createQuotesStream() throws IOException;

    protected abstract Stream<T> createDealStream() throws IOException;

    protected abstract Stream<T> createFullOrdersLogStream() throws IOException;

    protected abstract boolean readNextRecordHeader();

    @Override
    public boolean hasNext() {
        return isRead = readNextRecordHeader();
    }

    @Override
    public T next() {
        if (isRead) {
            try {
                return stream.read(currentDateTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void remove() {
        throw new RuntimeException("Not implemented");
    }
}
