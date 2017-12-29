package com.myroom.wesna.polititrak.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;
import com.myroom.wesna.polititrak.R;
import com.myroom.wesna.polititrak.models.FloorActionModel;
import com.myroom.wesna.polititrak.models.TimelineModel;
import com.myroom.wesna.polititrak.models.VoteModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BillTimelineAdpater extends RecyclerView.Adapter<BillTimelineAdpater.BillTimelineViewHolder> {
    private JSONArray actionsArray = null;
    private JSONArray votesArray = null;
    private int itemCount = 0;
    private List<TimelineModel> dataList = null;

    public BillTimelineAdpater(JSONObject bill){


        try {
            actionsArray = bill.getJSONArray("actions");
            votesArray = bill.getJSONArray("votes");

            itemCount += (actionsArray != null) ? actionsArray.length() : 0;
            itemCount += (votesArray != null) ? votesArray.length() : 0;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.dataList = getDataList(actionsArray, votesArray);

    }

    @Override
    public int getItemViewType(int position){
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public BillTimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View billTimelineViewHolder = layoutInflater.inflate(R.layout.bill_timeline_list_item, parent, false);

        return new BillTimelineViewHolder(billTimelineViewHolder, viewType);
    }

    @Override
    public void onBindViewHolder(BillTimelineViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    private List<TimelineModel> getDataList(JSONArray actionsArray, JSONArray votesArray){
        List<TimelineModel> dataList = new ArrayList<TimelineModel>();
        int actionsLength = (actionsArray != null) ? actionsArray.length() : 0;
        int votesLength = (votesArray != null) ? votesArray.length() : 0;



        //Get all floor actions
        for(int i = 0; i < actionsLength; i++){
            int id = 0;
            String chamber = null;
            String actionType = null;
            String dateTime = null;
            String description = null;

            try {
                JSONObject actionObject = actionsArray.getJSONObject(i);

                id = actionObject.getInt("id");
                chamber = actionObject.getString("chamber");
                actionType = actionObject.getString("action_type");
                dateTime = actionObject.getString("datetime");
                description = actionObject.getString("description");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataList.add(new FloorActionModel(id, chamber, actionType, dateTime, description));
        }

        for(int i = 0; i < votesLength; i++){
            String chamber = null;
            String date = null;
            String time = null;
            int rollCall = 0;
            String question = null;
            String result = null;
            int totalYes = 0;
            int totalNo = 0;
            int totalNotVoting = 0;
            String apiUrlString = null;

            try {
                JSONObject voteObject = votesArray.getJSONObject(i);

                chamber = voteObject.getString("chamber");
                date = voteObject.getString("date");
                time = voteObject.getString("time");
                rollCall = voteObject.getInt("roll_call");
                question = voteObject.getString("question");
                result = voteObject.getString("result");
                totalYes = voteObject.getInt("total_yes");
                totalNo = voteObject.getInt("total_no");
                totalNotVoting = voteObject.getInt("total_not_voting");
                apiUrlString = voteObject.getString("api_url");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataList.add(new VoteModel(chamber, date, time, rollCall, question, result, totalYes, totalNo, totalNotVoting, apiUrlString));
        }

        return dataList;
    }

    class BillTimelineViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TimelineView timelineMarker;
        private AppCompatTextView timelineDate;
        private AppCompatTextView timelineTitle;

        public BillTimelineViewHolder(View view, int viewType){
            super(view);

            context = view.getContext();

            timelineMarker = (TimelineView) view.findViewById(R.id.time_marker);
            timelineMarker.initLine(viewType);

            timelineDate = (AppCompatTextView) view.findViewById(R.id.text_timeline_date);
            timelineTitle = (AppCompatTextView) view.findViewById(R.id.text_timeline_title);
        }

        public void bind(TimelineModel timelineModel){
            switch (timelineModel.getMarkerString()){
                case FloorActionModel.FLOOR_ACTION:
                    timelineMarker.setMarker(getDrawable(context, R.drawable.button_with_dot));
                    break;
                case VoteModel.PASSED_VOTE:
                    timelineMarker.setMarker(getDrawable(context, R.drawable.passed_vote));
                    break;
                case VoteModel.FAILED_VOTE:
                    timelineMarker.setMarker(getDrawable(context, R.drawable.failed_vote));
                    break;
                case VoteModel.STALEMATE_VOTE:
                    timelineMarker.setMarker(getDrawable(context, R.drawable.stalemate_vote));
                    break;
            }

            timelineDate.setText(timelineModel.getBillTimelineDate());
            timelineTitle.setText(timelineModel.getBillTimelineTitle());
        }

        public Drawable getDrawable(Context context, int drawableResId) {
            Drawable drawable;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                drawable = context.getResources().getDrawable(drawableResId, context.getTheme());
            } else {
                drawable = VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
            }

            return drawable;
        }
    }



}
