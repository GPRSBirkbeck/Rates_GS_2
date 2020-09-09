package com.example.rates_gs.util;

import android.util.Log;

import com.example.rates_gs.models.CurrencyRate;

import java.util.List;

public class Testing {
    public static void printRates(List<CurrencyRate> list, String tag){
        for(CurrencyRate rate: list){
            Log.d(tag, "onChanged: "+ rate.getRateDouble());
        }
    }
}
