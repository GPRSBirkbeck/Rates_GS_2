package com.example.rates_gs;

import com.example.rates_gs.requests.RatesAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.*;


public class RatesService {

    private static RatesAPI ratesAPI;
    private static Retrofit retrofit = null;

    static Retrofit getRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiring.revolut.codes/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ratesAPI = retrofit.create(RatesAPI.class);

        return retrofit;
    }
}
