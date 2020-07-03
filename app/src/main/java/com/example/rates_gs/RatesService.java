package com.example.rates_gs;

import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.*;


public class RatesService {

    private RatesAPI ratesAPI;

    RatesService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ratesAPI = retrofit.create(RatesAPI.class);
    }
/*
    Observable<Double> getRate(String current_rate) throws IOException {
        return ratesAPI.returnRate("AUD");
    }
*/

}
