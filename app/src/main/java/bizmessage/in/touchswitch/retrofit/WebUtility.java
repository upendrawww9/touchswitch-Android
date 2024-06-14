package bizmessage.in.touchswitch.retrofit;

import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.utils.PreferenceData;

public class WebUtility  {





    public static final String BASE_URL = "http://www.ilmc.xyz/onlook/";


    public static final String API_SOURCE = "android";
    public static final String DEFAULT_TIME = "00";
    public static final String LOGIN_OPS = "login";
    public static final String REGISTER_OPS = "new";
    public static final String CHANGE_PASSWORD_OPS = "changepasswd";
    public static final String FORGOT_PASSWORD_OPS = "forgot";
    public static final String ADD_FAMILY_MEMBER_OPS = "addfamily";
    public static final String DEVICE_SHARE_OPS = "deviceshare";

    public static final String SET_TIME1_OPS = "t1";
    public static final String SET_TIME2_OPS = "t2";
    public static final String SET_PLUG1_OPS = "t3";
    public static final String SET_PLUG2_OPS = "t4";

    public static final String SET_LIGHT_ONOFF = "lonoff";
    public static final String SET_PLUG_ONOFF = "ponoff";
    public static final String SET_LED_ONOFF = "ledonoff";
    public static final String SET_VIB_ONOFF = "vibonoff";
    public static final String SET_BUZZ_ONOFF = "buzzonoff";

    public static final String SET_LWT = "setlwt";




    public static final String SUPPORT_EMAIL = "onlook@bizmessage.in";
    public static final String LOGIN = "login.php?";
    public static final String LOGIN1 = "jsondata.php?";
    public static final String REGISTRATION = "login.php?";
    public static final String DOWNLOAD_REPORT = "reportcsv.php?";
    public static final String ADD_SCAN_DEVICE = "createdev.php?";
    public static final String SCAN_INVENT_DEVICE = "inventory.php?";

    public static final String ON_DASHBOARD_DATA = "onboardhome.php?";
    public static final String UPDATE_LOCALE = "settings.php?";
    public static final String UPDATE_FIRE = "firetoken.php?";
    public static final String RESET_OFFER_WHATSAPP = "whatsappofferreset.php?";

    public static final String ADDWHATSNUMBER = "addwhatsnumber.php?";

    public static final String ADDDEVICE = "preloaduser.php?";



    public static final String SETTINGS_DATA = "settings.php?";
    public static final String DEVICE_TIMER_DATA = "devicetimer.php?";
    public static final String DEVICE_SETTING_DATA = "getdevsettings.php?";
    public static final String MORE_REPORTS_DATA = "morereport.php?";
    public static final String MY_REPORTS_DATA = "myreport.php?";
    public static final String MY_YOUTUBE_DATA = "youtube.php?";


    public static final String BUZZ_DATA = "buzz.php?";
    public static final String JSON_DATA = "jsondata.php?";
    public static final String REGISTER_PUSHY = "registerpushy.php?";
    public static final String UN_REGISTER_PUSHY = "unregisterpushy.php?";
    public static final String SET_PLUG = "setplug.php?";
    public static final String GET_PLUG_SETTING = "getplugsettings.php?";
    public static final String SET_SWITCH = "setswitch.php?";
    public static final String GET_SWITCH_SETTINGS = "getswitchsettings.php?";
    public static final String UPDATE_LOCATIONS = "updatelocation_new.php?";
    public static final String HART_BEAT_SERVICE = "heartbeat.php?";

//  public static final String WEB_HELP = "http://www.ilmc.xyz/support/customer/do_login?email="+ OnlookApplication.SELECTED_DEVICE.getEmail()+"&password="+PreferenceData.getPassword();

    public static final String WEB_HELP = "http://www.ilmc.xyz/support/public";
    public static final String WEB_Newcust = "http://www.ilmc.xyz/onlook/dealercust.php?email=";

     public static final String WEB_MY_DEVICE = "http://www.ilmc.xyz/launch/mydevstatus.php?email=";

    public static final String WEB_CONTACT_US = "http://www.ilmc.xyz/launch/index.php?email=";
    public static final String WEB_WHATS_APP = "https://api.whatsapp.com/send?phone=";


    public static final String MQTT_SERVER_URL = OnlookApplication.SELECTED_DEVICE.getmqtthost();
    public static final String MQTT_USER_ID = OnlookApplication.SELECTED_DEVICE.getmqttuser();
    public static final String MQTT_PASSWORD = OnlookApplication.SELECTED_DEVICE.getmqttpass();

    public static final String CLIENT_ID = "Switchbar";
    public static final String LED_CLIENT_ID = "Lightbar";
    public static final String PLUG_CLIENT_ID = "Plugbar";
    public static final String SENSOR_CLIENT_ID = "Sensibar";
    public static final String SSID_CLIENT_ID = "SSIDSettings";

}


