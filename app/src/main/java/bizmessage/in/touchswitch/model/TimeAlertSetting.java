package bizmessage.in.touchswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeAlertSetting {

    @SerializedName("autolight")
    @Expose
    private String autoLight;


    @SerializedName("autoled")
    @Expose
    private String autoled;





    @SerializedName("timer1")
    @Expose
    private String timer1;

    @SerializedName("timer2")
    @Expose
    private String timer2;

    @SerializedName("timer3")
    @Expose
    private String timer3;

    @SerializedName("timer4")
    @Expose
    private String timer4;


    @SerializedName("autobuzz")
    @Expose
    private String autoBuzz;
    @SerializedName("autolight_time")
    @Expose
    private String autoLightLime;

    @SerializedName("autoled_time")
    @Expose
    private String autoledLime;



    @SerializedName("autobuzz_time")
    @Expose
    private String autoBuzzTime;
    @SerializedName("r_noti1")
    @Expose
    private String notification1;
    @SerializedName("r_noti2")
    @Expose
    private String notification2;
    @SerializedName("r_noti3")
    @Expose
    private String notification3;
    @SerializedName("r_noti4")
    @Expose
    private String notification4;
    @SerializedName("r_tone1")
    @Expose
    private String notes1;
    @SerializedName("r_tone2")
    @Expose
    private String notes2;
    @SerializedName("r_tone3")
    @Expose
    private String notes3;
    @SerializedName("r_tone4")
    @Expose
    private String notes4;
    @SerializedName("r_noti_email")
    @Expose
    private String notificationEmail;
    @SerializedName("r_blk_all_noti")
    @Expose
    private String notificationAll;
    @SerializedName("r_sil_noti")
    @Expose
    private String notificationSilent;
    @SerializedName("r_noti_only")
    @Expose
    private String notificationOnly;
    @SerializedName("r_ring_only")
    @Expose
    private String ringOnly;
    @SerializedName("r_on_h")
    @Expose
    private String onHours;
    @SerializedName("r_off_h")
    @Expose
    private String offHours;
    @SerializedName("r_on_m")
    @Expose
    private String onMinutes;
    @SerializedName("r_off_m")
    @Expose
    private String offMinutes;
    @SerializedName("r_act_timer")
    @Expose
    private String isTimerActive = "";
    @SerializedName("r_all_day")
    @Expose
    private String isAllDays;

    @SerializedName("ispower")
    @Expose
    private String isPower;

    @SerializedName("is_power_ring")
    @Expose
    private String isPowerring;



    @SerializedName("isfire")
    @Expose
    private String isFire;

    @SerializedName("autofire_hooter")
    @Expose
    private String isAutofirehooter;//autofire_hooter

    @SerializedName("autofire_hooter_time")
    @Expose
    private String autofirehootertime;//autofire_hooter

    @SerializedName("is_fire_model")
    @Expose
    private String isFiremodel;


    @SerializedName("ismot")
    @Expose
    private String isMotion;
    @SerializedName("isvib")
    @Expose
    private String isVibration;
    @SerializedName("r_vib_only")
    @Expose
    private String vibrationOnly;
    @SerializedName("fam_noti1")
    @Expose
    private String famNotification1;
    @SerializedName("fam_noti2")
    @Expose
    private String famNotification2;
    @SerializedName("fam_noti3")
    @Expose
    private String famNotification3;
    @SerializedName("fam_noti4")
    @Expose
    private String famNotification4;
    @SerializedName("fam_tone1")
    @Expose
    private String famTone1;
    @SerializedName("fam_tone2")
    @Expose
    private String famTone2;
    @SerializedName("fam_tone3")
    @Expose
    private String famTone3;
    @SerializedName("fam_tone4")
    @Expose
    private String famTone4;
    @SerializedName("r_fam_noti_email")
    @Expose
    private String famNotificationEmail;
    @SerializedName("r_fam_blk_all_noti")
    @Expose
    private String famNotificationAll;
    @SerializedName("r_fam_vib_only")
    @Expose
    private String famVibrationOnly;
    @SerializedName("r_fam_sil_noti")
    @Expose
    private String famNotificationSilent;
    @SerializedName("r_fam_noti_only")
    @Expose
    private String famNotificationOnly;
    @SerializedName("r_fam_ring_only")
    @Expose
    private String famRingOnly;

    public String getAutoLight() {
        return autoLight;
    }

    public void setAutoLight(String autoLight) {
        this.autoLight = autoLight;
    }


    public String getAutoled() {
        return autoled;
    }

    public void setAutoled(String autoled) {
        this.autoled = autoled;
    }





    public String getAutoBuzz() {
        return autoBuzz;
    }

    public void setAutoBuzz(String autoBuzz) {
        this.autoBuzz = autoBuzz;
    }

    public String getAutoLightLime() {
        return autoLightLime;
    }

    public void setAutoLightLime(String autoLightLime) {
        this.autoLightLime = autoLightLime;
    }


    public String getAutoledLime() {
        return autoledLime;
    }

    public void setAutoledLime(String autoledLime) {
        this.autoledLime = autoledLime;
    }





    public String getAutoBuzzTime() {
        return autoBuzzTime;
    }

    public void setAutoBuzzTime(String autoBuzzTime) {
        this.autoBuzzTime = autoBuzzTime;
    }

    public String getNotification1() {
        return notification1;
    }

    public void setNotification1(String notification1) {
        this.notification1 = notification1;
    }

    public String getNotification2() {
        return notification2;
    }

    public void setNotification2(String notification2) {
        this.notification2 = notification2;
    }

    public String getNotification3() {
        return notification3;
    }

    public void setNotification3(String notification3) {
        this.notification3 = notification3;
    }

    public String getNotification4() {
        return notification4;
    }

    public void setNotification4(String notification4) {
        this.notification4 = notification4;
    }

    public String getNotes1() {
        return notes1;
    }

    public void setNotes1(String notes1) {
        this.notes1 = notes1;
    }

    public String getNotes2() {
        return notes2;
    }

    public void setNotes2(String notes2) {
        this.notes2 = notes2;
    }

    public String getNotes3() {
        return notes3;
    }

    public void setNotes3(String notes3) {
        this.notes3 = notes3;
    }

    public String getNotes4() {
        return notes4;
    }

    public void setNotes4(String notes4) {
        this.notes4 = notes4;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public String getNotificationAll() {
        return notificationAll;
    }

    public void setNotificationAll(String notificationAll) {
        this.notificationAll = notificationAll;
    }

    public String getNotificationSilent() {
        return notificationSilent;
    }

    public void setNotificationSilent(String notificationSilent) {
        this.notificationSilent = notificationSilent;
    }

    public String getNotificationOnly() {
        return notificationOnly;
    }

    public void setNotificationOnly(String notificationOnly) {
        this.notificationOnly = notificationOnly;
    }

    public String getRingOnly() {
        return ringOnly;
    }

    public void setRingOnly(String ringOnly) {
        this.ringOnly = ringOnly;
    }

    public String getOnHours() {
        return onHours;
    }

    public void setOnHours(String onHours) {
        this.onHours = onHours;
    }

    public String getOffHours() {
        return offHours;
    }

    public void setOffHours(String offHours) {
        this.offHours = offHours;
    }

    public String getOnMinutes() {
        return onMinutes;
    }

    public void setOnMinutes(String onMinutes) {
        this.onMinutes = onMinutes;
    }

    public String getOffMinutes() {
        return offMinutes;
    }

    public void setOffMinutes(String offMinutes) {
        this.offMinutes = offMinutes;
    }

    public String getIsTimer1() {
        return timer1;
    }

    public void setIsTimer1(String isTimerActive) {
        this.timer1 = timer1;
    }



    public String getIsTimer2() {
        return timer2;
    }

    public void setIsTimer2(String isTimerActive) {
        this.timer2 = timer2;
    }


    public String getIsTimer3() {
        return timer3;
    }

    public void setIsTimer3(String isTimerActive) {
        this.timer3 = timer3;
    }


    public String getIsTimer4() {
        return timer4;
    }

    public void setIsTimer4(String isTimerActive) {
        this.timer4 = timer4;
    }





    public String getIsTimerActive() {
        return isTimerActive;
    }

    public void setIsTimerActive(String isTimerActive) {
        this.isTimerActive = isTimerActive;
    }

    public String getIsAllDays() {
        return isAllDays;
    }

    public void setIsAllDays(String isAllDays) {
        this.isAllDays = isAllDays;
    }

    public String getIsPower() {
        return isPower;
    }

    public void setIsPower(String isPower) {
        this.isPower = isPower;
    }

    public String getIsPowerring() {
        return isPowerring;
    }

    public void setIsPowerring(String isPowerring) {
        this.isPowerring = isPowerring;
    }



    public String getIsMotion() {
        return isMotion;
    }

    public void setIsFire(String isFire) {
        this.isFire = isFire;
    }

    public String getIsFire() {
        return isFire;
    }

    public void setIsAutofirehooter(String isAutofirehooter) {
        this.isAutofirehooter = isAutofirehooter;
    }

    public String getIsAutofirehooter() {
        return isAutofirehooter;
    }


    public void setAutofirehooterTime(String autofirehootertime) {
        this.autofirehootertime = autofirehootertime;
    }

    public String getAutofirehooterTime() {
        return autofirehootertime;
    }

    public void setIsFiremodel(String isFiremodel) {
        this.isFiremodel = isFiremodel;
    }

    public String getIsFiremodel() {
        return isFiremodel;
    }



    public void setIsMotion(String isMotion) {
        this.isMotion = isMotion;
    }



    public String getIsVibration() {
        return isVibration;
    }

    public void setIsVibration(String isVibration) {
        this.isVibration = isVibration;
    }

    public String getVibrationOnly() {
        return vibrationOnly;
    }

    public void setVibrationOnly(String vibrationOnly) {
        this.vibrationOnly = vibrationOnly;
    }

    public String getFamNotification1() {
        return famNotification1;
    }

    public void setFamNotification1(String famNotification1) {
        this.famNotification1 = famNotification1;
    }

    public String getFamNotification2() {
        return famNotification2;
    }

    public void setFamNotification2(String famNotification2) {
        this.famNotification2 = famNotification2;
    }

    public String getFamNotification3() {
        return famNotification3;
    }

    public void setFamNotification3(String famNotification3) {
        this.famNotification3 = famNotification3;
    }

    public String getFamNotification4() {
        return famNotification4;
    }

    public void setFamNotification4(String famNotification4) {
        this.famNotification4 = famNotification4;
    }

    public String getFamTone1() {
        return famTone1;
    }

    public void setFamTone1(String famTone1) {
        this.famTone1 = famTone1;
    }

    public String getFamTone2() {
        return famTone2;
    }

    public void setFamTone2(String famTone2) {
        this.famTone2 = famTone2;
    }

    public String getFamTone3() {
        return famTone3;
    }

    public void setFamTone3(String famTone3) {
        this.famTone3 = famTone3;
    }

    public String getFamTone4() {
        return famTone4;
    }

    public void setFamTone4(String famTone4) {
        this.famTone4 = famTone4;
    }

    public String getFamNotificationEmail() {
        return famNotificationEmail;
    }

    public void setFamNotificationEmail(String famNotificationEmail) {
        this.famNotificationEmail = famNotificationEmail;
    }

    public String getFamNotificationAll() {
        return famNotificationAll;
    }

    public void setFamNotificationAll(String famNotificationAll) {
        this.famNotificationAll = famNotificationAll;
    }

    public String getFamVibrationOnly() {
        return famVibrationOnly;
    }

    public void setFamVibrationOnly(String famVibrationOnly) {
        this.famVibrationOnly = famVibrationOnly;
    }

    public String getFamNotificationSilent() {
        return famNotificationSilent;
    }

    public void setFamNotificationSilent(String famNotificationSilent) {
        this.famNotificationSilent = famNotificationSilent;
    }

    public String getFamNotificationOnly() {
        return famNotificationOnly;
    }

    public void setFamNotificationOnly(String famNotificationOnly) {
        this.famNotificationOnly = famNotificationOnly;
    }

    public String getFamRingOnly() {
        return famRingOnly;
    }

    public void setFamRingOnly(String famRingOnly) {
        this.famRingOnly = famRingOnly;
    }
}