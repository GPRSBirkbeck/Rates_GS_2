package com.example.rates_gs;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RatesAPI {

    //The returnRates() method retrieves the current rate for a given rate passed as a path parameter.
    @GET("rates/{current_rate}")
    Observable<Double> returnRate(@Path("current_rate") String current_rate);

    //The listRepos() method retrieves a list of all rates.
    @GET("rates")
    Observable<List> listAllRates();

    //The returnBaseRate() method retrieves the name of the baserate.
    @GET("baseCurrency/{set_base_rate}")
    Observable<String> returnBaseRate(
            @Path("set_base_rate") String base_rate);




}
