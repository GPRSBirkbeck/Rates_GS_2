package com.example.rates_gs.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.repositories.RatesRepository;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.util.List;

import io.reactivex.Observable;

public class MainActivityViewModel extends ViewModel {
    //a mutable version of livedata: it can be changed
    //allows setting and posting - if not needed make it livedata
    //private MutableLiveData<List<CurrencyRate>> mCurrencyRates;
    private RatesRepository mRatesRepository;
    private CurrencyRate mBaseRate;

    public MainActivityViewModel(){
        //TODO make a livedata for the ratesResponse from Client to here
        mRatesRepository = RatesRepository.getInstance();
    }

    public LiveData<RevolutApiResponse> getCurrencyRates(){
        return mRatesRepository.getCurrencyRates();
    }

    //method below takes inputs for our repository search method
    public void searchRates(String baseRate){
        mRatesRepository.searchRates(baseRate);
    }

    // TODO Add the logic for updating based on the baserate here
    public Observable<RatesResponse> getObservableData(String baseRate){
        return mRatesRepository.getObservableData(baseRate);
    }
}
