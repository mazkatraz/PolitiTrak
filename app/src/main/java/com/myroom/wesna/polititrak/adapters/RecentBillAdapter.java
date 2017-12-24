package com.myroom.wesna.polititrak.adapters;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myroom.wesna.polititrak.AsyncResponse;
import com.myroom.wesna.polititrak.R;
import com.myroom.wesna.polititrak.asynctasks.PolitiTrakAsyncTask;
import com.myroom.wesna.polititrak.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RecentBillAdapter extends RecyclerView.Adapter<RecentBillAdapter.BillItemViewHolder> implements AsyncResponse{
    private int numberOfItems = 0;
    private JSONArray bills;
    private static final String LOG_TAG = "BILL_ADAPTER";

    public RecentBillAdapter(){

        //Make an async call to get most recent bills from web service
        URL getRecentBillsURL = NetworkUtils.buildRecentBillsUrl();
        new PolitiTrakAsyncTask(this).execute(getRecentBillsURL);
    }

    @Override
    public BillItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the view to create the view holder
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View billListItemView = layoutInflater.inflate(R.layout.bill_list_item, parent,false);

        return new BillItemViewHolder(billListItemView);
    }

    @Override
    public void onBindViewHolder(BillItemViewHolder holder, int position) {
        if(bills == null){
            //TODO: Error handling
            Log.d(LOG_TAG, "Bills global variable was null entering onBindViewHolder");
        }

        String billTitle = null;

        //Parse JSON to get bill information at a certain position in the JSON array
        try {
            JSONObject bill = bills.getJSONObject(position);
            billTitle = bill.getString("short_title");
            billTitle = billTitle.substring(0, 29) + "...";
        } catch (JSONException e) {
            e.printStackTrace();

            billTitle = "Error at JSON object " + position;
        }

        //Log.d(LOG_TAG, "Bill Title at position " + position + ": " + billTitle);

        //Bind data to the view holder so that data can be displayed to user
        holder.bind(billTitle);
    }

    @Override
    public int getItemCount() {
        return numberOfItems;
    }

    /**
     * Handler for the data that comes from the Async call to the web service.
     * @param asyncOutput The output from the web service call to get most recent bills
     */
    @Override
    public void asyncResponseHandler(String asyncOutput) {
        JSONArray bills = null;

        if(asyncOutput == null){
            //TODO: Display an error message in the UI
            Log.d(LOG_TAG, "Async output was null");
        } else {

            try {
                //Get the bill JSON array to populate the recycler view
                JSONObject jsonReader = new JSONObject(asyncOutput);
                JSONArray results = jsonReader.getJSONArray("results");
                numberOfItems = results.getJSONObject(0).getInt("num_results");
                bills = results.getJSONObject(0).getJSONArray("bills");
            } catch (JSONException e) {

                //TODO: Display an error message in the UI

                e.printStackTrace();
            }
        }

        this.bills = bills;
        this.notifyDataSetChanged(); //Update UI
    }

    class BillItemViewHolder extends RecyclerView.ViewHolder{
        TextView billItemTitle;

        BillItemViewHolder(View itemView) {
            super(itemView);

            billItemTitle = (TextView) itemView.findViewById(R.id.tv_bill_name);
        }

        void bind(String billTitle){
            billItemTitle.setText(billTitle);
        }
    }
}
