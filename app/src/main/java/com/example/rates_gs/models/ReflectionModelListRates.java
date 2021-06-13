package com.example.rates_gs.models;

import android.app.Application;
import android.os.Build;

import com.example.rates_gs.R;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
        currencyRateList = new ArrayList<>();
        String baseRate = revolutApiResponse.getBaseCurrency();
        RatesResponse r = revolutApiResponse.getRates();

/*        //reflection to get the currency's long name
        int resourceName = Integer.parseInt("R.string." + baseRate);
        String currencyLong = String.valueOf(resourceName);

        //flag name
        int flagName = Integer.parseInt("R.drawable.flag_" + baseRate);

        //reflection for method name
        Class c = r.getClass();
        Method m = null;
        try {
            m = c.getMethod("get"+baseRate);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            int result= (int) m.invoke(r);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        currencyRateList.add(0, new CurrencyRate(baseRate, currencyLong, flagName,r.getAUD()));*/

        //reflection for method name
        Class c = r.getClass();
        //get all getter methods
        ArrayList<Method> methods = new ArrayList<>();
        for(Method meths : c.getDeclaredMethods()){
                if(meths.getName().contains("get")){
                    methods.add(meths);
                }
        }
        //get all fields
        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(c.getDeclaredFields()));

        for(Field f : fields) {
            String fieldName = f.getName();
            //reflection to get the currency's long name
            fieldName = fieldName.toUpperCase();

            //currency name string
            String currency_long_name;
            //currency name R.string id
            int currency_long_name_id = 0;

            //flag id
            int fieldFlagName = 0;
            String fieldNameLowerCase = fieldName.toLowerCase();
            String flagName = "flag_" + fieldNameLowerCase;
            //Integer.parseInt("R.drawable.flag_" + fieldName);
            try {
                currency_long_name_id = R.string.class.getField(fieldName).getInt(null);
                fieldFlagName = R.drawable.class.getField(flagName).getInt(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if(currency_long_name_id != 0){
                currency_long_name = String.valueOf(currency_long_name_id);
            }
            else{
                currency_long_name = "Unknown Currency";
            }
            if(fieldFlagName == 0){
                fieldFlagName = R.drawable.flag_rev;
            }

            //flag name
            //int fieldFlagName = Integer.parseInt("R.drawable.flag_" + fieldName);

            double fieldValue = 0.00;
            try {
                fieldValue = f.getDouble(r);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            int name = R.string.AUD;

            if (fieldName.equals(baseRate)) {
                currencyRateList.add(0, new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, fieldValue));
            } else {
                currencyRateList.add(new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, fieldValue));
            }
        }
    }
}
