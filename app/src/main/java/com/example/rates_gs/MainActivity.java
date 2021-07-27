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
import com.example.rates_gs.models.ReflectionModelListRates;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;
import com.example.rates_gs.viewmodels.MainActivityViewModel;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Maybe;
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
    private String mBaseRate;
    private String mBaseCurrencyName;

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

        mRecyclerView = findViewById(R.id.currency_recycler_view);

        //instantiation of the viewmodelprovider
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mBaseRate = "EUR";
        //call our functions
        subscribeObservers();

        //call functions for rateslistobervers
        //setObservableRatesList("ZAR");

        initRecylcerView();
        getObservableBaseRate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchRatesApi(mBaseRate);
        Timer timer = new Timer();
        timer.schedule(new refreshClass(), 0, 3000);

    }
    //TODO find a better way of running the below
    class refreshClass extends TimerTask {
        public void run() {
            searchRatesApi(mBaseRate);
        }
    }


    public void subscribeObservers(){
        mMainActivityViewModel.getRates().observe(this, new androidx.lifecycle.Observer<List<CurrencyRate>>() {
            @Override
            public void onChanged(List<CurrencyRate> currencyRates) {
                if(currencyRates!=null){
                    //we are viewing livedata so that the data doesnt change if the user changes state (e.g. screen lock)
                    List<CurrencyRate> modifiedRates = currencyRates;
                    for(CurrencyRate currencyRate: modifiedRates){
                        Double currentDouble = currencyRate.getRateDouble();
                        Double finalDouble = currentDouble*5;
                        double roundOff = Math.round(finalDouble * 100.0) / 100.0;

                        currencyRate.setRateDouble(roundOff);
                    }

                    mRatesListAdapter.setRates(modifiedRates);
                }
            }

        });
        mMainActivityViewModel.getBaseCurrencyName().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String s) {
                System.out.println(mBaseCurrencyName);

            }
        });
    }

    //method below takes inputs for our repository search method
    public void searchRatesApi(String baseRate) {
        mMainActivityViewModel.searchRates(baseRate);
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
        mRatesListAdapter.swapRates(position);
        //the next three lines are used to get the value of the rate we clicked, might be easier to build a function for this in model class
        //List list = new ArrayList();
        //ArrayList<CurrencyRate> list = new ArrayList<>(Objects.requireNonNull(mMainActivityViewModel.getRates().getValue()));
        //List list2 = list;
        List list = new ReflectionModelListRates(mMainActivityViewModel.getCurrencyRates().getValue()).getCurrencyRateList();
        CurrencyRate myRate = (CurrencyRate) list.get(position);
        mBaseRate = myRate.getRateNameShort();
        Toast.makeText(this, "New baserate is" + mBaseRate + " number of former currency is  " + " " + myRate.getRateDouble(), Toast.LENGTH_SHORT).show();
        double formerRate = myRate.getRateDouble();

        searchRatesApi(mBaseRate);
        //TODO make the line below work somehow
        myRate.setRateDouble(formerRate);
    }

    private Observable<Double> getObservableBaseRate() {
        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        base_rate_editText.setText("1");

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



}