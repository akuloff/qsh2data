package com.alex09x.qsh.reader.v4;

import com.alex09x.qsh.reader.DataReader;
import com.alex09x.qsh.reader.type.Deal;
import com.alex09x.qsh.reader.type.DealFlags;
import com.alex09x.qsh.reader.type.DealType;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by alex on 11.01.14.
 */
public class DealsStream<T> extends Stream<T> {
    public long lastPrice;
    public int lastVolume;
    public int lastOI;
    private long lassMillis;
    private long lastId;

    public DealsStream(DataInput dataInput) throws IOException {
        super(dataInput);
    }

    public T read(Timestamp currentDateTime) throws IOException {
        Deal deal = new Deal();

        int type = dataInput.readUnsignedByte();

        if ((type & DealFlags.DATE_TIME.getValue()) > 0) {
            lassMillis = DataReader.readGrowing(dataInput, lassMillis);
        }
//        Timestamp timestamp = Utils.millis2Timestamp(lassMillis);
        deal.setTime(currentDateTime);
        if ((type & DealFlags.ID.getValue()) > 0) {
            lastId = DataReader.readGrowing(dataInput, lastId);
        }
        if ((type & DealFlags.ORDER_ID.getValue()) > 0) {
            DataReader.readLeb128(dataInput);
        }

        if ((type & DealFlags.PRICE.getValue()) > 0) {
            lastPrice += DataReader.readLeb128(dataInput);
        }
        deal.setPrice(lastPrice * stepPrice);

        if ((type & DealFlags.VOLUME.getValue()) > 0) {
            lastVolume = (int)DataReader.readLeb128(dataInput);
        }
        if ((type & DealFlags.OI.getValue()) > 0) {
            lastOI += (int)DataReader.readLeb128(dataInput);
        }
        deal.setVolume(lastVolume);

//        int dealType = type & DealFlags.TYPE.getValue();
        int dealType = type & DealFlags.TYPE.getValue();

        if (dealType == 2) {
            deal.setType(DealType.SELL);
        } else if (dealType == 1) {
            deal.setType(DealType.BUY);
        } else {//if (dealType == 0) {
            deal.setType(DealType.UNKNOWN);
        }

        deal.setSymbol(symbol);
        return (T) deal;
    }
}
