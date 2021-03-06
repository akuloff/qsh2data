package com.alex09x.qsh.reader.v3;

import com.alex09x.qsh.reader.DataReader;
import com.alex09x.qsh.reader.QshReader;
import com.alex09x.qsh.reader.Stream;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by alex on 09.01.14.
 */
public class QshReader3<T> extends QshReader<T> {

    public QshReader3(DataInputStream dataInput) throws IOException {
        super(dataInput);
    }

    @Override
    protected Stream<T> createQuotesStream() throws IOException {
        return new StockStream<>(dataInput);
    }

    @Override
    protected Stream<T> createDealStream() throws IOException {
        return new DealsStream<>(dataInput);
    }

    @Override
    protected boolean readNextRecordHeader() {
        try {
            currentDateTime = DataReader.readDateTime(dataInput, baseDateTime);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    protected Stream<T> createFullOrdersLogStream() throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }
}
