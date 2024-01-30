package com.udaya.virak_buntham.vetpickup.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {
    public static String separateThousandFormat(String fee){
        double amount = Double.parseDouble(fee);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.setDecimalFormatSymbols(decimalFormatSymbols);
        return formatter.format(amount);
    }
}
