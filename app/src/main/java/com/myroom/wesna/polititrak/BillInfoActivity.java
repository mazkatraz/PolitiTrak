package com.myroom.wesna.polititrak;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class BillInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String billId = i.getStringExtra(BillMainActivity.EXTRA_BILL_ID);

        Toast.makeText(this, billId, Toast.LENGTH_SHORT).show();
    }
}
