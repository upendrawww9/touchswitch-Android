package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;

import bizmessage.in.touchswitch.databinding.ActivityLightOneBinding;

import bizmessage.in.touchswitch.model.SwitchSettingResponse;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

//import org.eclipse.paho.android.service.MqttAndroidClient;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.retrofit.WebUtility.DEFAULT_TIME;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class LightOneActivity extends AppCompatActivity implements SensorEventListener ,View.OnClickListener, TimePickerDialog.OnTimeSetListener, DialogButtonClickListener {

    private static final String TAG = LightOneActivity.class.getSimpleName();
    private ActivityLightOneBinding binding;
    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "";
    private int timeSelectFor = 1; // 1 = LIGHT ON 1 , 10 = LIGHT OFF 1, 2 = LIGHT ON 2, 20 = LIGHT OFF 2
    private String onHourTime1, onMinuteTime1, offHourTime1, offMinuteTime1, onHourTime2, onMinuteTime2, offHourTime2, offMinuteTime2;
    private boolean isSwitchOn = false, isOffLine = true;
    String activeTime1 = "0";
    String activeTime2 = "0";
    final Handler handler = new Handler(Looper.getMainLooper());
    int state=0;
    int qos=2;

    int flag;
    String sartttime,endtime,pattern,sartttime2,endtime2;
    SimpleDateFormat sdf;
    Date date2,date1;

    Context context;

    Resources resources;


    private float lastX, lastY, lastZ;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_light_one);
        setSupportActionBar(binding.toolbar);
       // getSupportActionBar().setTitle(R.string.title_light_one);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.imgSwitch.setVisibility(View.GONE);
        binding.imgSwitchBulb.setVisibility(View.GONE);



        if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "hi");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ka");
            resources = context.getResources();
            binding.textSwitchTimer1.setTextSize(17);
            binding.textSwitchTimer2.setTextSize(17);
            binding.textStatusValue.setTextSize(19);
            binding.btnStatus1.setWidth(250);
            binding.btnStatus2.setWidth(250);
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ta");
            resources = context.getResources();
            binding.textSwitchTimer1.setTextSize(15);
            binding.textSwitchTimer2.setTextSize(15);
            binding.textStatusValue.setTextSize(12);
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
            binding.textSwitchTimer1.setTextSize(15);
            binding.textSwitchTimer2.setTextSize(15);
            binding.textStatusValue.setTextSize(12);

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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

			// success! we have an accelerometer

			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
 		} else {
			// fai! we dont have an accelerometer!
		}



        if (OnlookApplication.SELECTED_DEVICE.getRIsSwitch().equalsIgnoreCase("false")) {
            Utility.showToast(resources.getString(R.string.not_ebable));


            finish();

        }else {

        }

        if(DBG) Log.i(TAG, "ISOFFLINE: " + OnlookApplication.SELECTED_DEVICE.getLwt());
        if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
            isOffLine = false;
        } else {

            Utility.hideProgress();
            setDeviceOffline();

        }

        setAccessControl();
        setupMqttConnection();

        binding.textTimeOnValue1.setOnClickListener(this);
        binding.textTimeOffValue1.setOnClickListener(this);
        binding.textTimeOnValue2.setOnClickListener(this);
        binding.textTimeOffValue2.setOnClickListener(this);
        binding.imgSwitch.setOnClickListener(this);
        binding.imgSwitchBulb.setOnClickListener(this);///////////

        binding.btnStatus1.setOnClickListener(this);
        binding.btnStatus2.setOnClickListener(this);

        binding.textControl.setText(resources.getString(R.string.light_control));///////////////
        binding.textTimeOn1.setText(resources.getString(R.string.light_ontime));///////////////
        binding.textTimeOff1.setText(resources.getString(R.string.light_offtime));///////////////
        binding.textTimeOn2.setText(resources.getString(R.string.light_ontime));///////////////
        binding.textTimeOff2.setText(resources.getString(R.string.light_offtime));///////////////
        binding.textStatus.setText(resources.getString(R.string.light_status));///////////////
        Utility.showToast(resources.getString(R.string.please_wait));


        getSupportActionBar().setTitle(resources.getString(R.string.title_light_one)); // title setting here



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
        }, 60000);

    }

    private void setAccessControl() {
        // Check if isFamily Member access or not.
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
            /*binding.ViewForegroundDark.setVisibility(View.VISIBLE);
            binding.textTimeOnValue1.setEnabled(false);
            binding.textTimeOnValue2.setEnabled(false);
            binding.textTimeOffValue1.setEnabled(false);
            binding.textTimeOffValue2.setEnabled(false);
            binding.btnStatus1.setEnabled(false);
            binding.btnStatus2.setEnabled(false);*/
            binding.cardSwitchTimer1.setVisibility(View.GONE);
            binding.ViewForegroundDark.setVisibility(View.GONE);
            binding.cardSwitchTimer2.setVisibility(View.GONE);
        } else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)){
            binding.cardSwitchTimer1.setVisibility(View.GONE);
            binding.ViewForegroundDark.setVisibility(View.GONE);
            binding.cardSwitchTimer2.setVisibility(View.GONE);
        } else {
            binding.ViewForegroundDark.setVisibility(View.GONE);
        }
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
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER3";
                    subscribeToTopic(subscriptionTopic);
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER3", "");
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
                binding.imgSwitch.setVisibility(View.VISIBLE);
                binding.imgSwitchBulb.setVisibility(View.VISIBLE);
                String invalue;

                String inboundtopic = null;
                String inboundvalue = null;
                inboundtopic = topic.concat("/");
                inboundvalue = new String(message.getPayload());
                if (DBG) Log.i(TAG, "FUNCTION : COMING HERE");

                //Some times when deice is online and databse is not updated so it will cause light to
                // stop allowing user to on the light in that case we will check if message arrived and deivce is online
                // then we update device status as online and then user can use the light.
 if(inboundvalue.contains("ON")||inboundvalue.contains("OFF")) {
     if (!OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("Online")) {
         if (DBG) Log.i(TAG, "ISOFFLINE: COMING HERE");

         if (DBG) Log.i(TAG, "ISOFFLINE: COMING HERE " + isOffLine);

         setTimer("Online", 4);
         Utility.hideProgress();
         Utility.showToast(resources.getString(R.string.button_try_again));
         Intent intent = new Intent(LightOneActivity.this, MainActivity.class);
         startActivity(intent);
         //  OnlookApplication.SELECTED_DEVICE.setLwt("Online");
     }
 }

                if (inboundvalue.equalsIgnoreCase("ON")) {

  //                  setTimer("Online",4);
                    //Utility.hideProgress();

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

                if (inboundvalue.isEmpty()) {
                    if(DBG) Log.i(TAG, "messageArrived: NO MESSAGE");
                    setDeviceOffline();
                }
                if (inboundvalue.equalsIgnoreCase("Offline")) {
                    if(DBG) Log.i(TAG, "messageArrived: DEVICE OFFLINE");
                   Utility.hideProgress();
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
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER3";
                    subscribeToTopic(subscriptionTopic);
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER3", "");
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

    private void setTimeValue(SwitchSettingResponse settingResponse,boolean isFirstTime) {
        if (isFirstTime){
            activeTime1 = settingResponse.getSettings().getActivet1();
            activeTime2 = settingResponse.getSettings().getActivet2();
        }
        if (activeTime1.equalsIgnoreCase("1")) {
            //binding.btnStatus1.setText("DEACTIVATE");
            binding.btnStatus1.setText(resources.getString(R.string.light_deactivate));/////////////
            binding.textTimeStatus1.setText("");
            binding.textTimeOnValue1.setEnabled(false);
            binding.textTimeOffValue1.setEnabled(false);
           // binding.textTimeStatus1.setTextColor(Color.parseColor("#FF358500"));
            binding.textSwitchTimer1.setTextColor(Color.parseColor("#FF358500"));
            //binding.textSwitchTimer1.setText("Remote Timer1- Running");
            binding.textSwitchTimer1.setTextColor(Color.parseColor("#FF358500"));

            binding.textSwitchTimer1.setText(resources.getString(R.string.light_remote_timer1running));///////////////
        } else {
            binding.textTimeOnValue1.setEnabled(true);
            binding.textTimeOffValue1.setEnabled(true);
            binding.textTimeStatus1.setText("");
           // binding.btnStatus1.setText("ACTIVE");
            binding.btnStatus1.setText(resources.getString(R.string.light_activate));/////////////
          //  binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));
            binding.textSwitchTimer1.setTextColor(Color.parseColor("#f1c70623"));
           // binding.textSwitchTimer1.setText("Remote Timer1 - Stopped");
            binding.textSwitchTimer1.setTextColor(Color.parseColor("#f1c70623"));

            binding.textSwitchTimer1.setText(resources.getString(R.string.light_remote_timer1stopped));///////////////
        }

        if (activeTime2.equalsIgnoreCase("1")) {
            binding.textTimeOnValue2.setEnabled(false);
            binding.textTimeOffValue2.setEnabled(false);
          binding.textTimeStatus2.setText("");
            //binding.btnStatus2.setText("DEACTIVATE");
            binding.btnStatus2.setText(resources.getString(R.string.light_deactivate));/////////////

          //  binding.textSwitchTimer2.setText("Remote Timer2 - Running");
            binding.textSwitchTimer2.setTextColor(Color.parseColor("#FF358500"));

            binding.textSwitchTimer2.setText(resources.getString(R.string.light_remote_timer2running));///////////////

            //binding.textTimeStatus2.setTextColor(Color.parseColor("#FF358500"));
            binding.textSwitchTimer2.setTextColor(Color.parseColor("#FF358500"));
        } else {
            binding.textTimeOnValue2.setEnabled(true);
            binding.textTimeOffValue2.setEnabled(true);
            binding.textTimeStatus2.setText("");
     //       binding.btnStatus2.setText("ACTIVE");
            binding.btnStatus2.setText(resources.getString(R.string.light_activate));/////////////

            // binding.textTimeStatus2.setTextColor(Color.parseColor("#F1c70623"));
            binding.textSwitchTimer2.setTextColor(Color.parseColor("#F1c70623"));
//            binding.textSwitchTimer2.setText("Remote Timer2 - Stopped");
            binding.textSwitchTimer2.setTextColor(Color.parseColor("#f1c70623"));

            binding.textSwitchTimer2.setText(resources.getString(R.string.light_remote_timer2stopped));///////////////


        }

        if (settingResponse.getSettings().getOnHourTime1() != null && !settingResponse.getSettings().getOnHourTime1().isEmpty()) {
            String[] onTime1 = settingResponse.getSettings().getOnHourTime1().split(":");
            onHourTime1 = onTime1[0];
            onMinuteTime1 = onTime1[1];
        } else {
            onHourTime1 = DEFAULT_TIME;
            onMinuteTime1 = DEFAULT_TIME;
        }

        if (settingResponse.getSettings().getOffHourTime1() != null && !settingResponse.getSettings().getOffHourTime1().isEmpty()) {
            String[] onTime1 = settingResponse.getSettings().getOffHourTime1().split(":");
            offHourTime1 = onTime1[0];
            offMinuteTime1 = onTime1[1];
        } else {
            offHourTime1 = DEFAULT_TIME;
            offMinuteTime1 = DEFAULT_TIME;
        }

        if (settingResponse.getSettings().getOnHourTime2() != null && !settingResponse.getSettings().getOnHourTime2().isEmpty()) {
            String[] onTime1 = settingResponse.getSettings().getOnHourTime2().split(":");
            onHourTime2 = onTime1[0];
            onMinuteTime2 = onTime1[1];
        } else {
            onHourTime2 = DEFAULT_TIME;
            onMinuteTime2 = DEFAULT_TIME;
        }

        if (settingResponse.getSettings().getOffHoursTime2() != null && !settingResponse.getSettings().getOffHoursTime2().isEmpty()) {
            String[] onTime1 = settingResponse.getSettings().getOffHoursTime2().split(":");
            offHourTime2 = onTime1[0];
            offMinuteTime2 = onTime1[1];
        } else {
            offHourTime2 = DEFAULT_TIME;
            offMinuteTime2 = DEFAULT_TIME;
        }

        binding.textTimeOnValue1.setText(onHourTime1 + " : " + onMinuteTime1);
        binding.textTimeOnValue2.setText(onHourTime2 + " : " + onMinuteTime2);
        binding.textTimeOffValue1.setText(offHourTime1 + " : " + offMinuteTime1);
        binding.textTimeOffValue2.setText(offHourTime2 + " : " + offMinuteTime2);

    }

    private void updateSwitchStatus(boolean isSwitchOn) {
       // Utility.showProgress(LightOneActivity.this);
        if (isSwitchOn) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER3", "ON");
        } else {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER3", "OFF");
        }
    }

    private boolean isValidOnTime() {
        return onHourTime1 != null && onMinuteTime1 != null
                && !onHourTime1.isEmpty()
                && !onMinuteTime1.isEmpty()
                && !onHourTime1.isEmpty()
                && !onMinuteTime1.isEmpty();
    }

    private boolean isValidOffTime() {
        return offHourTime1 != null
                && offMinuteTime1 != null
                && !offHourTime1.isEmpty()
                && !offHourTime1.isEmpty()
                && !offHourTime1.isEmpty()
                && !offHourTime1.isEmpty();

    }

    private boolean isValidOnTime2() {
        return onHourTime2 != null
                && onMinuteTime2 != null
                && !onHourTime2.isEmpty()
                && !onMinuteTime2.isEmpty()
                && !onHourTime2.isEmpty()
                && !onMinuteTime2.isEmpty();
    }

    private boolean isValidOffTime2() {
        return offHourTime2 != null
                && offMinuteTime2 != null
                && !offHourTime2.isEmpty()
                && !offHourTime2.isEmpty()
                && !offHourTime2.isEmpty()
                && !offHourTime2.isEmpty();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLightOff() {
        setTimer("off",3);
        PreferenceData.setisLighton(false);
        binding.imgSwitch.setImageDrawable(getDrawable(R.drawable.ic_switch_off));
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.ic_bulb_off));
       // binding.textStatusValue.setText(R.string.light_off);
        binding.textStatusValue.setText(resources.getString(R.string.light_off));

        binding.textStatusValue.setTextColor(getResources().getColor(R.color.red));
        if (isOffLine) {
   //         Utility.showSnackBar(binding.imgSwitch,getString(R.string.message_device_offline));
            Utility.showSnackBar(binding.imgSwitch,resources.getString(R.string.message_device_offline));

        }
        Utility.hideProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        setTimer("Offline",4);
        PreferenceData.setisLighton(false);
        binding.textStatus.setVisibility(View.GONE);
        binding.cardSwitchTimer1.setVisibility(View.GONE);
        binding.ViewForegroundDark.setVisibility(View.GONE);
        binding.cardSwitchTimer2.setVisibility(View.GONE);

        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.ic_bulb_off));
        //binding.textStatusValue.setText(R.string.device_offline);
        binding.textStatusValue.setText(resources.getString(R.string.device_offline));

        binding.textStatusValue.setTextSize(20);
        binding.textStatusValue.setTextColor(getResources().getColor(R.color.red));
  //      Utility.showSnackBar(binding.imgSwitch, resources.getString(R.string.message_device_offline));
        Utility.showSnackBar(binding.imgSwitch, resources.getString(R.string.message_device_offline));
        Utility.hideProgress();
    }
    //resources.getString(R.string.message_device_offline)

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLightOn() {

PreferenceData.setisLighton(true);
        setTimer("on",3);
         binding.imgSwitch.setImageDrawable(getDrawable(R.drawable.ic_switch_on));
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.ic_bulb_on));
   //     binding.textStatusValue.setText(R.string.light_on);
        binding.textStatusValue.setText(resources.getString(R.string.light_on));

        binding.textStatusValue.setTextColor(getResources().getColor(R.color.green));
        if (isOffLine) {
          //  Utility.showSnackBar(binding.imgSwitch, getString(R.string.message_device_offline));
            Utility.showSnackBar(binding.imgSwitch, resources.getString(R.string.message_device_offline));
        }
        Utility.hideProgress();
    }
    @Override
    protected void onStop() {
         super.onStop();
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
                        activeTime1 = "0";
                        setTimer("0", 1);
                    } else {
                        if (!isValidOnTime()) {
                           // Utility.showSnackBar(binding.btnStatus2, getString(R.string.valid_on_time));
                            Utility.showSnackBar(binding.btnStatus2, resources.getString(R.string.valid_on_time));


                        } else if (!isValidOffTime()) {
                           // Utility.showSnackBar(binding.btnStatus2, getString(R.string.valid_off_time));
                            Utility.showSnackBar(binding.btnStatus2, resources.getString(R.string.valid_off_time));
                        } else {
                            activeTime1 = "1";
//////////////////



                    //        setTimer("1", 1);

                            try {
                                sartttime=onHourTime1+":"+onMinuteTime1+":00";
                                endtime=offHourTime1+":"+ offMinuteTime1+":00";

                                pattern = "HH:mm:00";
                                sdf = new SimpleDateFormat(pattern);


                                Date date1 = sdf.parse(sartttime);
                                Date date2 = sdf.parse(endtime);

                                //    if(DBG) Log.i(TAG, "Date HOUR: " + date1.getHours());
                                //  if(DBG) Log.i(TAG, "Date: HOUR " + date2.getHours());



                                if(date1.getHours()>12 && date1.getHours()< 24 && date2.getHours()>=0 && date2.getHours()<13) {
                                    flag=1;

                                }
                                else{
                                    flag=0;
                                }

                            } catch (ParseException e){
                                e.printStackTrace();
                            }

                            if(flag!=1) {
                                if (checktimings(sartttime, endtime)) {
                                    setTimer("1", 1);
                                }
                            }
                            else {
                                setTimer("1", 1);


                            }






/////////

                        }
                    }
                } else {
                    setDeviceOffline();
                }

                binding.btnStatus1.setEnabled(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        binding.btnStatus1.setEnabled(true);

                    }
                }, 2000);
                break;
            case R.id.btnStatus2:
                if (!isOffLine) {
                    if (activeTime2.equalsIgnoreCase("1")) {
                        activeTime2 = "0";
                        setTimer("0", 2);
                    } else {
                        if (!isValidOnTime2()) {
                           // Utility.showSnackBar(binding.btnStatus2, getString(R.string.valid_on_time));
                            Utility.showSnackBar(binding.btnStatus2, resources.getString(R.string.valid_on_time));


                        } else if (!isValidOffTime2()) {
                          //  Utility.showSnackBar(binding.btnStatus2, getString(R.string.valid_off_time));
                            Utility.showSnackBar(binding.btnStatus2, resources.getString(R.string.valid_off_time));

                        } else {
                            activeTime2 = "1";
                        //   setTimer("1", 2);

                        /////////
                            try {
                                 sartttime2=onHourTime2+":"+onMinuteTime2+":00";
                                endtime2=offHourTime2+":"+ offMinuteTime2+":00";

                                pattern = "HH:mm:00";
                                sdf = new SimpleDateFormat(pattern);


                                Date date3 = sdf.parse(sartttime2);
                                Date date4 = sdf.parse(endtime2);

                                //    if(DBG) Log.i(TAG, "Date HOUR: " + date1.getHours());
                                //  if(DBG) Log.i(TAG, "Date: HOUR " + date2.getHours());



                                if(date3.getHours()>12 && date3.getHours()< 24 && date4.getHours()>=0 && date4.getHours()<13) {
                                    flag=1;

                                }
                                else{
                                    flag=0;
                                }

                            } catch (ParseException e){
                                e.printStackTrace();
                            }

                            if(flag!=1) {
                                if (checktimings(sartttime2, endtime2)) {
                                    setTimer("1", 2);
                                }
                            }
                            else {
                                setTimer("1", 2);


                            }


                            ///////
                        }
                    }
                } else {
                    setDeviceOffline();
                }
             //   final Handler handler2 = new Handler(Looper.getMainLooper());
                binding.btnStatus2.setEnabled(false);
             //   binding.btnStatus2.setBackgroundColor(Color.GRAY);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        binding.btnStatus2.setEnabled(true);
               //         binding.btnStatus2.setBackgroundColor(Color.BLACK);
                 //       binding.btnStatus2.setDr
                    }
                }, 2000);
                break;
            case R.id.textTimeOnValue1:
                if (!isOffLine) {
                    timeSelectFor = 1;
                    selectTime();
                } else {
                    setDeviceOffline();
                }
                break;
            case R.id.textTimeOffValue1:
                if (!isOffLine) {
                    timeSelectFor = 10;
                    selectTime();
                } else {
                    setDeviceOffline();
                }
                break;
            case R.id.textTimeOnValue2:
                if (!isOffLine) {
                    timeSelectFor = 2;
                    selectTime();
                } else {
                    setDeviceOffline();
                }
                break;
            case R.id.textTimeOffValue2:
                if (!isOffLine) {
                    timeSelectFor = 20;
                    selectTime();
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
              //  Utility.showProgress(this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utility.hideProgress();
                        binding.imgSwitch.setEnabled(true);
                        binding.imgSwitchBulb.setEnabled(true);

                        //Do something after 100ms
                    }
                }, 2000);
                break;
            case R.id.imgSwitchBulb:
                if (!isOffLine) {
                    updateSwitchStatus(isSwitchOn);
                } else {
                    setDeviceOffline();
                }
                binding.imgSwitch.setEnabled(false);
                binding.imgSwitchBulb.setEnabled(false);
            //    Utility.showProgress(this);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utility.hideProgress();
                        binding.imgSwitch.setEnabled(true);
                        binding.imgSwitchBulb.setEnabled(true);
                        //Do something after 100ms
                    }
                }, 2000);
                break;



        }
    }

    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm:00";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);
            if(date1.before(date2)) {

                return true;
            } else {
                Utility.showToast("Select Off time Greater than ON Time");
                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
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

    private void setTimer(String activate, int isFrom) {
        if (!Utility.isNetworkAvailable(LightOneActivity.this)) {
//            Utility.showSnackBar(binding.appBarLayout, getString(R.string.no_internet_connection));
            Utility.showSnackBar(binding.appBarLayout, resources.getString(R.string.no_internet_connection));

            //resources.getString(R.string.valid_off_time)
        } else {
            if (isFrom != 3 || isFrom != 4) {
               // Utility.showProgress(LightOneActivity.this);
            }
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = null;
            if (isFrom == 1) {
                responseCall = service.setSwitch(activate, WebUtility.SET_TIME1_OPS, onHourTime1, onMinuteTime1, offHourTime1, offMinuteTime1, OnlookApplication.SELECTED_DEVICE.getDevid());
            }
            if (isFrom == 2) {
                responseCall = service.setSwitch(activate, WebUtility.SET_TIME2_OPS, onHourTime2, onMinuteTime2, offHourTime2, offMinuteTime2, OnlookApplication.SELECTED_DEVICE.getDevid());
            }
            if (isFrom == 3) {
                responseCall = service.setSwitch(activate, WebUtility.SET_LIGHT_ONOFF, "x", "x", "x", "x", OnlookApplication.SELECTED_DEVICE.getDevid());
            }

            if (isFrom == 4) {
                responseCall = service.setSwitch(activate, WebUtility.SET_LWT, "x", "x", "x", "x", OnlookApplication.SELECTED_DEVICE.getDevid());
            }


            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Utility.hideProgress();
                     //   Utility.showSnackBar(binding.btnStatus1, getString(R.string.status_updated_successfully));
                        Utility.showSnackBar(binding.btnStatus1, resources.getString(R.string.status_updated_successfully));

                      getSetting(false);
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
                    //    Utility.showToast("Website Not reachable, Please check internet");
                        Utility.showToast(resources.getString(R.string.status_website));

                    }
                }
            });
        }
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        Utility.hideProgress();
        sensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utility.hideProgress();
        checkConnection(LightOneActivity.this,this);
        getSetting(true);
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
            Utility.hideProgress();
   if(DBG) Log.i(TAG, "FLIP: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+lastZ) ;
   state=1;

    if (!isOffLine) {
        updateSwitchStatus(isSwitchOn);
    } else {
        setDeviceOffline();
    }
    binding.imgSwitch.setEnabled(false);
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            binding.imgSwitch.setEnabled(true);

            //Do something after 100ms
        }

    }, 2000);


        }
        if(lastZ > 7 ) {
        state=0;
        }


        }

    private void getSetting(boolean isShowProgress) {
        if (!Utility.isNetworkAvailable(LightOneActivity.this)) {
            checkConnection(LightOneActivity.this,this);
        } else {
            if (isShowProgress) {
              //  Utility.showProgress(LightOneActivity.this);
            }
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<SwitchSettingResponse> responseCall = service.getSwitchSetting("no", OnlookApplication.SELECTED_DEVICE.getDevid());
            responseCall.enqueue(new Callback<SwitchSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<SwitchSettingResponse> call, @NotNull Response<SwitchSettingResponse> response) {
                    if (response.isSuccessful()) {
                        SwitchSettingResponse settingResponse = response.body();
                        setTimeValue(settingResponse,isShowProgress);
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

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LightOneActivity.this, R.style.TimePickerTheme, this, hour, minute, true);
//        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.setTitle(resources.getString(R.string.select_time));

        mTimePicker.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
        if (timeSelectFor == 1) { // SELECT ON TIME 1

            if (selectedHour < 10) {
                onHourTime1 = new DecimalFormat("00").format(selectedHour);
            } else {
                onHourTime1 = Integer.toString(selectedHour);
            }

            if (selectedMinute < 10) {
                onMinuteTime1 = new DecimalFormat("00").format(selectedMinute);
            } else {
                onMinuteTime1 = Integer.toString(selectedMinute);
            }
            binding.textTimeOnValue1.setText(onHourTime1 + " : " + onMinuteTime1);
            PreferenceData.setLight1OnHoursTime(onHourTime1);
            PreferenceData.setLight1OnMinuteTime(onMinuteTime1);

        } else if (timeSelectFor == 10) { //SELECT  OFF TIME 1
            if (selectedHour < 10) {
                offHourTime1 = new DecimalFormat("00").format(selectedHour);
            } else {
                offHourTime1 = Integer.toString(selectedHour);
            }

            if (selectedMinute < 10) {
                offMinuteTime1 = new DecimalFormat("00").format(selectedMinute);
            } else {
                offMinuteTime1 = Integer.toString(selectedMinute);
            }
            binding.textTimeOffValue1.setText(offHourTime1 + " : " + offMinuteTime1);
            PreferenceData.setLight1OffHoursTime(offHourTime1);
            PreferenceData.setLight1OffMinuteTime(offMinuteTime1);

        } else if (timeSelectFor == 2) { //SELECT  ON TIME 2

            if (selectedHour < 10) {
                onHourTime2 = new DecimalFormat("00").format(selectedHour);
            } else {
                onHourTime2 = Integer.toString(selectedHour);
            }

            if (selectedMinute < 10) {
                onMinuteTime2 = new DecimalFormat("00").format(selectedMinute);
            } else {
                onMinuteTime2 = Integer.toString(selectedMinute);
            }
            binding.textTimeOnValue2.setText(onHourTime2 + " : " + onMinuteTime2);
            PreferenceData.setLight2OnHoursTime(onHourTime2);
            PreferenceData.setLight2OnMinuteTime(onMinuteTime2);

        } else if (timeSelectFor == 20) { //SELECT  OFF TIME 2

            if (selectedHour < 10) {
                offHourTime2 = new DecimalFormat("00").format(selectedHour);
            } else {
                offHourTime2 = Integer.toString(selectedHour);
            }

            if (selectedMinute < 10) {
                offMinuteTime2 = new DecimalFormat("00").format(selectedMinute);
            } else {
                offMinuteTime2 = Integer.toString(selectedMinute);
            }
            binding.textTimeOffValue2.setText(offHourTime2 + " : " + offMinuteTime2);
            PreferenceData.setLight2OffHoursTime(offHourTime2);
            PreferenceData.setLight2OffMinuteTime(offMinuteTime2);
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
//            Utility.showDialog(context, context.getResources().getString(R.string.no_internet_title),
  //                  context.getResources().getString(R.string.no_internet),
    //                context.getResources().getString(R.string.button_try_again),
      //              context.getResources().getString(R.string.button_setting),
        //            listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);

            Utility.showDialog(context, resources.getString(R.string.no_internet_title),
                    resources.getString(R.string.no_internet),
                    resources.getString(R.string.button_try_again),
                    resources.getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);




        }
    }

}
