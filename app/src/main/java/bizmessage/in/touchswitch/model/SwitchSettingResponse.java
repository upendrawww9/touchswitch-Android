package bizmessage.in.touchswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SwitchSettingResponse {

    @SerializedName("SETTING")
    @Expose
    private SwitchSetting settings;

    public SwitchSetting getSettings() {
        return settings;
    }

    public void setSettings(SwitchSetting settings) {
        this.settings = settings;
    }
}