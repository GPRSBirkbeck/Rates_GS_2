package com.example.rates_gs.viewmodels;

import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.rates_gs.BR;
import com.example.rates_gs.BaseRateData;
import com.example.rates_gs.models.CurrencyRate;
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
   /* public class MainActivitityInner extends BaseObservable {
        private BaseRateData baseRateData;

        public MainActivitityInner() {
            this.baseRateData = new BaseRateData(0.0);
        }

        @Bindable
        public String getBaserate(){
            return baseRateData.getRateDouble();
        }
        public void setBaseRate(Double baseRate){
            if (baseRateData.getRateDouble() != baseRate) {
                baseRateData.getRateDouble() = baseRate;

                // React to the change.
                saveData();

                // Notify observers of a new value.
                notifyPropertyChanged(BR.remember_me);
            }

        }

        @Bindable
        public Boolean getRememberMe() {
            return data.rememberMe;
        }

        public void setRememberMe(Boolean value) {
            // Avoids infinite loops.
            if (data.rememberMe != value) {
                data.rememberMe = value;

                // React to the change.
                saveData();

                // Notify observers of a new value.
                notifyPropertyChanged(BR.remember_me);
            }
        }
    }*/
    //a mutable version of livedata: it can be changed
    //allows setting and posting - if not needed make it livedata
    //private MutableLiveData<List<CurrencyRate>> mCurrencyRates;

    private final MutableLiveData<Double> baseRateDouble = new MutableLiveData<>();

    private RatesRepository mRatesRepository;
    private CurrencyRate mBaseCurrencyRate;

    // Create a LiveData with a Double
    //private MutableLiveData<Double> mBaseRate;
    private MutableLiveData<String> baseRateNameShort;
    private MutableLiveData<String> baseRateNameLong;
    private MutableLiveData<List<CurrencyRate>> ratesTimesdByBasedRate;
    private LiveData<List<CurrencyRate>> regularRates;

    public MainActivityViewModel(){
        //TODO make a livedata for the ratesResponse from Client to here
        mRatesRepository = RatesRepository.getInstance();
        baseRateDouble.setValue(0.00);
        baseRateNameLong.setValue("Base Rate Default");
        baseRateNameShort.setValue("BAR");
        //regularRates.setValue(getRates().getValue());

        //LiveData<List<CurrencyRate>> ratesTimesBAR = Transformations.map(getBaseRateDouble(),multiplyRates(getRates(), getBaseRateDouble()))
        //ratesTimesdByBasedRate = (LiveData<List<CurrencyRate>>) Transformations.map(getRates(),multiplyRates(getRates()));
/*        LiveData<List<CurrencyRate>> ratesTimesBAR = Transformations.map(new Function<Double, LiveData<List<CurrencyRate>>>() {
            @Override
            public LiveData<List<CurrencyRate>> apply(@NotNull Double aDouble) throws Exception {
                it
            }

            @Override
            public Double apply(@NotNull LiveData<List<CurrencyRate>> listLiveData) throws Exception {
                MutableLiveData<List<CurrencyRate>> myList = new MutableLiveData<>();

                for(CurrencyRate currencyRate: myList.getValue()){
                    currencyRate.setRateDouble(currencyRate.getRateDouble()*Double);

                }
                return null;
            }
        })*/

        //baseRateNameLong = Transformations.map()
/*        ratesTimesdByBasedRate = Transformations.map(regularRates, baseRateDouble){
            ArrayList<CurrencyRate> basicList = new ArrayList<>();
            for(CurrencyRate rate : getRates().getValue()){
                CurrencyRate orignal_CR = rate;
                Double original_rate = orignal_CR.getRateDouble();
                Double newRate = original_rate*baseRateDouble.getValue();
                orignal_CR.setRateDouble(newRate);
                basicList.add(orignal_CR);

            }
            ratesTimesdByBasedRate.postValue(basicList);

        }*/

    }

/*    public LiveData<List<CurrencyRate>> multiplyRates(LiveData<List<CurrencyRate>> currencyRates, ){
        LiveData<List<CurrencyRate>> currencyFinal;

        List<CurrencyRate> multipliedRates = currencyRates.getValue();
        for(CurrencyRate currency: multipliedRates){
            currency.setRateDouble();

        }

        return currencyFinal;
    }*/


/*    public MutableLiveData<List<CurrencyRate>> getRatesTimesdByBasedRate() {
        return ratesTimesdByBasedRate;
    }

    public void setRatesTimesdByBasedRate(MutableLiveData<List<CurrencyRate>> ratesTimesdByBasedRate) {
        this.ratesTimesdByBasedRate = ratesTimesdByBasedRate;
    }*/

    public MutableLiveData<String> getBaseRateNameLong() {
        return baseRateNameLong;
    }

    public void setBaseRateNameLong(MutableLiveData<String> baseRateNameLong) {
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

    public void setmBaseRate(Double baseRateDouble){
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

    // TODO Add the logic for updating based on the baserate here
    public Observable<RatesResponse> getObservableData(String baseRate){
        return mRatesRepository.getObservableData(baseRate);
    }

    public LiveData<String> getBaseCurrencyName(){ return mRatesRepository.getBaseCurrencyName(); }


/*    public LiveData<Double> getBaseRate(){
        return mBaseRate;
    }*/
}
