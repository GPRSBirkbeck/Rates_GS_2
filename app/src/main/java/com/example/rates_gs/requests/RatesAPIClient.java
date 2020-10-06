package com.example.rates_gs.requests;

import android.telecom.Call;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.AppExecutors;
import com.example.rates_gs.R;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.example.rates_gs.util.Constants.NETWORK_TIMEOUT;

public class RatesAPIClient {
    private static final String TAG = "RatesAPIClient";
    private static RatesAPIClient instance;
    //TODO use the mutbleLiveData to update the values based on what comes out of the observable
    private MutableLiveData<List<CurrencyRate>> mCurrencyRates;
    private RetrieveRatesRunnable mRetrieveRatesRunnable;

    //TODO figure out if we'd be better of with a regular call and only have the observables at the end (viewmodel)
    public static RatesAPIClient getInstance() {
        if (instance == null) {
            instance = new RatesAPIClient();
        }
        return instance;
    }


    //TODO figure out what to do with this
    private ArrayList<CurrencyRate> currencyRatesDataSet = new ArrayList<>();
    private RatesAPIClient() {
        mCurrencyRates = new MutableLiveData<>();
    }

    public Observable getObservableData(String baseRate){
        RatesAPI ratesAPI = ServiceGenerator.getObservableRatesApi();
        Observable<RatesResponse> ratesObservable =
                ratesAPI.getObservableRates(baseRate)
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                        .map(new Function<RevolutApiResponse, RatesResponse>() {
                            @Override
                            public RatesResponse apply(RevolutApiResponse revolutApiResponse) throws Exception {
                                return revolutApiResponse.getRates();
                            }
                        });
        return ratesObservable;

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
        currencyRatesDataSet.add(new CurrencyRate("EUR","Euro",R.drawable.flag_eur,100.01));
        currencyRatesDataSet.add(new CurrencyRate("AUD","Australian Dollar", R.drawable.flag_aud,100.00));
        currencyRatesDataSet.add(new CurrencyRate("BGN","Bulgarian Lev", R.drawable.flag_bgn,100.00));
        currencyRatesDataSet.add(new CurrencyRate("BRL","Brazilian Real", R.drawable.flag_brl,100.00));
        currencyRatesDataSet.add(new CurrencyRate("CAD","Canadian Dollar", R.drawable.flag_cad,100.00));
        currencyRatesDataSet.add(new CurrencyRate("CHF","Swiss Franc", R.drawable.flag_chf,100.00));
        currencyRatesDataSet.add(new CurrencyRate("CNY","Chinese Yuan", R.drawable.flag_cny,100.00));

        currencyRatesDataSet.add(new CurrencyRate("CZK","Czech Koruna", R.drawable.flag_czk,100.00));
        currencyRatesDataSet.add(new CurrencyRate("DKK","Danish Krone", R.drawable.flag_dkk,100.00));
        currencyRatesDataSet.add(new CurrencyRate("GBP","Pound sterling", R.drawable.flag_gbp,100.00));
        currencyRatesDataSet.add(new CurrencyRate("HKD","Hong Kong Dollar", R.drawable.flag_hkd,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("HRK","Croatian Kuna", R.drawable.flag_hrk,100.00));
        currencyRatesDataSet.add(new CurrencyRate("HUF","Hungarian Forint", R.drawable.flag_huf,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("IDR","Indonesian Rupiah", R.drawable.flag_idr,100.00));

        currencyRatesDataSet.add(new CurrencyRate("ILS","Israeli New Shekel", R.drawable.flag_ils,100.00));
        currencyRatesDataSet.add(new CurrencyRate("INR","Indian Rupee", R.drawable.flag_inr,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("ISK","Icelandic Króna", R.drawable.flag_isk,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("JPY","Japanese Yen", R.drawable.flag_jpy,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("KRW","South Korean won", R.drawable.flag_krw,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("MXN","Mexican Peso", R.drawable.flag_mxn,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("MYR","Malaysian Ringgit", R.drawable.flag_myr,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("NOK","Norwegian Krone", R.drawable.flag_nok,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("NZD","New Zealand Dollar", R.drawable.flag_nzd,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("PHP","Philippine peso", R.drawable.flag_php,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("PLN","Poland złoty", R.drawable.flag_pln,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("RON","Romanian Leu", R.drawable.flag_ron,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("RUB","Russian Ruble", R.drawable.flag_rub,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("SEK","Swedish Krona", R.drawable.flag_sek,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("SGD","Singapore Dollar", R.drawable.flag_sgd,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("THB","Thai Baht", R.drawable.flag_thb,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("USD","US Dollar", R.drawable.flag_usd,100.00 ));
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

        @Override
        public void run() {

        }
    }
}


