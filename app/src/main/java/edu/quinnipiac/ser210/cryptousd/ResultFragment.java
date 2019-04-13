/*
 * This class is in charged of the backend code for the result activity
 *
 * Fragment to be used in the result activity
 *
 * Created by: Kevin Sangurima
 * Last updated: 04/12/19
 */


package edu.quinnipiac.ser210.cryptousd;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {

    private String finalString;
    private Intent intent;
    TextView result;

    // Fragment requires an empty public constructor
    public ResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflates the layout with this fragment
        View layout = inflater.inflate(R.layout.fragment_result, container, false);
        // Finds the view for the TextView and then changes the text to show the result
        result = layout.findViewById(R.id.resultDisplay);
        setText();
        ResultActivity activity = (ResultActivity) getActivity();
        activity.getCoinPriceFrag(finalString);
        return layout;
    }
    //Sets the intent
    public void setIntent(Intent intent){
        this.intent = intent;
    }

    // this method sets the text to the value selected from the previous screen/API
    public void setText(){
        String value = intent.getStringExtra("coinValue");
        String coin = intent.getStringExtra("coin");
        String currency = intent.getStringExtra("currency");
        finalString = "The price of 1 " + coin + " is " + value + " " + currency;
        result.setText(finalString);
    }
}
