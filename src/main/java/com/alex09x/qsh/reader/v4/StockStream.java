package com.alex09x.qsh.reader.v4;

import com.alex09x.qsh.reader.DataReader;
import com.alex09x.qsh.reader.type.Quote;
import com.alex09x.qsh.reader.type.QuoteType;
import com.alex09x.qsh.reader.type.Quotes;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 10.01.14.
 */
public class StockStream<T> extends Stream<T> {
    public long lastPrice;

    public StockStream(DataInput dataInput) throws IOException {
        super(dataInput);
    }

    public T read(Timestamp currentDateTime) throws IOException {
        int rows = (int)DataReader.readLeb128(dataInput);

        List<Quote> quotes = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            lastPrice += DataReader.readLeb128(dataInput);
            double price = lastPrice * stepPrice;
            int volume = (int)DataReader.readLeb128(dataInput);

            if (volume > 0) {
                quotes.add(new Quote(price, volume, QuoteType.ASK));
            } else {
                quotes.add(new Quote(price, -volume, QuoteType.BID));
            }
        }

        return (T) new Quotes(quotes, currentDateTime);
    }
}
