package com.example.rates_gs.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.AppExecutors;
import com.example.rates_gs.R;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.rates_gs.util.Constants.NETWORK_TIMEOUT;

public class RatesAPIClient {
    private static final String TAG = "RatesAPIClient";
    private static RatesAPIClient instance;
    private MutableLiveData<List<CurrencyRate>> mCurrencyRates;
    private RetrieveRatesRunnable mRetrieveRatesRunnable;

    public static RatesAPIClient getInstance() {
        if (instance == null) {
            instance = new RatesAPIClient();
        }
        return instance;
    }

    private RatesAPIClient() {
        mCurrencyRates = new MutableLiveData<>();
    }

    public LiveData<List<CurrencyRate>> getRates() {
        return mCurrencyRates;
    }

    public void getRatesApi(String baseRate) {
        if (mRetrieveRatesRunnable != null) {
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
    private class RetrieveRatesRunnable implements Runnable {
        private String baseRate;
        boolean cancelRequest;

        public RetrieveRatesRunnable(String baseRate) {
            this.baseRate = baseRate;
            cancelRequest = false;
        }

        //this run is responsible for running the query
        @Override
        public void run() {
            //This is the actual line of code that will run on the background thread
            try {
                Response response = getRates(baseRate).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<CurrencyRate> list = new ArrayList<>();
                    double aud = ((RatesResponse) response.body()).getAUD();
                    CurrencyRate currrencyRate1 = new CurrencyRate("Australian Dollar", "AUD", R.drawable.flag_aud, aud);
                    list.add(currrencyRate1);
                    double bgn = ((RatesResponse) response.body()).getBGN();
                    currrencyRate1 = new CurrencyRate("US Dollar", "AUD", R.drawable.flag_bgn, bgn);
                    list.add(currrencyRate1);
                    mCurrencyRates.postValue(list);
                    Log.d(TAG, "run: it worked");
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mCurrencyRates.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mCurrencyRates.postValue(null);
            }
        }

        private Call<RevolutApiResponse> getRates(String baseRate) {
            return ServiceGenerator.getRecipeApi().getObservableRates(
                    baseRate
            );
/*            return revolutApiResponseToRatesResponse(ServiceGenerator.getRecipeApi().getObservableRates(
                    baseRate
            ));*/
        }

        //TODO figure out what to do with the below...
/*        private Observable<RatesResponse> getRates(String baseRate){
            return revolutApiResponseToRatesReponse(ServiceGenerator.getRecipeApi().getObservableRates(
                    baseRate
            ));
        }*/
        private void cancelRequest() {
            Log.d(TAG, "instance initializer: cancelling the search request");
            cancelRequest = true;
        }
    }
}
/*    private Observable<RatesResponse> observableRevolutApiResponseToRatesReponse(Flowable<RevolutApiResponse> revolutApiResponseFlowable){
        return revolutApiResponseFlowable.
                toObservable()
                .map(new Function<RevolutApiResponse, RatesResponse>() {
                    @Override
                    public RatesResponse apply(Call<RevolutApiResponse> revolutApiResponse) throws Exception {
                        return revolutApiResponse.getRates();
                    }
                });
    }*/
/*    private RatesResponse revolutApiResponseToRatesResponse(RevolutApiResponse revolutApiResponse){
        return revolutApiResponse.getRates();
    }*/


