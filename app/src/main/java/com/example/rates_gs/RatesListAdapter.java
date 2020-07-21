package com.example.rates_gs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatesListAdapter extends RecyclerView.Adapter<RatesListAdapter.Rate_view_holder>{
    //this class adapts each currency from the layout_rates_listitem into the main activity
    //this TAG is just for logging
    private static final String TAG = "RatesListAdapter";




    //these are the methods that RatesListAdapter needs as a class if it extends Recyclerview.Adapter
    @NonNull
    @Override
    public Rate_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Rate_view_holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //this class is our viewholder for the recyclerview.
    //this viewholder holds each view in memory (so each rate) - the view holder holds that view and gets ready to add rhe next one
    //android dev page says "A ViewHolder describes an item view and metadata about its place within the RecyclerView."
    public class Rate_view_holder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView textView_long;
        TextView textView_short;
        EditText editText_rate;
        ConstraintLayout constraintLayout;
        public Rate_view_holder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.flag_image);
            textView_long = itemView.findViewById(R.id.textView_currency_short);
            textView_short = itemView.findViewById(R.id.textView_currency_long);
            editText_rate = itemView.findViewById(R.id.edittext_rate);
            constraintLayout = itemView.findViewById(R.id.parent_layout);


        }
    }
}
