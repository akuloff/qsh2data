package com.alex09x.qsh.reader;

import com.alex09x.qsh.reader.type.Deal;
import com.alex09x.qsh.reader.type.Quote;
import com.alex09x.qsh.reader.type.Quotes;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * Created by alex on 12.01.14.
 */
public class QshReaderTest {
    @Test
    public void testDeal() throws Exception {
        Iterator<Deal> open = new QshReader().openPath("src/test/resources/Ticks.RTS-12.13_FT.2013-12-10.qsh");

        double priceSum = 0;
        int volumeSum = 0;
        int typeSum = 0;
        int count = 0;

        while (open.hasNext()) {
            Deal next = open.next();

            priceSum += next.getPrice();
            volumeSum += next.getVolume();
            typeSum += next.getType().getValue();
            count++;
        }

        MatcherAssert.assertThat(priceSum, CoreMatchers.equalTo(31105791460D));
        MatcherAssert.assertThat(volumeSum, CoreMatchers.equalTo(702226));
        MatcherAssert.assertThat(typeSum, CoreMatchers.equalTo(333823));
        MatcherAssert.assertThat(count, CoreMatchers.equalTo(221022));
    }

    @Test
    public void testQuotes() throws Exception {
        Iterator<Quotes> open = new QshReader().openPath("src/test/resources/Stock.RTS-12.13_FT.2013-12-10.qsh");

        double priceSum = 0;
        int volumeSum = 0;
        int typeSum = 0;
        int count = 0;

        while (open.hasNext()) {
            Quotes next = open.next();

            for (Quote quote : next.getQuotes()) {
                priceSum += quote.getPrice();
                volumeSum += quote.getVolume();
                typeSum += quote.getType().getValue();
                count++;
            }
        }

        MatcherAssert.assertThat(priceSum, CoreMatchers.equalTo(125728129600D));
        MatcherAssert.assertThat(volumeSum, CoreMatchers.equalTo(76038273));
        MatcherAssert.assertThat(typeSum, CoreMatchers.equalTo(3141022));
        MatcherAssert.assertThat(count, CoreMatchers.equalTo(893420));
    }
}
