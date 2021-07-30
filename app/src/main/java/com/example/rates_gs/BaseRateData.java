package com.example.rates_gs;

public class BaseRateData {
    private Double rateDouble;

    public Double getRateDouble() {
        return rateDouble;
    }

    public void setRateDouble(Double rateDouble) {
        this.rateDouble = rateDouble;
    }

    public BaseRateData(Double rateDouble) {
        this.rateDouble = rateDouble;
    }
}
