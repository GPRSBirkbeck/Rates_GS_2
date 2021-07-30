package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rates_gs.adapters.OnRateListener;
import com.example.rates_gs.adapters.RatesListAdapter;
import com.example.rates_gs.databinding.ActivityMainBinding;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.models.ReflectionModelListRates;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;
import com.example.rates_gs.viewmodels.MainActivityViewModel;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.jetbrains.annotations.NotNull;

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
    private TextView textView_currency_short;
    private TextView textView_currency_long;
    private RecyclerView mRecyclerView;
    private String mBaseRate;
    private String mBaseCurrencyName;
    private ImageView baseImage;

    //adapter for our ViewModel
    private RatesListAdapter mRatesListAdapter;
    //viewmodel object for running the viewmodel
    private MainActivityViewModel mMainActivityViewModel;
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    //TODO In general, moving code out of the activity is great for maintainability and testability.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO tidy up this mainactivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Rates");
        Log.d(TAG, "onCreate: started");

        mRecyclerView = findViewById(R.id.currency_recycler_view);
        textView_currency_short = findViewById(R.id.textView_currency_short);
        textView_currency_long = findViewById(R.id.textView_currency_long);
        baseImage = findViewById(R.id.imageButton_Flag);

        //instantiation of the viewmodelprovider
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mBaseRate = "EUR";
        //call our functions
        subscribeObservers();

        //call functions for rateslistobervers
        //setObservableRatesList("ZAR");


        //TODO figure out if i need this or whether the line below suffices? - if this, scrap the "setContentView(R.layout.activity_main);"
        //ActivityMainBinding binding2 = DataBindingUtil.setContentView(this, R.layout.activity_main);


        //The below is probably right
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setViewmodel(mMainActivityViewModel);
        binding.setLifecycleOwner(this);

        setContentView(binding.getRoot());
        setContentView(binding.getRoot());

        initRecylcerView();
        getObservableBaseRate();
        subscribeBaseRate();

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

    public void subscribeBaseRate(){
        mMainActivityViewModel.getBaseRateDouble().observe(this, Double -> binding.editTextBaseRate.setText(Double + ""));
    }


    public void subscribeObservers(){
        mMainActivityViewModel.getRates().observe(this, new androidx.lifecycle.Observer<List<CurrencyRate>>() {
            @Override
            public void onChanged(List<CurrencyRate> currencyRates) {
                if(currencyRates!=null){
                    //we are viewing livedata so that the data doesnt change if the user changes state (e.g. screen lock)
                    List<CurrencyRate> modifiedRates = currencyRates;
                    //modifiedRates.add(new CurrencyRate(mMainActivityViewModel.getBaseCurrencyName().getValue(),))
                    double firstrate = currencyRates.get(0).getRateDouble();
                    for (int i = 1; i < modifiedRates.size(); i++) {

                        Double currentDouble = modifiedRates.get(i).getRateDouble();
                        Double finalDouble = currentDouble*firstrate;
                        double roundOff = Math.round(finalDouble * 100.0) / 100.0;

                        modifiedRates.get(i).setRateDouble(roundOff);
                    }

                    mRatesListAdapter.setRates(modifiedRates);
                    CurrencyRate myrate = createRate(mMainActivityViewModel.getBaseCurrencyName().getValue());
                    setBaseRateXML(myrate);
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
        List list = new ReflectionModelListRates(mMainActivityViewModel.getCurrencyRates().getValue(), mBaseRate).getCurrencyRateList();
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

/*
    private Double getPlainBaseRate() {
        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        //base_rate_editText.setText("1");

        //make an InitialValueObservable out of the base_rate_editText
        InitialValueObservable<CharSequence> baseRateInput =
                RxTextView.textChanges(base_rate_editText);
        final double[] returndouble = new double[1];
        //map the baserate input so that it returns zero if empty, and a double of its value otherwise
        Double baseRateObservable =
                baseRateInput.map(new Function<CharSequence, Double>() {
                    @Override
                    public Double apply(@NotNull CharSequence charSequence) throws Exception {
                        if (charSequence.length() < 1) {
                            //this sets the baserate observable to zero when empty
                            return returndouble[0] = 0.00;
                        } else {
                            return returndouble[0] = Double.parseDouble(charSequence.toString());
                        }
                        //return returndouble[0];
                    }
                    //return returndouble[0];
                });


*/
/*                baseRateInput.map(new Function<CharSequence, Double>() {
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
                });*//*

        //return baseRateObservable;
    }

*/

    private CurrencyRate createRate(String rateNameShort){

        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        base_rate_editText.setText("1");

        String fieldName = rateNameShort;
        //currency name R.string id
        int currency_long_name_id = 0;
        //flag id
        int fieldFlagName = 0;
        String fieldNameLowerCase = fieldName.toLowerCase();
        String flagName = "flag_" + fieldNameLowerCase;

        try {
            currency_long_name_id = R.string.class.getField(fieldName).getInt(null);
            fieldFlagName = R.drawable.class.getField(flagName).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if(currency_long_name_id == 0){
            //exception handling - set currency name to unknown if currency isnt known
            currency_long_name_id = R.string.UNK;
        }

        if(fieldFlagName == 0){
            //exception handling - set currency flag to revolut logo if flag not known
            fieldFlagName = R.drawable.flag_rev;
        }

        //return new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, getObservableBaseRate().blockingLatest());
        return new CurrencyRate(fieldName, currency_long_name_id, fieldFlagName, mMainActivityViewModel.getBaseRateDouble().getValue());
    }

    public void setBaseRateXML(CurrencyRate currencyRate){
        baseImage.setImageResource(currencyRate.getFlagImage());
        textView_currency_short.setText(currencyRate.getRateNameShort());
        textView_currency_long.setText(currencyRate.getRateNameLong());




    }



}