package com.example.rates_gs.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.models.ReflectionModelListRates;
import com.example.rates_gs.requests.RatesAPIClient;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * THis uses the singleton pattern, which is needed for accessing webservices or accessing database
 * it avoids having a bunch of open connections to webservices, apis or caches
 */
//TODO research singleton pattern

public class RatesRepository {

    //the data gets into the VM from this repository class.
    private static RatesRepository instance;
    private RatesAPIClient mRatesAPIClient;

    //return method for our instance
    public static RatesRepository getInstance() {
        //TODO put an if in here to check whether or not the connection has been made to the API
        if (instance == null) {instance = new RatesRepository();}
        return instance;
    }
    private RatesRepository(){
        mRatesAPIClient = RatesAPIClient.getInstance();
    }

    public LiveData<RatesResponse> getCurrencyRates(){
        return mRatesAPIClient.getCurrencyRates();
    }

    public LiveData<List<CurrencyRate>> getRates(){
        return mRatesAPIClient.getRates();
    }

    public LiveData<String> getBaseCurrencyName(){ return mRatesAPIClient.getBaseCurrencyName(); }

    public LiveData<Integer> getmBaseCurrencyNameLong() { return mRatesAPIClient.getmBaseCurrencyNameLong(); }
    public LiveData<Integer> getmFlag() { return mRatesAPIClient.getmFlag(); }

    //method below takes inputs for our client search query
    public void searchRates(String baseRate){
        mRatesAPIClient.getRatesApi(baseRate);
    }

}
