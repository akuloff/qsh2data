/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alex09x.qsh.reader.type;

import java.sql.Timestamp;

/**
 *
 * @author kulikov
 */
public class OrdersLogRecord {
    private int headerFlags;
    private int orderFlags;
    private String symbol;
    private double orderPrice;
    private double dealPrice;
    
    private int volume = 0;
    private int restVolume = 0;
    
    private DealType type;
    private Timestamp time;
    private long orderId;
    private long dealId;
    private long openInterest;
    
    private boolean isAdd = false, isFill = false, isNewSession = false;
    private boolean isEndTransaction = false, isNonSystem = false;
    private boolean isMoved = false; 
    private boolean isCanceled = false; //в т.ч. групповое удаление
    private boolean isCrossTrade = false;
    private boolean isQuote = false;
    private boolean isCounter = false;

    public boolean isCounter() {
        return isCounter;
    }

    public void setIsCounter(boolean isCounter) {
        this.isCounter = isCounter;
    }
    
    public boolean isQuote() {
        return isQuote;
    }

    public void setIsQuote(boolean isQuote) {
        this.isQuote = isQuote;
    }

    public boolean isCrossTrade() {
        return isCrossTrade;
    }

    public void setIsCrossTrade(boolean isCrossTrade) {
        this.isCrossTrade = isCrossTrade;
    }
    
    public int getRestVolume() {
        return restVolume;
    }

    public void setRestVolume(int restVolume) {
        this.restVolume = restVolume;
    }
    
    
    public boolean isNewSession() {
        return isNewSession;
    }

    public void setIsNewSession(boolean isNewSession) {
        this.isNewSession = isNewSession;
    }
    

    public int getHeaderFlags() {
        return headerFlags;
    }

    public void setHeaderFlags(int headerFlags) {
        this.headerFlags = headerFlags;
    }

    public int getOrderFlags() {
        return orderFlags;
    }

    public void setOrderFlags(int orderFlags) {
        this.orderFlags = orderFlags;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }

    public long getOpenInterest() {
        return openInterest;
    }

    public void setOpenInterest(long openInterest) {
        this.openInterest = openInterest;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setIsFill(boolean isFill) {
        this.isFill = isFill;
    }

    public boolean isEndTransaction() {
        return isEndTransaction;
    }

    public void setIsEndTransaction(boolean isEndTransaction) {
        this.isEndTransaction = isEndTransaction;
    }

    public boolean isNonSystem() {
        return isNonSystem;
    }

    public void setIsNonSystem(boolean isNonSystem) {
        this.isNonSystem = isNonSystem;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setIsMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }
    
    
}
