package com.alex09x.qsh.reader.v3;

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
    public double lastPrice;
    public int lastVolume;
    private Timestamp baseDateTime = new Timestamp(0);

    public DealsStream(DataInput dataInput) throws IOException {
        super(dataInput);
    }

    public T read(Timestamp currentDateTime) throws IOException {
        Deal deal = new Deal();

        int type = dataInput.readUnsignedByte();

        if ((type & DealFlags.DATE_TIME.getValue()) > 0) {
            DataReader.readDateTime(dataInput, baseDateTime);
        }
        deal.setTime(currentDateTime);

        if ((type & DealFlags.PRICE.getValue()) > 0) {
            lastPrice = DataReader.readRelative(dataInput, basePrice) * stepPrice;
        }
        deal.setPrice(lastPrice);


        if ((type & DealFlags.VOLUME.getValue()) > 0) {
            lastVolume = DataReader.readPackInt(dataInput);
        }
        deal.setVolume(lastVolume);

        int dealType = type & DealFlags.TYPE.getValue();

        if (dealType == 2) {
            deal.setType(DealType.SELL);
        } else if (dealType == 1) {
            deal.setType(DealType.BUY);
        } else if (dealType == 0) {
            deal.setType(DealType.UNKNOWN);
        }

        deal.setSymbol(symbol);
        return (T) deal;
    }
}
