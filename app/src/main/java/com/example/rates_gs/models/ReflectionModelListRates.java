package com.example.rates_gs.models;

import android.app.Application;
import android.os.Build;

import com.example.rates_gs.R;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

            //currency name R.string id
            int currency_long_name_id = 0;

            //flag id
            int fieldFlagName = 0;
            String fieldNameLowerCase = fieldName.toLowerCase();
            String flagName = "flag_" + fieldNameLowerCase;




            try {
                currency_long_name_id = R.string.class.getField(fieldName).getInt(null);
                fieldFlagName = R.drawable.class.getField(flagName).getInt(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if(currency_long_name_id == 0){
                //exception handling - set currency name to unknown if currency isnt known
                currency_long_name_id = R.string.UNK;
            }

            if(fieldFlagName == 0){
                //exception handling - set currency flag to revolut logo if flag not known
                fieldFlagName = R.drawable.flag_rev;
            }
/*            try {
                if (!Modifier.isPublic(f.getModifiers())) {
                    f.setAccessible(true);
                }
                fieldValue = f.getDouble(r);
                //fieldValue = (double) f.get(r);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }*/
            if (fieldName.equals(baseRate)) {
                currencyRateList.add(0, new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, 1.00));
            } else {
                //field value as 0 for now
                double fieldValue = 0.00;

                try {
                    //getter name
                    String getter = "get" + fieldName;
                    Method m = c.getDeclaredMethod(getter, null);

                    Object[] response = new Object[] {r};
                    Object[] responses = new Object[] {response};

                    fieldValue = (double) m.invoke(r);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                currencyRateList.add(new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, fieldValue));
            }
        }
    }
}
