package com.example.rates_gs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Collections;

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

    private ArrayList<String> mRateNamesLong = new ArrayList<>();
    private ArrayList<String> mRateNamesShort = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();

    //declare onRateListener within the Adapter class;
    private OnRateListener mOnRateListener;



    //default constructor.
    public RatesListAdapter(Context mContext, ArrayList<String> mRateNamesLong, ArrayList<String> mRateNamesShort, ArrayList<Integer> mImages, OnRateListener onRateListener) {
        this.mContext = mContext;
        this.mRateNamesLong = mRateNamesLong;
        this.mRateNamesShort = mRateNamesShort;
        this.mImages = mImages;
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
        
        holder.textView_short.setText(mRateNamesShort.get(position));
        holder.textView_long.setText(mRateNamesLong.get(position));
        holder.circleImageView.setImageResource(mImages.get(position));
/*        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on " + mRateNamesShort.get(position));
                //Toast.makeText(mContext, mRateNamesShort.get(position), Toast.LENGTH_SHORT).show();
                swapRates(swapInt);
            }
    });*/
    }

    @Override
    public int getItemCount() {
        return mRateNamesShort.size();
    }

    public void swapRates(int fromPosition){
        Collections.swap(mRateNamesLong, fromPosition, 0);
        Collections.swap(mRateNamesShort, fromPosition, 0);
        Collections.swap(mImages, fromPosition, 0);
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
