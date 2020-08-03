package com.example.rates_gs.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.rates_gs.R;
import com.example.rates_gs.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

/**
 * THis uses the singleton pattern, which is needed for accessing webservices or accessing database
 * it avoids having a bunch of open connections to webservices, apis or caches
 */
//TODO research singleton pattern

public class RatesRepository {

    //the data gets into the VM from this repository class.
    //TODO add methods for accessing webservices and databases here

    private static RatesRepository instance;
    private ArrayList<CurrencyRate> currencyRatesDataSet = new ArrayList<>();

    //return method for our instance
    public static RatesRepository getInstance(){
        //TODO put an if in here to check whether or not the connection has been made to the API
        if(instance == null){
            instance = new RatesRepository();
        }
        return instance;
    }

    //this is the method to get cache, database or API or whatever, improve this.
    public MutableLiveData<List<CurrencyRate>> getCurrencyRates(){
        //this is mimicking what it would be like to get the data from the webservices by calling the set method.
        setCurrencyRates();
        MutableLiveData<List<CurrencyRate>> data = new MutableLiveData<>();
        //this sets our data to the currencyRatesDataSet which theoretically is called from the API and DB just not done eyt, having called this method
        data.setValue(currencyRatesDataSet);
        return data;

    }

    private void setCurrencyRates(){
        //instead of phony data below use real linked data

        //dummy data - to fill the arraylists
        currencyRatesDataSet.add(new CurrencyRate("Second set of Dollars", "USD",R.drawable.flag_usd,100.00 ));
        currencyRatesDataSet.add(new CurrencyRate("Zuid Afrikaanse Rand", "ZAR",R.drawable.flag_zar,130.00 ));
        currencyRatesDataSet.add(new CurrencyRate("Thai Bhat", "TBH",R.drawable.flag_thb,2450.00 ));
        currencyRatesDataSet.add(new CurrencyRate("Singapore Dollar", "SGD",R.drawable.flag_sgd,22.57 ));
        currencyRatesDataSet.add(new CurrencyRate("Swedish Krona", "SEK",R.drawable.flag_sek,146.12 ));
    }

}
