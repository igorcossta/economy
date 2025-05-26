package com.github.igorcossta.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Format {

    public static String money(BigDecimal currency) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(currency);
    }
}
