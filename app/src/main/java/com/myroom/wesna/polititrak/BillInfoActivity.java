package com.myroom.wesna.polititrak;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.myroom.wesna.polititrak.adapters.BillTimelineAdpater;
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
    private RecyclerView billTimelineRecyclerView;

    private static final int REPUBLICAN_COLOR = Color.RED;
    private static final int DEMOCRAT_COLOR = Color.BLUE;
    private static final int INDEPENDENT_COLOR = Color.LTGRAY;

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

        //Initialize and format pie chart
        pieChart = (PieChart) findViewById(R.id.pie_chart_cosponsors);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.colorPrimary));

        pieChart.getLegend().setEnabled(false);
    }

    private void initializePieChart(float republicanAmt, float democratAmt, float independentAmt, int amtCosponsors){
        Log.d(LOG_TAG, republicanAmt + " " + democratAmt + " " + independentAmt);

        //Enter pie entry values and get colors
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int i = 0;
        if(republicanAmt != 0){
            pieEntries.add(i++, new PieEntry(republicanAmt, "Republican"));
            colors.add(REPUBLICAN_COLOR);
        }
        if(democratAmt != 0){
            pieEntries.add(i++, new PieEntry(democratAmt, "Democrat"));
            colors.add(DEMOCRAT_COLOR);
        }
        if(independentAmt != 0){
            pieEntries.add(i++, new PieEntry(independentAmt, "Independent"));
            colors.add(INDEPENDENT_COLOR);
        }

        //Construct pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Co-sponsors");

        pieDataSet.setDrawIcons(false);

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(5f);

        //Set colors to data set
        pieDataSet.setColors(colors);

        //Set data to chart
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        //Set pie chart title
        SpannableString s = new SpannableString("Co-sponsors\n(" + amtCosponsors + ")");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), Spanned.SPAN_COMPOSING);
        pieChart.setCenterText(s);

        //Start animation
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        //Initialize recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        billTimelineRecyclerView = (RecyclerView) findViewById(R.id.rv_timeline);
        billTimelineRecyclerView.setLayoutManager(linearLayoutManager);
        billTimelineRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void asyncResponseHandler(String result) {
        JSONObject bill = null;
        String billTitleString = null;
        int republicanAmt = 0;
        int democratAmt = 0;
        int independentAmt = 0;
        int amtCosponsors = 0;

        try {
            //Get bill JSON object
            JSONObject jsonReader = new JSONObject(result);
            JSONArray resultsArray = jsonReader.getJSONArray("results");
            bill = resultsArray.getJSONObject(0);

            //Get bill title
            billTitleString = bill.getString("title");

            //Get amount of co-sponsors
            amtCosponsors = bill.getInt("cosponsors");

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

            //Count the main sponsor to the party too. And set their photo border to their pary color
            String sponsorParty = bill.getString("sponsor_party");
            switch (sponsorParty){
                case "R":
                    //republicanAmt++;
                    billSponsorImageView.setBorderColor(REPUBLICAN_COLOR);
                    break;
                case "D":
                    //democratAmt++;
                    billSponsorImageView.setBorderColor(DEMOCRAT_COLOR);
                    break;
                case "I":
                    //independentAmt++;
                    billSponsorImageView.setBorderColor(INDEPENDENT_COLOR);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set bill title
        billTitle.setText(billTitleString);

        //Initialize pie chart with data from JSON
        initializePieChart(republicanAmt, democratAmt, independentAmt, amtCosponsors);

        //Put data in recycler view
        billTimelineRecyclerView.setAdapter(new BillTimelineAdpater(bill));
    }
}
