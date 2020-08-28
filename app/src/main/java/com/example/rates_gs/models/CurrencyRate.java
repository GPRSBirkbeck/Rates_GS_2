package com.example.rates_gs.models;

import java.util.ArrayList;

import io.reactivex.Observable;

public class CurrencyRate {

    //this class represents the POJO for the rates that are actually displayed in the ratesListAdapter
    private String rateNamesLong;
    private String rateNamesShort;
    private Integer flagImage;
    private Double rateDouble;

    //constructor
    public CurrencyRate(String rateNamesLong, String rateNamesShort, Integer flagImage, Double rateDouble) {
        this.rateNamesLong = rateNamesLong;
        this.rateNamesShort = rateNamesShort;
        this.flagImage = flagImage;
        this.rateDouble = rateDouble;
    }

    //getters and setters below
    public void setRateNamesLong(String rateNamesLong) {
        this.rateNamesLong = rateNamesLong;
    }

    public void setRateNamesShort(String rateNamesShort) {
        this.rateNamesShort = rateNamesShort;
    }

    public void setFlagImage(Integer flagImage) {
        this.flagImage = flagImage;
    }

    public void setRateDouble(Double rateDouble) {
        this.rateDouble = rateDouble;
    }

    public String getRateNamesLong() {
        return rateNamesLong;
    }

    public String getRateNamesShort() {
        return rateNamesShort;
    }

    public Integer getFlagImage() {
        return flagImage;
    }

    public Double getRateDouble() {
        return rateDouble;
    }


}
