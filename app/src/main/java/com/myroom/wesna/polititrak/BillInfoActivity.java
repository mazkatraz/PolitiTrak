package com.myroom.wesna.polititrak;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.myroom.wesna.polititrak.asynctasks.PolitiTrakAsyncTask;
import com.myroom.wesna.polititrak.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;


public class BillInfoActivity extends AppCompatActivity implements AsyncResponse {
    private static final String LOG_TAG = "BILL_INFO_ACTIVITY";
    private PieChart pieChart;
    private TextView billTitle;
    private CircleImageView billSponsorImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_info);

        //Get the bill id of the bill that this activity was launched for
        Intent i = getIntent();
        String billSlug = i.getStringExtra(BillMainActivity.EXTRA_BILL_SLUG);
        String bioGuide = i.getStringExtra(BillMainActivity.EXTRA_SPONSOR_ID);

        //Make call to API to get bill info
        URL specificBillUrl = NetworkUtils.buildSpecificBillUrl(billSlug);
        new PolitiTrakAsyncTask(this).execute(specificBillUrl);

        //Get bill sponsor's photo
        billSponsorImageView = (CircleImageView) findViewById(R.id.bill_sponsor_image_view);
        String memberPicUrlString = NetworkUtils.buildMemberPicUrlString("225x275", bioGuide);
        Picasso.with(this).load(memberPicUrlString).into(billSponsorImageView);

        billTitle = (TextView) findViewById(R.id.tv_bill_title);

        pieChart = (PieChart) findViewById(R.id.pie_chart_cosponsors);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.colorPrimary));

        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Co-sponsors");
    }

    private void initializePieChart(float republicanAmt, float democratAmt, float independentAmt){
        Log.d(LOG_TAG, republicanAmt + " " + democratAmt + " " + independentAmt);

        //Enter pie entry values
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        int i = 0;
        if(republicanAmt != 0){
            pieEntries.add(i++, new PieEntry(republicanAmt, "Republican"));
        }
        if(democratAmt != 0){
            pieEntries.add(i++, new PieEntry(democratAmt, "Democrat"));
        }
        if(independentAmt != 0){
            pieEntries.add(i++, new PieEntry(independentAmt, "Independent"));
        }

        //Construct pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Co-sponsors");

        pieDataSet.setDrawIcons(false);

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(5f);

        //Get colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.LTGRAY);

        //Set colors to data set
        pieDataSet.setColors(colors);

        //Set data to chart
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    @Override
    public void asyncResponseHandler(String result) {
        String billTitleString = null;
        int republicanAmt = 0;
        int democratAmt = 0;
        int independentAmt = 0;

        try {
            //Get bill JSON object
            JSONObject jsonReader = new JSONObject(result);
            JSONArray resultsArray = jsonReader.getJSONArray("results");
            JSONObject bill = resultsArray.getJSONObject(0);

            //Get bill title
            billTitleString = bill.getString("title");

            //Co-sponsor party amounts
            JSONObject cosponsorAmtObj = bill.getJSONObject("cosponsors_by_party");
            Iterator<String> iterator = cosponsorAmtObj.keys();
            while(iterator.hasNext()){
                String key = iterator.next();

                if(key.equals("R")){
                    try {
                        republicanAmt = cosponsorAmtObj.getInt(key);
                    } catch (Exception e) {
                        //Nothing
                    }
                } else if (key.equals("D")){
                    try {
                        democratAmt = cosponsorAmtObj.getInt(key);
                    } catch (Exception e) {
                        //Nada
                    }
                } else if (key.equals("I")){
                    try {
                        independentAmt = cosponsorAmtObj.getInt(key);
                    } catch (Exception e) {
                        //Nope
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        billTitle.setText(billTitleString);
        initializePieChart(republicanAmt, democratAmt, independentAmt);
    }
}
