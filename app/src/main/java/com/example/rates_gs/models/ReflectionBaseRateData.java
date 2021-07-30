package com.example.rates_gs.models;

import com.example.rates_gs.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class ReflectionBaseRateData {

    private int baseRateLong;
    private String baseRateShortName;
    private int baseFieldFlagName = 0;

    public int getBaseRateLong() {
        return baseRateLong;
    }

    public void setBaseRateLong(int baseRateLong) {
        this.baseRateLong = baseRateLong;
    }

    public String getBaseRateShortName() {
        return baseRateShortName;
    }

    public void setBaseRateShortName(String baseRateShortName) {
        this.baseRateShortName = baseRateShortName;
    }

    public int getBaseFieldFlagName() {
        return baseFieldFlagName;
    }

    public void setBaseFieldFlagName(int baseFieldFlagName) {
        this.baseFieldFlagName = baseFieldFlagName;
    }

    public ReflectionBaseRateData(String baseRate) {

        String baseRateShort = baseRate;
        //reflection to get the currency's long name
        baseRateShort = baseRateShort.toUpperCase();

        //currency name R.string id
        int currency_long_name_id = 0;

        //flag id
        int fieldFlagName = 0;
        String fieldNameLowerCase = baseRateShort.toLowerCase();
        String flagName = "flag_" + fieldNameLowerCase;

        try {
            currency_long_name_id = R.string.class.getField(baseRateShort).getInt(null);
            fieldFlagName = R.drawable.class.getField(flagName).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (currency_long_name_id == 0) {
            //exception handling - set currency name to unknown if currency isnt known
            currency_long_name_id = R.string.UNK;
        }

        if (fieldFlagName == 0) {
            //exception handling - set currency flag to revolut logo if flag not known
            fieldFlagName = R.drawable.flag_rev;
        }
        baseRateShortName = baseRateShort;
        baseRateLong = currency_long_name_id;
        baseFieldFlagName = fieldFlagName;

    }
}
