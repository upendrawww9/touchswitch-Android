package bizmessage.in.touchswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SwitchSetting {

    @SerializedName("onhourt1")
    @Expose
    private String onHourTime1;
    @SerializedName("offhourt1")
    @Expose
    private String offHourTime1;
    @SerializedName("activet1")
    @Expose
    private String activet1;
    @SerializedName("onhourt2")
    @Expose
    private String onHourTime2;
    @SerializedName("offhourt2")
    @Expose
    private String offHoursTime2;
    @SerializedName("activet2")
    @Expose
    private String activet2;

    public String getOnHourTime1() {
        return onHourTime1;
    }

    public void setOnHourTime1(String onHourTime1) {
        this.onHourTime1 = onHourTime1;
    }

    public String getOffHourTime1() {
        return offHourTime1;
    }

    public void setOffHourTime1(String offHourTime1) {
        this.offHourTime1 = offHourTime1;
    }

    public String getActivet1() {
        return activet1;
    }

    public void setActivet1(String activet1) {
        this.activet1 = activet1;
    }

    public String getOnHourTime2() {
        return onHourTime2;
    }

    public void setOnHourTime2(String onHourTime2) {
        this.onHourTime2 = onHourTime2;
    }

    public String getOffHoursTime2() {
        return offHoursTime2;
    }

    public void setOffHoursTime2(String offHoursTime2) {
        this.offHoursTime2 = offHoursTime2;
    }

    public String getActivet2() {
        return activet2;
    }

    public void setActivet2(String activet2) {
        this.activet2 = activet2;
    }
}