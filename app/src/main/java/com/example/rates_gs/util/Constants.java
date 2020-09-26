package com.example.rates_gs.util;

public class Constants {
    public static final String BASE_URL = "https://hiring.revolut.codes";
    public static final int NETWORK_TIMEOUT = 30000;

    //TODO figure out if i want to use these to inflate the rates
    public static final String[] RATE_NAMES_LONG = {
            "Euro", "Australian Dollar", "Bulgarian Lev", "Brazilian Real", "Canadian Dollar",
            "Swiss Franc", "Chinese Yuan",

            "Czech Koruna", "Danish Krone", "Pound sterling", "Hong Kong Dollar", "Croatian Kuna",
            "Hungarian Forint", "Indonesian Rupiah",

            "Israeli New Shekel", "Indian Rupee", "Icelandic Króna", "Japanese Yen", "South Korean won",
            "Mexican Peso", "Malaysian Ringgit",

            "Norwegian Krone", "New Zealand Dollar", "Philippine peso", "Poland złoty", "Romanian Leu",
            "Russian Ruble", "Swedish Krona","Singapore Dollar","Thai Baht","US Dollar"
    };

    public static final String[] RATE_NAMES_SHORT = {
            "EUR", "AUD", "BGN", "BRL", "CAD",
            "CHF", "CNY",

            "CZK", "DKK", "GBP", "HKD", "HRK",
            "HUF", "IDR",

            "ILS", "INR", "ISK", "JPY", "KRW",
            "MXN", "MYR",

            "NOK", "NZD", "PHP", "PLN", "RON",
            "RUB", "SEK","SGD","THB","USD"
    };

    public static final String[] FLAGS = {
            "flag_EUR", "flag_AUD", "flag_BGN", "flag_BRL", "flag_CAD",
            "flag_CHF", "flag_CNY",

            "flag_CZK", "flag_DKK", "flag_GBP", "flag_HKD", "flag_HRK",
            "flag_HUF", "flag_IDR",

            "flag_ILS", "flag_INR", "flag_ISK", "flag_JPY", "flag_KRW",
            "flag_MXN", "flag_MYR",

            "flag_NOK", "flag_NZD", "flag_PHP", "flag_PLN", "flag_RON",
            "flag_RUB", "flag_SEK","flag_SGD","flag_THB","flag_USD"
    };

    public static final double[] DEFAULT_EXCHANGE_RATES = {
            1.00,2.00,3.00,4.00,5.00,6.00,7.00,
            8.00,9.00,10.00,11.00,12.00,13.00,14.00,
            15.00,16.00,17.00,18.00,19.00,20.00,21.00,
            22.00,23.00,24.00,25.00,26.00,27.00,28.00,29.00,30.00,31.00
    };
}
