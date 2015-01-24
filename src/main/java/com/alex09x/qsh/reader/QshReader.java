package com.alex09x.qsh.reader;

import com.alex09x.qsh.reader.type.Deal;
import com.alex09x.qsh.reader.type.Quotes;
import com.alex09x.qsh.reader.type.StreamType;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;

/**
 * Created by alexey on 24.01.15.
 */
public abstract class QshReader<T> implements Iterator<T> {
    protected final DataInputStream dataInput;
    protected Timestamp baseDateTime;
    protected Timestamp currentDateTime;
    private final Stream<T> stream;
    private boolean isRead = true;

    public QshReader(DataInputStream dataInput) throws IOException {
        this.dataInput = dataInput;
        System.out.println("Application name = " + Utils.readString(dataInput));
        System.out.println("Comment = " + Utils.readString(dataInput));
        baseDateTime = new Timestamp((Utils.readLong(dataInput) - 621355968000000000L) / 10000L);
        System.out.printf("Base Time = %s%n", baseDateTime);
        currentDateTime = baseDateTime;


        System.out.printf("Stream count = %d%n", dataInput.readUnsignedByte());
        int streamType = dataInput.readByte();
        if (StreamType.STOCK.getValue() == streamType) {
            stream = createQuotesStream();
            System.out.printf("Stream type = %s%n", StreamType.STOCK);
        } else if (StreamType.DEALS.getValue() == streamType) {
            stream = createDealStream();
            System.out.printf("Stream type = %s%n", StreamType.DEALS);
        } else {
            throw new RuntimeException("Unknown stream type " + streamType);
        }
    }

    protected abstract Stream<T> createQuotesStream() throws IOException;

    protected abstract Stream<T> createDealStream() throws IOException;

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
//                return stream.read(Utils.millis2Timestamp(lassMillis));
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
