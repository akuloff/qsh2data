package com.alex09x.qsh.reader.v4;

import com.alex09x.qsh.reader.DataReader;
import com.alex09x.qsh.reader.QshReader;
import com.alex09x.qsh.reader.Utils;
import com.alex09x.qsh.reader.type.StreamType;
import com.alex09x.qsh.reader.Stream;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;

/**
 * Created by alex on 09.01.14.
 */
public class QshReader4<T> extends QshReader<T> {

    public QshReader4(DataInputStream dataInput) throws IOException {
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
            currentDateTime = DataReader.readGrowingDateTime(dataInput, currentDateTime);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
