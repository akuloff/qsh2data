package com.alex09x.qsh.reader.type;

import java.sql.Timestamp;
import java.util.List;

/**
 * User: alex
 * Date: 13.01.14
 * Time: 17:18
 */
public class Quotes {
    private final List<Quote> quotes;
    private final Timestamp time;

    public Quotes(List<Quote> quotes, Timestamp time) {
        this.quotes = quotes;
        this.time = time;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public Timestamp getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Quotes{" +
                "quotes=" + quotes +
                ", time=" + time +
                '}';
    }
}
