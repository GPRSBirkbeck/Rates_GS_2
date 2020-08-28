package com.example.rates_gs.requests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.models.CurrencyRate;

import java.util.List;
import java.util.concurrent.Future;

public class RatesAPIClient {
    private static final String TAG = "RecipeApiClient";
    private static RatesAPIClient instance;
    private MutableLiveData<List<CurrencyRate>> mCurrencyRate;
    private RetrieveRatesRunnable mRetrieveRatesRunnable;

    public static RatesAPIClient getInstance(){
        if(instance == null){
            instance = new RatesAPIClient();
        }
        return instance;
    }
    private RatesAPIClient{
        mCurrencyRate = new MutableLiveData<>();
    }
    public LiveData<List<CurrencyRate>> getRates(){
        return mCurrencyRate;
    }
    public void getRatesApi(String baseRate){
        if(mRetrieveRatesRunnable != null){
            mRetrieveRatesRunnable = null;
        }
        mRetrieveRatesRunnable = new RetrieveRatesRunnable(baseRate);

        final Future handler = AppExecutors.get
    }




}
