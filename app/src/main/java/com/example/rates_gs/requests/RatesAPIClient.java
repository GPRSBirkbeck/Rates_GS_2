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

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.rates_gs.util.Constants.NETWORK_TIMEOUT;

public class RatesAPIClient {
    private static final String TAG = "RatesAPIClient";
    private static RatesAPIClient instance;
    private MutableLiveData<List<CurrencyRate>> mCurrencyRates;
    private RetrieveRatesRunnable mRetrieveRatesRunnable;

    //TODO figure out what to do with this
    private ArrayList<CurrencyRate> currencyRatesDataSet = new ArrayList<>();

    //TODO figure out if we'd be better of with a regular call and only have the observables at the end (viewmodel)

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
        //this is mimicking what it would be like to get the data from the webservices by calling the set method.
        if(currencyRatesDataSet.size()==0){
            setCurrencyRates();
        }
        MutableLiveData<List<CurrencyRate>> data = new MutableLiveData<>();
        //this sets our data to the currencyRatesDataSet which theoretically is called from the API and DB just not done eyt, having called this method
        data.setValue(currencyRatesDataSet);
        return data;
    }

    //TODO figure out why this list can only work with the same length as the list in MainActivity for interactions
    private void setCurrencyRates(){
        //instead of phony data below use real linked data
        //dummy data - to fill the arraylists
        currencyRatesDataSet.add(new CurrencyRate("Second set of Dollars", "USD",R.drawable.flag_usd,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("Zuid Afrikaanse Rand", "ZAR",R.drawable.flag_zar,130.00 ));
        currencyRatesDataSet.add(new CurrencyRate("Thai Bhat", "TBH",R.drawable.flag_thb,2450.00 ));
        currencyRatesDataSet.add(new CurrencyRate("Singapore Dollar", "SGD",R.drawable.flag_sgd,22.57 ));
        currencyRatesDataSet.add(new CurrencyRate("Swedish Krona", "SEK",R.drawable.flag_sek,146.12 ));
        currencyRatesDataSet.add(new CurrencyRate("German Krona", "SEK",R.drawable.flag_gbp,146.12 ));
        currencyRatesDataSet.add(new CurrencyRate("ZSwedish Krona", "SEK",R.drawable.flag_dkk,146.12 ));
    }


    //TODO refactor this in the
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
                Flowable<RevolutApiResponse> ratesApiResponse = getRates(baseRate);
                if (cancelRequest) {
                    List<CurrencyRate> list = new ArrayList<>();
                    Observable<RatesResponse> ratesObservable =
                            ratesApiResponse
                                    .toObservable()
                                    .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                                    .map(new Function<RevolutApiResponse, RatesResponse>() {
                                        @Override
                                        public RatesResponse apply(RevolutApiResponse revolutApiResponse) throws Exception {
                                            return revolutApiResponse.getRates();
                                        }
                                    });

                    Observable<Double> aud = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getAUD());
                                }
                            });
                    CurrencyRate currrencyRate1 = new CurrencyRate("Australian Dollar", "AUD", R.drawable.flag_aud, 1.00);
                    currrencyRate1.setRateDouble(aud.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> bgn = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getBGN());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Bulgarian Lev", "BGN", R.drawable.flag_bgn, 1.00);
                    currrencyRate1.setRateDouble(bgn.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> brl = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getBRL());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Brazilian Real", "BRL", R.drawable.flag_brl, 1.00);
                    currrencyRate1.setRateDouble(brl.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> cad = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getCAD());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Canadian Dollar", "CAD", R.drawable.flag_cad, 1.00);
                    currrencyRate1.setRateDouble(cad.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> chf = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getCHF());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Swiss Franc", "CHF", R.drawable.flag_chf, 1.00);
                    currrencyRate1.setRateDouble(chf.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> cny = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getCNY());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Chinese Yuan", "CNY", R.drawable.flag_cny, 1.00);
                    currrencyRate1.setRateDouble(cny.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> czk = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getCZK());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Czech Koruna", "CZK", R.drawable.flag_czk, 1.00);
                    currrencyRate1.setRateDouble(czk.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> dkk = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getDKK());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Danish Krone", "DKK", R.drawable.flag_dkk, 1.00);
                    currrencyRate1.setRateDouble(dkk.blockingLast());
                    list.add(currrencyRate1);

                    Observable<Double> eur = ratesObservable
                            .map(new Function<RatesResponse, Double>() {
                                @Override
                                public Double apply(RatesResponse rates) throws Exception {
                                    return (rates.getEUR());
                                }
                            });
                    currrencyRate1 = new CurrencyRate("Euro", "EUR", R.drawable.flag_eur, 1.00);
                    currrencyRate1.setRateDouble(eur.blockingLast());
                    list.add(currrencyRate1);

                    // TODO figure out how to handle this list
                    mCurrencyRates.postValue(list);
                    Log.d(TAG, "run: it worked");
                }
        }

/*        @Override
        public void run() {
            //This is the actual line of code that will run on the background thread
            try {
                Flowable<RevolutApiResponse> response = getRates(baseRate);
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
        }*/

        private Flowable<RevolutApiResponse> getRates(String baseRate) {
            return ServiceGenerator.getRecipeApi().getObservableRates(
                    baseRate
            );
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


