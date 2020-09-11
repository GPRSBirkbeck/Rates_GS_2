package com.example.rates_gs.requests;

import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RatesAPI {
    @GET("api/android/latest") //this is the end part of our URL that we want, so for
        // rates this would be @GET("EUR") and the rest is ... api/android/latest?base=
        //use a flowable as we can convert to livedata later on
    Flowable<RevolutApiResponse> getObservableRates(
            @Query("base") String baseRate
    );

    @GET("api/android/latest") //this is the end part of our URL that we want, so for
        // rates this would be @GET("EUR") and the rest is ... api/android/latest?base=
        //use a flowable as we can convert to livedata later on
    Call<RevolutApiResponse> getCallableRates(
            @Query("base") String baseRate
    );
}
