package com.example.rates_gs;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RatesAPI {
    @GET("api/android/latest?base=GBP") //this is the end part of our URL that we want, so for
        // rates this would be @GET("EUR") and the rest is ... api/android/latest?base=
    Call<RatesApiAllData> getRates();

}
