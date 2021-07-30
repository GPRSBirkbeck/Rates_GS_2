package com.example.rates_gs.models;

import com.example.rates_gs.R;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ReflectionModelListRates {

    private List<CurrencyRate> currencyRateList;

    public List<CurrencyRate> getCurrencyRateList() {
        return currencyRateList;
    }

    public void setCurrencyRateList(List<CurrencyRate> currencyRateList) {
        this.currencyRateList = currencyRateList;
    }

    //TODO move this class, it DOES NOT belong here
    public void multiplyCRListByBaseRate(Double BaseRate){
        List<CurrencyRate> multiplyIt = this.currencyRateList;
        //Observable<Double> baseRateObservable = BaseRate;

        for(CurrencyRate currencyRate: multiplyIt){
            Double currentDouble = currencyRate.getRateDouble();
            Double finalDouble = currentDouble*BaseRate;
            currencyRate.setRateDouble(finalDouble);

            //this double combines the latest baserate and rate from the API and returns that double
/*            Observable<Double> obsInt = new Observable<Double>() {
                @Override
                protected void subscribeActual(Observer<? super Double> observer) {
                    currencyRate.getRateDouble();
                }*/
            };
/*

            Observable<Double> multipliedRateObservable =
                    (Observable<Double>) Observable.combineLatest(baseRateObservable, obsInt,
                            (a, b) -> (a * b))
                            //adding this to the observable to be subscribed to by subscriber seems to speed up the UI load
                            .observeOn(AndroidSchedulers.mainThread());

            currencyRate.setRateDouble(multipliedRateObservable.blockingLatest());*/
        }


    public ReflectionModelListRates(RatesResponse ratesResponse, String baseRate) {
        currencyRateList = new ArrayList<>();
        //String baseRate = revolutApiResponse.getBaseCurrency();
        RatesResponse r = ratesResponse;
        baseRate = baseRate;

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

            if (fieldName.equals(baseRate)) {
                //currencyRateList.add(0, new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, 1.00));
            } else {
                //field value as 0 for now
                double fieldValue = 0.00;

                try {
                    //getter name
                    String getter = "get" + fieldName;
                    Method m = c.getDeclaredMethod(getter, null);

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
