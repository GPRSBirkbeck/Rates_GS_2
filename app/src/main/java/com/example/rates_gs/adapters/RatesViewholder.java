package com.example.rates_gs.adapters;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rates_gs.R;

import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatesViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView ratesname_long, ratesname_short;
    EditText rates_double;
    CircleImageView flag_image;
    OnRateListener onRateListener;

    public RatesViewholder(@NonNull View itemView, OnRateListener onRateListener) {
        super(itemView);
        this.onRateListener = onRateListener;
        ratesname_long = itemView.findViewById(R.id.textView_currency_long_recycler);
        ratesname_short = itemView.findViewById(R.id.textView_currency_short_recycler);
        rates_double = itemView.findViewById(R.id.edittext_rate_recycler);
        flag_image = itemView.findViewById(R.id.flag_image_recycler);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRateListener.onRatesClick(getAdapterPosition());
        //TODO make the list shuffle when clicked.
        ///RatesListAdapter.swapRates


    }
}
