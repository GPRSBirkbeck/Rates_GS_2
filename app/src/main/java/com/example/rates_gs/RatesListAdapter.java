package com.example.rates_gs;

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

import java.util.ArrayList;

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

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();


    //default constructor.
    public RatesListAdapter(Context mContext, ArrayList<String> mImageNames, ArrayList<String> mImages) {
        this.mContext = mContext;
        this.mImageNames = mImageNames;
        this.mImages = mImages;
    }

    //these are the methods that RatesListAdapter needs as a class if it extends Recyclerview.Adapter

    //This method creates and returns an instance of the rate_view_holder (which we will use for each of our rates)
    @NonNull
    @Override
    public Rate_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this creates an instance of the layout_rates_listitem view, and inflates it to the
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rates_listitem, parent, false);
        Rate_view_holder rate_view_holder = new Rate_view_holder(view);
        return rate_view_holder;
    }


    @Override
    public void onBindViewHolder(@NonNull Rate_view_holder holder, int position) {
        //this will tell me after how many items the binding crashed (by printing out each time it passed)
        Log.d(TAG, "onBindViewHolder: called.");
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
