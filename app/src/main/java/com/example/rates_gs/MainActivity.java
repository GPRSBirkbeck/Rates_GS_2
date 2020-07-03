package com.example.rates_gs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {
    public RatesService ratesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RatesService.getRetrofitService();


        final TextView textView = (TextView) findViewById(R.id.text_view); //the textview

        Observable<String> myObservable =
                Observable.just("YoYOYO")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + " -Dan";                    }

                })
                ;

        Consumer<String> mConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                textView.setText(s);
            }
        };
        myObservable.subscribe(mConsumer);

/*        Consumer<RatesApiAllData> ratesApiAllDataConsumer = new Consumer<RatesApiAllData>() {
            @Override
            public void accept(RatesApiAllData ratesApiAllData) throws Exception {
                String s= ratesApiAllData.getRates();
                textView.setText(s);
            }

        };
        try {
            ratesService.observableGetRate().subscribe(ratesApiAllDataConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //end of onCreate
    }
}