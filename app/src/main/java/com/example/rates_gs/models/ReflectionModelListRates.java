package com.example.rates_gs.models;

import com.example.rates_gs.R;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.util.ArrayList;
import java.util.List;

public class ReflectionModelListRates {

    private List<CurrencyRate> currencyRateList;

    public List<CurrencyRate> getCurrencyRateList() {
        return currencyRateList;
    }

    public void setCurrencyRateList(List<CurrencyRate> currencyRateList) {
        this.currencyRateList = currencyRateList;
    }

    public ReflectionModelListRates(RevolutApiResponse revolutApiResponse) {
        //todo insert DI

        currencyRateList = new ArrayList<>();
        String baseRate = revolutApiResponse.getBaseCurrency();
        RatesResponse r = revolutApiResponse.getRates();

        //reflection to get the currency's long name
        int resourceName = Integer.parseInt("R.string." + baseRate);
        String currencyLong = String.valueOf(resourceName);

        int flagName = Integer.parseInt("R.drawable.flag_" + baseRate);
        currencyRateList.add(0, new CurrencyRate(baseRate, currencyLong, flagName,r.getAUD()));
    }
}
