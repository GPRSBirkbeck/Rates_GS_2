package com.example.rates_gs.requests;

import com.example.rates_gs.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RatesAPI ratesAPI = retrofit.create(RatesAPI.class);

    public static RatesAPI getRecipeApi(){
        return ratesAPI;
    }


}
