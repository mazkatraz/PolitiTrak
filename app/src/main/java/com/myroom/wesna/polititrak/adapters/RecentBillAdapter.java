package com.myroom.wesna.polititrak.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myroom.wesna.polititrak.AsyncResponse;
import com.myroom.wesna.polititrak.BillListItemClickListener;
import com.myroom.wesna.polititrak.R;
import com.myroom.wesna.polititrak.asynctasks.PolitiTrakAsyncTask;
import com.myroom.wesna.polititrak.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class RecentBillAdapter extends RecyclerView.Adapter<RecentBillAdapter.BillItemViewHolder> implements AsyncResponse{
    private int numberOfItems = 0;
    private JSONArray bills;
    private static final String LOG_TAG = "BILL_ADAPTER";
    private BillListItemClickListener billListItemClickListener;

    public RecentBillAdapter(BillListItemClickListener billListItemClickListener){
        this.billListItemClickListener = billListItemClickListener;

        //Make a web service call to get most recent bills from web service
        URL getRecentBillsURL = NetworkUtils.buildRecentBillsUrl();
        new PolitiTrakAsyncTask(this).execute(getRecentBillsURL);
    }

    @Override
    public BillItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the view to create the view holder
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View billListItemView = layoutInflater.inflate(R.layout.bill_list_item, parent,false);

        return new BillItemViewHolder(billListItemView, billListItemClickListener);
    }

    @Override
    public void onBindViewHolder(BillItemViewHolder holder, int position) {
        if(bills == null){
            //TODO: Error handling
            Log.d(LOG_TAG, "Bills global variable was null entering onBindViewHolder");
        }
        JSONObject bill = null;

        try {
            //Parse JSON to get bill information at a certain position in the JSON array
            bill = bills.getJSONObject(position);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(bill != null){
            holder.bind(bill);
        }

    }

    @Override
    public int getItemCount() {
        return numberOfItems;
    }

    /**
     * Handler for the data that comes from the Async call to the web service.
     * @param result The output from the web service call to get most recent bills
     */
    @Override
    public void asyncResponseHandler(String result) {
        JSONArray bills = null;

        if(result == null){
            //TODO: Display an error message in the UI
            Log.d(LOG_TAG, "Async output was null");
        } else {

            try {
                //Get the bill JSON array to populate the recycler view
                JSONObject jsonReader = new JSONObject(result);
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

    class BillItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView billItemTitle;
        private TextView billItemSponsor;
        private TextView billItemIntroDate;
        private ImageView profileImageVIew;
        private BillListItemClickListener billListItemClickListener;
        private String billSlug;
        private String sponsorId;


        BillItemViewHolder(View itemView, BillListItemClickListener billListItemClickListener) {
            super(itemView);
            billItemTitle = (TextView) itemView.findViewById(R.id.tv_bill_name);
            billItemSponsor = (TextView) itemView.findViewById(R.id.tv_bill_sponsor);
            billItemIntroDate = (TextView) itemView.findViewById(R.id.tv_bill_intro_date);
            profileImageVIew = (ImageView) itemView.findViewById(R.id.profile_image);

            this.billListItemClickListener = billListItemClickListener;

            itemView.setOnClickListener(this);
        }

        void bind(JSONObject bill){
            String billTitle = null;
            String billSponsor = null;
            String billIntroDate = null;

            try {
                //Get bill id
                billSlug = bill.getString("bill_slug");

                //Get picture of congress member who sponsored the bill
                billTitle = bill.getString("short_title");

                //Get picture of congress member who sponsored the bill
                sponsorId = bill.getString("sponsor_id");
                String memberPicUrlString = NetworkUtils.buildMemberPicUrlString("225x275", sponsorId);

                //Picasso.with(itemView.getContext()).load(memberPicUrlString).into(memberCircleImageView);
                Picasso.with(itemView.getContext()).load(memberPicUrlString).into(profileImageVIew);

                //Get sponsor name
                String sponsorTitle = bill.getString("sponsor_title");
                String sponsorName = bill.getString("sponsor_name");
                billSponsor = sponsorTitle + " " + sponsorName;

                //Get bill introduction date
                billIntroDate = bill.getString("introduced_date");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            billItemTitle.setText(billTitle);
            billItemSponsor.setText(billSponsor);
            billItemIntroDate.setText(billIntroDate);
        }

        @Override
        public void onClick(View view) {
            billListItemClickListener.onBillItemClick(billSlug, sponsorId);
        }
    }
}
