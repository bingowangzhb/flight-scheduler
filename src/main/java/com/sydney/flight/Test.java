package com.sydney.flight;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Test
 *
 * @author zhibin.wang
 * @since 2021/04/12 15:46
 */
public class Test {

    public static void main(String[] args) {

        Arrays.asList("add").contains("ad");

    }

    public static boolean isLegalLatitude(String latitude){
        try {
            Double.valueOf(latitude);

            BigDecimal d = new BigDecimal(latitude);
            return d.compareTo(new BigDecimal("+85.0")) <= 0 && d.compareTo(new BigDecimal("-85.0")) >= 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}
