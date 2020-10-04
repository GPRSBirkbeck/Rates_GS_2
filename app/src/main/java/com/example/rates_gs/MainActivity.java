package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rates_gs.adapters.OnRateListener;
import com.example.rates_gs.adapters.RatesListAdapter;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.viewmodels.MainActivityViewModel;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnRateListener {
    //view elements
    private TextView usd_rate_textView;
    private TextView eur_rate_textView;
    private TextView brl_rate_textView;
    private TextView cad_rate_textView;
    private RecyclerView mRecyclerView;

    //adapter for our ViewModel
    private RatesListAdapter mRatesListAdapter;
    //viewmodel object for running the viewmodel
    private MainActivityViewModel mMainActivityViewModel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO tidy up this mainactivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Rates");
        Log.d(TAG, "onCreate: started");
        usd_rate_textView = findViewById(R.id.textview_num_usd);
        eur_rate_textView = findViewById(R.id.textview_num_eur);
        brl_rate_textView = findViewById(R.id.textview_num_brl);
        cad_rate_textView = findViewById(R.id.textview_num_cad);
        mRecyclerView = findViewById(R.id.currency_recycler_view);

        //instantiation of the viewmodelprovider
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //call our functions
        subscribeObservers();
        setObservableRates("ZAR");
        initRecylcerView();
        getObservableBaseRate();
        testRetrofitGetRequest();
    }

    public void subscribeObservers(){
        mMainActivityViewModel.getCurrencyRates().observe(this, new androidx.lifecycle.Observer<List<CurrencyRate>>() {
            @Override
            public void onChanged(List<CurrencyRate> currencyRates) {
                if(currencyRates != null){
                    //we are viewing livedata so that the data doesnt change if the user changes state (e.g. screen lock)
                    //we want the adapter below to be notified if changes are made to our livedata
                    mRatesListAdapter.setRates(currencyRates);
                }
            }
        });
    }

    //method below takes inputs for our repository search method
    public void searchRatesApi(String baseRate){
        mMainActivityViewModel.searchRates(baseRate);
    }
    private void testRetrofitGetRequest(){
        searchRatesApi("ZAR");
    }

    private void initRecylcerView(){
        mRatesListAdapter = new RatesListAdapter(this);
        mRecyclerView.setAdapter(mRatesListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // this works with the onRateClick interface as defined in OnRateListener and gets the position of the clicked item,
    // to return to the onclick method of the RateViewHolder class
    //all the interface does is get the position of the rate
    @Override
    public void onRatesClick(int position) {
        //mMainActivityViewModel.getCurrencyRates()
        //mRateNamesLong.get(position);
        //Toast.makeText(this, mRateNamesLong.get(position), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Clicked me!" + mRatesListAdapter.getItemCount() , Toast.LENGTH_SHORT).show();
        mRatesListAdapter.swapRates(position);
        List list = mMainActivityViewModel.getCurrencyRates().getValue();
        CurrencyRate myrate = (CurrencyRate) list.get(position);
        String baserate = myrate.getRateNameShort();
        setObservableRates(baserate);
        Toast.makeText(this, "Clicked me!" + baserate, Toast.LENGTH_SHORT).show();
    }

    private Observable<Double> getObservableBaseRate() {
        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        base_rate_editText.setText("100");

        //make an InitialValueObservable out of the base_rate_editText
        InitialValueObservable<CharSequence> baseRateInput =
                RxTextView.textChanges(base_rate_editText);

        //map the baserate input so that it returns zero if empty, and a double of its value otherwise
        Observable<Double> baseRateObservable =
                baseRateInput.map(new Function<CharSequence, Double>() {
                    @Override
                    public Double apply(CharSequence charSequence) throws Exception {
                        if(charSequence.length()<1){
                            //this sets the baserate observable to zero when empty
                            return 0.00;
                        }
                        else{
                            return Double.parseDouble(charSequence.toString());
                        }
                    }
                });
        return baseRateObservable;
    }

    //this observable takes an observable baserate, an edittext textview, and an observable rate from the API
    public void setDouble(Observable<Double> baseRateObservable, TextView textView, Observable<Double> ratesDoubleObservable){

        //this double combines the latest baserate and rate from the API and returns that double
        Observable<Double> multipliedRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, ratesDoubleObservable,
                        (a, b) -> (a * b))
                        //adding this to the observable to be subscribed to by subscriber seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());

        //this subscriber is used to set the text of your textview to the double created in the previous observer
        multipliedRateObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Double aDouble) {
                        if(aDouble == 0){
                            textView.setText("");
                        }
                        else{
                            String strDouble = String.format("%.2f", aDouble);
                            textView.setText(strDouble);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Onerror: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //TODO make these all update concurrently (at present not the case and was the case when they were separate methods
    //this should all be in the viewModel
    //this class first builds a retrofit call, and then makes an observable for
    //the baserate, which is taken by making the edittext an observable using Jake Whartons RxBinding work
    //it then builds an observable for the rates class
    private void setObservableRates(String baseRate) {
        Observable<RatesResponse> ratesObservable = mMainActivityViewModel.getObservableData(baseRate);

        //call the setDouble method to set all the edittext values to values created by the observables.
        setDouble(getObservableBaseRate(), usd_rate_textView, getUsdObservableRate(ratesObservable));
        setDouble(getObservableBaseRate(), eur_rate_textView, getEurbservableRate(ratesObservable));
        setDouble(getObservableBaseRate(), brl_rate_textView, getBrlObservableRate(ratesObservable));
        setDouble(getObservableBaseRate(), cad_rate_textView, getCadObservableRate(ratesObservable));
    }

    //each of the below four returns an observable double of the intended currency
    public Observable<Double> getUsdObservableRate(Observable<RatesResponse> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<RatesResponse, Double>() {
                            @Override
                            public Double apply(RatesResponse rates) throws Exception {
                                return (rates.getuSD());
                            }
                        });
        return usdRatesApiAllDataObservable;
    }

    public Observable<Double> getEurbservableRate(Observable<RatesResponse> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<RatesResponse, Double>() {
                            @Override
                            public Double apply(RatesResponse rates) throws Exception {
                                return (rates.getCNY());
                            }
                        });
        return usdRatesApiAllDataObservable;
    }

    public Observable<Double> getBrlObservableRate(Observable<RatesResponse> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<RatesResponse, Double>() {
                            @Override
                            public Double apply(RatesResponse rates) throws Exception {
                                return (rates.getBRL());
                            }
                        });
        return usdRatesApiAllDataObservable;
    }

    public Observable<Double> getCadObservableRate(Observable<RatesResponse> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<RatesResponse, Double>() {
                            @Override
                            public Double apply(RatesResponse rates) throws Exception {
                                return (rates.getCAD());
                            }
                        });
        return usdRatesApiAllDataObservable;
    }
}