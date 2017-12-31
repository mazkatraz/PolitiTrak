package com.myroom.wesna.polititrak.models;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.myroom.wesna.polititrak.R;

public class FloorActionModel implements TimelineModel {
    private int id = 0;
    private String chamber;
    private String actionType;
    private String datetime;
    private String description;

    public static final String FLOOR_ACTION = "FLOOR_ACTION";

    public FloorActionModel(int id, String chamber, String actionType, String datetime, String description) {
        this.id = id;
        this.chamber = chamber;
        this.actionType = actionType;
        this.datetime = datetime;
        this.description = description;
    }

    @Override
    public String getMarkerString() {
        return FLOOR_ACTION;
    }

    @Override
    public String getBillTimelineDate() {
        return datetime;
    }

    @Override
    public String getBillTimelineTitle() {
        return description;
    }
}
