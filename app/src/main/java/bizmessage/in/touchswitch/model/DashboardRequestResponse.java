package bizmessage.in.touchswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DashboardRequestResponse implements Serializable {

    @SerializedName("esps")
    @Expose
    private List<Esp> esps = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("initoffer")
    @Expose
    private String initoffer;

    @SerializedName("isdealer")
    @Expose
    private String isdealer;



    @SerializedName("devices")
    @Expose
    private String devices;
    @SerializedName("alerts")
    @Expose
    private String alerts;

    @SerializedName("wifi")
    @Expose
    private String wifi;




    @SerializedName("notitype")
    @Expose
    private String notitype;

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("is_led_on")
    @Expose
    private String is_led_on;

    @SerializedName("is_plug_hooter_on")
    @Expose
    private String is_plug_hooter_on;


    @SerializedName("is_light_on")
    @Expose
    private String is_light_on;




    public String getInitoffer() {
        return initoffer;
    }

    public void setInitoffer(String initoffer) {
        this.initoffer = initoffer;
    }






    public List<Esp> getEsps() {
        return esps;
    }

    public void setEsps(List<Esp> esps) {
        this.esps = esps;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }



    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }





    public String getNotitype() {
        return notitype;
    }

    public void setNotitype(String notitype) {
        this.notitype = notitype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Esp implements Serializable{

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("roomtype")
        @Expose
        private String roomtype;

        @SerializedName("touchtype")
        @Expose
        private String touchtype;


        @SerializedName("email")
        @Expose
        private String email;





        @SerializedName("ssid")
        @Expose
        private String ssid;
        @SerializedName("mqttupgrade")
        @Expose
        private String mqttupgrade;
        @SerializedName("paswd")
        @Expose
        private String paswd;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("r_play_update")
        @Expose
        private String playupdate;
        @SerializedName("devid")
        @Expose
        private String devid;


        @SerializedName("offer")
        @Expose
        private String offer;

        @SerializedName("vcc")
        @Expose
        private String vcc;

        @SerializedName("devip")
        @Expose
        private String devip;
        @SerializedName("r_dev_wifi")
        @Expose
        private String rDevWifi;



        @SerializedName("show")
        @Expose
        private String show;

        @SerializedName("lwt")
        @Expose
           private String lwt = "";
        @SerializedName("main")
        @Expose
        private String main;

        @SerializedName("parent")
        @Expose
        private String parent;

        @SerializedName("is_light_on")
        @Expose
        private String is_light_on;


        @SerializedName("iswhatsalert")
        @Expose
        private String iswhatsalert;

        @SerializedName("whatsalertnumb")
        @Expose
        private String whatsalertnumb;




        @SerializedName("is_plug_hooter_on")
        @Expose
        private String is_plug_hooter_on;

        @SerializedName("is_buzzer_on")
        @Expose
        private String is_buzzer_on;


        @SerializedName("campackage")
        @Expose
        private String iscampackage;

        @SerializedName("is_led_on")
        @Expose
        private String is_led_on;


        @SerializedName("camurl")
        @Expose
        private String iscamurl;


        @SerializedName("is_hooter")
        @Expose
        private String is_hooter;

        @SerializedName("nikname")
        @Expose
        private String nikname = "Smart Home";


        @SerializedName("prop")
        @Expose
        private String prop;
        @SerializedName("active")
        @Expose
        private String active;
        @SerializedName("r_plug")
        @Expose
        private String rPlug;
        @SerializedName("r_noti_email")
        @Expose
        private String rNotiEmail;
        @SerializedName("r_noti_sms")
        @Expose
        private String rNotiSms;
        @SerializedName("r_alert_count")
        @Expose
        private String rAlertCount;
        @SerializedName("r_on_h")
        @Expose
        private String rOnH;
        @SerializedName("r_on_m")
        @Expose
        private String rOnM;
        @SerializedName("r_act_timer")
        @Expose
        private String rActTimer;
        @SerializedName("r_off_h")
        @Expose
        private String rOffH;
        @SerializedName("r_off_m")
        @Expose
        private String rOffM;
        @SerializedName("r_all_day")
        @Expose
        private String rAllDay;
        @SerializedName("r_blk_all_noti")
        @Expose
        private String rBlkAllNoti;
        @SerializedName("r_sil_noti")
        @Expose
        private String rSilNoti;
        @SerializedName("r_noti_only")
        @Expose
        private String rNotiOnly;
        @SerializedName("r_ring_only")
        @Expose
        private String rRingOnly;
        @SerializedName("r_vib_only")
        @Expose
        private String rVibOnly;
        @SerializedName("r_head_name")
        @Expose
        private String rHeadName;
        @SerializedName("r_fam_email")
        @Expose
        private String rFamEmail;

        @SerializedName("r_fam_pass")
        @Expose
        private String rFamPass;

        @SerializedName("is_vibrate_model")
        @Expose
        private String risVibratemodel;

        @SerializedName("shared")
        @Expose
        private String shared;

        @SerializedName("mqtthost")
        @Expose
        private String mqtthost;

        @SerializedName("mqttuser")
        @Expose
        private String mqttuser;

        @SerializedName("mqttpass")
        @Expose
        private String mqttpass;








        @SerializedName("share_allow")
        @Expose
        private String share_allow;


        @SerializedName("familyToken")
        @Expose
        private String familyToken;
        @SerializedName("r_fam_ispushy")
        @Expose
        private String rFamIspushy;
        @SerializedName("r_is_pushy")
        @Expose
        private String rIsPushy;
        @SerializedName("r_pushy_token")
        @Expose
        private String rPushyToken;
        @SerializedName("r_fam_blk_all_noti")
        @Expose
        private String rFamBlkAllNoti;
        @SerializedName("r_fam_sil_noti")
        @Expose
        private String rFamSilNoti;
        @SerializedName("r_fam_noti_only")


        @Expose
        private String rFamNotiOnly;
        @SerializedName("r_fam_ring_only")
        @Expose
        private String rFamRingOnly;
        @SerializedName("r_fam_vib_only")
        @Expose
        private String rFamVibOnly;
        @SerializedName("r_fam_noti_email")
        @Expose
        private String rFamNotiEmail;
        @SerializedName("r_fam_noti_sms")
        @Expose
        private String rFamNotiSms;
        @SerializedName("r_tone1")
        @Expose
        private String rTone1;
        @SerializedName("r_tone2")
        @Expose
        private String rTone2;
        @SerializedName("r_tone3")
        @Expose
        private String rTone3;
        @SerializedName("r_tone4")
        @Expose
        private String rTone4;
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
        @SerializedName("r_noti1")
        @Expose
        private String rNoti1;
        @SerializedName("r_noti2")
        @Expose
        private String rNoti2;
        @SerializedName("r_noti3")
        @Expose
        private String rNoti3;
        @SerializedName("r_noti4")
        @Expose
        private String rNoti4;
        @SerializedName("fam_noti1")
        @Expose
        private String famNoti1;
        @SerializedName("fam_noti2")
        @Expose
        private String famNoti2;
        @SerializedName("fam_noti3")
        @Expose
        private String famNoti3;
        @SerializedName("fam_noti4")
        @Expose
        private String famNoti4;
        @SerializedName("r_is_switch")
        @Expose
        private String rIsSwitch;
        @SerializedName("r_is_led")
        @Expose
        private String rIsLed;
        @SerializedName("r_is_plug")
        @Expose
        private String rIsPlug;
        @SerializedName("r_light_onoff")
        @Expose
        private String rLightOnoff;
        @SerializedName("r_plug_onoff")
        @Expose
        private String rPlugOnoff;
        @SerializedName("r_led_onoff")
        @Expose
        private String rLedOnoff;
        @SerializedName("r_whatsapp")
        @Expose
        private String rWhatsapp;
        @SerializedName("r_pow_alert")
        @Expose
        private String rPowAlert;
        @SerializedName("r_mot_alert")
        @Expose
        private String rMotAlert;
        @SerializedName("r_vib_alert")
        @Expose
        private String rVibAlert;
        @SerializedName("r_fam_pow_alert")
        @Expose
        private String rFamPowAlert;
        @SerializedName("r_fam_mot_alert")
        @Expose
        private String rFamMotAlert;
        @SerializedName("r_fam_vib_alert")
        @Expose
        private String rFamVibAlert;
        @SerializedName("call911")
        @Expose
        private String Call911;

        @SerializedName("alertmode")
        @Expose
        private String alertMode;

        @SerializedName("alertmodefam")
        @Expose
        private String alertModef;

        @SerializedName("alertscount")
        @Expose
        private String alertsCount;

        @SerializedName("setting_lock")
        @Expose
        private String setting_lock;

        @SerializedName("image")
        @Expose
        private String image;

        public String getAlertsCount() {
            return alertsCount;
        }

        public void setAlertsCount(String alertsCount) {
            this.alertsCount = alertsCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoomtype() {
            return roomtype;
        }

        public void setroomtype(String roomtype) {
            this.roomtype = roomtype;
        }


        public String getTouchtype() {
            return touchtype;
        }

        public void setTouchtype(String touchtype) {
            this.touchtype = touchtype;
        }




        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }





        public String getFampass() {
            return rFamPass;
        }

        public void setFampass(String rFamPass) {
            this.rFamPass = rFamPass;
        }


        public String getOffer() {
            return offer;
        }

        public void setOffer(String offer) {
            this.offer = offer;
        }




        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getMqttupgrade() {
            return mqttupgrade;
        }

        public void setMqttupgrade(String mqttupgrade) {
            this.mqttupgrade = mqttupgrade;
        }

        public String getPaswd() {
            return paswd;
        }

        public void setPaswd(String paswd) {
            this.paswd = paswd;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }


        public String getLock() {
            return setting_lock;
        }

        public void setLock(String lng) {
            this.setting_lock = setting_lock;
        }


        public String getPlayupdate() {
            return playupdate;
        }

        public void setPlayupdate(String playupdate) {
            this.playupdate = playupdate;
        }

        public String getDevid() {
            return devid;
        }

        public void setDevid(String devid) {
            this.devid = devid;
        }

        public String getDevip() {
            return devip;
        }

        public void setDevip(String devip) {
            this.devip = devip;
        }


        public String getVcc() {
            return vcc;
        }

        public void setVcc(String vcc) {
            this.vcc = vcc;
        }


        public String getRDevWifi() {
            return rDevWifi;
        }



        public void setRDevWifi(String rDevWifi) {
            this.rDevWifi = rDevWifi;
        }

        public String getLwt() {
            return lwt;
        }

        public void setLwt(String lwt) {
            this.lwt = lwt;
        }


         //below getShow() will tell if device is used one time ifgetShow()r returns   "1" then device is mqtt connected
            // if show is 0 then its kind of new device just joining. if its 0 for more than 1 day then we can delete in espstatus table
         public String getShow() {
            return show;
        }

        public void setShow(String show) {
            this.show = show;
        }



        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getisVibratemodel() {
            return risVibratemodel;
        }

        public void setisVibratemodel(String risVibratemodel) {
            this.risVibratemodel = risVibratemodel;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }
        //is_hooter

        public String getisHooter() {
            return is_hooter;
        }

        public void setisHooter(String is_hooter) {
            this.is_hooter = is_hooter;
        }


        public String getNikname() {
            return nikname;
        }

        public void setNikname(String nikname) {
            this.nikname = nikname;
        }

        public String getProp() {
            return prop;
        }

        public void setProp(String prop) {
            this.prop = prop;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getRPlug() {
            return rPlug;
        }

        public void setRPlug(String rPlug) {
            this.rPlug = rPlug;
        }

        public String getRNotiEmail() {
            return rNotiEmail;
        }

        public void setRNotiEmail(String rNotiEmail) {
            this.rNotiEmail = rNotiEmail;
        }

        public String getRNotiSms() {
            return rNotiSms;
        }

        public void setRNotiSms(String rNotiSms) {
            this.rNotiSms = rNotiSms;
        }

        public String getRAlertCount() {
            return rAlertCount;
        }

        public void setRAlertCount(String rAlertCount) {
            this.rAlertCount = rAlertCount;
        }

        public String getROnH() {
            return rOnH;
        }

        public void setROnH(String rOnH) {
            this.rOnH = rOnH;
        }

        public String getROnM() {
            return rOnM;
        }

        public void setROnM(String rOnM) {
            this.rOnM = rOnM;
        }

        public String getRActTimer() {
            return rActTimer;
        }

        public void setRActTimer(String rActTimer) {
            this.rActTimer = rActTimer;
        }

        public String getROffH() {
            return rOffH;
        }

        public void setROffH(String rOffH) {
            this.rOffH = rOffH;
        }

        public String getROffM() {
            return rOffM;
        }

        public void setROffM(String rOffM) {
            this.rOffM = rOffM;
        }

        public String getRAllDay() {
            return rAllDay;
        }

        public void setRAllDay(String rAllDay) {
            this.rAllDay = rAllDay;
        }

        public String getRBlkAllNoti() {
            return rBlkAllNoti;
        }

        public void setRBlkAllNoti(String rBlkAllNoti) {
            this.rBlkAllNoti = rBlkAllNoti;
        }

        public String getIslighton() {
            return is_light_on;
        }

        public void setIslighton(String is_light_on) {
            this.is_light_on = is_light_on;
        }


        public String getIsWhatsalert() {
            return iswhatsalert;
        }

        public void setIsWhatsalert(String iswhatsalert) {
            this.iswhatsalert = iswhatsalert;
        }


        public String getWhatsnum() {
            return whatsalertnumb;
        }

        public void setWhatsnum(String whatsalertnumb) {
            this.whatsalertnumb = whatsalertnumb;
        }







        public String getIsledon() {
            return is_led_on;
        }

        public void setIsledon(String is_led_on) {
            this.is_led_on = is_led_on;
        }


        public String getIscampackage() {
            return iscampackage;
        }

        public void setIscampackage(String iscampackage) {
            this.iscampackage = iscampackage;
        }


        public String getIscamurl() {
            return iscamurl;
        }

        public void setIscamurl(String iscamurl) {
            this.iscamurl = iscamurl;
        }





        public String getIsbuzzeron() {
            return is_buzzer_on;
        }

        public void setIsbuzzeron(String is_buzzer_on) {
            this.is_buzzer_on = is_buzzer_on;
        }


        public String getIsplughotteron() {
            return is_plug_hooter_on;
        }

        public void setIsplughotteron(String is_plug_hooter_on) {
            this.is_plug_hooter_on = is_plug_hooter_on;
        }


        public String getRSilNoti() {
            return rSilNoti;
        }

        public void setRSilNoti(String rSilNoti) {
            this.rSilNoti = rSilNoti;
        }

        public String getRNotiOnly() {
            return rNotiOnly;
        }

        public void setRNotiOnly(String rNotiOnly) {
            this.rNotiOnly = rNotiOnly;
        }

        public String getRRingOnly() {
            return rRingOnly;
        }

        public void setRRingOnly(String rRingOnly) {
            this.rRingOnly = rRingOnly;
        }

        public String getRVibOnly() {
            return rVibOnly;
        }

        public void setRVibOnly(String rVibOnly) {
            this.rVibOnly = rVibOnly;
        }

        public String getRHeadName() {
            return rHeadName;
        }

        public void setRHeadName(String rHeadName) {
            this.rHeadName = rHeadName;
        }

        public String getRFamEmail() {
            return rFamEmail;
        }

        public void setRFamEmail(String rFamEmail) {
            this.rFamEmail = rFamEmail;
        }

        public String getFamilyToken() {
            return familyToken;
        }

        public void setFamilyToken(String familyToken) {
            this.familyToken = familyToken;
        }


        public String getIsshared() {           return shared;
        }

        public void setIsshared(String shared) {
            this.shared = shared;
        }



        public String getmqtthost() {           return mqtthost;
        }

        public void setmqtthost(String mqtthost) {
            this.mqtthost = mqtthost;
        }



        public String getmqttuser() {           return mqttuser;
        }

        public void setmqttuser(String mqttuser) {
            this.mqttuser = mqttuser;
        }


        public String getmqttpass() {           return mqttpass;
        }

        public void setmqttpass(String mqttpass) {
            this.mqttpass = mqttpass;
        }








        public String getShareallow() { return share_allow;
        }

        public void setShareallow(String share_allow) {
            this.share_allow = share_allow;
        }


        public String getRFamIspushy() {
            return rFamIspushy;
        }

        public void setRFamIspushy(String rFamIspushy) {
            this.rFamIspushy = rFamIspushy;
        }

        public String getRIsPushy() {
            return rIsPushy;
        }

        public void setRIsPushy(String rIsPushy) {
            this.rIsPushy = rIsPushy;
        }

        public String getRPushyToken() {
            return rPushyToken;
        }

        public void setRPushyToken(String rPushyToken) {
            this.rPushyToken = rPushyToken;
        }

        public String getRFamBlkAllNoti() {
            return rFamBlkAllNoti;
        }

        public void setRFamBlkAllNoti(String rFamBlkAllNoti) {
            this.rFamBlkAllNoti = rFamBlkAllNoti;
        }

        public String getRFamSilNoti() {
            return rFamSilNoti;
        }

        public void setRFamSilNoti(String rFamSilNoti) {
            this.rFamSilNoti = rFamSilNoti;
        }

        public String getRFamNotiOnly() {
            return rFamNotiOnly;
        }

        public void setRFamNotiOnly(String rFamNotiOnly) {
            this.rFamNotiOnly = rFamNotiOnly;
        }

        public String getRFamRingOnly() {
            return rFamRingOnly;
        }

        public void setRFamRingOnly(String rFamRingOnly) {
            this.rFamRingOnly = rFamRingOnly;
        }

        public String getRFamVibOnly() {
            return rFamVibOnly;
        }

        public void setRFamVibOnly(String rFamVibOnly) {
            this.rFamVibOnly = rFamVibOnly;
        }

        public String getRFamNotiEmail() {
            return rFamNotiEmail;
        }

        public void setRFamNotiEmail(String rFamNotiEmail) {
            this.rFamNotiEmail = rFamNotiEmail;
        }

        public String getRFamNotiSms() {
            return rFamNotiSms;
        }

        public void setRFamNotiSms(String rFamNotiSms) {
            this.rFamNotiSms = rFamNotiSms;
        }

        public String getRTone1() {
            return rTone1;
        }

        public void setRTone1(String rTone1) {
            this.rTone1 = rTone1;
        }

        public String getRTone2() {
            return rTone2;
        }

        public void setRTone2(String rTone2) {
            this.rTone2 = rTone2;
        }

        public String getRTone3() {
            return rTone3;
        }

        public void setRTone3(String rTone3) {
            this.rTone3 = rTone3;
        }

        public String getRTone4() {
            return rTone4;
        }

        public void setRTone4(String rTone4) {
            this.rTone4 = rTone4;
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

        public String getRNoti1() {
            return rNoti1;
        }

        public void setRNoti1(String rNoti1) {
            this.rNoti1 = rNoti1;
        }

        public String getRNoti2() {
            return rNoti2;
        }

        public void setRNoti2(String rNoti2) {
            this.rNoti2 = rNoti2;
        }

        public String getRNoti3() {
            return rNoti3;
        }

        public void setRNoti3(String rNoti3) {
            this.rNoti3 = rNoti3;
        }

        public String getRNoti4() {
            return rNoti4;
        }

        public void setRNoti4(String rNoti4) {
            this.rNoti4 = rNoti4;
        }

        public String getFamNoti1() {
            return famNoti1;
        }

        public void setFamNoti1(String famNoti1) {
            this.famNoti1 = famNoti1;
        }

        public String getFamNoti2() {
            return famNoti2;
        }

        public void setFamNoti2(String famNoti2) {
            this.famNoti2 = famNoti2;
        }

        public String getFamNoti3() {
            return famNoti3;
        }

        public void setFamNoti3(String famNoti3) {
            this.famNoti3 = famNoti3;
        }

        public String getFamNoti4() {
            return famNoti4;
        }

        public void setFamNoti4(String famNoti4) {
            this.famNoti4 = famNoti4;
        }

        public String getRIsSwitch() {
            return rIsSwitch;
        }

        public void setRIsSwitch(String rIsSwitch) {
            this.rIsSwitch = rIsSwitch;
        }

        public String getRIsLed() {
            return rIsLed;
        }

        public void setRIsLed(String rIsLed) {
            this.rIsLed = rIsLed;
        }

        public String getRIsPlug() {
            return rIsPlug;
        }

        public void setRIsPlug(String rIsPlug) {
            this.rIsPlug = rIsPlug;
        }

        public String getRLightOnoff() {
            return rLightOnoff;
        }

        public void setRLightOnoff(String rLightOnoff) {
            this.rLightOnoff = rLightOnoff;
        }

        public String getRPlugOnoff() {
            return rPlugOnoff;
        }

        public void setRPlugOnoff(String rPlugOnoff) {
            this.rPlugOnoff = rPlugOnoff;
        }

        public String getRLedOnoff() {
            return rLedOnoff;
        }

        public void setRLedOnoff(String rLedOnoff) {
            this.rLedOnoff = rLedOnoff;
        }

        public String getRWhatsapp() {
            return rWhatsapp;
        }

        public void setRWhatsapp(String rWhatsapp) {
            this.rWhatsapp = rWhatsapp;
        }

        public String getRPowAlert() {
            return rPowAlert;
        }

        public void setRPowAlert(String rPowAlert) {
            this.rPowAlert = rPowAlert;
        }

        public String getRMotAlert() {
            return rMotAlert;
        }

        public void setRMotAlert(String rMotAlert) {
            this.rMotAlert = rMotAlert;
        }

        public String getRVibAlert() {
            return rVibAlert;
        }

        public void setRVibAlert(String rVibAlert) {
            this.rVibAlert = rVibAlert;
        }

        public String getRFamPowAlert() {
            return rFamPowAlert;
        }

        public void setRFamPowAlert(String rFamPowAlert) {
            this.rFamPowAlert = rFamPowAlert;
        }

        public String getRFamMotAlert() {
            return rFamMotAlert;
        }

        public void setRFamMotAlert(String rFamMotAlert) {
            this.rFamMotAlert = rFamMotAlert;
        }

        public String getRFamVibAlert() {
            return rFamVibAlert;
        }

        public void setRFamVibAlert(String rFamVibAlert) {
            this.rFamVibAlert = rFamVibAlert;
        }

        public String getCall911() {
            return Call911;
        }

        public void setCall911(String call911) {
            this.Call911 = Call911;
        }

        public String getAlertMode() {
            return alertMode;
        }

        public void setAlertMode(String alertMode) {
            this.alertMode = alertMode;
        }

        public String getAlertModef() {
            return alertModef;
        }

        public void setAlertModef(String stg) {
            this.alertModef = stg;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}