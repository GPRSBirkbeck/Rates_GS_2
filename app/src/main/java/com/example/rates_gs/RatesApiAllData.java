package com.example.rates_gs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatesApiAllData {


    @SerializedName("baseCurrency")
    @Expose
    private String baseCurrency;
    @SerializedName("rates")
    @Expose
    private Rates rates;
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

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

}

