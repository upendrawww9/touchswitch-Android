package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

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
import bizmessage.in.touchswitch.databinding.ActiyitySirenBinding;
import bizmessage.in.touchswitch.model.SwitchSettingResponse;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;


public class SirenActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = SirenActivity.class.getSimpleName();
    private ActiyitySirenBinding binding;
    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "";
    private final int timeSelectFor = 1; // 1 = LIGHT ON 1 , 10 = LIGHT OFF 1, 2 = LIGHT ON 2, 20 = LIGHT OFF 2
    private String onHourTime1, onMinuteTime1, offHourTime1, offMinuteTime1, onHourTime2, onMinuteTime2, offHourTime2, offMinuteTime2;
    private boolean isSwitchOn = false, isOffLine = true;
    String activeTime1 = "0";
    String activeTime2 = "0";
    final Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Resources resources;
    private float lastX, lastY, lastZ;
int init=0;
    int qos=2;
    int state=0;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private final float deltaXMax = 0;
    private final float deltaYMax = 0;
    private final float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.actiyity_siren);
        setSupportActionBar(binding.toolbar);
       //getSupportActionBar().setTitle(R.string.title_siren);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.imgSwitch.setVisibility(View.GONE);
        binding.imgSwitchBulb.setVisibility(View.GONE);

     //   Utility.showProgress(SirenActivity.this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }




        setupMqttConnection();
        binding.imgSwitch.setOnClickListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              //  Utility.showToast("Closing Connection for saftey");

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
        }, 30000);


        binding.imgSwitchBulb.setOnClickListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  Utility.showToast("Closing Connection for saftey");

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
        }, 30000);



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

        if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
            isOffLine = false;
        } else {
            setDeviceOffline();
        }

        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        getSupportActionBar().setTitle(resources.getString(R.string.buzzer));

        binding.textControl.setText(resources.getString(R.string.buzzer_control));///////////////

        binding.textStatus.setText(resources.getString(R.string.buzzer_status));///////////////
        //binding.textStatusValue.setText(resources.getString(R.string.buzzer_on));///////////////





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
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER2";
                    subscribeToTopic(subscriptionTopic);
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER2", "");
                    } catch (IllegalArgumentException illegalArgumentException) {
                        illegalArgumentException.printStackTrace();
                    }
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Utility.hideProgress();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String inboundtopic = null;
                String inboundvalue = null;
                inboundtopic = topic.concat("/");
                inboundvalue = new String(message.getPayload());
                binding.imgSwitch.setVisibility(View.VISIBLE);
                binding.imgSwitchBulb.setVisibility(View.VISIBLE);

                if (inboundvalue.equalsIgnoreCase("ON")) {
                    if(DBG) Log.i(TAG, "messageArrived: SWITCH ON");
                    binding.textStatus.setVisibility(View.VISIBLE);
                    isSwitchOn = false;
                    setLightOn();
                }

                if (inboundvalue.equalsIgnoreCase("OFF")) {
                    if(DBG) Log.i(TAG, "messageArrived: SWITCH OFF");
                    binding.textStatus.setVisibility(View.VISIBLE);
                    isSwitchOn = true;
                    setLightOff();
                }

                if (inboundvalue.equalsIgnoreCase("")) {
                    if(DBG) Log.i(TAG, "messageArrived: DEVICE OFFLINE");
                    setDeviceOffline();
                }
              Utility.hideProgress();
                if(DBG) Log.d(TAG, "Message  Arived: " + inboundtopic + "value " + inboundvalue + "CONTANNT" + inboundtopic.contains("LWT"));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                if(DBG) Log.i(TAG, "deliveryComplete: " + token);
                Utility.hideProgress();
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
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER2";
                    subscribeToTopic(subscriptionTopic);
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER2", "");
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


    private void updateSwitchStatus(boolean isSwitchOn) {
     //   Utility.showProgress(SirenActivity.this);
        if (isSwitchOn) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER2", "ON");
        } else {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER2", "OFF");
        }
    }

    private boolean isValidOnTime() {
        return onHourTime1 != null && onMinuteTime1 != null
                && !onHourTime1.isEmpty()
                && !onMinuteTime1.isEmpty()
                && !onHourTime1.equalsIgnoreCase("00")
                && !onMinuteTime1.equalsIgnoreCase("00");
    }

    private boolean isValidOffTime() {
        return offHourTime1 != null
                && offMinuteTime1 != null
                && !offHourTime1.isEmpty()
                && !offHourTime1.isEmpty()
                && !offHourTime1.equalsIgnoreCase("00")
                && !offHourTime1.equalsIgnoreCase("00");
    }

    private boolean isValidOnTime2() {
        return onHourTime2 != null
                && onMinuteTime2 != null
                && !onHourTime2.isEmpty()
                && !onMinuteTime2.isEmpty()
                && !onHourTime2.equalsIgnoreCase("00")
                && !onMinuteTime2.equalsIgnoreCase("00");
    }

    private boolean isValidOffTime2() {
        return offHourTime2 != null
                && offMinuteTime2 != null
                && !offHourTime2.isEmpty()
                && !offHourTime2.isEmpty()
                && !offHourTime2.equalsIgnoreCase("00")
                && !offHourTime2.equalsIgnoreCase("00");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLightOff() {
        setTimer("off",1);
        binding.imgSwitch.setImageDrawable(getDrawable(R.drawable.ic_switch_off));
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.buzzerblack));
       // binding.textStatusValue.setText(R.string.light_off);
        binding.textStatusValue.setText(resources.getString(R.string.buzzer_off));
        binding.textStatusValue.setTextColor(getResources().getColor(R.color.red));
        if (isOffLine) {
  //          Utility.showSnackBar(binding.imgSwitch, getString(R.string.message_device_offline));
            Utility.showSnackBar(binding.imgSwitch,resources.getString(R.string.message_device_offline));

        }
        Utility.hideProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        binding.imgSwitch.setVisibility(View.GONE);
        binding.imgSwitchBulb.setVisibility(View.GONE);

        binding.textStatus.setVisibility(View.GONE);
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.buzzerblack));
        //binding.textStatusValue.setText(R.string.device_offline);
        binding.textStatusValue.setText(resources.getString(R.string.device_offline));

        binding.textStatusValue.setTextSize(20);
        binding.textStatusValue.setTextColor(getResources().getColor(R.color.red));
        //Utility.showSnackBar(binding.imgSwitch, getString(R.string.message_device_offline));
        Utility.showSnackBar(binding.imgSwitch,resources.getString(R.string.message_device_offline));
        Utility.hideProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLightOn() {
        setTimer("on",1);
        binding.imgSwitch.setImageDrawable(getDrawable(R.drawable.ic_switch_on));
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.buzzer));
        //binding.textStatusValue.setText(R.string.light_on);
        binding.textStatusValue.setText(resources.getString(R.string.buzzer_on));
        binding.textStatusValue.setTextColor(getResources().getColor(R.color.green));
        if (isOffLine) {
           // Utility.showSnackBar(binding.imgSwitch, getString(R.string.message_device_offline));
            Utility.showSnackBar(binding.imgSwitch,resources.getString(R.string.message_device_offline));

        }
        Utility.hideProgress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                Utility.hideProgress();
                onBackPressed();
                break;
            case R.id.btnStatus1:
                if (!isOffLine) {
                    if (activeTime1.equalsIgnoreCase("1")) {
                        setTimer("0", 1);
                    } else {
                        if (!isValidOnTime()) {
                            Utility.showSnackBar(binding.imgSwitchBulb, getString(R.string.valid_on_time));
                        } else if (!isValidOffTime()) {
                            Utility.showSnackBar(binding.imgSwitchBulb, getString(R.string.valid_off_time));
                        } else {
                            setTimer("1", 1);
                        }
                    }
                } else {
                    setDeviceOffline();
                }
                break;
            case R.id.btnStatus2:
                if (!isOffLine) {
                    if (activeTime2.equalsIgnoreCase("1")) {
                      //  setTimer("0", 2);
                    } else {
                        if (!isValidOnTime2()) {
                            Utility.showSnackBar(binding.imgSwitchBulb, getString(R.string.valid_on_time));
                        } else if (!isValidOffTime2()) {
                            Utility.showSnackBar(binding.imgSwitchBulb, getString(R.string.valid_off_time));
                        } else {
                           // setTimer("1", 2);
                        }
                    }
                } else {
                    setDeviceOffline();
                }
                break;
            case R.id.imgSwitch:
                if (!isOffLine) {
                    updateSwitchStatus(isSwitchOn);
                } else {
                    setDeviceOffline();
                }
                binding.imgSwitch.setEnabled(false);
                binding.imgSwitchBulb.setEnabled(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.imgSwitch.setEnabled(true);
                        binding.imgSwitchBulb.setEnabled(true);
                        //Do something after 100ms
                    }
                }, 1000);
                break;

            case R.id.imgSwitchBulb:
                if (!isOffLine) {
                    updateSwitchStatus(isSwitchOn);
                } else {
                    setDeviceOffline();
                }
                binding.imgSwitch.setEnabled(false);
                binding.imgSwitchBulb.setEnabled(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.imgSwitch.setEnabled(true);
                        binding.imgSwitchBulb.setEnabled(true);


                        //Do something after 100ms
                    }
                }, 1000);
                break;



        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mqttAndroidClient.isConnected()) {
                mqttAndroidClient.close();
                mqttAndroidClient.disconnect();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        finish();
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();


        checkConnection(SirenActivity.this,this);
      //  getSetting(true);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //flip down to on light https://www.vogella.com/tutorials/AndroidSensor/article.html
    @Override
    public void onSensorChanged(SensorEvent event) {

        // clean current values
        // displayCleanValues();
        // display the current x,y,z accelerometer values
        // displayCurrentValues();
        // display the max x,y,z accelerometer values
        //displayMaxValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if (deltaZ < 2)
            deltaZ = 0;

        // set the last know values of x,y,z
        lastX = event.values[0];
        lastY = event.values[1];
        lastZ = event.values[2];
//binding.textDeviceId.setText("x"+lastX+"||| y :"+lastY+" ||| z:"+lastZ   );
        if(DBG) Log.i("FLIP VALUE", ""+lastZ) ;

        if(lastZ > -7 && lastZ < -6 && state==0 ){
            if(DBG) Log.i(TAG, "FLIP: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+lastZ) ;
            state=1;

            if (!isOffLine) {
                updateSwitchStatus(isSwitchOn);
            } else {
                setDeviceOffline();
            }
            binding.imgSwitch.setEnabled(false);
            binding.imgSwitchBulb.setEnabled(false);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.imgSwitch.setEnabled(true);
                    binding.imgSwitchBulb.setEnabled(true);


                    //Do something after 100ms
                }

            }, 2000);


        }
        if(lastZ > 7 ) {
            state=0;
        }


    }


    private void setTimer(String activate, int isFrom) {
        if (!Utility.isNetworkAvailable(SirenActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout, getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(SirenActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = null;
            if (isFrom == 1) {
                responseCall = service.setSwitch(activate, WebUtility.SET_BUZZ_ONOFF, "x", "x", "x", "x", OnlookApplication.SELECTED_DEVICE.getDevid());
            }
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Utility.showSnackBar(binding.imgSwitchBulb, getString(R.string.status_updated_successfully));
                      //  getSetting(false);
                    } else {
                        Utility.showSnackBar(binding.appBarLayout, response.message());
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



    private void getSetting(boolean isShowProgress) {
        if (!Utility.isNetworkAvailable(SirenActivity.this)) {
            checkConnection(SirenActivity.this,this);
        } else {
            if (isShowProgress) {
                Utility.showProgress(SirenActivity.this);
            }
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<SwitchSettingResponse> responseCall = service.getSwitchSetting("no", OnlookApplication.SELECTED_DEVICE.getDevid());
            responseCall.enqueue(new Callback<SwitchSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<SwitchSettingResponse> call, @NotNull Response<SwitchSettingResponse> response) {
                    if (response.isSuccessful()) {
                        SwitchSettingResponse settingResponse = response.body();
                    } else {
                        Utility.showSnackBar(binding.appBarLayout, response.message());
                    }
                   // Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<SwitchSettingResponse> call, @NotNull Throwable t) {
                    if(DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                }
            });
        }
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
          //  Utility.showDialog(context, context.getResources().getString(R.string.no_internet_title),
            //        context.getResources().getString(R.string.no_internet),
              //      context.getResources().getString(R.string.button_try_again),
                //    context.getResources().getString(R.string.button_setting),
                  //  listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);
            Utility.showDialog(context, resources.getString(R.string.no_internet_title),
                    resources.getString(R.string.no_internet),
                    resources.getString(R.string.button_try_again),
                    resources.getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);


        }
    }

}
