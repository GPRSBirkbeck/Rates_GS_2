package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.SerialDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public RatesService ratesService;
    private TextView textView;
    private TextView textView_result;
    private TextView textView_observable;


    Handler handler = new Handler();
    int waitInt = 1000; //1second
    Runnable runnable;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RatesService.getRetrofitService();
        textView = findViewById(R.id.text_view);
        textView_result = findViewById(R.id.text_view_result);
        textView_observable = findViewById(R.id.observable_text_view);

        EditText editText = (EditText) findViewById(R.id.edit_text);
        EditText editText2 = (EditText) findViewById(R.id.edit_text_2);


        InitialValueObservable<CharSequence> input1 =
                RxTextView.textChanges(editText);
        InitialValueObservable<CharSequence> input2 =
                RxTextView.textChanges(editText2);


        Observable<String> combinedString =
                (Observable<String>) Observable.combineLatest(input1, input2,
                        (a, b) -> a + " " + b);

        getRateCalls();
        getObservableRateCalls();

        combinedString.subscribe(textView::setText);



    }

    //this works as the start/stop signal.
    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do your function;
                getRateCalls();
                handler.postDelayed(runnable, waitInt);
            }
        }, waitInt); // so basically after your getHeroes(), from next time it will be 5 sec repeated
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }

    //this should all be in the viewModel
    private void getObservableRateCalls() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RatesAPI ratesAPI = retrofit.create(RatesAPI.class);

        //The first step is to create an observable and start the request.



        //Observable
        Observable<RatesApiAllData> ratesApiAllDataObservable =
                ratesAPI.getObservableRates()
                        .toObservable()
                        //adding this seems to speed up the UI load
                        .observeOn(AndroidSchedulers.mainThread());


        //subscriber
        ratesApiAllDataObservable

                .subscribeOn(Schedulers.io())
                .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                .subscribe(new Observer<RatesApiAllData>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RatesApiAllData ratesApiAllData) {
                        textView_observable.setText(ratesApiAllData.returnRates());
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


    //uses non reactive code
    private void getRateCalls() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RatesAPI ratesAPI = retrofit.create(RatesAPI.class);

        Call<RatesApiAllData> call = ratesAPI.getRates();

        call.enqueue(new Callback<RatesApiAllData>() {
            @Override
            public void onResponse(Call<RatesApiAllData> ratesApiAllDataCall, Response<RatesApiAllData> response) {
                RatesApiAllData ratesApiAllData = response.body();
                String content = "";
                content += "ID: " + ratesApiAllData.returnRates() + "\n";

                textView_result.setText(content);

            }

            @Override
            public void onFailure(Call<RatesApiAllData> ratesApiAllDataCall, Throwable t) {
                textView_result.setText(t.getMessage());
            }
        });
    }
}