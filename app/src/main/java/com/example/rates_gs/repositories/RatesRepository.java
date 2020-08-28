package com.example.rates_gs.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.R;
import com.example.rates_gs.Rates;
import com.example.rates_gs.requests.RatesAPI;
import com.example.rates_gs.RatesApiAllData;
import com.example.rates_gs.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * THis uses the singleton pattern, which is needed for accessing webservices or accessing database
 * it avoids having a bunch of open connections to webservices, apis or caches
 */
//TODO research singleton pattern

public class RatesRepository {

    //the data gets into the VM from this repository class.
    //TODO add methods for accessing webservices and databases here

    private static RatesRepository instance;
    private ArrayList<CurrencyRate> currencyRatesDataSet = new ArrayList<>();

    //return method for our instance
    public static RatesRepository getInstance() {
        //TODO put an if in here to check whether or not the connection has been made to the API
        if (instance == null) {
            instance = new RatesRepository();
        }
        return instance;
    }

    //this is the method to get cache, database or API or whatever, improve this.
    public MutableLiveData<List<CurrencyRate>> getCurrencyRates() {
        //this is mimicking what it would be like to get the data from the webservices by calling the set method.
        setCurrencyRates();
        MutableLiveData<List<CurrencyRate>> data = new MutableLiveData<>();
        //this sets our data to the currencyRatesDataSet which theoretically is called from the API and DB just not done eyt, having called this method
        data.setValue(currencyRatesDataSet);
        return data;

    }

    private void setCurrencyRates() {
        //TODO: refactor this into a network class

        //refactor: make this a seperate service class
        //retrofit call for our rates from the revolut API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //This creates an instance of the ratesAPI interface we made
        RatesAPI ratesAPI = retrofit.create(RatesAPI.class);

        Observable<Rates> ratesObservable =
                ratesAPI.getObservableRates("GBP")
                        .toObservable()
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                        .map(new Function<RatesApiAllData, Rates>() {
                            @Override
                            public Rates apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return ratesApiAllData.getRates();
                            }
                        });


        //instead of phony data below use real linked data

        //dummy data - to fill the arraylists
        currencyRatesDataSet.add(new CurrencyRate("Second set of Dollars", "USD", R.drawable.flag_usd, 100.00));
        currencyRatesDataSet.add(new CurrencyRate("Zuid Afrikaanse Rand", "ZAR", R.drawable.flag_zar, 100.00));
        currencyRatesDataSet.add(new CurrencyRate("Thai Bhat", "TBH", R.drawable.flag_thb, 100.00));
        currencyRatesDataSet.add(new CurrencyRate("Singapore Dollar", "SGD", R.drawable.flag_sgd, 100.00));
        currencyRatesDataSet.add(new CurrencyRate("Swedish Krona", "SEK", R.drawable.flag_sek, 100.00));
    }

/*
    //each of the below four returns an observable double of the intended currency
    public Observable<Double> getUsdObservableRate(Observable<Rates> ratesObservable) {
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<Rates, Double>() {
                            @Override
                            public Double apply(Rates rates) throws Exception {
                                return (rates.getuSD());
                            }
                        });

        return usdRatesApiAllDataObservable;
    }

    //this observable takes an observable baserate, an edittext textview, and an observable rate from the API
    public Double setDouble(Observable<Double> ratesDoubleObservable){

        Double returnDouble = 1.00;

        //this double combines the latest baserate and rate from the API and returns that double
        Observable<Double> multipliedRateObservable = ratesDoubleObservable;

        //this subscriber is used to set the text of your textview to the double created in the previous observer
        multipliedRateObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Double aDouble) {
                        if(aDouble == 0){
                            returnDouble = 0.00;
                        }

                        else{
                            returnDouble = aDouble;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Onerror: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return returnDouble[0];
    }*/
}
