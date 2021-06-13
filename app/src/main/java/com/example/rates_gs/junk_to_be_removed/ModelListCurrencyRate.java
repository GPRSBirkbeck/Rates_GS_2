package com.example.rates_gs.junk_to_be_removed;

import com.example.rates_gs.R;
import com.example.rates_gs.models.CurrencyRate;
import com.example.rates_gs.requests.responses.RatesResponse;
import com.example.rates_gs.requests.responses.RevolutApiResponse;

import java.util.ArrayList;
import java.util.List;

public class ModelListCurrencyRate {
    private List<CurrencyRate> currencyRateList;

    public List<CurrencyRate> getCurrencyRateList() {
        return currencyRateList;
    }

    public void setCurrencyRateList(List<CurrencyRate> currencyRateList) {
        this.currencyRateList = currencyRateList;
    }

    //TODO move to the junk zone

    public ModelListCurrencyRate(RevolutApiResponse revolutApiResponse) {
        //todo insert DI
        currencyRateList = new ArrayList<>();
        String baseRate = revolutApiResponse.getBaseCurrency();
        RatesResponse r = revolutApiResponse.getRates();

  /*      if(baseRate.equals("EUR")){
            this.currencyRateList.add(0, new CurrencyRate("EUR","Euro", R.drawable.flag_eur,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("EUR","Euro", R.drawable.flag_eur,r.getEUR()));
        }
        if(baseRate.equals("AUD")){
            this.currencyRateList.add(0, new CurrencyRate("AUD","Australian Dollar", R.drawable.flag_aud,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("AUD","Australian Dollar", R.drawable.flag_aud,r.getAUD()));
        }
        if(baseRate.equals("BGN")){
            this.currencyRateList.add(0, new CurrencyRate("BGN","Bulgarian Lev", R.drawable.flag_bgn,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("BGN","Bulgarian Lev", R.drawable.flag_bgn,r.getBGN()));
        }
        if(baseRate.equals("BRL")){
            this.currencyRateList.add(0, new CurrencyRate("BRL","Brazilian Real", R.drawable.flag_brl,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("BRL","Brazilian Real", R.drawable.flag_brl,r.getBRL()));
        }
        if(baseRate.equals("CAD")){
            this.currencyRateList.add(0, new CurrencyRate("CAD","Canadian Dollar", R.drawable.flag_cad,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("CAD","Canadian Dollar", R.drawable.flag_cad,r.getCAD()));
        }
        if(baseRate.equals("CHF")){
            this.currencyRateList.add(0, new CurrencyRate("CHF","Swiss Franc", R.drawable.flag_chf,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("CHF","Swiss Franc", R.drawable.flag_chf,r.getCHF()));
        }
        if(baseRate.equals("CNY")){
            this.currencyRateList.add(0,new CurrencyRate("CNY","Chinese Yuan", R.drawable.flag_cny,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("CNY","Chinese Yuan", R.drawable.flag_cny,r.getCNY()));
        }
        if(baseRate.equals("CZK")){
            this.currencyRateList.add(0, new CurrencyRate("CZK","Czech Koruna", R.drawable.flag_czk,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("CZK","Czech Koruna", R.drawable.flag_czk,r.getCZK()));
        }
        if(baseRate.equals("DKK")){
            this.currencyRateList.add(0, new CurrencyRate("DKK","Danish Krone", R.drawable.flag_dkk,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("DKK","Danish Krone", R.drawable.flag_dkk,r.getDKK()));
        }
        if(baseRate.equals("GBP")){
            this.currencyRateList.add(0, new CurrencyRate("GBP","Pound sterling", R.drawable.flag_gbp,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("GBP","Pound sterling", R.drawable.flag_gbp,r.getGBP()));
        }
        if(baseRate.equals("HKD")){
            this.currencyRateList.add(0, new CurrencyRate("HKD","Hong Kong Dollar", R.drawable.flag_hkd,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("HKD","Hong Kong Dollar", R.drawable.flag_hkd,r.getHKD() ));
        }
        if(baseRate.equals("HRK")){
            this.currencyRateList.add(0, new CurrencyRate("HRK","Croatian Kuna", R.drawable.flag_hrk,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("HRK","Croatian Kuna", R.drawable.flag_hrk,r.getHRK()));
        }
        if(baseRate.equals("HUF")){
            this.currencyRateList.add(0, new CurrencyRate("HUF","Hungarian Forint", R.drawable.flag_huf,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("HUF","Hungarian Forint", R.drawable.flag_huf,r.getHUF() ));
        }
        if(baseRate.equals("IDR")){
            this.currencyRateList.add(0, new CurrencyRate("IDR","Indonesian Rupiah", R.drawable.flag_idr,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("IDR","Indonesian Rupiah", R.drawable.flag_idr,r.getIDR()));
        }
        if(baseRate.equals("ILS")){
            this.currencyRateList.add(0, new CurrencyRate("ILS","Israeli New Shekel", R.drawable.flag_ils,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("ILS","Israeli New Shekel", R.drawable.flag_ils,r.getILS()));
        }
        if(baseRate.equals("INR")){
            this.currencyRateList.add(0, new CurrencyRate("INR","Indian Rupee", R.drawable.flag_inr,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("INR","Indian Rupee", R.drawable.flag_inr,r.getINR() ));
        }
        if(baseRate.equals("ISK")){
            this.currencyRateList.add(0, new CurrencyRate("ISK","Icelandic Króna", R.drawable.flag_isk,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("ISK","Icelandic Króna", R.drawable.flag_isk,r.getISK() ));
        }
        if(baseRate.equals("JPY")){
            this.currencyRateList.add(0, new CurrencyRate("JPY","Japanese Yen", R.drawable.flag_jpy,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("JPY","Japanese Yen", R.drawable.flag_jpy,r.getjPY() ));
        }
        if(baseRate.equals("KRW")){
            this.currencyRateList.add(0, new CurrencyRate("KRW","South Korean won", R.drawable.flag_krw,1.00 ));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("KRW","South Korean won", R.drawable.flag_krw,r.getkRW() ));
        }
        if(baseRate.equals("MXN")){
            this.currencyRateList.add(0, new CurrencyRate("MXN","Mexican Peso", R.drawable.flag_mxn,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("MXN","Mexican Peso", R.drawable.flag_mxn,r.getmXN() ));
        }
        if(baseRate.equals("MYR")){
            this.currencyRateList.add(0, new CurrencyRate("MYR","Malaysian Ringgit", R.drawable.flag_myr,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("MYR","Malaysian Ringgit", R.drawable.flag_myr,r.getmYR() ));
        }
        if(baseRate.equals("NOK")){
            this.currencyRateList.add(0, new CurrencyRate("NOK","Norwegian Krone", R.drawable.flag_nok,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("NOK","Norwegian Krone", R.drawable.flag_nok,r.getnOK() ));
        }
        if(baseRate.equals("NZD")){
            this.currencyRateList.add(0, new CurrencyRate("NZD","New Zealand Dollar", R.drawable.flag_nzd,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("NZD","New Zealand Dollar", R.drawable.flag_nzd,r.getnZD() ));
        }
        if(baseRate.equals("PHP")){
            this.currencyRateList.add(0, new CurrencyRate("PHP","Philippine peso", R.drawable.flag_php,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("PHP","Philippine peso", R.drawable.flag_php,r.getpHP() ));
        }
        if(baseRate.equals("PLN")){
            this.currencyRateList.add(0, new CurrencyRate("PLN","Poland złoty", R.drawable.flag_pln,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("PLN","Poland złoty", R.drawable.flag_pln,r.getpLN() ));
        }
        if(baseRate.equals("RON")){
            this.currencyRateList.add(0, new CurrencyRate("RON","Romanian Leu", R.drawable.flag_ron,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("RON","Romanian Leu", R.drawable.flag_ron,r.getrON() ));
        }
        if(baseRate.equals("RUB")){
            this.currencyRateList.add(0, new CurrencyRate("RUB","Russian Ruble", R.drawable.flag_rub,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("RUB","Russian Ruble", R.drawable.flag_rub,r.getrUB() ));
        }
        if(baseRate.equals("SEK")){
            this.currencyRateList.add(0, new CurrencyRate("SEK","Swedish Krona", R.drawable.flag_sek,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("SEK","Swedish Krona", R.drawable.flag_sek,r.getsEK() ));
        }
        if(baseRate.equals("SGD")){
            this.currencyRateList.add(0, new CurrencyRate("SGD","Singapore Dollar", R.drawable.flag_sgd,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("SGD","Singapore Dollar", R.drawable.flag_sgd,r.getsGD() ));
        }
        if(baseRate.equals("THB")){
            this.currencyRateList.add(0, new CurrencyRate("THB","Thai Baht", R.drawable.flag_thb,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("THB","Thai Baht", R.drawable.flag_thb,r.gettHB() ));
        }
        if(baseRate.equals("USD")){
            this.currencyRateList.add(0, new CurrencyRate("USD","US Dollar", R.drawable.flag_usd,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("USD","US Dollar", R.drawable.flag_usd,r.getuSD() ));
        }
        if(baseRate.equals("ZAR")){
            this.currencyRateList.add(0, new CurrencyRate("ZAR","South African Rand", R.drawable.flag_zar,1.00));
        }
        else{
            this.currencyRateList.add(new CurrencyRate("ZAR","South African Rand", R.drawable.flag_zar,r.getzAR() ));
        }*/
    }
}
