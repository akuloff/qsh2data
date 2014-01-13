package com.alex09x.qsh.reader.type;

import java.sql.Timestamp;

/**
 * Created by alex on 12.01.14.
 */
public class Deal {
    private String symbol;
    private double price;
    private int volume;
    private DealType type;
    private Timestamp time;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public DealType getType() {
        return type;
    }

    public void setType(DealType type) {
        this.type = type;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "symbol=" + symbol +
                ", price=" + price +
                ", volume=" + volume +
                ", type=" + type +
                ", time=" + time +
                '}';
    }
}
