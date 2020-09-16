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
import com.example.rates_gs.viewmodels.MainActivityViewModel;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRateListener {
    //view elements
    public RatesService ratesService;
    private TextView usd_rate_textView;
    private TextView eur_rate_textView;
    private TextView brl_rate_textView;
    private TextView cad_rate_textView;
    private RecyclerView mRecyclerView;

    //adapter for our ViewModel
    private RatesListAdapter mRatesListAdapter;

    //variables -arraylists that populate the recyclerview
    private ArrayList<String> mRateNamesLong = new ArrayList<>();
    private ArrayList<String> mRateNamesShort = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<Double> mRateDouble = new ArrayList<>();

    //viewmodel object for running the viewmodel
    private MainActivityViewModel mMainActivityViewModel;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO tidy up this mainactivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RatesService.getRetrofitService();
        setTitle("Rates");

        Log.d(TAG, "onCreate: started");

        usd_rate_textView = findViewById(R.id.textview_num_usd);
        eur_rate_textView = findViewById(R.id.textview_num_eur);
        brl_rate_textView = findViewById(R.id.textview_num_brl);
        cad_rate_textView = findViewById(R.id.textview_num_cad);
        mRecyclerView = findViewById(R.id.currency_recycler_view);


        //instantiation of the viewmodelprovider
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //call our function to initiate this dataset
        initRecylcerView();
        setObservableBaseRate();
        subscribeObservers();
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

    private void setObservableBaseRate() {
        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        base_rate_editText.setText("100");

        //make an InitialValueObservable out of the base_rate_editText
        InitialValueObservable<CharSequence> baseRateInput =
                RxTextView.textChanges(base_rate_editText);
    }

    // this works with the onRateClick interface as defined in rateslistadapter and gets the position of the clicked item,
    // to return to the onclick method of the Rate_view_holder class


    private void initRecylcerView(){
        mRatesListAdapter = new RatesListAdapter(this);
        mRecyclerView.setAdapter(mRatesListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // this works with the onRateClick interface as defined in OnRateListener and gets the position of the clicked item,
    // to return to the onclick method of the Rate_view_holder class
    @Override
    public void onRatesClick(int position) {
        //mMainActivityViewModel.getCurrencyRates()
        //mRateNamesLong.get(position);
        //Toast.makeText(this, mRateNamesLong.get(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBaseRateClick(int position) {

    }


    //TODO make these all update concurrently (at present not the case and was the case when they were separate methods
    //this should all be in the viewModel
    //this class first builds a retrofit call, and then makes an observable for
    //the baserate, which is taken by making the edittext an observable using Jake Whartons RxBinding work
  /*  //it then builds an observable for the rates class
    private void getObservableRateCalls() {

        //refactor: make this a seperate service class
        //retrofit call for our rates from the revolut API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //This creates an instance of the ratesAPI interface we made
        RatesAPI ratesAPI = retrofit.create(RatesAPI.class);

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

        //create an observable by turning the instance of the ratesAPI into an observable of type Rates
        //it also refreshes every second
        Observable<Rates> ratesObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                        .map(new Function<RatesApiAllData, Rates>() {
                            @Override
                            public Rates apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return ratesApiAllData.getRates();
                            }
                        });
        //call the setDouble method to set all the edittext values to values created by the observables.
        setDouble(baseRateObservable, usd_rate_textView, getUsdObservableRate(ratesObservable));
        setDouble(baseRateObservable, eur_rate_textView, getEurbservableRate(ratesObservable));
        setDouble(baseRateObservable, brl_rate_textView, getBrlObservableRate(ratesObservable));
        setDouble(baseRateObservable, cad_rate_textView, getCadObservableRate(ratesObservable));
    }

    //each of the below four returns an observable double of the intended currency
    public Observable<Double> getUsdObservableRate(Observable<Rates> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<Rates, Double>() {
                            @Override
                            public Double apply(Rates rates) throws Exception {
                                return (rates.getuSD());
                            }
                        });

        return usdRatesApiAllDataObservable;
    }
    //as above
    public Observable<Double> getEurbservableRate(Observable<Rates> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<Rates, Double>() {
                            @Override
                            public Double apply(Rates rates) throws Exception {
                                return (rates.getCNY());
                            }
                        });

        return usdRatesApiAllDataObservable;
    }
    public Observable<Double> getBrlObservableRate(Observable<Rates> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<Rates, Double>() {
                            @Override
                            public Double apply(Rates rates) throws Exception {
                                return (rates.getBRL());
                            }
                        });

        return usdRatesApiAllDataObservable;
    }
    public Observable<Double> getCadObservableRate(Observable<Rates> ratesObservable){
        Observable<Double> usdRatesApiAllDataObservable =
                ratesObservable
                        .map(new Function<Rates, Double>() {
                            @Override
                            public Double apply(Rates rates) throws Exception {
                                return (rates.getCAD());
                            }
                        });

        return usdRatesApiAllDataObservable;
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
                            mRateDouble.set(0,aDouble);
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
    }*/
}