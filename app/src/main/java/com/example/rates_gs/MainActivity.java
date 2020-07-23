package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity implements RatesListAdapter.OnRateListener{
    //view elements
    public RatesService ratesService;
    private TextView usd_rate_textView;
    private TextView eur_rate_textView;
    private TextView brl_rate_textView;
    private TextView cad_rate_textView;


    //vars
    private ArrayList<String> mRateNamesLong = new ArrayList<>();
    private ArrayList<String> mRateNamesShort = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RatesService.getRetrofitService();

        Log.d(TAG, "onCreate: started");

        usd_rate_textView = findViewById(R.id.textview_num_usd);
        eur_rate_textView = findViewById(R.id.textview_num_eur);
        brl_rate_textView = findViewById(R.id.textview_num_brl);
        cad_rate_textView = findViewById(R.id.textview_num_cad);

        getObservableRateCalls();


        //call our function to initiate this dataset
        initFlagList();

    }

    //TODO refactor so this is a list of rates
    private void initFlagList(){
        Log.d(TAG, "flagList: preparing flaglist");

        mRateNamesLong.add("USD");
        mRateNamesShort.add("Second set of Dollars");
        mImages.add(R.drawable.flag_usd);

        mRateNamesLong.add("Zar");
        mRateNamesShort.add("Zuid Afrikaanse Rand");
        mImages.add(R.drawable.flag_zar);


        mRateNamesLong.add("TBH");
        mRateNamesShort.add("Thai Bhat");
        mImages.add(R.drawable.flag_thb);

        mRateNamesLong.add("SGD");
        mRateNamesShort.add("Singapore Dollar");
        mImages.add(R.drawable.flag_sgd);

        mRateNamesLong.add("SEK");
        mRateNamesShort.add("Swedish Krona");
        //something about the type of this flag makes android studio happy
        mImages.add(R.drawable.flag_sek);

        //TODO make all flags of the preferred image type for android

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        RecyclerView recyclerView = findViewById(R.id.currency_recycler_view);

        //to populate our recyclerview i need to pass the adapter our context, ratenames long and short and imageviews
        RatesListAdapter ratesListAdapter = new RatesListAdapter(this, mRateNamesLong, mRateNamesShort, mImages, this);
        //use the above adapter as the adapter for this recyclerview
        recyclerView.setAdapter(ratesListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRateClick(int position) {
        mRateNamesLong.get(position);
        Toast.makeText(this, mRateNamesShort.get(position), Toast.LENGTH_SHORT).show();
        Collections.swap(mRateNamesLong, position, 0);
        Collections.swap(mRateNamesShort, position, 0);
        Collections.swap(mImages, position, 0);

        Log.d(TAG, "onRateClick: clicked");


    }















//TODO make these all update concurrently (at present not the case and was the case when they were separate methods
    //this should all be in the viewModel
    private void getObservableRateCalls() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RatesAPI ratesAPI = retrofit.create(RatesAPI.class);
        //Observable


        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        base_rate_editText.setText("100");

        InitialValueObservable<CharSequence> baseRateInput =
                RxTextView.textChanges(base_rate_editText);


        //this sets the baserate observable to zero when empty
        Observable<Double> baseRateObservable =
                baseRateInput.map(new Function<CharSequence, Double>() {
                    @Override
                    public Double apply(CharSequence charSequence) throws Exception {
                        if(charSequence.length()<1){
                            return 0.00;
                        }
                        else{
                            return Double.parseDouble(charSequence.toString());
                        }
                    }
                });

        Observable<Double> usdRatesApiAllDataObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))

                        .map(new Function<RatesApiAllData, Double>() {
                            @Override
                            public Double apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return (ratesApiAllData.getRates().getuSD());
                            }
                        });

        Observable<Double> multipliedRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, usdRatesApiAllDataObservable,
                        (a, b) -> (a * b))
                        //adding this to the observable to be subscribed to by subscriber seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());


        Observable<Double> eurRatesApiAllDataObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))

                        .map(new Function<RatesApiAllData, Double>() {
                            @Override
                            public Double apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return (ratesApiAllData.getRates().getIDR());
                            }
                        });

        Observable<Double> multipliedEurRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, eurRatesApiAllDataObservable,
                        (a, b) -> (a * b))
                        //adding this to the observable to be subscribed to by subscriber seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());


        Observable<Double> brlRatesApiAllDataObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))

                        .map(new Function<RatesApiAllData, Double>() {
                            @Override
                            public Double apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return (ratesApiAllData.getRates().getBRL());
                            }
                        });

        Observable<Double> multipliedBrlRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, brlRatesApiAllDataObservable,
                        (a, b) -> (a * b))
                        //adding this to the observable to be subscribed to by subscriber seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());



        Observable<Double> cadRatesApiAllDataObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))

                        .map(new Function<RatesApiAllData, Double>() {
                            @Override
                            public Double apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return (ratesApiAllData.getRates().getCAD());
                            }
                        });

        Observable<Double> multipliedCadRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, cadRatesApiAllDataObservable,
                        (a, b) -> (a * b))
                        //adding this to the observable to be subscribed to by subscriber seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());

        //subscriber
        multipliedRateObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        if(aDouble == 0){
                            usd_rate_textView.setText("");
                        }
                        else{
                            String strDouble = String.format("%.2f", aDouble);
                            usd_rate_textView.setText(strDouble);
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


        //subscriber
        multipliedEurRateObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        if(aDouble == 0){
                            eur_rate_textView.setText("");
                        }
                        else{
                            String strDouble = String.format("%.2f", aDouble);
                            eur_rate_textView.setText(strDouble);
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

        //subscriber
        multipliedBrlRateObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        if(aDouble == 0){
                            brl_rate_textView.setText("");
                        }
                        else{
                            String strDouble = String.format("%.2f", aDouble);
                            brl_rate_textView.setText(strDouble);
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

        //subscriber
        multipliedCadRateObservable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        if(aDouble == 0){
                            cad_rate_textView.setText("");
                        }
                        else{
                            String strDouble = String.format("%.2f", aDouble);
                            cad_rate_textView.setText(strDouble);
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

}