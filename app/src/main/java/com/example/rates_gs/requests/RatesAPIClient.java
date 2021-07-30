package com.example.rates_gs.requests;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.AppExecutors;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.models.ReflectionModelListRates;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.rates_gs.util.Constants.NETWORK_TIMEOUT;

public class RatesAPIClient {

    private static final String TAG = "RatesAPIClient";
    private static RatesAPIClient instance;
    private MutableLiveData<RatesResponse> mCurrencyRatesResponse;
    private MutableLiveData<List<CurrencyRate>> mRates;
    private MutableLiveData<String> mBaseCurrencyName;
    private RetrieveRatesRunnable mRetrieveRatesRunnable;


    // Instance
    public static RatesAPIClient getInstance() {
        if (instance == null) {instance = new RatesAPIClient(); }
        return instance;}

    // class when built
    private RatesAPIClient() {
        mCurrencyRatesResponse = new MutableLiveData<>();
        mRates = new MutableLiveData<>();
        mBaseCurrencyName = new MutableLiveData<>();
    }

    // Getters
    public MutableLiveData<RatesResponse> getCurrencyRates() { return mCurrencyRatesResponse; }

    public MutableLiveData<List<CurrencyRate>> getRates(){ return mRates; }

    public MutableLiveData<String> getBaseCurrencyName(){ return mBaseCurrencyName; }

    //call the api on the network
    public void getRatesApi(String baseRate) {
        //reset the runnable if there is one already
        if (mRetrieveRatesRunnable != null) {mRetrieveRatesRunnable = null;}

        mRetrieveRatesRunnable = new RetrieveRatesRunnable(baseRate);
        //we use a future to set the timeout for the request, and it is getting the scheduled threadpool we built in the AppExecutors class
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRatesRunnable);
        //here is our timeout, referencing the constant 3000 miliseconds
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //this will run after those 3 second have passed
                //it will interrupt background thread from running the request to the API
                handler.cancel(true);
                //TODO let user know network has timed out
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }


    // runnable class used to retrieve the data from the restAPI
    private class RetrieveRatesRunnable implements Runnable {
        private String baseRate;
        boolean cancelRequest;

        public RetrieveRatesRunnable(String baseRate) {
            this.baseRate = baseRate;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //THIS is the actual line of code that will run on the background thread
            try {
                Response response = getRates(baseRate).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    RevolutApiResponse revolutApiResponse = (RevolutApiResponse) response.body();
                    //may need to improve this line of code
                    //if we dont have any in our list yet (sub 15 as 15 is the amount we load per time)
                    //here we are sending value to the livedata
                    //postvalue used for background thread
                    List<CurrencyRate> currencyRates= new ArrayList<>();
                    assert revolutApiResponse != null;
                    String baseRateName = revolutApiResponse.getBaseCurrency();
                    mBaseCurrencyName.postValue(baseRateName);
                    ReflectionModelListRates modelListRates = new ReflectionModelListRates(revolutApiResponse.getRates(), revolutApiResponse.getBaseCurrency());

                    mRates.postValue(modelListRates.getCurrencyRateList());


                    mCurrencyRatesResponse.postValue(revolutApiResponse.getRates());
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mCurrencyRatesResponse.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mCurrencyRatesResponse.postValue(null);
            }
        }

        private Call<RevolutApiResponse> getRates(String baseRate) {
            return ServiceGenerator.getRatesApi().getCallableRates(
                    baseRate
            );
        }

        private void cancelRequest() {
            Log.d(TAG, "instance initializer: cancelling the search request");
            cancelRequest = true;
        }
    }
}


