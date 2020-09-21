package com.example.rates_gs.requests;

import com.example.rates_gs.util.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    //Observable call
    private static Retrofit.Builder observableRetrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit retrofit = observableRetrofitBuilder.build();

    private static RatesAPI observableRatesApi = retrofit.create(RatesAPI.class);

    public static RatesAPI getObservableRatesApi(){
        return observableRatesApi;
    }

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit2 = retrofitBuilder.build();

    private static RatesAPI ratesApi = retrofit2.create(RatesAPI.class);

    public static RatesAPI getRatesApi(){
        return ratesApi;
    }


}
