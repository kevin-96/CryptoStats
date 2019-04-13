/*
 *
 * Created by: Kevin Sangurima
 * Last Updated: 04/12/19
 *
 */

package edu.quinnipiac.ser210.cryptousd;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MainFragment.Listener {

    private Handler handler = new Handler();
    private FrameLayout currentLayout; // Stores current layout
    private ShareActionProvider shareActionProvider;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentLayout = findViewById(R.id.mainActivityContainer);
        Toolbar toolbar = findViewById(R.id.include2);
        setSupportActionBar(toolbar);

        MainFragment fragment = new MainFragment();
        FragmentTransaction  ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mainActivityContainer,fragment);
        ft.commit();

    }
    //This block of code is for when the user clicks on one of the options of the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){

            case R.id.action_copy:
                Toast.makeText(this, "No Value to Copy", Toast.LENGTH_SHORT).show();
                return true;
            //This line will create the dialog box
            case R.id.help_menu:
                handler.dialogBox(this);
                return true;
            //Following lines change the color of the background
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
    // Populates the toolbar with menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent(res);
        return super.onCreateOptionsMenu(menu);
    }
    // Method used to pass the string to be used with the share menu item
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }
    @Override
    public void onPostExecute(Intent intent) {
        startActivity(intent);
    }
    //gets the string from the fragment in order to be used in the share option in the toolbar
    public void getCoinPriceFrag(String string){
        res = string;
    }
}

























