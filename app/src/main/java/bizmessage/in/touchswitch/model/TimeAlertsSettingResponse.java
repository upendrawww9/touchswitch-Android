package bizmessage.in.touchswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeAlertsSettingResponse {

    @SerializedName("SETTING")
    @Expose
    private TimeAlertSetting settings;

    public TimeAlertSetting getSettings() {
        return settings;
    }

    public void setSettings(TimeAlertSetting settings) {
        this.settings = settings;
    }
}