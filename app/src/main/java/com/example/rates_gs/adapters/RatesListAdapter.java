package com.example.rates_gs.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rates_gs.R;
import com.example.rates_gs.models.CurrencyRate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatesListAdapter extends RecyclerView.Adapter<RatesListAdapter.Rate_view_holder>{
    //this class adapts each currency from the layout_rates_listitem into the main activity
    //this TAG is just for logging
    private static final String TAG = "RatesListAdapter";

    //What is context? well: is it "Interface to global information about an application environment.
    // This is an abstract class whose implementation is provided by the Android system.
    // It allows access to application-specific resources and classes, as well as
    // up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc."
    private Context mContext;

    private List<CurrencyRate> mCurrencyRate = new ArrayList<>();


    //declare onRateListener within the Adapter class;
    private OnRateListener mOnRateListener;

    //default constructor.
    public RatesListAdapter(Context mContext, List<CurrencyRate> mCurrencyRate, OnRateListener onRateListener) {
        this.mContext = mContext;
        this.mCurrencyRate = mCurrencyRate;
        this.mOnRateListener = onRateListener;
    }

    //these are the methods that RatesListAdapter needs as a class if it extends Recyclerview.Adapter

    //This method creates and returns an instance of the rate_view_holder (which we will use for each of our rates)
    @NonNull
    @Override
    public Rate_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this creates an instance of the layout_rates_listitem view, and inflates it to the
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rates_listitem, parent, false);
        //view.setOnClickListener(new RateClickListener());
        Rate_view_holder rate_view_holder = new Rate_view_holder(view, mOnRateListener);
        return rate_view_holder;
    }


    @Override
    public void onBindViewHolder(@NonNull Rate_view_holder holder, int position) {
        //this will tell me after how many items the binding crashed (by printing out each time it passed)
        Log.d(TAG, "onBindViewHolder: called.");

        //this int is used for swapping as position will lead to incorrect usage
        int swapInt = position;
        
        holder.textView_short.setText(mCurrencyRate.get(position).getRateNamesShort());
        holder.textView_long.setText(mCurrencyRate.get(position).getRateNamesLong());
        holder.circleImageView.setImageResource(mCurrencyRate.get(position).getFlagImage());
        holder.editText_rate.setText(mCurrencyRate.get(position).getRateDouble().toString());
    }

    @Override
    public int getItemCount() {
        return mCurrencyRate.size();
    }

    //TODO refactor these all as CurrencyRates (new class to hold this data type)
    public void swapRates(int fromPosition){
        Collections.swap(mCurrencyRate, fromPosition, 0);
        notifyItemMoved(fromPosition, 0);
    }

    //this class is our viewholder for the recyclerview.
    //this viewholder holds each view in memory (so each rate) - the view holder holds that view and gets ready to add rhe next one
    //android dev page says "A ViewHolder describes an item view and metadata about its place within the RecyclerView."
    public class Rate_view_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView circleImageView;
        TextView textView_long;
        TextView textView_short;
        EditText editText_rate;
        ConstraintLayout constraintLayout;

        //Attach the OnRateListener to the Viewholder
        OnRateListener onRateListener;

        public Rate_view_holder(@NonNull View itemView, OnRateListener onRateListener) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.flag_image);
            textView_long = itemView.findViewById(R.id.textView_currency_short);
            textView_short = itemView.findViewById(R.id.textView_currency_long);
            editText_rate = itemView.findViewById(R.id.edittext_rate);
            constraintLayout = itemView.findViewById(R.id.parent_layout);
            this.onRateListener = onRateListener;

            //onclick listener is attached to the entire viewholder
            itemView.setOnClickListener(this::onClick);
        }
        @Override
        public void onClick(View v) {
            onRateListener.onRateClick(getAdapterPosition());
            swapRates(this.getAdapterPosition());
        }
    }

    public interface OnRateListener{
        //send the position of the clicked rate
        void onRateClick(int position);
    }
}
