package com.example.rates_gs.util;

import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseMethod;

import com.example.rates_gs.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Converter {
        @InverseMethod("rateToString")
        public static String rateToString(EditText view, Double oldValue, Double value) {
            if(oldValue != value){
                return String.valueOf(value);
            }
            return String.valueOf(oldValue);
        }

        @BindingAdapter("stringToRate")
        public static Double stringToRate(EditText view, String oldValue,
                                        String value) {
            if(oldValue != value){
                return Double.parseDouble(value);

            }
            return Double.parseDouble(oldValue);
            // Converts String to long.
        }


    @InverseMethod("toDouble")
    public static String toString(EditText view, Double oldValue,
                                  Double value) {
        NumberFormat numberFormat = getNumberFormat(view);
        try {
            // Don't return a different value if the parsed value
            // doesn't change
            String inView = view.getText().toString();
            Double ob =
                    numberFormat.parse(inView).doubleValue();

            Double parsed = new Double(ob);

            if (parsed == value) {
                return view.getText().toString();
            }
        } catch (ParseException e) {
            // Old number was broken
        }
        return numberFormat.format(value);
    }

    public static Double toDouble(EditText view, Double oldValue,
                                  String value) {
        NumberFormat numberFormat = getNumberFormat(view);
        try {
            double val = numberFormat.parse(value).doubleValue();
            Double ob = new Double(val);
            return ob;
            //return numberFormat.parse(value).doubleValue();
        } catch (ParseException e) {
            Resources resources = view.getResources();
            String errStr = resources.getString(R.string.error_converting_input);
            view.setError(errStr);
            return oldValue;
        }
    }

    private static NumberFormat getNumberFormat(View view) {
        Resources resources= view.getResources();
        Locale locale = resources.getConfiguration().locale;
        NumberFormat format =
                NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setGroupingUsed(false);
        }
        return format;
    }
}
