package com.example.rates_gs.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.repositories.RatesRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    //a mutable version of livedata: it can be changed
    //allows setting and posting - if not needed make it livedata
    //private MutableLiveData<List<CurrencyRate>> mCurrencyRates;
    private RatesRepository mRatesRepository;
    private CurrencyRate mBaseRate;

    //private RatesRepository mRatesRepository;

    public MainActivityViewModel(){
        mRatesRepository = RatesRepository.getInstance();
    }

    //the equivalent of using getters and setters, livedata cant be editted, mutablelivedata can
    public LiveData<List<CurrencyRate>> getRates(){
        return mRatesRepository.getCurrencyRates();
    }

    //method below takes inputs for our repository search method
    public void searchRates(String baseRate){
        mRatesRepository.searchRates(baseRate);
    }
    // TODO Add the logic for updating based on the baserate here
/*
    public void init(){
        if(mCurrencyRates!=null){
            return;
        }
        //get instance of repository
        mRatesRepository = mRatesRepository.getInstance();
        //get the data from that repository
        mCurrencyRates = mRatesRepository.getCurrencyRates();

    }*/

    //a getter for our mutableLiveData mCurrencyRates
/*    public LiveData<List<CurrencyRate>> getCurrencyRates(){
        return mCurrencyRates;
    }*/
}
