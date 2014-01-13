package com.alex09x.qsh.reader;

import com.alex09x.qsh.reader.type.Deal;
import com.alex09x.qsh.reader.type.Quote;
import com.alex09x.qsh.reader.type.Quotes;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

/**
 * Created by alex on 12.01.14.
 */
public class QshReaderTest {
    @Test
    public void testDeal() throws Exception {
        QshReader3<Deal> open = new QshReader().open("src/test/resources/Ticks.RTS-12.13_FT.2013-12-10.qsh");

        double priceSum = 0;
        int volumeSum = 0;
        int typeSum = 0;

        while (open.hasNext()) {
            Deal next = open.next();

            priceSum += next.getPrice();
            volumeSum += next.getVolume();
            typeSum += next.getType().getValue();
        }

        MatcherAssert.assertThat(priceSum, CoreMatchers.equalTo(31105791460D));
        MatcherAssert.assertThat(volumeSum, CoreMatchers.equalTo(702226));
        MatcherAssert.assertThat(typeSum, CoreMatchers.equalTo(333823));

//        price sum 3.110579146E10, volume sum 702226, type sum333823
        System.out.println("price sum " + priceSum + ", volume sum " + volumeSum + ", type sum " + typeSum);
    }

    @Test
    public void testQuotes() throws Exception {
        QshReader3<Quotes> open = new QshReader().open("src/test/resources//Stock.RTS-12.13_FT.2013-12-10.qsh");

        double priceSum = 0;
        int volumeSum = 0;
        int typeSum = 0;

        while (open.hasNext()) {
            Quotes next = open.next();

            for (Quote quote : next.getQuotes()) {
                priceSum += quote.getPrice();
                volumeSum += quote.getVolume();
                typeSum += quote.getType().getValue();
            }
        }


        MatcherAssert.assertThat(priceSum, CoreMatchers.equalTo(125728129600D));
        MatcherAssert.assertThat(volumeSum, CoreMatchers.equalTo(76038273));
        MatcherAssert.assertThat(typeSum, CoreMatchers.equalTo(3141022));

//        price sum 1.257281296E11, volume sum 76038273, type sum 3141022
        System.out.println("price sum " + priceSum + ", volume sum " + volumeSum + ", type sum " + typeSum);
    }
}
