package com.example.rates_gs.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.AppExecutors;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.requests.responses.RatesResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import retrofit2.Response;

import static com.example.rates_gs.util.Constants.NETWORK_TIMEOUT;

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

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRatesRunnable);

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
    private class RetrieveRatesRunnable implements Runnable{
        private String baseRate;
        boolean cancelRequest;

        public RetrieveRatesRunnable(String baseRate){
            this.baseRate = baseRate;
            cancelRequest = false;
        }

        //this run is responsible for running the query
        @Override
        public void run() {
            //This is the actual line of code that will run on the background thread
            try {
                Response response = getRates(baseRate).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() ==200){
                    List<CurrencyRate> list = new ArrayList<>(((RatesResponse)response.body()).getResults());
                 }
                else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mCurrencyRate.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mCurrencyRate.postValue(null);
            }
        }
        private Flowable<RatesResponse> getRates(String baseRate){
            return ServiceGenerator.getRecipeApi().getObservableRates(
                    baseRate
            );
        }
        private void cancelRequest(){
            Log.d(TAG, "instance initializer: cancelling the search request");
            cancelRequest = true;
        }
    }
}
