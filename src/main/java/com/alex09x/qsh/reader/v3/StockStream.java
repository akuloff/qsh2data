package com.alex09x.qsh.reader.v3;

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

    public StockStream(DataInput dataInput) throws IOException {
        super(dataInput);
    }

    public T read(Timestamp currentDateTime) throws IOException {
        int rows = DataReader.readPackInt(dataInput);

        List<Quote> quotes = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            int price = DataReader.readRelative(dataInput, basePrice) * stepPrice;
            int volume = DataReader.readPackInt(dataInput);

            if (volume > 0) {
                quotes.add(new Quote(price, volume, QuoteType.ASK));
            } else {
                quotes.add(new Quote(price, -volume, QuoteType.BID));
            }
        }

        return (T) new Quotes(quotes, currentDateTime);
    }
}
