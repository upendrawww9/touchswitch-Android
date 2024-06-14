package bizmessage.in.touchswitch.utils;

import com.google.gson.Gson;
import bizmessage.in.touchswitch.model.LoginRequestResponse;

public class PreferenceData {

    public static String getDeviceToken() {
        return (SharedPreferenceUtil.contains(AppConstant.DEVICE_TOKEN) &&
                (SharedPreferenceUtil.getString(AppConstant.DEVICE_TOKEN, null) != null) ?
                SharedPreferenceUtil.getString(AppConstant.DEVICE_TOKEN, null) : "");
    }

    public static void setDeviceToken(String deviceToken){
        SharedPreferenceUtil.putValue(AppConstant.DEVICE_TOKEN,deviceToken);
    }

    public static void setPassword(String password) {
        SharedPreferenceUtil.putValue(AppConstant.PASSWORD, password);
    }

    public static String getPassword() {
        return SharedPreferenceUtil.getString(AppConstant.PASSWORD, "");
    }

    public static void setLanguage(String password) {
        SharedPreferenceUtil.putValue(AppConstant.Language, password);
    }

    public static String getlanguage() {
        return SharedPreferenceUtil.getString(AppConstant.Language, "en");
    }


    public static void setFCM(String fcm) {
        SharedPreferenceUtil.putValue(AppConstant.fcm, fcm);
    }

    public static String getFCM() {
        return SharedPreferenceUtil.getString(AppConstant.fcm, "en");
    }





    public static void setisLanguage(String password) {
        SharedPreferenceUtil.putValue(AppConstant.ISLanguage, password);
    }

    public static String getislanguage() {
        return SharedPreferenceUtil.getString(AppConstant.ISLanguage, "true");
    }







    public static void setSkip(boolean skip) {
        SharedPreferenceUtil.putValue(AppConstant.SKIP, skip);
    }

    public static boolean isSkip() {
        return SharedPreferenceUtil.getBoolean(AppConstant.SKIP, false);
    }
//ISLIGHTON

    public static void setisLighton(boolean isLighton) {
        SharedPreferenceUtil.putValue(AppConstant.ISLIGHTON, isLighton);
    }

    public static boolean isLighton() {
        return SharedPreferenceUtil.getBoolean(AppConstant.ISLIGHTON, false);
    }

    public static void setisLedon(boolean isLedon) {
        SharedPreferenceUtil.putValue(AppConstant.ISLEDON, isLedon);
    }

    public static boolean isLedon() {
        return SharedPreferenceUtil.getBoolean(AppConstant.ISLEDON, false);
    }

    public static void setSensoron(boolean isSensoron) {
        SharedPreferenceUtil.putValue(AppConstant.ISSENSORON, isSensoron);
    }

    public static boolean isSensoron() {
        return SharedPreferenceUtil.getBoolean(AppConstant.ISSENSORON, false);
    }





    public static void setisSirenon(boolean isSirenon) {
        SharedPreferenceUtil.putValue(AppConstant.ISSIRENON, isSirenon);
    }

    public static boolean isSirenon() {
        return SharedPreferenceUtil.getBoolean(AppConstant.ISSIRENON, false);
    }

    public static void setisPowring(boolean isPowring) {
        SharedPreferenceUtil.putValue(AppConstant.ISPOWERRING, isPowring);
    }

    public static boolean getisPowerring() {
        return SharedPreferenceUtil.getBoolean(AppConstant.ISPOWERRING, false);
    }


    public static void setDarkTheme(boolean isDarkTheme) {
        SharedPreferenceUtil.putValue(AppConstant.THEME, isDarkTheme);
    }

    public static boolean isDarkTheme() {
        return SharedPreferenceUtil.getBoolean(AppConstant.THEME, false);
    }

    public static void setIsparent(String isfamily) {
        SharedPreferenceUtil.putValue(AppConstant.IS_FAMIY, isfamily);
    }

    public static String getIsparent() {
        return SharedPreferenceUtil.getString(AppConstant.LOCTIME, "");
    }



    public static void setLoctime(String loctime) {
        SharedPreferenceUtil.putValue(AppConstant.LOCTIME, loctime);
    }

    public static String getLoctime() {
        return SharedPreferenceUtil.getString(AppConstant.LOCTIME, "");
    }







    public static void setSilent(String silpre) {
        SharedPreferenceUtil.putValue(AppConstant.SOUND_SILENT, silpre);
    }

    public static String getSilent() {
        return SharedPreferenceUtil.getString(AppConstant.SOUND_SILENT, "");
    }

    public static void setNoti(String notipre) {
        SharedPreferenceUtil.putValue(AppConstant.SOUND_NOTI, notipre);
    }

    public static String getNoti() {
        return SharedPreferenceUtil.getString(AppConstant.SOUND_NOTI, "");
    }

    public static void setRing(String ringpre) {
        SharedPreferenceUtil.putValue(AppConstant.SOUND_RING, ringpre);
    }

    public static String getRing() {
        return SharedPreferenceUtil.getString(AppConstant.SOUND_RING, "");
    }

    public static void setVib(String vibpre) {
        SharedPreferenceUtil.putValue(AppConstant.SOUND_VIB, vibpre);
    }

    public static String getVib() {
        return SharedPreferenceUtil.getString(AppConstant.SOUND_VIB, "");
    }


    public static void setNotinum(String notinumpre) {
        SharedPreferenceUtil.putValue(AppConstant.SOUND_NOTI_NUM, notinumpre);
    }

    public static String getNotinum() {
        return SharedPreferenceUtil.getString(AppConstant.SOUND_NOTI_NUM, "");
    }

    public static void setRingnum(String ringnumpre) {
        SharedPreferenceUtil.putValue(AppConstant.SOUND_RING_NUM, ringnumpre);
    }

    public static String getRingnum() {
        return SharedPreferenceUtil.getString(AppConstant.SOUND_RING_NUM, "");
    }

    public static void setIsfire(String isfire) { // For fast view look its duplication info
        SharedPreferenceUtil.putValue(AppConstant.IS_FIRE, isfire);
    }

    public static String getIsfire() { // For fast view look its duplication info
        return SharedPreferenceUtil.getString(AppConstant.IS_FIRE, "");
    }




    /////  upen end








    public static void setBuzzStatus(boolean buzzStatus) {
        SharedPreferenceUtil.putValue(AppConstant.BUZZ_STATUS, buzzStatus);
    }

    public static boolean isBuzzStatusOn() {
        return SharedPreferenceUtil.getBoolean(AppConstant.BUZZ_STATUS, false);
    }


    public static void setDevshare(String mobid) {
        SharedPreferenceUtil.putValue(AppConstant.DEV_SHARE_MOBID, mobid);
    }

    public static String getDevshare() {
        return SharedPreferenceUtil.getString(AppConstant.DEV_SHARE_MOBID, "");
    }


    public static void setFamilyPassword(String password) {
        SharedPreferenceUtil.putValue(AppConstant.PASSWORD_FAMILY, password);
    }

    public static String getFamilyPassword() {
        return SharedPreferenceUtil.getString(AppConstant.PASSWORD_FAMILY, "");
    }

    public static void setLight1OnHoursTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT1_ON_HOUR_TIME, time);
    }

    public static String getLight1OnHoursTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT1_ON_HOUR_TIME, "00");
    }

    public static void setLight2OnHoursTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT2_ON_HOUR_TIME, time);
    }

    public static String getLight2OnHoursTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT2_ON_HOUR_TIME, "00");
    }

    public static void setLight1OffHoursTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT1_OFF_HOUR_TIME, time);
    }

    public static String getLight1OffHoursTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT1_OFF_HOUR_TIME, "00");
    }

    public static void setLight2OffHoursTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT2_OFF_HOUR_TIME, time);
    }

    public static String getLight2OffHoursTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT2_OFF_HOUR_TIME, "00");
    }

    public static void setLight1OnMinuteTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT1_ON_MINUTE_TIME, time);
    }

    public static String getLight1OnMinuteTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT1_ON_MINUTE_TIME, "00");
    }

    public static void setLight2OnMinuteTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT2_ON_MINUTE_TIME, time);
    }

    public static String getLight2OnMinuteTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT2_ON_MINUTE_TIME, "00");
    }





    public static void setLight1OffMinuteTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT1_OFF_MINUTE_TIME, time);
    }

    public static String getLight1OffMinuteTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT1_OFF_MINUTE_TIME, "00");
    }

    public static void setLight2OffMinuteTime(String time) {
        SharedPreferenceUtil.putValue(AppConstant.LIGHT2_OFF_MINUTE_TIME, time);
    }

    public static String getLight2OffMinuteTime() {
        return SharedPreferenceUtil.getString(AppConstant.LIGHT2_OFF_MINUTE_TIME, "00");
    }

    public static void setDeviceId(String deviceId) {
        SharedPreferenceUtil.putValue(AppConstant.DEVICE_ID, deviceId);
    }

    public static String getDeviceId() {
        return SharedPreferenceUtil.getString(AppConstant.DEVICE_ID, "");
    }

    public static void setNickName(String deviceId) {
        SharedPreferenceUtil.putValue(AppConstant.NICK_NAME, deviceId);
    }

    public static String getNickName() {
        return SharedPreferenceUtil.getString(AppConstant.NICK_NAME, "");
    }

    public static void setAppVersion(String appVersionCode) {
        SharedPreferenceUtil.putValue(AppConstant.APP_VERSION_CODE, appVersionCode);
    }

    public static String getAppVersion() {
        return SharedPreferenceUtil.getString(AppConstant.APP_VERSION_CODE, "");
    }

    public static void setMotionControlValue(int motionControlValue) {
        SharedPreferenceUtil.putValue(AppConstant.MOTION_CONTROL_VALUE, motionControlValue);
    }

    public static int getMotionControlValue() {
        return SharedPreferenceUtil.getInt(AppConstant.MOTION_CONTROL_VALUE, 0);
    }


    public static void setWifino(int wifinets) {
        SharedPreferenceUtil.putValue(AppConstant.TOTAL_WIFINETWORKS, wifinets);
    }

    public static int getWifino() {
        return SharedPreferenceUtil.getInt(AppConstant.TOTAL_WIFINETWORKS, 0);
    }


    public static void setCurrentSelectedPage(int page) {
        SharedPreferenceUtil.putValue("PAGE", page);
    }

    public static int getCurrentSelectedPage() {
        return SharedPreferenceUtil.getInt("PAGE", 0);
    }



    /* isdealer concept is created to make device adding easy.
                 Any normal end customer can also use this he will enter device id and user will see device as offline and he ac
                 tivate hotspot so that he get online. Any dealer  or seller can create one page info with customer name and device id so that user can login


                */
    public static void setIsdealer(String isdealer) {
        SharedPreferenceUtil.putValue(AppConstant.ISDEALER, isdealer);
    }

    public static String getIsdealer() {
        return SharedPreferenceUtil.getString(AppConstant.ISDEALER, "");
    }



    public static void setLoginid(String loginid) {
        SharedPreferenceUtil.putValue(AppConstant.LOGINID, loginid);
    }

    public static String getLoginid() {
        return SharedPreferenceUtil.getString(AppConstant.LOGINID, "");
    }



    public static void setEmail(String email) {
        SharedPreferenceUtil.putValue(AppConstant.EMAIL, email);
    }

    public static String getEmail() {
        return SharedPreferenceUtil.getString(AppConstant.EMAIL, "");
    }

    public static void setSupportNumber(String supportNumber) {
        SharedPreferenceUtil.putValue(AppConstant.SUPPORT, supportNumber);
    }

    public static String getSupportNumber() {
        return SharedPreferenceUtil.getString(AppConstant.SUPPORT, "");
    }

    public static void setLatitude(String latitude) {
        SharedPreferenceUtil.putValue(AppConstant.LATITUDE, latitude);
    }

    public static String getLatitude() {
        return SharedPreferenceUtil.getString(AppConstant.LATITUDE, "");
    }

    public static void setLongitude(String longitude) {
        SharedPreferenceUtil.putValue(AppConstant.LONGITUDE, longitude);
    }

    public static String getLongitude() {
        return SharedPreferenceUtil.getString(AppConstant.LONGITUDE, "");
    }

    public static void setUserId(String userId) {
        SharedPreferenceUtil.putValue(AppConstant.USER_ID, userId);
    }

    public static String getUserId() {
        return SharedPreferenceUtil.getString(AppConstant.USER_ID, "");
    }

    public static void setUserName(String userId) {
        SharedPreferenceUtil.putValue(AppConstant.NAME, userId);
    }

    public static String getUserName() {
        return SharedPreferenceUtil.getString(AppConstant.NAME, "");
    }

    public static void setLogin(boolean isLogin){
        SharedPreferenceUtil.putValue(AppConstant.IS_LOGIN, isLogin);
    }

    public static boolean isLogin(){
        return SharedPreferenceUtil.getBoolean(AppConstant.IS_LOGIN, false);
    }

    public static void setFcmToken(String fcmToken){
        SharedPreferenceUtil.putValue(AppConstant.FCM_TOKEN, fcmToken);
    }

    public static String getFcmToken(){
        return SharedPreferenceUtil.getString(AppConstant.FCM_TOKEN,"");
    }



    public static void setOffer(String offer){
        SharedPreferenceUtil.putValue(AppConstant.FCM_OFFER, offer);
    }

    public static String getOffer(){
        return SharedPreferenceUtil.getString(AppConstant.FCM_OFFER,"");
    }


    public static boolean getNotificationStatus(){
        return SharedPreferenceUtil.getBoolean(AppConstant.NOTIFICATION_STATUS, false);
    }

    public static void setNotificationStatus(boolean notification){
        SharedPreferenceUtil.putValue(AppConstant.NOTIFICATION_STATUS, notification);
    }

    public static void setUserData(LoginRequestResponse loginRequestResponse){
        Gson gson = new Gson();
        String json = gson.toJson(loginRequestResponse);
        SharedPreferenceUtil.putValue(AppConstant.USER_DATA, json);
    }

    public static LoginRequestResponse getUserData(){
        LoginRequestResponse userData = new LoginRequestResponse();
        Gson gson = new Gson();
        String userDataStr = SharedPreferenceUtil.getString(AppConstant.USER_DATA, "");
        userData = gson.fromJson(userDataStr,LoginRequestResponse.class);
        return userData;
    }

    public static void saveLoginUserData(String userData) {
        SharedPreferenceUtil.putValue(AppConstant.LOGIN_REQUEST_RESPONSE, userData);
    }

    public static void clear(){
        SharedPreferenceUtil.clear();
    }

}
