package com.alex09x.qsh.reader.v4;

import com.alex09x.qsh.reader.DataReader;
import com.alex09x.qsh.reader.Stream;
import com.alex09x.qsh.reader.type.DealType;
import com.alex09x.qsh.reader.type.OrdersLogRecord;

import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;

public class FullOrdersLogStream<T> extends Stream<T> {
    private long lastMillis = 0;
    private long lastOrderId = 0;
    private long lastDealId = 0;
    private long lastPrice = 0;
    private int lastVolume = 0;
    private int restVolume = 0;
    
    private long linkedId = 0;
    private long linkedPrice = 0;
    private long lastDealPrice = 0;
    
    private long lastOI = 0;

    public FullOrdersLogStream(DataInput dataInput) throws IOException {
        super(dataInput);
    }

    @Override
    public T read(Timestamp currentDateTime) throws IOException {
        boolean is_new, is_fill;
        DealType dealType;
        
        double curPrice = 0;
        double curLinkedPrice = 0;
        
        boolean new_sess, end_trans;
        
        OrdersLogRecord record  = new OrdersLogRecord();
        
        int h = dataInput.readUnsignedByte();
        
        int tradeFlags;
        
        int tr_byte1 = dataInput.readUnsignedByte();
        int tr_byte2 = dataInput.readUnsignedByte();
        tradeFlags = (tr_byte2 << 8) | tr_byte1;
        
        record.setTime(currentDateTime);
        record.setSymbol(symbol);
        record.setHeaderFlags(h);
        record.setOrderFlags(tradeFlags);
        
        new_sess = (tradeFlags & 2) > 0;
        end_trans = (tradeFlags & 1024) > 0;
        
        record.setNewSession(new_sess);
        record.setEndTransaction(end_trans);
        
        record.setMoved((tradeFlags & 4096) > 0);  //признак перемещения заявки (move)
        
        //признак удаления заявки
        record.setCanceled( ((tradeFlags & 8192) > 0) || ((tradeFlags & 16384) > 0) );
        record.setCrossTrade((tradeFlags & 32768) > 0);
        record.setQuote((tradeFlags & 128) > 0);
        record.setCounter((tradeFlags & (1 << 8)) > 0);
        record.setNonSystem((tradeFlags & (1 << 9)) > 0);
        
        if ((tradeFlags & 16) > 0) {
            dealType = DealType.BUY;
        } else if ((tradeFlags & 32) > 0) {
            dealType = DealType.SELL;
        } else {
            dealType = DealType.UNKNOWN;
        }
        
        is_new = (tradeFlags & 4) > 0;
        is_fill = (tradeFlags & 8) > 0;
        
        record.setType(dealType);
        record.setAdd(is_new);
        record.setFill(is_fill);
        
        if ((h & 1) > 0) {
            lastMillis = DataReader.readGrowing(dataInput, lastMillis);
        }            
        
        long ord_id;
        if ((h & 2) > 0) {
            if ( is_new ) { //новая заявка
                ord_id = lastOrderId = DataReader.readGrowing(dataInput, lastOrderId);
            } else {
                ord_id = DataReader.readLeb128(dataInput) + lastOrderId;
            }
        } else {
            ord_id = lastOrderId;
        }
        record.setOrderId(ord_id);

        
        if ((h & 4) > 0) { //цена
            lastPrice += DataReader.readLeb128(dataInput);
        }
        curPrice = lastPrice*stepPrice;
        record.setOrderPrice(curPrice);
        
        if ((h & 8) > 0) { //объем
            lastVolume = (int) DataReader.readLeb128(dataInput);
        }
        record.setVolume(lastVolume);
        
        long cur_oi = 0;
        restVolume = 0;
        if ( is_fill )  {
            if ((h & 16) > 0) { //остаток в заявке
                restVolume = (int) DataReader.readLeb128(dataInput);
            }    
            
            if ((h & 32) > 0) { //связанная сделка
                linkedId = lastDealId = DataReader.readGrowing(dataInput, lastDealId);
            } else{
                linkedId = lastDealId;
            }
            
            if ((h & 64) > 0) { //цена связанной заявки
                lastDealPrice += DataReader.readLeb128(dataInput);
                linkedPrice = lastDealPrice;
            } else {
                linkedPrice = lastDealPrice;
            }
            
            if ((h & 128) > 0) { //ОИ после сделки
                cur_oi = lastOI += DataReader.readLeb128(dataInput);
            } else {
                cur_oi = lastOI;
            }
            
        } else {
            if ( is_new ){
                restVolume = lastVolume;
            }
            linkedId = 0;
            linkedPrice = 0;
        }
        
        record.setRestVolume(restVolume);
        record.setDealId(linkedId);
        curLinkedPrice = linkedPrice*stepPrice;
        record.setDealPrice(curLinkedPrice);
        record.setOpenInterest(cur_oi);
        
        return (T) record;
    }
    
}
