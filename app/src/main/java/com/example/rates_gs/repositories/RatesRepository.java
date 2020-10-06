package com.example.rates_gs.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.R;
import com.example.rates_gs.requests.RatesAPIClient;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.RatesAPI;
import com.example.rates_gs.requests.responses.RevolutApiResponse;
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
    //private ArrayList<CurrencyRate> currencyRatesDataSet = new ArrayList<>();
    private RatesAPIClient mRatesAPIClient;

    //return method for our instance
    public static RatesRepository getInstance() {
        //TODO put an if in here to check whether or not the connection has been made to the API
        if (instance == null) {
            instance = new RatesRepository();
        }
        return instance;
    }
    private RatesRepository(){
        mRatesAPIClient = RatesAPIClient.getInstance();
    }
/*    public LiveData<List<CurrencyRate>> getCurrencyRates(){
        return mRatesAPIClient.getRates();
    }*/
    public LiveData<RevolutApiResponse> getCurrencyRates(){
        return mRatesAPIClient.getRates();
    }

    //our observable rates
    public Observable<RatesResponse> getObservableData(String baseRate){
        return mRatesAPIClient.getObservableData(baseRate);
    }

    //method below takes inputs for our client search query
    public void searchRates(String baseRate){
        mRatesAPIClient.getRatesApi(baseRate);
    }

}
