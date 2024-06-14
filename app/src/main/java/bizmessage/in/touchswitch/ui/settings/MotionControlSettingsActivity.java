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

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityMotionControlSettingsBinding;

import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class MotionControlSettingsActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = MotionControlSettingsActivity.class.getSimpleName();
    private ActivityMotionControlSettingsBinding binding;
    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "", isFamily = "no";
    private final boolean isSwitchOn = false;
    private boolean isOffLine = false;
    Integer seekBarValue ;
    final Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Resources resources;
    int qos=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_motion_control_settings);
        setSupportActionBar(binding.toolbar);
      // getSupportActionBar().setTitle(R.string.title_motion_sensitivity_control_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnConnect.setOnClickListener(this);


        if(OnlookApplication.SELECTED_DEVICE.getLock().equalsIgnoreCase("true")){
            binding.btnConnect.setEnabled(false);
            binding.seekBar.setEnabled(false);
            binding.btnConnect.setBackgroundColor(Color.GRAY);
            Utility.showToast("Settings are Locked by Admin for saftey");
        }
        else{
           // Utility.showToast(resources.getString(R.string.please_wait));

            //Utility.showToast("Settings are Unlocked");
            binding.btnConnect.setEnabled(true);

            binding.seekBar.setEnabled(true);
        }
        if(OnlookApplication.SELECTED_DEVICE.getLock().equalsIgnoreCase("true")) {

        }
        else{
            setupMqttConnection();
            setSeekBarValue();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            //    Utility.showToast("Closing Connection for saftey");

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
        }, 20000);

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

        binding.textmotionsensi.setText(resources.getString(R.string.motion_sensivity_head));///////////////
        binding.textStatus.setText(resources.getString(R.string.motion_sensivity_status));///////////////
        binding.textChangebar.setText(resources.getString(R.string.motion_sensivity_changebar));///////////////

        binding.textSensorTimeDescription.setText(resources.getString(R.string.motion_sensivity_desc));///////////////
        binding.btnConnect.setText(resources.getString(R.string.motion_sensivity_button));///////////////


        getSupportActionBar().setTitle(resources.getString(R.string.motion_sensivity));

    }

    private void setSeekBarValue() {

        seekBarValue = PreferenceData.getMotionControlValue();


       /* if (seekBarValue > 0 && seekBarValue < 500) {
            binding.textSensorTimeDescriptionInfo.setText((seekBarValue + 2000) / 1000 + " Sec");
          //  binding.textSensorStatus.setText("Meaning: Very High Sensitivity, Slightest human motions are alerted");
        }
        if (seekBarValue > 500 && seekBarValue < 1000) {
            binding.textSensorTimeDescriptionInfo.setText( (seekBarValue + 2000) / 1000 + " Sec");
          //  binding.textSensorStatus.setText("Meaning: High Sensitivity, Good to have bizmessage.in normal conditions");
        }
        if (seekBarValue > 1000 && seekBarValue < 1500) {
            binding.textSensorTimeDescriptionInfo.setText( (seekBarValue + 2000) / 1000 + " Sec");
          //  binding.textSensorStatus.setText("Meaning:  Medium Sensitivity, Good to have if there are to many alerts");
        }
        if (seekBarValue > 1500 && seekBarValue < 2000) {
            binding.textSensorTimeDescriptionInfo.setText((seekBarValue + 2000) / 1000 + " Sec");
          //  binding.textSensorStatus.setText("Meaning:   Good to have if there is CCTV interference");
        }
        if (seekBarValue > 2000 && seekBarValue < 3000) {
            binding.textSensorTimeDescriptionInfo.setText((seekBarValue + 2000) / 1000 + " Sec");
          //  binding.textSensorStatus.setText("Sensitivity: Low Good to have if you need delayed alerts");
        }

          binding.textSensorTimeDescriptionInfo.setText(seekBarValue   + " Sec"+resources.getString(R.string.please_wait));



*/
        seekBarValue = PreferenceData.getMotionControlValue();
        binding.textSensorTimeDescriptionInfo.setText(seekBarValue+"");
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

/*                    if (seekBar.getProgress() > 0 && seekBar.getProgress() < 500) {
                        binding.textSensorTimeDescriptionInfo.setText( (seekBar.getProgress() + 2000) / 1000 + " Sec");
                    //    binding.textSensorStatus.setText("Meaning: Very High, Slightest human motions are alerted");
                    }
                    if (seekBar.getProgress() > 500 && seekBar.getProgress() < 1000) {
                        binding.textSensorTimeDescriptionInfo.setText( (seekBar.getProgress() + 2000) / 1000 + " Sec");
                      //  binding.textSensorStatus.setText("Meaning: High, Good to have bizmessage.in normal conditions");
                    }
                    if (seekBar.getProgress() > 1000 && seekBar.getProgress() < 1500) {
                        binding.textSensorTimeDescriptionInfo.setText((seekBar.getProgress() + 2000) / 1000 + " Sec");
                        //binding.textSensorStatus.setText("Meaning:  Medium, Good to have if there are to many alerts");
                    }
                    if (seekBar.getProgress() > 1500 && seekBar.getProgress() < 2000) {
                        binding.textSensorTimeDescriptionInfo.setText( (seekBar.getProgress() + 2000) / 1000 + " Sec");
                       // binding.textSensorStatus.setText("Meaning:  Good to have if there is CCTV interference");
                    }
                    if (seekBar.getProgress() > 2000 && seekBar.getProgress() < 3000) {
                        binding.textSensorTimeDescriptionInfo.setText( (seekBar.getProgress() + 2000) / 1000 + " Sec");
                       // binding.textSensorStatus.setText("Sensitivity: Low Good to have if you need delayed alerts");
                    }
                    */

                    if (seekBar.getProgress() < 4) {


                        binding.textSensorTimeDescriptionInfo.setText(seekBar.getProgress() + " Sec ("+resources.getString(R.string.high)+")");
                    }

                    if (seekBar.getProgress() > 4 && seekBar.getProgress() < 10) {


                        binding.textSensorTimeDescriptionInfo.setText(seekBar.getProgress() +  " Sec ("+resources.getString(R.string.medium)+")");
                    }


                    if (seekBar.getProgress() > 11 && seekBar.getProgress() < 26) {


                        binding.textSensorTimeDescriptionInfo.setText(seekBar.getProgress() +  " Sec ("+resources.getString(R.string.low)+")");
                    }





                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(MotionControlSettingsActivity.this,this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnConnect:
                if (isOffLine) {
                    setDeviceOffline();
                } else {
                    setConnectionUpdateValue();
                }
                break;
        }
    }

    private void setConnectionUpdateValue() {
        updateSettings(isFamily, binding.seekBar.getProgress() + "", "mysen", false);

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
        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Latitude", binding.seekBar.getProgress()+"");

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

    private void updateSettings(String isFamily, String sensorValue, String status, boolean isChecked) {
        if (!Utility.isNetworkAvailable(MotionControlSettingsActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(MotionControlSettingsActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.updateMotionSettings(isFamily, status, sensorValue, PreferenceData.getEmail(), isChecked);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                    } else {
                        Utility.showSnackBar(binding.appBarLayout,response.message());
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        //binding.textStatusInfo.setText("Onlook Status : OFFLINE ");ffd
        binding.textStatusInfo.setText(resources.getString(R.string.motion_sensivity_offline));

        binding.textStatusInfo.setText(resources.getString(R.string.motion_sensivity_offline));
      //  Utility.showSnackBar(binding.seekBar, getString(R.string.message_device_offline));
        Utility.showSnackBar(binding.seekBar,resources.getString(R.string.message_device_offline));
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
