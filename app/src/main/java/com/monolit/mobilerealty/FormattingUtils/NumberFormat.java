package com.monolit.mobilerealty.Format;

import java.text.DecimalFormat;

public class NumberFormat {
    private static String numericFormat(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);

        return output;
    }

    public static String convertToSumFormat(Double value){
        return numericFormat("#,###.00", value);
    }
}
