/*
 * Fragment to be used in the main activity
 *
 * This class is in charged of the first activity, it has all the info to receive the JSON
 * and it has all the backend methods used in the main activity
 *
 * Created by: Kevin Sangurima
 * Last updated: 04/12/19
 */

package edu.quinnipiac.ser210.cryptousd;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    interface Listener {
        void onPostExecute(Intent intent);
    }
    //Fragment requires an empty constructor
    public MainFragment() {

    }

    private String[] coins = new String[] {"BTC", "ETH", "XRP", "BCH", "LTC", "ZEC", "XLM", "XMR", "TRX", "DASH",
            "NEO", "ETC", "DOGE", "NANO", "SC"};
    private String[] currency = new String[] {"USD", "EUR", "CAD", "JPY", "MXN", "CNY"};
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String coinValue;
    private String currencyValue, res;
    private Handler handler = new Handler();
    private String url1 = "https://bravenewcoin-v1.p.rapidapi.com/ticker?show="; // after goes price
    private String url2 = "&coin=";// after goes coin
    private Listener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        super.onCreate(savedInstanceState);

        //gets both spinners and finds the layout
        final Spinner coinSpinner = layout.findViewById(R.id.coinSpinner);
        final Spinner currencySpinner = layout.findViewById(R.id.currencySpinner);

        //add the values from the coin array to the first spinner
        ArrayAdapter<String> coinAdapter = new ArrayAdapter<String>(getContext(),  R.layout.spinner_item, coins);
        coinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        coinSpinner.setAdapter(coinAdapter);
        //add the values of the currency array to the second spinner
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, currency);
        currencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);

        Button button = layout.findViewById(R.id.getPriceButton);
        button.setOnClickListener(this);
        // Stores the value selected on the first spinner related to the cryptocurrency
        coinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                coinValue = (String) parent.getItemAtPosition(position);
                Log.d("coin", coinValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Stores the value selected on the second spinner related to the currency
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyValue = (String) parent.getItemAtPosition(position);
                Log.d("currency", currencyValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Next 2 lines send the result to the activity so it can be used in the share option of the toolbar
        MainActivity activity = (MainActivity) getActivity();
        activity.getCoinPriceFrag(res);
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener)context;
    }

    @Override
    public void onClick(View v) {
        new FetchCoinValue().execute(coinValue, currencyValue);
    }

    private class FetchCoinValue extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection =null;
            BufferedReader reader =null;

            try {
                URL url = new URL(url1 + params[1] + url2 + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","006da6b0d5msh97aaea208d3f61fp1a7561jsn5292b28eff9a");
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                if (in == null) {
                    return null;
                }
                reader  = new BufferedReader(new InputStreamReader(in));
                // call getBufferString to get the string from the buffer.

                String coinValueJsonString = getBufferStringFromBuffer(reader).toString();

                // call a method to parse the JSON data and return a string.
                res =  handler.getCoinValue(coinValueJsonString);
                Log.d("Price",res);

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e(LOG_TAG,"Error" + e.getMessage());
                        return null;
                    }
                }
            }

            return res;
        }

        protected void onPostExecute(String result){
            if (result != null){
                Log.d(LOG_TAG, result);
                // Creates an intent and sends the value obtained from the JSON as
                // well as the information of what currencies they are
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("coinValue",result);
                intent.putExtra("coin", coinValue);
                intent.putExtra("currency", currencyValue);
                listener.onPostExecute(intent);


            }
        }
        private StringBuffer getBufferStringFromBuffer(BufferedReader br) throws Exception{
            StringBuffer buffer = new StringBuffer();

            String line;
            while((line = br.readLine()) != null){
                buffer.append(line + '\n');
            }

            if (buffer.length() == 0)
                return null;

            return buffer;
        }
    }

}
