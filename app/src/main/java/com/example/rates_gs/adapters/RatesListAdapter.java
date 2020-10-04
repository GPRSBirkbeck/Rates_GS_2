package com.example.rates_gs.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rates_gs.R;
import com.example.rates_gs.models.CurrencyRate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CurrencyRate> mCurrencyRate = new ArrayList<>();
    private OnRateListener onRateListener;

    public RatesListAdapter(OnRateListener onRateListener) {
        this.onRateListener = onRateListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rates_listitem, parent, false);
        return new RatesViewholder(view, onRateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RatesViewholder)holder).ratesname_short.setText(mCurrencyRate.get(position).getRateNameShort());
        ((RatesViewholder)holder).ratesname_long.setText(mCurrencyRate.get(position).getRateNameLong());
        ((RatesViewholder)holder).rates_double.setText(String.valueOf(mCurrencyRate.get(position).getRateDouble()));
        ((RatesViewholder)holder).flag_image.setImageResource(mCurrencyRate.get(position).getFlagImage());

    }

    @Override
    public int getItemCount() {
        if(mCurrencyRate != null){
            return mCurrencyRate.size();
        }
        return 0;
    }
    public void setRates(List<CurrencyRate> rates){
        mCurrencyRate = rates;
        notifyDataSetChanged();
    }
    public void swapRates(int fromPosition){
        Collections.swap(mCurrencyRate, fromPosition, 0);
        notifyItemMoved(fromPosition, 0);
    }
}