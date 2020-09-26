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
            "Russian Ruble", "Swedish Krona","Singapore Dollar","Thai Baht","US Dollar", "South African Rand"
    };

    public static final String[] RATE_NAMES_SHORT = {
            "EUR", "AUD", "BGN", "BRL", "CAD",
            "CHF", "CNY",

            "CZK", "DKK", "GBP", "HKD", "HRK",
            "HUF", "IDR",

            "ILS", "INR", "ISK", "JPY", "KRW",
            "MXN", "MYR",

            "NOK", "NZD", "PHP", "PLN", "RON",
            "RUB", "SEK","SGD","THB","USD", "ZAR"
    };

    public static final String[] FLAGS = {
            "flag_eur", "flag_aud", "flag_bgn", "flag_brl", "flag_cad",
            "flag_chf", "flag_cny",

            "flag_czk", "flag_dkk", "flag_gbp", "flag_hkd", "flag_hrk",
            "flag_huf", "flag_idk",

            "flag_ils", "flag_inr", "flag_isk", "flag_jpy", "flag_krw",
            "flag_mxn", "flag_myr",

            "flag_nok", "flag_nzd", "flag_php", "flag_pln", "flag_ron",
            "flag_rub", "flag_sek","flag_sgd","flag_thb","flag_usd", "flag_zar"
    };

    public static final double[] DEFAULT_EXCHANGE_RATES = {
            1.00,2.00,3.00,4.00,5.00,6.00,7.00,
            8.00,9.00,10.00,11.00,12.00,13.00,14.00,
            15.00,16.00,17.00,18.00,19.00,20.00,21.00,
            22.00,23.00,24.00,25.00,26.00,27.00,28.00,29.00,30.00,31.00,32.00
    };
}
