package ml.ruby.weatherrecyclerview.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author: jwhan
 * @createTime: 2022/04/28 5:03 PM
 * @description:
 */
public class NumberOperation {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
