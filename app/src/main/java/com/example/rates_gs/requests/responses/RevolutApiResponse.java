package com.example.rates_gs.requests.responses;

import com.example.rates_gs.requests.responses.RatesResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevolutApiResponse {


    @SerializedName("baseCurrency")
    @Expose
    private String baseCurrency;
    @SerializedName("rates")
    @Expose
    private RatesResponse rates;
/*
    private Object rates;
    public String returnRates() {
        return rates.toString();
    }*/

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public RatesResponse getRates() {
        return rates;
    }

    public void setRates(RatesResponse rates) {
        this.rates = rates;
    }

}

