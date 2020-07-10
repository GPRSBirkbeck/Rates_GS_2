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

public class MainActivity extends AppCompatActivity {
    public RatesService ratesService;
    private TextView textView;
    private TextView textView_result;
    private TextView textView_observable;
    private TextView usd_rate_textView;


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
        textView = findViewById(R.id.text_view);
        textView_result = findViewById(R.id.text_view_result);
        textView_observable = findViewById(R.id.observable_text_view);
        usd_rate_textView = findViewById(R.id.textview_num_usd);

        EditText editText = (EditText) findViewById(R.id.edit_text);
        EditText editText2 = (EditText) findViewById(R.id.edit_text_2);
        EditText base_rate_editText = (EditText) findViewById(R.id.edit_text_base_rate);
        base_rate_editText.setText("300");



        InitialValueObservable<CharSequence> input1 =
                RxTextView.textChanges(editText);
        InitialValueObservable<CharSequence> input2 =
                RxTextView.textChanges(editText2);


        Observable<String> combinedString =
                (Observable<String>) Observable.combineLatest(input1, input2,
                        (a, b) -> a + " " + b);
        combinedString.subscribe(textView::setText);

        getObservableRateCalls();

/*        InitialValueObservable<CharSequence> baseRateInput =
                RxTextView.textChanges(base_rate_editText);

        InitialValueObservable<CharSequence> usdRateInput =
                RxTextView.textChanges(textView_observable);

        Observable<String> combinedRate =
                (Observable<String>) Observable.combineLatest(baseRateInput, usdRateInput,
                        ((a, b) -> Double.toString(Double.parseDouble(a.toString()) * Double.parseDouble(b.toString()))));

        //combinedRate.subscribe(usd_rate_textView::setText);*/
    }


    //this should all be in the viewModel
    private void getObservableRateCalls() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RatesAPI ratesAPI = retrofit.create(RatesAPI.class);
        //Observable

        Observable<Double> ratesApiAllDataObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        .map(new Function<RatesApiAllData, Double>() {
                            @Override
                            public Double apply(RatesApiAllData ratesApiAllData) throws Exception {
                                return ratesApiAllData.getRates().getuSD();
                            }
                        })
                        //adding this seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());

        //subscriber
        ratesApiAllDataObservable
                .subscribeOn(Schedulers.io())
                .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                .subscribe(new Observer<Double>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        textView_observable.setText(aDouble.toString());

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