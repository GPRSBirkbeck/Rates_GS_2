package com.example.rates_gs.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rates_gs.models.CurrencyRate;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    //a mutable version of livedata: it can be changed
    //allows setting and posting - if not needed make it livedata
    private MutableLiveData<List<CurrencyRate>> mCurrencyRates;

    //a getter for our mutableLiveData mCurrencyRates
    public LiveData<List<CurrencyRate>> getCurrencyRates(){
        return mCurrencyRates;
    }
}
