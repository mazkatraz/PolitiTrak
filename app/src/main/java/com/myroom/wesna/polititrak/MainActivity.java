package com.myroom.wesna.polititrak;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mancj.materialsearchbar.MaterialSearchBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //Set the click listener for the bill button
        Button billButton = (Button) findViewById(R.id.bill_button);
        billButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = BillMainActivity.class;

                Intent intent = new Intent(context, destinationActivity);
                startActivity(intent);
            }
        });
    }
}
