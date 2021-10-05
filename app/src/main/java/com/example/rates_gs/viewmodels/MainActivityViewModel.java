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
    private LiveData<String> baseRateNameShort = new MutableLiveData<>();
    private LiveData<Integer> baseRateNameLong = new MutableLiveData<>();
    private LiveData<Integer> baseRateFlag = new MutableLiveData<>();
    private LiveData<Double> baseRateDoubleLive = new MutableLiveData<>();

    private MutableLiveData<Double> oneRate;
    private Double boringDouble;


    public MainActivityViewModel() {
        //TODO make a livedata for the ratesResponse from Client to here
        mRatesRepository = RatesRepository.getInstance();
        //baseRateNameShort.setValue("Hi");
        this.baseRateNameShort = getBaseCurrencyName();
        this.baseRateNameLong = getmBaseCurrencyNameLong();
        this.baseRateFlag = getmFlag();

        //to set baserate to 1 when it starts
        oneRate = new MutableLiveData<>();
        oneRate.postValue(0.00);
        this.baseRateDoubleLive = oneRate;
        this.baseRateDoubleLive = getBaseRateDoubleLive();
        this.boringDouble = 1.00;

    }
    public LiveData<Double> getBaseRateDoubleLive() { return baseRateDoubleLive; }

    public void setBaseRateDoubleLive(LiveData<Double> baseRateDoubleLive) {
        this.baseRateDoubleLive = baseRateDoubleLive;
    }



    public LiveData<Integer> getBaseRateFlag() {
        return baseRateFlag;
    }

    public void setBaseRateFlag(MutableLiveData<Integer> baseRateFlag) {
        this.baseRateFlag = baseRateFlag;
    }

    public LiveData<Integer> getBaseRateNameLong() {
        return baseRateNameLong;
    }

    public void setBaseRateNameLong(MutableLiveData<Integer> baseRateNameLong) {
        this.baseRateNameLong = baseRateNameLong;
    }

    public LiveData<String> getBaseRateNameShort() {
        return baseRateNameShort;
    }

    public void setBaseRateNameShort(String baseRateNameShort) {
        MutableLiveData<String> thisData = new MutableLiveData<>();
        thisData.postValue(baseRateNameShort);

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

    public LiveData<Integer> getmBaseCurrencyNameLong() { return mRatesRepository.getmBaseCurrencyNameLong(); }
    public LiveData<Integer> getmFlag() { return mRatesRepository.getmFlag(); }

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

    public Double getBoringDouble() {
        return boringDouble;
    }

    public void setBoringDouble(Double boringDouble) {
        this.boringDouble = boringDouble;
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, MutableLiveData<Integer> baseRateFlag) {
        imageView.setImageResource(baseRateFlag.getValue());
    }
}
