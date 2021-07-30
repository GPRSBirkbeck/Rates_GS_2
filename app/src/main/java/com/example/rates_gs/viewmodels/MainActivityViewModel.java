package com.example.rates_gs.viewmodels;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.rates_gs.BR;
import com.example.rates_gs.BaseRateData;
import com.example.rates_gs.R;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.models.ReflectionBaseRateData;
import com.example.rates_gs.models.ReflectionModelListRates;
import com.example.rates_gs.repositories.RatesRepository;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MainActivityViewModel extends ViewModel  {


    private MutableLiveData<Double> baseRateDouble;

    private RatesRepository mRatesRepository;

    // Create a LiveData with a Double
    private MutableLiveData<String> baseRateNameShort;
    private MutableLiveData<Integer> baseRateNameLong;
    private MutableLiveData<Integer> baseRateFlag;


    public MainActivityViewModel() {
        //TODO make a livedata for the ratesResponse from Client to here
        mRatesRepository = RatesRepository.getInstance();
        //baseRateNameShort.setValue(mRatesRepository.getBaseCurrencyName().getValue());
        //ReflectionBaseRateData reflectionBaseRateData = new ReflectionBaseRateData(baseRateNameShort.getValue());
        //baseRateNameLong.setValue(reflectionBaseRateData.getBaseRateLong());
        //baseRateFlag.setValue(reflectionBaseRateData.getBaseFieldFlagName());

    }

    public MutableLiveData<Integer> getBaseRateFlag() {
        return baseRateFlag;
    }

    public void setBaseRateFlag(MutableLiveData<Integer> baseRateFlag) {
        this.baseRateFlag = baseRateFlag;
    }

    public MutableLiveData<Integer> getBaseRateNameLong() {
        return baseRateNameLong;
    }

    public void setBaseRateNameLong(MutableLiveData<Integer> baseRateNameLong) {
        this.baseRateNameLong = baseRateNameLong;
    }

    public MutableLiveData<String> getBaseRateNameShort() {
        return baseRateNameShort;
    }

    public void setBaseRateNameShort(MutableLiveData<String> baseRateNameShort) {
        this.baseRateNameShort = baseRateNameShort;
    }

    public MutableLiveData<Double> getBaseRateDouble() {
        return baseRateDouble;
    }

    public void setBaseRate(Double baseRateDouble){
        this.baseRateDouble.setValue(baseRateDouble);
    }

    public LiveData<RatesResponse> getCurrencyRates(){
        return mRatesRepository.getCurrencyRates();
    }

    public LiveData<List<CurrencyRate>> getRates(){
        return mRatesRepository.getRates();
    }

    //method below takes inputs for our repository search method
    public void searchRates(String baseRate){
        mRatesRepository.searchRates(baseRate);
    }


    public LiveData<String> getBaseCurrencyName(){ return mRatesRepository.getBaseCurrencyName(); }

/*    public LiveData<Double> getBaseRate(){
        return mBaseRate;
    }*/

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, MutableLiveData<Integer> baseRateFlag) {
        imageView.setImageResource(baseRateFlag.getValue());
    }
}
