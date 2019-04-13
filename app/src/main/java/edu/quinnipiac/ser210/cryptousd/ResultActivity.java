/*
 * Created by: Kevin Sangurima
 * Last Updated: 04/12/19
 *
 */

package edu.quinnipiac.ser210.cryptousd;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


public class ResultActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private String finalString = "The price of 1 BTC is 5074.19532986 USD";
    private ShareActionProvider shareActionProvider;
    private FrameLayout currentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        currentLayout = findViewById(R.id.resultActivityContainer);
        Toolbar toolbar = findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ResultFragment fragment = new ResultFragment();
        FragmentTransaction fragt = getSupportFragmentManager().beginTransaction();
        fragt.add(R.id.resultActivityContainer,fragment);
        fragt.commit();
    }
    //This block of code is for when the user clicks on one of the options of the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_copy:
                Toast.makeText(this, "Price Copied to ClipBoard", Toast.LENGTH_SHORT).show();
                ClipboardManager clipBoard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                clipBoard.setText(finalString);
                return true;
            case R.id.help_menu:
                handler.dialogBox(this);
                return true;
            case R.id.originalButton:
                handler.changeBackgroundColor(currentLayout, Color.rgb(57,61,72));
                return true;
            case R.id.blackButton:
                handler.changeBackgroundColor(currentLayout, Color.BLACK);
                return true;
            case R.id.blueButton:
                handler.changeBackgroundColor(currentLayout, Color.BLUE);
                return true;
            case R.id.yellowButton:
                handler.changeBackgroundColor(currentLayout, Color.YELLOW);
                return true;
            case R.id.redButton:
                handler.changeBackgroundColor(currentLayout, Color.RED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareActionIntent(finalString);
        return true;
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        ResultFragment results = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.resultActivityContainer);
        results.setIntent(getIntent());
    }
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }
    // Gets the string from the fragment so it can be used in the toolbar share option
    public void getCoinPriceFrag(String string){
        finalString = string;
    }
}
