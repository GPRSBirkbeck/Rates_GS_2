package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.rates_gs.viewmodels.MainActivityViewModel;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity implements OnRateListener {
    //view elements
    private TextView textView_currency_short;
    private TextView textView_currency_long;
    private RecyclerView mRecyclerView;
    private String mBaseRate;
    private ImageView baseImage;

    //adapter for our ViewModel
    private RatesListAdapter mRatesListAdapter;

    //viewmodel object for running the viewmodel
    private MainActivityViewModel mMainActivityViewModel;
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private String connection_status;
    private boolean connection_changed;

    //TODO In general, moving code out of the activity is great for maintainability and testability.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO tidy up this mainactivity
        super.onCreate(savedInstanceState);
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //TODO figure out if i need this or whether the line below suffices? - if this, scrap the "setContentView(R.layout.activity_main);"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //The below is probably right
        binding.setLifecycleOwner(this);
        binding.setViewmodel(mMainActivityViewModel);
        //setContentView(R.layout.activity_main);
        setTitle("Rates");
        Log.d(TAG, "onCreate: started");

        mRecyclerView = findViewById(R.id.currency_recycler_view);
        textView_currency_short = findViewById(R.id.textView_currency_short);
        textView_currency_long = findViewById(R.id.textView_currency_long);
        baseImage = findViewById(R.id.imageButton_Flag);

        //instantiation of the viewmodelprovider
        //setContentView(binding.getRoot());
        mBaseRate = "EUR";
        //call our functions
        subscribeObservers();
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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(connection_changed){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //your method
                                Toast toast = Toast.makeText(getApplicationContext(), connection_status, Toast.LENGTH_SHORT);
                                toast.show();

                        }
                    });
                }


            }
        }, 0, 10000);//put here time 1000 milliseconds=1 second


    }
    //TODO find a better way of running the below
    class refreshClass extends TimerTask {
        public void run() {
            searchRatesApi(mBaseRate);
            checkStatus();
        }


    }


    public void subscribeBaseRate(){
    }


    public void subscribeObservers(){
        mMainActivityViewModel.getRates().observe(this, new androidx.lifecycle.Observer<List<CurrencyRate>>() {
            @Override
            public void onChanged(List<CurrencyRate> currencyRates) {
                if(currencyRates!=null){
                    //we are viewing livedata so that the data doesnt change if the user changes state (e.g. screen lock)
                    List<CurrencyRate> modifiedRates = currencyRates;
                    //modifiedRates.add(new CurrencyRate(mMainActivityViewModel.getBaseCurrencyName().getValue(),))
                    //double firstrate = currencyRates.get(0).getRateDouble();
                    for (int i = 0; i < modifiedRates.size(); i++) {

                        Double currentDouble = modifiedRates.get(i).getRateDouble();

                        Double roundOff = currentDouble;
                        try{
                            //TODO make this observe live data, not the boring double
                            roundOff = roundOff * mMainActivityViewModel.getBoringDouble();
                            roundOff = new BigDecimal(roundOff).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        catch (NullPointerException n){
                            roundOff = currentDouble;
                        }
                        modifiedRates.get(i).setRateDouble(roundOff);
                    }

                    mRatesListAdapter.setRates(modifiedRates);
                }
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
        EditText base_rate_editText = (EditText) findViewById(R.id.etbaserate);
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

    public void checkStatus(){

        Context context= this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean wifiConnected;
        boolean mobileDataConnected;
        if (activeNetwork!=null && activeNetwork.isConnected()){
            connection_changed = false;
            wifiConnected = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            mobileDataConnected = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            int duration = Toast.LENGTH_SHORT;

            if (wifiConnected){

                CharSequence text = "WIFI is Connected";
                if(connection_status != text){
                    connection_changed = true;
                    connection_status = (String) text;
                }
                //Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
            } else if (mobileDataConnected){
                CharSequence text = "Mobile data is Connected";
                if(connection_status != text){
                    connection_changed = true;
                    connection_status = (String) text;
                }
                //Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
            }
        } else {
            CharSequence text = "No data connection";
            if(connection_status != text){
                connection_changed = true;
                connection_status = (String) text;
            }
            //int duration = Toast.LENGTH_SHORT;
            //Toast toast = Toast.makeText(context, text, duration);
            //toast.show();
        }
    }
}