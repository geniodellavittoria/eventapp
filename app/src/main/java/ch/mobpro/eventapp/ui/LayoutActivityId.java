package ch.mobpro.eventapp.ui;

import ch.mobpro.eventapp.R;

public enum LayoutActivityId {
    REGISTER_ACTIVITY(R.layout.activity_register),
    LOGIN_ACTIVITY(R.layout.activity_login);

    LayoutActivityId(int activityId) {
        this.activityId = activityId;
    }

    private int activityId;
}
