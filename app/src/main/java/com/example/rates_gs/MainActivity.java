package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

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

public class MainActivity extends AppCompatActivity {
    public RatesService ratesService;
    private TextView textView;
    private TextView textView_result;
    private TextView textView_observable;
    private TextView usd_rate_textView;
    private TextView eur_rate_textView;
    private TextView brl_rate_textView;
    private TextView cad_rate_textView;


    Handler handler = new Handler();
    int waitInt = 1000; //1second
    Runnable runnable;
    private String TAG;
    Double usd_rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RatesService.getRetrofitService();

        usd_rate_textView = findViewById(R.id.textview_num_usd);
        eur_rate_textView = findViewById(R.id.textview_num_eur);
        brl_rate_textView = findViewById(R.id.textview_num_brl);
        cad_rate_textView = findViewById(R.id.textview_num_cad);

        //TODO find an efficient way of doing this
        getCadObservableRateCalls();
        getUsdObservableRateCalls();
        getEurObservableRateCalls();
        getBrlObservableRateCalls();


    }

    //this should all be in the viewModel
    private void getUsdObservableRateCalls() {

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
    }

    //this should all be in the viewModel
    //EUR
    private void getEurObservableRateCalls() {

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

        Observable<Double> multipliedRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, eurRatesApiAllDataObservable,
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
    }

    //BRL
    //this should all be in the viewModel
    private void getBrlObservableRateCalls() {

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

        Observable<Double> multipliedRateObservable =
                (Observable<Double>) Observable.combineLatest(baseRateObservable, brlRatesApiAllDataObservable,
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
    }

    //CAD
    //this should all be in the viewModel
    private void getCadObservableRateCalls() {

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

        Observable<Double> multipliedRateObservable =
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