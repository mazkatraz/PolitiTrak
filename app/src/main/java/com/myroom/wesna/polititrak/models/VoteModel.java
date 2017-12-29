package com.myroom.wesna.polititrak.models;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.myroom.wesna.polititrak.R;

public class VoteModel implements TimelineModel {
    private String chamber;
    private String date;
    private String time;
    private int rollCall;
    private String question;
    private String result;
    private int totalYes = 0;
    private int totalNo = 0;
    private int totalNotVoting = 0;
    private String apiUrlString;

    public static final String PASSED_VOTE = "PASSED_VOTE";
    public static final String FAILED_VOTE  = "FAILED_VOTE";
    public static final String STALEMATE_VOTE = "STALEMATE_VOTE";

    public VoteModel(String chamber, String date, String time, int rollCall, String question, String result, int totalYes, int totalNo, int totalNotVoting, String apiUrlString) {
        this.chamber = chamber;
        this.date = date;
        this.time = time;
        this.rollCall = rollCall;
        this.question = question;
        this.result = result;
        this.totalYes = totalYes;
        this.totalNo = totalNo;
        this.totalNotVoting = totalNotVoting;
        this.apiUrlString = apiUrlString;
    }

    @Override
    public String getMarkerString() {
        if(totalYes > totalNo){
            return PASSED_VOTE;
        } else if (totalNo > totalYes){
            return FAILED_VOTE;
        }

        return STALEMATE_VOTE;
    }

    @Override
    public String getBillTimelineDate() {
        return time + ", " + date;
    }

    @Override
    public String getBillTimelineTitle() {
        return question;
    }
}
