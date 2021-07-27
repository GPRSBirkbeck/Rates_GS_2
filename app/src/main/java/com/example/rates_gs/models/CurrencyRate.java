package com.example.rates_gs.models;

import java.util.Collections;

public class CurrencyRate {

    //this class represents the POJO for the rates that are actually displayed in the ratesListAdapter
    private String rateNameShort;
    private int rateNameLong;
    private Integer flagImage;
    private Double rateDouble;

    //constructor
    public CurrencyRate(String rateNameShort, int rateNameLong, Integer flagImage, Double rateDouble) {
        this.rateNameShort = rateNameShort;
        this.rateNameLong = rateNameLong;
        this.flagImage = flagImage;
        this.rateDouble = rateDouble;
    }

    //getters and setters below
    public void setRateNameShort(String rateNameShort) {
        this.rateNameShort = rateNameShort;
    }

    public void setRateNameLong(int rateNameLong) {
        this.rateNameLong = rateNameLong;
    }

    public void setFlagImage(Integer flagImage) {
        this.flagImage = flagImage;
    }

    public void setRateDouble(Double rateDouble) {
        this.rateDouble = rateDouble;
    }

    public String getRateNameShort() {
        return rateNameShort;
    }

    public int getRateNameLong() {
        return rateNameLong;
    }

    public Integer getFlagImage() {
        return flagImage;
    }

    public Double getRateDouble() {
        return rateDouble;
    }


}
