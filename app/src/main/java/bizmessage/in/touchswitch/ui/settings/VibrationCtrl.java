package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityVibrateBinding;
//import bizmessage.in.touchswitch.model.SwitchSettingResponse;

import bizmessage.in.touchswitch.model.TimeAlertSetting;
import bizmessage.in.touchswitch.model.TimeAlertsSettingResponse;

import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;


public class VibrationCtrl extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = VibrationCtrl.class.getSimpleName();
    private ActivityVibrateBinding binding;
    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "", isFamily = "no";
    private final boolean isSwitchOn = false;
    private boolean isOffLine = false;
    private int seekBarValue = 33;
    int qos=2;
    private TimeAlertSetting timeAlertSetting;
    final Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Resources resources;
    Call<ResponseBody> responseCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vibrate);
        setSupportActionBar(binding.toolbar);
        // getSupportActionBar().setTitle(R.string.title_motion_sensitivity_control_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnStatus1.setOnClickListener(this);
        setupMqttConnection();
        setSeekBarValue();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    if (mqttAndroidClient.isConnected()) {
                        mqttAndroidClient.close();
                        mqttAndroidClient.disconnect();
                    }
                } catch (NullPointerException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
                finish();
                //Do something after 100ms
            }
        }, 80000);

        if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "hi");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ka");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ta");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "te");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "bn");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ml");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "en");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "gu");
            resources = context.getResources();
        }

        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        binding.vibratehead.setText(resources.getString(R.string.vibratehead));///////////////
     //   binding.textVibration.setText(resources.getString(R.string.vibrate_activate));///////////////


        binding.textStatus.setText(resources.getString(R.string.motion_sensivity_status));///////////////
        binding.textChangebar.setText(resources.getString(R.string.motion_sensivity_changebar));///////////////

        binding.textSensorTimeDescription.setText(resources.getString(R.string.motion_sensivity_desc));///////////////
         binding.btnStatus1.setText(resources.getString(R.string.motion_activate));///////////////
        Utility.showToast(resources.getString(R.string.please_wait));

       getSupportActionBar().setTitle(resources.getString(R.string.vibrate));

        if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
            isOffLine = false;
        } else {
            setDeviceOffline();
        }
        if (isFamilyMember().contains("yes")) {
            binding.btnStatus1.setEnabled(false);
            binding.btnStatus1.setVisibility(View.GONE);
            binding.textTimeStatus1.setVisibility(View.GONE);
            Utility.showToast(resources.getString(R.string.not_ebable));

        }

    }

    private void setSeekBarValue() {
        seekBarValue = PreferenceData.getMotionControlValue();
        if (DBG) Log.i(TAG, "Seekbar: 1" );

        binding.textSensorTimeDescriptionInfo.setText(seekBarValue + " m/s2");

        binding.seekBar.setProgress(seekBarValue);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isOffLine){
                    setDeviceOffline();
                }else{
                    PreferenceData.setMotionControlValue(seekBar.getProgress());
                    if (DBG) Log.i(TAG, "Seekbar: 2" +seekBar.getProgress());
                    binding.textSensorTimeDescriptionInfo.setText( seekBar.getProgress()  + " m/s2");


                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(VibrationCtrl.this,this);
        getSetting(true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnStatus1:


                if (timeAlertSetting.getIsVibration().equalsIgnoreCase("true")) {
                    updateSettings("false",1);//////////


                    setConnectionUpdateValue(false);

                }
                else{
                    updateSettings("true",1);///////////


                    setConnectionUpdateValue(true);

                }
                if(DBG) Log.d("Handler", "out");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(DBG) Log.d("Handler", "in");
                        //Do something after 100ms
                    }
                }, 30000);



                break;
        }
    }

    private void updateSettings(String checked,int isFrom) {
        if (!Utility.isNetworkAvailable(VibrationCtrl.this)) {
            Utility.showSnackBar(binding.appBarLayout, getString(R.string.no_internet_connection));
        } else {
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            if(isFrom==1) {
            timeAlertSetting.setIsVibration(checked);
             Utility.showProgress(VibrationCtrl.this);

//            Call<ResponseBody> responseCall = null;

             responseCall = service.updateDeviceNameSettings(isFamily, "vibnoti", OnlookApplication.SELECTED_DEVICE.getId(), "w", checked);
         }

            if(isFrom==2) {
                responseCall = service.setSwitch(checked, WebUtility.SET_VIB_ONOFF, "x", "x", "x", "x", OnlookApplication.SELECTED_DEVICE.getDevid());
            }
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        if(isFrom==1){

                        if (checked.equalsIgnoreCase("true")) {
                            binding.btnStatus1.setText(resources.getString(R.string.motion_deactivate));/////////////
                            binding.seekBar.setEnabled(false);
                            binding.textTimeStatus1.setText(resources.getString(R.string.timer_running));/////////////


                            binding.textTimeStatus1.setTextColor(Color.parseColor("#FF358500"));
                            Utility.hideProgress();
                        }

                        if (checked.equalsIgnoreCase("false")) {
                            binding.btnStatus1.setText(resources.getString(R.string.motion_activate));/////////////
                            binding.seekBar.setEnabled(true);
                            binding.textTimeStatus1.setText(resources.getString(R.string.timer_stopped));/////////////
                            binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));

                            Utility.hideProgress();

                        }

                        Utility.showSnackBar(binding.textStatus, resources.getString(R.string.status_updated_successfully));
                       // Utility.showToast(resources.getString(R.string.status_updated_successfully));
                        //      Intent intent = new Intent(VibrationCtrl.this, SettingsActivity.class);
                        //     startActivity(intent);
                        // getSetting(false);
                    } else {
                        Utility.showSnackBar(binding.appBarLayout, response.message());
                    }
                }
                    if(isFrom==2){
//                        Utility.showToast(resources.getString(R.string.status_updated_successfully));
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    if(DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast(resources.getString(R.string.status_website));
                    }
                }
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void setAllSettings(TimeAlertsSettingResponse settingResponse) {
        if (settingResponse != null) {
            if (settingResponse.getSettings() != null) {
                timeAlertSetting = settingResponse.getSettings();
                if (timeAlertSetting.getIsVibration() != null) {
              //      binding.checkVibration.setChecked(timeAlertSetting.getIsVibration().equalsIgnoreCase("true"));
             Log.d("SEttings info ",timeAlertSetting.getIsVibration());
             if(timeAlertSetting.getIsVibration().equalsIgnoreCase("true")){
                  binding.seekBar.setEnabled(false);
                 binding.btnStatus1.setText(resources.getString(R.string.motion_deactivate));/////////////
                 binding.textTimeStatus1.setText(resources.getString(R.string.timer_running));/////////////

                 binding.textTimeStatus1.setTextColor(Color.parseColor("#FF358500"));

             }
                    if(timeAlertSetting.getIsVibration().equalsIgnoreCase("false")){
                         binding.btnStatus1.setText(resources.getString(R.string.motion_activate));/////////////
                         binding.seekBar.setEnabled(true);
                        binding.textTimeStatus1.setText(resources.getString(R.string.timer_stopped));/////////////

                        binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));
                    }

                }





            }
        }
    }


    private void getSetting(boolean isShowing) {
        if (!Utility.isNetworkAvailable(VibrationCtrl.this)) {
            checkConnection(this, this);
        } else {
            if (isShowing) {
                Utility.showProgress(VibrationCtrl.this);

            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<TimeAlertsSettingResponse> responseCall = service.getDeviceSettingData(isFamilyMember(), OnlookApplication.SELECTED_DEVICE.getDevid());
            responseCall.enqueue(new Callback<TimeAlertsSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<TimeAlertsSettingResponse> call, @NotNull Response<TimeAlertsSettingResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAlertsSettingResponse settingResponse = response.body();
                        if (isFamilyMember().contains("yes")) {
                           // setFamilyAllSettings(settingResponse);
                            setAllSettings(settingResponse);
                        } else {
                            setAllSettings(settingResponse);
                        }
                    } else {
                        Utility.showSnackBar(binding.appBarLayout, response.message());
                    }
                    if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
                        isOffLine = false;
                    } else {
                        setDeviceOffline();
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<TimeAlertsSettingResponse> call, @NotNull Throwable t) {
                    if (DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                }
            });

        } //true
        }
    }


    private String isFamilyMember() {
        // Check if isFamily Member access or not.
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.deviceList.get(0).getEmail())) {
            return "no";
        }
            if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getIsshared())) {
                return "no";
            }
         else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            return "yes";
        } else {
            return "yes";
        }
    }



    @SuppressLint("SetTextI18n")
    private void setFamilyAllSettings(TimeAlertsSettingResponse settingResponse) {
        if (settingResponse != null) {
            if (settingResponse.getSettings() != null) {
                timeAlertSetting = settingResponse.getSettings();


                if (timeAlertSetting.getIsVibration() != null) {
                //    binding.checkVibration.setChecked(timeAlertSetting.getIsVibration().equalsIgnoreCase("true"));
                }
            }
        }

    }


    private void setConnectionUpdateValue(boolean active) {
      //  updateSettings(isFamily, binding.seekBar.getProgress() + "", "mysen", false);
        if (DBG) Log.i(TAG, "Seekbar: 3" +binding.seekBar.getProgress());
        /*if (binding.seekBar.getProgress() > 0 && binding.seekBar.getProgress() < 500) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Latitude", "1");
        }
        if (binding.seekBar.getProgress() > 500 && binding.seekBar.getProgress() < 1000) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Latitude", "2");
        }
        if (binding.seekBar.getProgress() > 1000 && binding.seekBar.getProgress() < 1500) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Latitude", "3");
        }
        if (binding.seekBar.getProgress() > 1500 && binding.seekBar.getProgress() < 2000) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Latitude", "4");
        }
        if (binding.seekBar.getProgress() > 2000 && binding.seekBar.getProgress() < 4000) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Latitude", "2500");
        }
        */
        if(active==true) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/STATUS",  "5");

            if (DBG) Log.i(TAG, "Seekbar: Sending 4");
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/longitude", binding.seekBar.getProgress() + "");
        }
        if(active==false){
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/STATUS",  "6");

        }
        Utility.showSnackBar(binding.seekBar, getString(R.string.message_update_motion_value));
    }

    @Override
    public void onBackPressed() {
        try {
            if (mqttAndroidClient.isConnected()) {
                mqttAndroidClient.close();
                mqttAndroidClient.disconnect();
            }
        } catch (NullPointerException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        finish();
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        updateSettings("false",2);//////////
        //binding.textStatusInfo.setText("Onlook Status : OFFLINE ");ffd
        binding.textStatusInfo.setText(resources.getString(R.string.motion_sensivity_offline));

        binding.textStatusInfo.setText(resources.getString(R.string.motion_sensivity_offline));
        binding.btnStatus1.setText(resources.getString(R.string.motion_sensivity_offline));
        //  Utility.showSnackBar(binding.seekBar, getString(R.string.message_device_offline));
        Utility.showSnackBar(binding.seekBar,resources.getString(R.string.message_device_offline));

        binding.btnStatus1.setEnabled(false);

        binding.textTimeStatus1.setText(resources.getString(R.string.device_offline));/////////////

        binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));



    }

    private void setupMqttConnection() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(WebUtility.MQTT_USER_ID);
        mqttConnectOptions.setPassword(WebUtility.MQTT_PASSWORD.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);


        mqttAndroidClient =new MqttAndroidClient(getApplicationContext(), WebUtility.MQTT_SERVER_URL,
                WebUtility.LED_CLIENT_ID + System.currentTimeMillis(), Ack.AUTO_ACK);



        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    subscriptionTopic = "tele/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/LWT";
                    subscribeToTopic(subscriptionTopic);
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/AP", "");
                    } catch (IllegalArgumentException illegalArgumentException) {
                        illegalArgumentException.printStackTrace();
                    }
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String inboundvalue = null;
                inboundvalue = new String(message.getPayload());

                if (inboundvalue.equalsIgnoreCase("")) {
                    isOffLine = true;
                    if(DBG) Log.i(TAG, "messageArrived: DEVICE OFFLINE");
                    setDeviceOffline();
                }

                if (inboundvalue.equalsIgnoreCase("Online")) {
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/RESULT";
                    binding.textStatusInfo.setText(resources.getString(R.string.motion_sensivity_online));
                    subscribeToTopic(subscriptionTopic);
                    //updateSettings("true",2);//////////
                }

                if (inboundvalue.equalsIgnoreCase("Offline")) {
                    if(DBG) Log.i(TAG, "messageArrived: DEVICE OFFLINE");
                    isOffLine = true;
                    setDeviceOffline();
                } else {
                    isOffLine = false;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                if(DBG) Log.i(TAG, "deliveryComplete: " + token);
            }
        });

        mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(10);
                    disconnectedBufferOptions.setPersistBuffer(true);
                    disconnectedBufferOptions.setDeleteOldestMessages(true);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscriptionTopic = "tele/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/LWT";
                    subscribeToTopic(subscriptionTopic);
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/AP", "");
                } catch (IllegalArgumentException ixp) {
                    ixp.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                exception.printStackTrace();
            }
        });
    }

    public void subscribeToTopic(String to) {
        final String topic = to;
        try {
            if (mqttAndroidClient != null) {
                if (mqttAndroidClient.isConnected()) {
                    mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            if (!topic.isEmpty()) {
                                try {
                                    unSubscribe(mqttAndroidClient, topic);
                                    subscribe(mqttAndroidClient, topic, 1);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            exception.printStackTrace();
                        }
                    });
                }
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    private void publishMessage(String publishTopic, String publishMessage) {
        MqttMessage message = new MqttMessage();
        message.setPayload(publishMessage.getBytes());
        mqttAndroidClient.publish(publishTopic, message);
    }

    public void subscribe(@NonNull MqttAndroidClient client, @NonNull final String topic, int qos) {
        IMqttToken token = null;
        try {
            token = client.subscribe(topic, qos);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    try {
                        mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    } catch (IllegalArgumentException exp) {
                        exp.printStackTrace();
                    }
                    if(DBG) Log.d(TAG, "Subscribe Successfully " + topic);
                }

                @NonNull
                private DisconnectedBufferOptions getDisconnectedBufferOptions() {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(true);
                    return disconnectedBufferOptions;
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    if(DBG) Log.e(TAG, "Subscribe Failed " + topic);
                }
            });
        } catch (NullPointerException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }

    public void unSubscribe(@NonNull MqttAndroidClient client, @NonNull final String topic) {
        try {
            IMqttToken token = client.unsubscribe(topic);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    if(DBG) Log.d(TAG, "UnSubscribe Successfully " + topic);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    if(DBG) Log.e(TAG, "UnSubscribe Failed " + topic);
                }
            });
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        checkConnection(this,this);
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {
        openWifiSettings();
    }

    private void openWifiSettings() {
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void checkConnection(Context context, DialogButtonClickListener listener) {
        if (!Utility.isNetworkAvailable(context)) {
            Utility.showDialog(context, context.getResources().getString(R.string.no_internet_title),
                    context.getResources().getString(R.string.no_internet),
                    context.getResources().getString(R.string.button_try_again),
                    context.getResources().getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);
        }
    }
}
