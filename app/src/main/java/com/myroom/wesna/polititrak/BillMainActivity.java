package com.myroom.wesna.polititrak;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.myroom.wesna.polititrak.adapters.BillAdapter;
import com.myroom.wesna.polititrak.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class BillMainActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);

        //Inflate the custom layout for the action bar
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null,
                true
        );

        //Set custom view for the action bar
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);

            //Set the color of the action bar
            actionBar.setBackgroundDrawable(new ColorDrawable(
                    getResources().getColor(R.color.colorPrimary)
            ));
        }

        //Set up search bar and add the nav button
        MaterialSearchBar materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setNavButtonEnabled(true);

        //Hook up the adapter and layout manager to the recycler view
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_bills);
        BillAdapter billAdapter = new BillAdapter();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);

        rv.setAdapter(billAdapter);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);

        //Make network call to get recent bills
        getRecentBills();
    }

    private void getRecentBills() {
        URL getRecentBillsURL = NetworkUtils.buildRecentBillsUrl();
        new GetRecentBillsTask(this).execute(getRecentBillsURL);
    }

    @Override
    public void processFinish(String output) {
        if(output == null){
            Toast.makeText(this, "You dun goofed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Got em", Toast.LENGTH_SHORT).show();
        }
    }


    private static class GetRecentBillsTask extends AsyncTask<URL, Void, String> {

        AsyncResponse delegate = null;

        GetRecentBillsTask(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL recentBillsUrl = urls[0];
            String recentBillsResults = null;
            try {
                recentBillsResults = NetworkUtils.getResponseFromHttpUrl(recentBillsUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return recentBillsResults;
        }

        @Override
        protected void onPostExecute(String recentBillsResults) {
                delegate.processFinish(recentBillsResults);
        }
    }
}
