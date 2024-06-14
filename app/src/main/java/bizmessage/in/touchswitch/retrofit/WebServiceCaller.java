package bizmessage.in.touchswitch.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.model.AlertsRequestResponse;
import bizmessage.in.touchswitch.model.DashboardRequestResponse;
import bizmessage.in.touchswitch.model.LoginRequestResponse;
import bizmessage.in.touchswitch.model.SwitchSettingResponse;
import bizmessage.in.touchswitch.model.TimeAlertsSettingResponse;

import java.util.concurrent.TimeUnit;

import bizmessage.in.touchswitch.model.YoutubeRequestResponse;
import bizmessage.in.touchswitch.utils.PreferenceData;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
public class WebServiceCaller {

    private static ApiInterface apiInterface;

    public static ApiInterface getClient() {
        if (apiInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okClient = new OkHttpClient();
            Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(WebUtility.BASE_URL)
                    .client(okClient.newBuilder()
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(100, TimeUnit.SECONDS)
                            .writeTimeout(100, TimeUnit.SECONDS)
                            .addInterceptor(logging).build())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            apiInterface = client.create(ApiInterface.class);
        }
        return apiInterface;
    }

    public interface ApiInterface {

        @GET(WebUtility.LOGIN)
        Call<LoginRequestResponse> login(@Query("source") String source,
                                         @Query("appbuild") String appbuild,
                                         @Query("mobile_id") String mobile_id,
                                         @Query("r_pushy_token") String r_pushy_token,
                                         @Query("fcm_token") String fcm_token,
                                         @Query("pass") String pass,
                                         @Query("email") String email,
                                         @Query("ops") String ops);

        @GET(WebUtility.LOGIN)
        Call<ResponseBody> changePassword(@Query("email") String email,
                                          @Query("pass") String pass,
                                          @Query("ops") String ops);

        @GET(WebUtility.LOGIN)
        Call<ResponseBody> forgotPassword(@Query("email") String email,
                                          @Query("ops") String ops);


           @GET(WebUtility.LOGIN)
           Call<ResponseBody> shareDevice(@Query("mobid") String mobid,
                                           @Query("devid") String devid,
                                          @Query("parentemail") String parentemail,
                                           @Query("manu") String manu,
                                           @Query("brand") String brand,
                                           @Query("version") String version,
                                           @Query("lat") String lat,
                                           @Query("lng") String lng,
                                          @Query("action") String action,
                                          @Query("ops") String ops);




        @GET(WebUtility.LOGIN)
        Call<ResponseBody> addFamilyMember(@Query("email") String email,
                                           @Query("familyemail") String familyEmail,
                                           @Query("familypass") String familyPass,
                                           @Query("manu") String menu,
                                           @Query("brand") String brand,
                                           @Query("version") String version,
                                           @Query("lat") String lat,
                                           @Query("lng") String lng,
                                           @Query("ops") String ops);

        @POST(WebUtility.REGISTRATION)
        Call<ResponseBody> register(@Query("mobile_id") String mobile_id,

                                    @Query("manu") String manu,
                                    @Query("brand") String brand,
                                    @Query("version") String version,
                                    @Query("r_pushy_token") String r_pushy_token,
                                    @Query("email") String email,
                                    @Query("firstname") String firstname,
                                    @Query("lastname") String lastname,
                                    @Query("lat") String lat,
                                    @Query("lng") String lng,
                                    @Query("city") String city,
                                    @Query("mobile") String mobile,
                                    @Query("pass") String pass,
                                    @Query("ops") String ops);



        @POST(WebUtility.ADDDEVICE)
        Call<ResponseBody> adddevice(@Query("devid") String devid,
                                     @Query("dealeremail") String dealeremail,
                                     @Query("dealerpassword") String dealerpassword,
                                     @Query("custemail") String custemail,
                                    @Query("firstname") String firstname,
                                    @Query("lastname") String lastname,
                                    @Query("lat") String lat,
                                    @Query("lng") String lng,
                                    @Query("mobile") String mobile,
                                    @Query("pass") String pass,
                                    @Query("ops") String ops);


        @GET(WebUtility.ADD_SCAN_DEVICE)
        Call<ResponseBody> addDevice(@Query("email") String email,
                                     @Query("lat") String lat,
                                     @Query("lng") String lng,
                                     @Query("time") String time,
                                     @Query("devid") String devid,
                                     @Query("ssid") String ssid);


        @GET(WebUtility.SCAN_INVENT_DEVICE)
        Call<ResponseBody> addinventory(@Query("email") String email,
                                     @Query("lat") String lat,
                                     @Query("lng") String lng,
                                     @Query("time") String time,
                                     @Query("devid") String devid,
                                     @Query("ssid") String ssid);

        @GET(WebUtility.ADDWHATSNUMBER)
        Call<ResponseBody> addWhatsNumber(@Query("devid") String devid,
                                          @Query("whatnumber") String whatnumber,
                                          @Query("lat") String lat,
                                        @Query("lng") String lng,
                                        @Query("userid") String userid,
                                        @Query("ops") String whatsalert);






        @GET(WebUtility.ON_DASHBOARD_DATA)
        Call<DashboardRequestResponse> getDashboardData(@Query("email") String email);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateSettings(@Query("isfamily") String isfamily,
                                          @Query("noti") String notification,
                                          @Query("id") String id,
                                          @Query("checked") boolean isChecked,
                                          @Query("device") String device);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateSettingsAutoLight(@Query("isfamily") String isfamily,
                                                   @Query("noti") String notification,
                                                   @Query("id") String id,
                                                   @Query("checked") String isChecked,
                                                   @Query("device") String device);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateSettingsAutoBuzz(@Query("isfamily") String isfamily,
                                                  @Query("noti") String notification,
                                                  @Query("id") String id,
                                                  @Query("checked") String isChecked,
                                                  @Query("device") String device);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateSettingsAutoLed(@Query("isfamily") String isfamily,
                                                  @Query("noti") String notification,
                                                  @Query("id") String id,
                                                  @Query("checked") String isChecked,
                                                  @Query("device") String device);




        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateDeviceNameSettings(@Query("isfamily") String isfamily,
                                                    @Query("noti") String notification,
                                                    @Query("id") String id,
                                                    @Query("checked") String isChecked);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateDeviceNameSettings(@Query("isfamily") String isfamily,
                                                    @Query("noti") String notification,
                                                    @Query("id") String id,
                                                    @Query("fill") String filler,
                                                    @Query("checked") String isChecked);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateMotionSettings(@Query("isfamily") String isfamily,
                                                @Query("noti") String notification,
                                                @Query("sen") String sen,
                                                @Query("id") String id,
                                                @Query("checked") boolean checked);


        @GET(WebUtility.RESET_OFFER_WHATSAPP)
        Call<ResponseBody> resetwhatsappoffer(@Query("email") String email,
                                              @Query("initoffer") String initoffer);


        @GET(WebUtility.UPDATE_FIRE)
        Call<ResponseBody> updateToken(@Query("token") String token,
                                                @Query("email") String email,
                                                @Query("lat") String lat,
                                                @Query("lng") String lng,
                                       @Query("loctime") String loctime);



        @GET(WebUtility.UPDATE_LOCALE)
        Call<ResponseBody> updatelocale(@Query("noti") String notification,
                                        @Query("email") String email,
                                        @Query("language") String lan);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateEmergencyCallSettings(@Query("isfamily") String isfamily,
                                                       @Query("noti") String status,
                                                       @Query("no") String callNo,
                                                       @Query("id") String id,
                                                       @Query("checked") boolean checked);

        @GET(WebUtility.SETTINGS_DATA)
        Call<ResponseBody> updateWifiSettings(@Query("isfamily") String isfamily,
                                              @Query("noti") String notification,
                                              @Query("apn") String apn,
                                              @Query("id") String id,
                                              @Query("checked") boolean checked,
                                              @Query("wifipass") String wifipass,
                                              @Query("lat") String lat,
                                              @Query("lng") String lng );

        @GET(WebUtility.DEVICE_TIMER_DATA)
        Call<ResponseBody> getDeviceTimerData(@Query("devid") String devid,
                                              @Query("tonh") String tonh,
                                              @Query("tonm") String tonm,
                                              @Query("toffh") String toffh,
                                              @Query("toffm") String toffm,
                                              @Query("allday") String allday,
                                              @Query("act") String act);

        @GET(WebUtility.DEVICE_SETTING_DATA)
        Call<TimeAlertsSettingResponse> getDeviceSettingData(@Query("isfamily") String isfamily,
                                                             @Query("devid") String devid);


        @GET(WebUtility.MY_REPORTS_DATA)
        Call<AlertsRequestResponse> getMyReportsData(@Query("show") String show,
                                                     @Query("devid") String devid);


        @GET(WebUtility.MY_YOUTUBE_DATA)
        Call<YoutubeRequestResponse> getMyYoutubeData(@Query("show") String show,
                                                      @Query("devid") String devid,
                                                      @Query("language") String language);

        @GET(WebUtility.BUZZ_DATA)
        Call<ResponseBody> getBuzz(@Query("email") String email);

        @GET(WebUtility.JSON_DATA)
        Call<DashboardRequestResponse> getJsonData(@Query("appbuild") String appbuild,
                                                   @Query("email") String email,
                                                   @Query("password") String password,
                                                   @Query("lat") String lat,
                                                   @Query("lng") String lng,
                                                   @Query("version") String version,
                                                   @Query("loctime") String loctime);

        @GET(WebUtility.REGISTER_PUSHY)
        Call<ResponseBody> registerPushy(@Query("parent") String parent,
                                         @Query("mobile_id") String mobile_id,
                                         @Query("ops") String ops,
                                         @Query("email") String email,
                                         @Query("r_pushy_token") String r_pushy_token);

        @GET(WebUtility.UN_REGISTER_PUSHY)
        Call<ResponseBody> unRegisterPushy(@Query("parent") String parent,
                                           @Query("email") String email);

        @GET(WebUtility.SET_PLUG)
        Call<ResponseBody> setPlug(@Query("activate") String activate,
                                   @Query("ops") String ops,
                                   @Query("onhour") String onhour,
                                   @Query("onmin") String onmin,
                                   @Query("offhour") String offhour,
                                   @Query("offmin") String offmin,
                                   @Query("devid") String devid);

        @GET(WebUtility.GET_PLUG_SETTING)
        Call<SwitchSettingResponse> getPlugSettings(@Query("isfamily") String isfamily,
                                                    @Query("devid") String devid);

        @GET(WebUtility.SET_SWITCH)
        Call<ResponseBody> setSwitch(@Query("activate") String activate,
                                     @Query("ops") String ops,
                                     @Query("onhour") String onhour,
                                     @Query("onmin") String onmin,
                                     @Query("offhour") String offhour,
                                     @Query("offmin") String offmin,
                                     @Query("devid") String devid);

        @GET(WebUtility.GET_SWITCH_SETTINGS)
        Call<SwitchSettingResponse> getSwitchSetting(@Query("isfamily") String isfamily,
                                                     @Query("devid") String devid);



        @GET(WebUtility.HART_BEAT_SERVICE)
        Call<ResponseBody> updateHartBeatService(@Query("email") String email,
                                                 @Query("pass") String pass,
                                                 @Query("lat") String lat,
                                                 @Query("lng") String lng,
                                                 @Query("loctime") String loctime);

    }
}
