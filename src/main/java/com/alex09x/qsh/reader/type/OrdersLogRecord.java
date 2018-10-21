/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alex09x.qsh.reader.type;

import lombok.*;

import java.sql.Timestamp;

/**
 *
 * @author kulikov
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    
    private boolean isAdd = false;
    private boolean isFill = false;
    private boolean isNewSession = false;
    private boolean isEndTransaction = false, isNonSystem = false;
    private boolean isMoved = false; 
    private boolean isCanceled = false; //в т.ч. групповое удаление
    private boolean isCrossTrade = false;
    private boolean isQuote = false;
    private boolean isCounter = false;
}
