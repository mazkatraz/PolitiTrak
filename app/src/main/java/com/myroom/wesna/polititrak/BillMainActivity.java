package com.myroom.wesna.polititrak;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.myroom.wesna.polititrak.adapters.RecentBillAdapter;

public class BillMainActivity extends AppCompatActivity /*implements AsyncResponse*/ {
    private static final String LOG_TAG = "BILL_MAIN_ACTIVITY";

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
        RecentBillAdapter recentBillAdapter = new RecentBillAdapter();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);

        rv.setAdapter(recentBillAdapter);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);
    }
}
