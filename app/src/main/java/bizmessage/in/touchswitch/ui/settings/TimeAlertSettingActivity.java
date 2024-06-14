package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TimePicker;

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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityTimeAlertSettingsBinding;
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

import static bizmessage.in.touchswitch.MainActivity.bindingMain;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;


import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;


public class TimeAlertSettingActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DialogButtonClickListener {

    private static final String TAG = TimeAlertSettingActivity.class.getSimpleName();
    private ActivityTimeAlertSettingsBinding binding;
    private String onHourTime1, onMinuteTime1, offHourTime1, offMinuteTime1;
    private int timeSelectFor = 1; // 1 = LIGHT ON 1 , 10 = LIGHT OFF 1,

    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "", isFamily = "no";
    private boolean isOffLine = true, isAutoLight = false,isAutoled = false, isAutoBuzz = false, isAutoLightSlider = false, isAutoBuzzSlider = false, isAutoLEDSlider = false;
    private TimeAlertSetting timeAlertSetting;
    private String autoLightTime = "", autoBuzzTime = "", autoLedTime = "";
    int i = 0;
    Uri notification;
    private Ringtone ringtone;
    private String id;
    private Vibrator vibrator;
    String allS, silS, notiS, ringS, vibS,noti,tone;
    boolean timerrun=false;
    Context context;
    Resources resources;

    final Handler handler = new Handler(Looper.getMainLooper());

    MediaPlayer player1;
    MediaPlayer player2;
    MediaPlayer player3;
    MediaPlayer player4; //noti1
    MediaPlayer player5; //noti2
    MediaPlayer player6; //noti3
    MediaPlayer player7;  //noti4


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_alert_settings);
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnStatus1.setOnClickListener(this);
        binding.cardNotificationEmail.setVisibility(View.GONE);
    //     binding.cardAlertType.setVisibility(View.GONE);

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
                //   finish();
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
            //textAutoBuzzerhead
            //  binding.textAutoBuzzerhead.setTextSize(14);
            binding.textBlockAllMobileNotification.setTextSize(16);///////////////
            //textAutoledhead

            binding.textAutoledhead.setTextSize(16);///////////////
            binding.textAutoLighthead.setTextSize(16);///////////////
            binding.textAutoBuzzerhead.setTextSize(16);///////////////
            binding.textautoledsubhead.setTextSize(14);///////////////
            binding.textautolightsubhead.setTextSize(14);///////////////
            binding.textViewbuzzsubhead.setTextSize(14);///////////////

        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ta");
            resources = context.getResources();
            binding.textMobileNotification.setTextSize(14);///////////////
            binding.textNotificationAlert.setTextSize(14);///////////////
            binding.textNotificationTone.setTextSize(14);///////////////

            binding.textTiming.setTextSize(14);///////////////
            binding.textNotificationEmail.setTextSize(14);///////////////
            binding.textallowNotification.setTextSize(12);
            binding.textBlockAllMobileNotification.setTextSize(14);///////////////
            binding.textAutoLighthead.setTextSize(12);///////////////
            binding.textAutoBuzzerhead.setTextSize(15);///////////////
            binding.textautolightsubhead.setTextSize(12);
            binding.textViewbuzzsubhead.setTextSize(12);
            binding.textallowNotification.setTextSize(12);
            binding.textNotificationEmail.setTextSize(13);

            binding.textAutoledhead.setTextSize(11);///////////////
            binding.textAutoLighthead.setTextSize(11);///////////////
            binding.textAutoBuzzerhead.setTextSize(11);///////////////
            binding.textautoledsubhead.setTextSize(13);///////////////
            binding.textautolightsubhead.setTextSize(13);///////////////
            binding.textViewbuzzsubhead.setTextSize(13);///////////////

        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "te");
            resources = context.getResources();
            binding.textAutoledhead.setTextSize(16);///////////////
            binding.textAutoLighthead.setTextSize(16);///////////////
            binding.textAutoBuzzerhead.setTextSize(16);///////////////
            binding.textautoledsubhead.setTextSize(14);///////////////
            binding.textautolightsubhead.setTextSize(14);///////////////
            binding.textViewbuzzsubhead.setTextSize(14);///////////////

        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "bn");
            resources = context.getResources();
            binding.textAutoledhead.setTextSize(15);///////////////
            binding.textAutoLighthead.setTextSize(15);///////////////
            binding.textAutoBuzzerhead.setTextSize(15);///////////////
            binding.textautoledsubhead.setTextSize(14);///////////////
            binding.textautolightsubhead.setTextSize(14);///////////////
            binding.textViewbuzzsubhead.setTextSize(14);///////////////
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ml");
            resources = context.getResources();
            binding.textTiming.setTextSize(18);///////////////
            binding.textNotificationAlert.setTextSize(20);///////////////
            binding.textMobileNotification.setTextSize(20);///////////////
            binding.textNotificationEmail.setTextSize(20);///////////////
            binding.textallowNotification.setTextSize(20);
            binding.textBlockAllMobileNotification.setTextSize(16);///////////////
            //    binding.textAutoLighthead.setTextSize(20);///////////////
            //  binding.textAutoLighthead.setTextSize(15);///////////////
            // binding.textAutoBuzzerhead.setTextSize(15);///////////////
            // binding.textautoledsubhead.setTextSize(12);///////////////
            // binding.textAutoledhead.setTextSize(13);///////////////


            binding.textAutoledhead.setTextSize(12);///////////////
            binding.textAutoLighthead.setTextSize(14);///////////////
            binding.textAutoBuzzerhead.setTextSize(14);///////////////
            binding.textautoledsubhead.setTextSize(13);///////////////
            binding.textautolightsubhead.setTextSize(13);///////////////
            binding.textViewbuzzsubhead.setTextSize(13);///////////////
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "en");
            resources = context.getResources();
            binding.textAutoledhead.setTextSize(15);///////////////
            binding.textAutoLighthead.setTextSize(15);///////////////
            binding.textAutoBuzzerhead.setTextSize(15);///////////////
            binding.textautoledsubhead.setTextSize(14);///////////////
            binding.textautolightsubhead.setTextSize(14);///////////////
            binding.textViewbuzzsubhead.setTextSize(14);///////////////
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "gu");
            resources = context.getResources();

        }
        binding.textDeviceId.setText(resources.getString(R.string.device)+" : " + OnlookApplication.SELECTED_DEVICE.getDevid());

        stop();
        if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            Utility.showToast(resources.getString(R.string.not_ebable));


            finish();

        } else {

        }


        binding.textTimeOffValue.setOnClickListener(v -> {
            timeSelectFor = 1;
            selectTime();
        });

        binding.textTimeOnValue.setOnClickListener(v -> {
            timeSelectFor = 10;
            selectTime();
        });
        isFamily = isFamilyMember();
  //      binding.checkPowerAlerts.setOnClickListener((buttonView) -> updateSettings(isFamily, "pownoti", binding.checkPowerAlerts.isChecked()));
    //    binding.checkMotionAlerts.setOnClickListener((buttonView) -> updateSettings(isFamily, "motnoti", binding.checkMotionAlerts.isChecked()));
        //   binding.checkVibrationAlerts.setOnClickListener((buttonView) -> updateSettings(isFamily, "vibnoti", binding.checkVibrationAlerts.isChecked()));
        //upen change start  sending parameter changed
        binding.checkNotificationTone1.setOnClickListener((buttonView) -> updateSettings(isFamily, "noti1", binding.checkNotificationTone1.isChecked()));
        binding.checkNotificationTone2.setOnClickListener((buttonView) -> updateSettings(isFamily, "noti2", binding.checkNotificationTone2.isChecked()));
        binding.checkNotificationTone3.setOnClickListener((buttonView) -> updateSettings(isFamily, "noti3", binding.checkNotificationTone3.isChecked()));
        binding.checkNotificationTone4.setOnClickListener((buttonView) -> updateSettings(isFamily, "noti4", binding.checkNotificationTone4.isChecked()));
        binding.checkPhoneRinger.setOnClickListener((buttonView) -> updateSettings(isFamily, "tone1", binding.checkPhoneRinger.isChecked()));
        binding.checkAlertRing1.setOnClickListener((buttonView) -> updateSettings(isFamily, "tone2", binding.checkAlertRing1.isChecked()));
        binding.checkAlertRing2.setOnClickListener((buttonView) -> updateSettings(isFamily, "tone3", binding.checkAlertRing2.isChecked()));
        binding.checkAlertRing3.setOnClickListener((buttonView) -> updateSettings(isFamily, "tone4", binding.checkAlertRing3.isChecked()));
        //upen cahnge end

        binding.checkNotificationEmail.setOnClickListener((buttonView) -> updateSettings(isFamily, "emailnoti", binding.checkNotificationEmail.isChecked()));
        binding.checkBlockAllMobileNotification.setOnClickListener((buttonView) -> updateSettings(isFamily, "allnoti", binding.checkBlockAllMobileNotification.isChecked()));
        binding.checkSilentAlert.setOnClickListener((buttonView) -> updateSettings(isFamily, "sil", binding.checkSilentAlert.isChecked()));
        binding.checkNotificationAlert.setOnClickListener((buttonView) -> updateSettings(isFamily, "nalert", binding.checkNotificationAlert.isChecked()));
        binding.checkRingerAlert.setOnClickListener((buttonView) -> updateSettings(isFamily, "ralert", binding.checkRingerAlert.isChecked()));
        binding.checkVibrationAlert.setOnClickListener((buttonView) -> updateSettings(isFamily, "vib", binding.checkVibrationAlert.isChecked()));

      // binding.textBlockAllMobileNotification.setVisibility(View.GONE);
      //  binding.checkBlockAllMobileNotification.setVisibility(View.GONE);


        binding.checkBoxAlways.setOnClickListener((buttonView) -> {

            if(binding.checkBoxAlways.isChecked()) {
                ////////////////// check alwayys will start server

                String sartttime=onHourTime1+":"+onMinuteTime1;
                String endtime=offHourTime1+":"+ offMinuteTime1;


                if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")) {
                    timeAlertSetting.setIsTimerActive("0");


                    setTimer("0", binding.checkBoxAlways.isChecked() ? "1" : "0");
                    binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));
                    binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));


                    //  binding.checkBoxAlways.setChecked(false);
                } else {
                    if (timeAlertSetting.getOnHours().equalsIgnoreCase("00") &&
                            timeAlertSetting.getOnMinutes().equalsIgnoreCase("00") &&
                            timeAlertSetting.getOffHours().equalsIgnoreCase("00") &&
                            timeAlertSetting.getOffMinutes().equalsIgnoreCase("00")) {
                        //     binding.checkBoxAlways.setChecked(true);
                    }
                    timeAlertSetting.setIsTimerActive("1");

                    setTimer("1", binding.checkBoxAlways.isChecked() ? "1" : "0");
                    binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));
                    binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));


                }
                if(DBG) Log.d("Handler", "out");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(DBG) Log.d("Handler", "in");
                        //Do something after 100ms
                    }
                }, 3000);








                /////////////////////
                // Utility.showToast(timeAlertSetting.getIsTimerActive());
                binding.textTimeOn.setTextColor(Color.BLACK);
                binding.textTimeOff.setTextColor(Color.BLACK);

                binding.checkBoxAlways.setTextColor(Color.parseColor("#FF358500"));


                         }
            else{
                binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));
                binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));

                binding.checkBoxAlways.setTextColor(Color.BLACK);
            }

        });

        binding.checkAutoled.setOnClickListener((buttonView) -> {
            isAutoled = true;
            if (!binding.checkAutoled.isChecked()) {
                binding.seekBarled.setOnTouchListener((view, motionEvent) -> true);
                binding.textautoledsubhead.setVisibility(View.GONE);
                binding.textautoledInfo.setVisibility(View.GONE);
                binding.seekBarled.setVisibility(View.GONE);
            } else {
                binding.seekBarled.setOnTouchListener((view, motionEvent) -> false);
                binding.seekBarled.setVisibility(View.VISIBLE);
                binding.textautoledsubhead.setVisibility(View.VISIBLE);
                binding.textautoledInfo.setVisibility(View.VISIBLE);

            }
            updateSettings(isFamily, "autoled", binding.checkAutoled.isChecked());
        });





        binding.checkAutoLight.setOnClickListener((buttonView) -> {
            isAutoLight = true;
            if (!binding.checkAutoLight.isChecked()) {
                binding.seekBar.setOnTouchListener((view, motionEvent) -> true);
                binding.textautolightsubhead.setVisibility(View.GONE);
                binding.textautolightInfo.setVisibility(View.GONE);
                binding.seekBar.setVisibility(View.GONE);
            } else {
                binding.seekBar.setOnTouchListener((view, motionEvent) -> false);
                binding.seekBar.setVisibility(View.VISIBLE);
                binding.textautolightsubhead.setVisibility(View.VISIBLE);
                binding.textautolightInfo.setVisibility(View.VISIBLE);

            }
            updateSettings(isFamily, "autol", binding.checkAutoLight.isChecked());
        });




        binding.checkAutoBuzzer.setOnClickListener((buttonView) -> {
            isAutoBuzz = true;
            if (!binding.checkAutoBuzzer.isChecked()) {
                binding.bseekBar.setOnTouchListener((view, motionEvent) -> true);
                binding.textViewbuzzsubhead.setVisibility(View.GONE);
                binding.textViewbuzzInfo.setVisibility(View.GONE);
                binding.bseekBar.setVisibility(View.GONE);
            } else {
                binding.bseekBar.setOnTouchListener((view, motionEvent) -> false);
                binding.bseekBar.setVisibility(View.VISIBLE);
                binding.textViewbuzzsubhead.setVisibility(View.VISIBLE);
                binding.textViewbuzzInfo.setVisibility(View.VISIBLE);
            }
            updateSettings(isFamily, "autobuzz", binding.checkAutoBuzzer.isChecked());
        });
        binding.seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        binding.bseekBar.setOnSeekBarChangeListener(seekBarBuzzerChangeListener);
        binding.seekBarled.setOnSeekBarChangeListener(seekBarLEDChangeListener);


        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
            binding.btnStatus1.setEnabled(false);
            binding.textTimeOnValue.setEnabled(false);
            binding.textTimeOffValue.setEnabled(false);
            binding.checkBoxAlways.setEnabled(false);
            binding.cardTiming.setVisibility(View.GONE);
            binding.cardAutoled.setVisibility(View.GONE);
            binding.cardAutobuzzer.setVisibility(View.GONE);
            binding.cardAutolight.setVisibility(View.GONE);
        } else {
            binding.cardTiming.setVisibility(View.VISIBLE);
        }





        getSupportActionBar().setTitle(resources.getString(R.string.setup_timing_alert_settings));
        binding.textTiming.setText(resources.getString(R.string.sensor_timer));///////////////
        binding.textTimeOn.setText(resources.getString(R.string.motion_ontime));///////////////
        binding.textTimeOff.setText(resources.getString(R.string.motion_offtime));///////////////
        binding.checkBoxAlways.setText(resources.getString(R.string.timer_always));///////////////
        binding.btnStatus1.setText(resources.getString(R.string.motion_activate));///////////////
        binding.textTimerSetInfo.setText(resources.getString(R.string.motion_set_info));///////////////
    //    binding.textAlertType.setText(resources.getString(R.string.motion_alerttype));///////////////
    //    binding.textPowerAlert.setText(resources.getString(R.string.motion_pow_alt));///////////////
     //   binding.textMotionAlert.setText(resources.getString(R.string.motion_mot_alt));///////////////
        //   binding.textVibrationAlert.setText(resources.getString(R.string.motion_vib_alt));///////////////
        binding.textNotificationEmail.setText(resources.getString(R.string.motion_email_noti));///////////////
        binding.textallowNotification.setText(resources.getString(R.string.motion_allow_email));
        binding.textTimeStatus1.setText(resources.getString(R.string.timer_running));/////////////



        binding.textNotificationTone.setText(resources.getString(R.string.motion_notitone));///////////////
        binding.textNotificationTone1.setText(resources.getString(R.string.motion_magic));///////////////

        binding.textNotificationTone2.setText(resources.getString(R.string.motion_fire));///////////////
        binding.textNotificationTone3.setText(resources.getString(R.string.motion_night));///////////////
        binding.textNotificationTone4.setText(resources.getString(R.string.motion_forest));///////////////
        binding.textRingSelection.setText(resources.getString(R.string.motion_ringtone));///////////////
        binding.textPhoneRinger.setText(resources.getString(R.string.motion_phoneringer));///////////////
        binding.textAlertRing1.setText(resources.getString(R.string.motion_jazz));///////////////
        binding.textAlertRing2.setText(resources.getString(R.string.motion_wakeup));///////////////
        binding.textAlertRing3.setText(resources.getString(R.string.motion_long_night));///////////////
        binding.textMobileNotification.setText(resources.getString(R.string.motion_mob_noti));///////////////
        binding.textBlockAllMobileNotification.setText(resources.getString(R.string.motion_blockall));///////////////
        binding.textSilentAlert.setText(resources.getString(R.string.motion_sil_alert));///////////////
        binding.textNotificationAlert.setText(resources.getString(R.string.motion_noti_alert));///////////////
        binding.textRingerAlert.setText(resources.getString(R.string.motion_ringer_alert));///////////////

        binding.textVibra.setText(resources.getString(R.string.motion_vibration_alertnoti));///////////////

        binding.texautolight.setText(resources.getString(R.string.motion_autolight));///////////////
        binding.textAutoLighthead.setText(resources.getString(R.string.motion_dect_autolight_head));///////////////
        binding.textautolightsubhead.setText(resources.getString(R.string.motion_dect_autolight_subhead));///////////////

        binding.texautoled.setText(resources.getString(R.string.motion_autoled));///////////////
        binding.textAutoledhead.setText(resources.getString(R.string.motion_dect_autoled_head));///////////////
        binding.textautoledsubhead.setText(resources.getString(R.string.motion_dect_autoled_subhead));///////////////


        binding.textAutoBuzzer.setText(resources.getString(R.string.motion_autobuzzer));///////////////
        binding.textAutoBuzzerhead.setText(resources.getString(R.string.motion_dect_autobuzz_head));///////////////
        binding.textViewbuzzsubhead.setText(resources.getString(R.string.motion_dect_autobuzz_subhead));///////////////



        setupMqttConnection();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(TimeAlertSettingActivity.this,this);
        getSetting(true);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            isAutoLightSlider = true;
            autoLightTime = seekBar.getProgress() + "";
            updateSettings(isFamily, "autoltime", true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            autoLightTime = progress + "";

            binding.textautolightInfo.setText(progress + " Min");

        }

    };

    SeekBar.OnSeekBarChangeListener seekBarBuzzerChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            isAutoBuzzSlider = true;
            autoBuzzTime = seekBar.getProgress() + "";
            updateSettings(isFamily, "autobuzztime", true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            autoLightTime = progress + "";
            //    binding.textViewbuzz.setText("Buzzer will be on for :" + progress + " Sec");
            binding.textViewbuzzInfo.setText(progress + " Sec");
        }

    };



    ////////////
    SeekBar.OnSeekBarChangeListener seekBarLEDChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            isAutoLEDSlider = true;
            autoLedTime = seekBar.getProgress() + "";
            updateSettings(isFamily, "autoledTime", true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            autoLedTime = progress + "";
            //    binding.textViewbuzz.setText("Buzzer will be on for :" + progress + " Sec");
            binding.textautoledInfo.setText(progress + " Min");
        }

    };



    ////////////



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnStatus1:
                String sartttime=onHourTime1+":"+onMinuteTime1;
                String endtime=offHourTime1+":"+ offMinuteTime1;


                if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")) {
                    timeAlertSetting.setIsTimerActive("0");


                    setTimer("0", binding.checkBoxAlways.isChecked() ? "1" : "0");
                    binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));
                    binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));


                    //  binding.checkBoxAlways.setChecked(false);
                } else {
                    if (timeAlertSetting.getOnHours().equalsIgnoreCase("00") &&
                            timeAlertSetting.getOnMinutes().equalsIgnoreCase("00") &&
                            timeAlertSetting.getOffHours().equalsIgnoreCase("00") &&
                            timeAlertSetting.getOffMinutes().equalsIgnoreCase("00")) {
                        //     binding.checkBoxAlways.setChecked(true);
                    }
                    timeAlertSetting.setIsTimerActive("1");

                    setTimer("1", binding.checkBoxAlways.isChecked() ? "1" : "0");
                    binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));
                    binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));
                    binding.checkBlockAllMobileNotification.setTextColor(Color.BLACK);

                    binding.checkBlockAllMobileNotification.setChecked(false);
                    binding.checkSilentAlert.setEnabled(true);
                    binding.checkNotificationAlert.setEnabled(true);
                    binding.checkRingerAlert.setEnabled(true);
                    binding.checkVibrationAlert.setEnabled(true);

                    binding.textBlockAllMobileNotification.setTextColor(Color.BLACK);
                    binding.textSilentAlert.setTextColor(Color.BLACK);
                    binding.textNotificationAlert.setTextColor(Color.BLACK);
                    binding.textRingerAlert.setTextColor(Color.BLACK);
                    binding.textVibra.setTextColor(Color.BLACK);


                }
                if(DBG) Log.d("Handler", "out");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(DBG) Log.d("Handler", "in");
                        //Do something after 100ms
                    }
                }, 3000);



                break;
        }
    }

    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            return date1.before(date2);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        Utility.showSnackBar(binding.btnStatus1, resources.getString(R.string.message_device_offline));

        binding.btnStatus1.setText("Device Offline");
        binding.textTimeStatus1.setText(resources.getString(R.string.device_offline));/////////////

        binding.textTimeStatus1.setTextColor(Color.RED);
        //  Utility.showSnackBar(binding.btnStatus1, getString(R.string.message_device_offline));
        binding.btnStatus1.setEnabled(false);
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
                    //  subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/RESULT";
                    //  binding.textStatusInfo.setText(resources.getString(R.string.motion_sensivity_online));
                    //  subscribeToTopic(subscriptionTopic);
//                    getSetting(true);
                    binding.btnStatus1.setEnabled(true);
                    if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")) {
                        // binding.btnStatus1.setText("DEACTIVATE");
                        binding.btnStatus1.setText(resources.getString(R.string.motion_deactivate));/////////////

//                        binding.cardNotificationEmail.setVisibility(View.VISIBLE);
                        binding.cardNotificationToneSelection.setVisibility(View.VISIBLE);
                        binding.cardRingSelection.setVisibility(View.VISIBLE);
                        binding.cardMobileNotification.setVisibility(View.VISIBLE);
                        binding.cardAutolight.setVisibility(View.VISIBLE);
                        binding.cardAutobuzzer.setVisibility(View.VISIBLE);
                        binding.cardAutoled.setVisibility(View.VISIBLE);



                        binding.textTimeStatus1.setText(resources.getString(R.string.timer_running));/////////////

                        binding.textTimeStatus1.setTextColor(Color.BLACK);

                        binding.textTimeOnValue.setEnabled(false);
                        binding.textTimeOffValue.setEnabled(false);
                        binding.checkBoxAlways.setEnabled(false);
                        binding.textTimeStatus1.setTextColor(Color.parseColor("#FF358500"));


                    }
                    if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("0")) {
                        binding.btnStatus1.setText("ACTIVATE");
  //                      binding.cardAlertType.setVisibility(View.GONE);
                        binding.cardNotificationEmail.setVisibility(View.GONE);
                        binding.cardNotificationToneSelection.setVisibility(View.GONE);
                        binding.cardRingSelection.setVisibility(View.GONE);
                        binding.cardMobileNotification.setVisibility(View.GONE);
                        binding.cardAutolight.setVisibility(View.GONE);
                        binding.cardAutobuzzer.setVisibility(View.GONE);
                        binding.cardAutoled.setVisibility(View.GONE);

                        binding.btnStatus1.setText(resources.getString(R.string.motion_activate));/////////////
                        binding.textTimeStatus1.setText(resources.getString(R.string.timer_stopped));/////////////

                        binding.textTimeStatus1.setTextColor(Color.RED);
                        binding.textTimeOnValue.setEnabled(true);
                        binding.textTimeOffValue.setEnabled(true);
                        binding.checkBoxAlways.setEnabled(true);
                        //binding.checkBoxAlways.setChecked(true);
                        binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));
                    }


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
    public void onBackPressed() {
        finish();
    }

    private void setTimer(String activate, String allDays) {
        if (!Utility.isNetworkAvailable(TimeAlertSettingActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout, resources.getString(R.string.no_internet_title));
        } else {
            Utility.showProgress(TimeAlertSettingActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.getDeviceTimerData(OnlookApplication.SELECTED_DEVICE.getDevid(), onHourTime1, onMinuteTime1, offHourTime1, offMinuteTime1, allDays, activate);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Utility.showSnackBar(binding.btnStatus1, resources.getString(R.string.status_updated_successfully));

                        if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")) {
                            //  binding.btnStatus1.setText("DEACTIVATE");
                            binding.btnStatus1.setText(resources.getString(R.string.motion_deactivate));/////////////
    //                        binding.cardAlertType.setVisibility(View.VISIBLE);
                         //   binding.cardNotificationEmail.setVisibility(View.VISIBLE);
                            binding.cardNotificationToneSelection.setVisibility(View.VISIBLE);
                            binding.cardRingSelection.setVisibility(View.VISIBLE);
                            binding.cardMobileNotification.setVisibility(View.VISIBLE);
                            binding.cardAutolight.setVisibility(View.VISIBLE);
                            binding.cardAutobuzzer.setVisibility(View.VISIBLE);
                            binding.cardAutoled.setVisibility(View.VISIBLE);





                            binding.textTimeStatus1.setText(resources.getString(R.string.timer_running));/////////////
                            binding.textTimeStatus1.setTextColor(Color.BLACK);
                            binding.textTimeOnValue.setEnabled(false);
                            binding.textTimeOffValue.setEnabled(false);
                            binding.checkBoxAlways.setEnabled(false);
                            binding.textTimeStatus1.setTextColor(Color.parseColor("#FF358500"));
                            if (OnlookApplication.SELECTED_DEVICE != null) {


                            }

                        } else if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("0")) {
                            //binding.btnStatus1.setText("ACTIVATE");
                            binding.btnStatus1.setText(resources.getString(R.string.motion_activate));/////////////

      //                      binding.cardAlertType.setVisibility(View.GONE);
                            binding.cardNotificationEmail.setVisibility(View.GONE);
                            binding.cardNotificationToneSelection.setVisibility(View.GONE);
                            binding.cardRingSelection.setVisibility(View.GONE);
                            binding.cardMobileNotification.setVisibility(View.GONE);
                            binding.cardAutolight.setVisibility(View.GONE);
                            binding.cardAutobuzzer.setVisibility(View.GONE);
                            binding.cardAutoled.setVisibility(View.GONE);


                            binding.textTimeStatus1.setText(resources.getString(R.string.timer_stopped));/////////////

                            binding.textTimeStatus1.setTextColor(Color.RED);
                            binding.textTimeOnValue.setEnabled(true);
                            binding.textTimeOffValue.setEnabled(true);
                            binding.checkBoxAlways.setEnabled(true);
                            binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));
                        }
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

    private void updateSettings(String isFamily, String status, boolean isChecked) {
        if (!Utility.isNetworkAvailable(TimeAlertSettingActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout, resources.getString(R.string.no_internet_title));
        } else {
            Utility.showProgress(TimeAlertSettingActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = null;
            if (isAutoLight) {
                isAutoLight = false;
                if (isChecked) {
                    autoLightTime = "true";
                } else {
                    autoLightTime = "false";
                }
                responseCall = service.updateSettingsAutoLight(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), autoLightTime, AppConstant.DEVICE_ANDROID);

            } else if (isAutoBuzz) {
                if (isChecked) {
                    autoBuzzTime = "true";
                } else {
                    autoBuzzTime = "false";
                }
                isAutoBuzz = false;
                responseCall = service.updateSettingsAutoBuzz(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), autoBuzzTime, AppConstant.DEVICE_ANDROID);

            } else if (isAutoled) {
                if (isChecked) {
                    autoLedTime = "true";
                } else {
                    autoLedTime = "false";
                }
                isAutoled = false;
                responseCall = service.updateSettingsAutoLed(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), autoLedTime, AppConstant.DEVICE_ANDROID);


            } else if (isAutoLightSlider) {
                isAutoLightSlider = false;
                responseCall = service.updateSettingsAutoLight(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), autoLightTime, AppConstant.DEVICE_ANDROID);
            }
            else if (isAutoLEDSlider) {
                isAutoLEDSlider = false;
                responseCall = service.updateSettingsAutoLight(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), autoLedTime, AppConstant.DEVICE_ANDROID);
            }
            else if (isAutoBuzzSlider) {
                isAutoBuzzSlider = false;
                responseCall = service.updateSettingsAutoBuzz(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), autoBuzzTime, AppConstant.DEVICE_ANDROID);
            } else {
                responseCall = service.updateSettings(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), isChecked, AppConstant.DEVICE_ANDROID);
            }





            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Utility.showSnackBar(binding.btnStatus1, resources.getString(R.string.status_updated_successfully));
                        setSettings(isFamily, status, isChecked);
                        Utility.hideProgress();
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

    //upendra start
    private void setSettings(String isFamily, String item, boolean status) {

        if(DBG) Log.i(TAG, "Settings: isFamily" + isFamily);
        if(DBG) Log.i(TAG, "Settings: Item" + item);
        if(DBG) Log.i(TAG, "Settings: status" + status);
        boolean tone1flag1, tone1flag2, tone1flag3, tone1flag4;


        if (item == "nalert") {
            if (status == true) {
                PreferenceData.setNoti("true");
            }
            if (status == false) {
                PreferenceData.setNoti("false");
            }
        }

        if (item == "ralert") {
            if (status == true) {
                PreferenceData.setRing("true");

            }
            if (status == false) {
                PreferenceData.setRing("false");


            }
        }

        if (item == "vib") {
            if (status == true) {
                if(DBG) Log.i(TAG, "Settings: status VIB" + status);
                PreferenceData.setVib("true");
                if(DBG) Log.i(TAG, "Settings: status VIB" + PreferenceData.getVib());


            }
            if (status == false) {
                if(DBG) Log.i(TAG, "Settings: status VIB" + status);
                PreferenceData.setVib("false");
                if(DBG) Log.i(TAG, "Settings: status VIB" + PreferenceData.getVib());

            }
        }


        if (item == "noti1") {
            if (status == true) {
                playT("1");
                PreferenceData.setNotinum("1");
                binding.checkNotificationTone2.setChecked(false);
                binding.checkNotificationTone3.setChecked(false);
                binding.checkNotificationTone4.setChecked(false);
            }
        }

        if (item == "noti2") {

            if (status == true) {
                playT("2");
                PreferenceData.setNotinum("2");
                binding.checkNotificationTone1.setChecked(false);
                binding.checkNotificationTone3.setChecked(false);
                binding.checkNotificationTone4.setChecked(false);
            }
        }
        if (item == "noti3") {
            if (status == true) {
                playT("3");
                PreferenceData.setNotinum("3");
                binding.checkNotificationTone1.setChecked(false);
                binding.checkNotificationTone2.setChecked(false);
                binding.checkNotificationTone4.setChecked(false);
            }
        }

        if (item == "noti4") {
            if (status == true) {
                playT("4");
                PreferenceData.setNotinum("4");
                binding.checkNotificationTone1.setChecked(false);
                binding.checkNotificationTone2.setChecked(false);
                binding.checkNotificationTone3.setChecked(false);
            }
        }
        if (!binding.checkNotificationTone1.isChecked() && !binding.checkNotificationTone2.isChecked() && !binding.checkNotificationTone3.isChecked() && !binding.checkNotificationTone4.isChecked()) {
            binding.checkNotificationTone1.setChecked(true);
            isFamily = isFamilyMember();
            PreferenceData.setNotinum("1");
            Utility.hideProgress();
            updateSettings(isFamily, "noti1", true);
        }


        //ring tone
        if (item == "tone1") {
            if (status == true) {
                playT("5");
                PreferenceData.setRingnum("1");
                binding.checkAlertRing1.setChecked(false);
                binding.checkAlertRing2.setChecked(false);
                binding.checkAlertRing3.setChecked(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
                        //Do something after 100ms
                        stop();
                    }
                }, 3000);


            }
        }

        if (item == "tone2") {

            if (status == true) {
                playT("6");
                PreferenceData.setRingnum("2");
                binding.checkPhoneRinger.setChecked(false);
                binding.checkAlertRing2.setChecked(false);
                binding.checkAlertRing3.setChecked(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
                        //Do something after 100ms
                        stop();
                    }
                }, 3000);
            }
        }
        if (item == "tone3") {
            if (status == true) {
                playT("7");
                PreferenceData.setRingnum("3");
                binding.checkPhoneRinger.setChecked(false);
                binding.checkAlertRing1.setChecked(false);
                binding.checkAlertRing3.setChecked(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
                        //Do something after 100ms
                        stop();
                    }
                }, 3000);
            }
        }

        if (item == "tone4") {
            if (status == true) {
                playT("8");
                PreferenceData.setRingnum("4");
                binding.checkPhoneRinger.setChecked(false);
                binding.checkAlertRing1.setChecked(false);
                binding.checkAlertRing2.setChecked(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
                        //Do something after 100ms
                        stop();
                    }
                }, 3000);
            }
        }
        if (!binding.checkPhoneRinger.isChecked() && !binding.checkAlertRing1.isChecked() && !binding.checkAlertRing2.isChecked() && !binding.checkAlertRing3.isChecked()) {
            binding.checkPhoneRinger.setChecked(true);
            isFamily = isFamilyMember();
            Utility.hideProgress();
            PreferenceData.setRingnum("1");
            updateSettings(isFamily, "tone1", true);

        }
        if (item == "allnoti") {
            if (status == true) {

                binding.checkSilentAlert.setEnabled(false);
                binding.checkNotificationAlert.setEnabled(false);
                binding.checkRingerAlert.setEnabled(false);
                binding.checkVibrationAlert.setEnabled(false);

                binding.textBlockAllMobileNotification.setTextColor(Color.RED);
                binding.textSilentAlert.setTextColor(Color.RED);

                binding.textNotificationAlert.setTextColor(Color.RED);
                binding.textRingerAlert.setTextColor(Color.RED);
                binding.textVibra.setTextColor(Color.RED);





            }

            if (status == false) {
                binding.textBlockAllMobileNotification.setTextColor(Color.BLACK);
                binding.checkSilentAlert.setEnabled(true);
                binding.checkNotificationAlert.setEnabled(true);
                binding.checkRingerAlert.setEnabled(true);
                binding.checkVibrationAlert.setEnabled(true);
                binding.textSilentAlert.setTextColor(Color.BLACK);
                binding.textNotificationAlert.setTextColor(Color.BLACK);
                binding.textRingerAlert.setTextColor(Color.BLACK);
                binding.textVibra.setTextColor(Color.BLACK);
                if(binding.checkSilentAlert.isChecked()){
                    binding.textSilentAlert.setTextColor(Color.RED);
                    binding.textVibra.setTextColor(Color.RED);
                    binding.textNotificationAlert.setTextColor(Color.RED);
                    binding.textRingerAlert.setTextColor(Color.RED);

                }
                if(!binding.checkSilentAlert.isChecked()){

                    binding.textSilentAlert.setTextColor(Color.BLACK);
                    binding.textNotificationAlert.setTextColor(Color.BLACK);
                    binding.textRingerAlert.setTextColor(Color.BLACK);
                    binding.textVibra.setTextColor(Color.BLACK);
                }
            }
        }

        if (item == "sil") {
            if (status == true) {
                PreferenceData.setSilent("true");
                binding.checkNotificationAlert.setEnabled(false);
                binding.checkRingerAlert.setEnabled(false);
                binding.checkVibrationAlert.setEnabled(false);
                binding.textSilentAlert.setTextColor(Color.RED);
                binding.textVibra.setTextColor(Color.RED);
                binding.textNotificationAlert.setTextColor(Color.RED);
                binding.textRingerAlert.setTextColor(Color.RED);


            }
            if (status == false) {
                PreferenceData.setSilent("false");
                binding.checkNotificationAlert.setEnabled(true);
                binding.checkRingerAlert.setEnabled(true);
                binding.checkVibrationAlert.setEnabled(true);
                binding.textSilentAlert.setTextColor(Color.BLACK);
                binding.textNotificationAlert.setTextColor(Color.BLACK);
                binding.textRingerAlert.setTextColor(Color.BLACK);
                binding.textVibra.setTextColor(Color.BLACK);
            }


        }
        if (!binding.checkNotificationAlert.isChecked() && !binding.checkRingerAlert.isChecked() && !binding.checkVibrationAlert.isChecked()) {
            binding.checkRingerAlert.setChecked(true);
            isFamily = isFamilyMember();
            Utility.hideProgress();
            updateSettings(isFamily, "ralert", true);

        }

    }

    // upendra end
    private void getSetting(boolean isShowing) {
        if (!Utility.isNetworkAvailable(TimeAlertSettingActivity.this)) {
            checkConnection(this, this);
        } else {
            if (isShowing) {
                Utility.showProgress(TimeAlertSettingActivity.this);
            }
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<TimeAlertsSettingResponse> responseCall = service.getDeviceSettingData(isFamilyMember(), OnlookApplication.SELECTED_DEVICE.getDevid());
            responseCall.enqueue(new Callback<TimeAlertsSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<TimeAlertsSettingResponse> call, @NotNull Response<TimeAlertsSettingResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAlertsSettingResponse settingResponse = response.body();
                        if (isFamilyMember().contains("yes")) {
                            setFamilyAllSettings(settingResponse);
                        } else {
                            setAllSettings(settingResponse);
                        }
                    } else {
                        Utility.showSnackBar(binding.appBarLayout, response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<TimeAlertsSettingResponse> call, @NotNull Throwable t) {
                    if(DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                }
            });
        }

        if (OnlookApplication.SELECTED_DEVICE != null) {

            if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
                isOffLine = false;
            } else {
                setDeviceOffline();
            }

        }


    }

    @SuppressLint("SetTextI18n")
    private void setAllSettings(TimeAlertsSettingResponse settingResponse) {
        if (settingResponse != null) {
            if (settingResponse.getSettings() != null) {
                timeAlertSetting = settingResponse.getSettings();
                if (timeAlertSetting.getAutoBuzz() != null) {
                    binding.checkAutoBuzzer.setChecked(timeAlertSetting.getAutoBuzz().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getAutoLight() != null) {
                    binding.checkAutoLight.setChecked(timeAlertSetting.getAutoLight().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getAutoled() != null) {
                    binding.checkAutoled.setChecked(timeAlertSetting.getAutoled().equalsIgnoreCase("true"));
                }



                if (timeAlertSetting.getIsAllDays() != null) {
                    binding.checkBoxAlways.setChecked(timeAlertSetting.getIsAllDays().equalsIgnoreCase("1"));

                    if(timeAlertSetting.getIsAllDays().equalsIgnoreCase("1")) {
                        binding.checkBoxAlways.setTextColor(Color.parseColor("#FF358500"));
                        if(timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")){
                            Utility.showToast("Sensor will Alert All Day");
                        }
                    }
                    else{
                        binding.checkBoxAlways.setTextColor(Color.BLACK);
                    }




                }

                if (timeAlertSetting.getIsMotion() != null) {
                  //  binding.checkMotionAlerts.setChecked(timeAlertSetting.getIsMotion().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getIsPower() != null) {
              //      binding.checkPowerAlerts.setChecked(timeAlertSetting.getIsPower().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")) {
                    // binding.btnStatus1.setText("DEACTIVATE");
                    binding.btnStatus1.setText(resources.getString(R.string.motion_deactivate));/////////////

   //                 binding.cardAlertType.setVisibility(View.VISIBLE);
                //    binding.cardNotificationEmail.setVisibility(View.VISIBLE);
                    binding.cardNotificationToneSelection.setVisibility(View.VISIBLE);
                    binding.cardRingSelection.setVisibility(View.VISIBLE);
                    binding.cardMobileNotification.setVisibility(View.VISIBLE);
                    binding.cardAutolight.setVisibility(View.VISIBLE);
                    binding.cardAutobuzzer.setVisibility(View.VISIBLE);
                    binding.cardAutoled.setVisibility(View.VISIBLE);



                    binding.textTimeStatus1.setText(resources.getString(R.string.timer_running));/////////////

                    binding.textTimeStatus1.setTextColor(Color.BLACK);

                    binding.textTimeOnValue.setEnabled(false);
                    binding.textTimeOffValue.setEnabled(false);
                    binding.checkBoxAlways.setEnabled(false);
                    binding.textTimeStatus1.setTextColor(Color.parseColor("#FF358500"));


                }
                if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("0")) {
                    binding.btnStatus1.setText("ACTIVATE");
     //               binding.cardAlertType.setVisibility(View.GONE);
                    binding.cardNotificationEmail.setVisibility(View.GONE);
                    binding.cardNotificationToneSelection.setVisibility(View.GONE);
                    binding.cardRingSelection.setVisibility(View.GONE);
                    binding.cardMobileNotification.setVisibility(View.GONE);
                    binding.cardAutolight.setVisibility(View.GONE);
                    binding.cardAutobuzzer.setVisibility(View.GONE);
                    binding.cardAutoled.setVisibility(View.GONE);

                    binding.btnStatus1.setText(resources.getString(R.string.motion_activate));/////////////
                    binding.textTimeStatus1.setText(resources.getString(R.string.timer_stopped));/////////////

                    binding.textTimeStatus1.setTextColor(Color.RED);
                    binding.textTimeOnValue.setEnabled(true);
                    binding.textTimeOffValue.setEnabled(true);
                    binding.checkBoxAlways.setEnabled(true);
                    //binding.checkBoxAlways.setChecked(true);
                    binding.textTimeStatus1.setTextColor(Color.parseColor("#f1c70623"));
                }


                if (timeAlertSetting.getIsVibration() != null) {
                    binding.checkVibrationAlert.setChecked(timeAlertSetting.getIsVibration().equalsIgnoreCase("true"));
                }

                setNotificationTone();
                setTonesStatus();

                if (timeAlertSetting.getNotificationEmail() != null) {
                    binding.checkNotificationEmail.setChecked(timeAlertSetting.getNotificationEmail().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getNotification1() != null) {

                    binding.checkNotificationTone1.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification1()));
                }

                if (timeAlertSetting.getNotification2() != null) {

                    binding.checkNotificationTone2.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification2()));
                }

                if (timeAlertSetting.getNotification3() != null) {

                    binding.checkNotificationTone3.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification3()));
                }

                if (timeAlertSetting.getNotification4() != null) {

                    binding.checkNotificationTone4.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification4()));
                }

                if (timeAlertSetting.getNotes1() != null) {

                    binding.checkPhoneRinger.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes1()));
                }

                if (timeAlertSetting.getNotes2() != null) {

                    binding.checkAlertRing1.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes2()));
                }
                if (timeAlertSetting.getNotes3() != null) {

                    binding.checkAlertRing2.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes3()));
                }
                if (timeAlertSetting.getNotes4() != null) {
                    binding.checkAlertRing3.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes4()));
                }







                if (timeAlertSetting.getNotificationAll() != null) {
                    binding.checkBlockAllMobileNotification.setChecked(timeAlertSetting.getNotificationAll().equalsIgnoreCase("true"));
                    if (timeAlertSetting.getNotificationAll().equalsIgnoreCase("true")) {


                        binding.textSilentAlert.setTextColor(Color.RED);

                        binding.textNotificationAlert.setTextColor(Color.RED);
                        binding.textRingerAlert.setTextColor(Color.RED);
                        binding.textVibra.setTextColor(Color.RED);
                        binding.textBlockAllMobileNotification.setTextColor(Color.RED);

                        binding.checkNotificationAlert.setEnabled(false);
                        binding.checkRingerAlert.setEnabled(false);
                        binding.checkVibrationAlert.setEnabled(false);
                        binding.checkSilentAlert.setEnabled(false);



                    }
                    if (timeAlertSetting.getNotificationAll().equalsIgnoreCase("false")) {
                        binding.checkBlockAllMobileNotification.setTextColor(Color.BLACK);

                        binding.checkSilentAlert.setEnabled(true);
                        binding.checkNotificationAlert.setEnabled(true);
                        binding.checkRingerAlert.setEnabled(true);
                        binding.checkVibrationAlert.setEnabled(true);

                        binding.textBlockAllMobileNotification.setTextColor(Color.BLACK);
                        binding.textSilentAlert.setTextColor(Color.BLACK);
                        binding.textNotificationAlert.setTextColor(Color.BLACK);
                        binding.textRingerAlert.setTextColor(Color.BLACK);
                        binding.textVibra.setTextColor(Color.BLACK);
                    }
                }

                if (timeAlertSetting.getNotificationSilent() != null) {
                    binding.checkSilentAlert.setChecked(timeAlertSetting.getNotificationSilent().equalsIgnoreCase("true"));
                    if (timeAlertSetting.getNotificationSilent().equalsIgnoreCase("true")) {


                        binding.checkNotificationAlert.setEnabled(false);
                        binding.checkRingerAlert.setEnabled(false);
                        binding.checkVibrationAlert.setEnabled(false);
                        binding.textSilentAlert.setTextColor(Color.RED);
                        binding.textVibra.setTextColor(Color.RED);
                        binding.textNotificationAlert.setTextColor(Color.RED);
                        binding.textRingerAlert.setTextColor(Color.RED);
                        binding.textVibra.setTextColor(Color.RED);
                    } else {
                        binding.checkNotificationAlert.setEnabled(true);
                        binding.checkRingerAlert.setEnabled(true);
                        binding.checkVibrationAlert.setEnabled(true);
                        binding.textSilentAlert.setTextColor(Color.BLACK);
                        binding.textSilentAlert.setTextColor(Color.BLACK);
                        binding.textNotificationAlert.setTextColor(Color.BLACK);
                        binding.textRingerAlert.setTextColor(Color.BLACK);
                        binding.textVibra.setTextColor(Color.BLACK);
                    }
                }

//                if (timeAlertSetting.getIsVibration() != null) {
                //                  binding.checkVibrationAlerts.setChecked(timeAlertSetting.getIsVibration().equalsIgnoreCase("true"));
                //            }

                if (timeAlertSetting.getVibrationOnly() != null) {
                    binding.checkVibrationAlert.setChecked(timeAlertSetting.getVibrationOnly().equalsIgnoreCase("true"));
                }





                if (timeAlertSetting.getNotificationOnly() != null) {
                    binding.checkNotificationAlert.setChecked(timeAlertSetting.getNotificationOnly().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getRingOnly() != null) {
                    binding.checkRingerAlert.setChecked(timeAlertSetting.getRingOnly().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getAutoLight() != null) {
                    binding.checkAutoLight.setChecked(timeAlertSetting.getAutoLight().equalsIgnoreCase("true"));
                }



                if (timeAlertSetting.getAutoLightLime() != null && timeAlertSetting.getAutoLightLime().length() > 0) {
                    int progress = Integer.parseInt(timeAlertSetting.getAutoLightLime());
                    binding.seekBar.setProgress(progress);
                }

                if (timeAlertSetting.getAutoledLime() != null && timeAlertSetting.getAutoledLime().length() > 0) {
                    int progress = Integer.parseInt(timeAlertSetting.getAutoledLime());
                    binding.seekBarled.setProgress(progress);
                }


                if (timeAlertSetting.getAutoBuzz() != null) {
                    binding.checkAutoBuzzer.setChecked(timeAlertSetting.getAutoBuzz().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getAutoBuzzTime() != null && timeAlertSetting.getAutoBuzzTime().length() > 0) {
                    int progress = Integer.parseInt(timeAlertSetting.getAutoBuzzTime());
                    binding.bseekBar.setProgress(progress);
                }


                if (!binding.checkAutoLight.isChecked()) {
                    binding.seekBar.setOnTouchListener((view, motionEvent) -> true);
                    binding.seekBar.setVisibility(View.GONE);
                    binding.textautolightsubhead.setVisibility(View.GONE);
                    binding.textautolightInfo.setVisibility(View.GONE);
                }
                else{
                    binding.seekBar.setVisibility(View.VISIBLE);
                    binding.textautolightsubhead.setVisibility(View.VISIBLE);
                    binding.textautolightInfo.setVisibility(View.VISIBLE);

                }


                //////////
                if (!binding.checkAutoled.isChecked()) {
                    binding.seekBarled.setOnTouchListener((view, motionEvent) -> true);
                    binding.seekBarled.setVisibility(View.GONE);
                    binding.textautoledsubhead.setVisibility(View.GONE);
                    binding.textautoledInfo.setVisibility(View.GONE);
                }
                else{
                    binding.seekBarled.setVisibility(View.VISIBLE);
                    binding.textautoledsubhead.setVisibility(View.VISIBLE);
                    binding.textautoledInfo.setVisibility(View.VISIBLE);

                }





                if (!binding.checkAutoBuzzer.isChecked()) {
                    binding.bseekBar.setOnTouchListener((view, motionEvent) -> true);
                    binding.bseekBar.setVisibility(View.GONE);
                    binding.textViewbuzzsubhead.setVisibility(View.GONE);
                    binding.textViewbuzzInfo.setVisibility(View.GONE);
                }
                else{
                    binding.bseekBar.setVisibility(View.VISIBLE);
                    binding.textViewbuzzsubhead.setVisibility(View.VISIBLE);
                    binding.textViewbuzzInfo.setVisibility(View.VISIBLE);
                }

                onHourTime1 = timeAlertSetting.getOnHours();
                onMinuteTime1 = timeAlertSetting.getOnMinutes();
                offHourTime1 = timeAlertSetting.getOffHours();
                offMinuteTime1 = timeAlertSetting.getOffMinutes();

                if (onHourTime1 != null && !onHourTime1.isEmpty() && onMinuteTime1 != null && !onMinuteTime1.isEmpty()) {
                    binding.textTimeOnValue.setText(timeAlertSetting.getOnHours() + " : " + timeAlertSetting.getOnMinutes());
                } else {
                    binding.textTimeOnValue.setText("00 : 00");
                }
                if (offHourTime1 != null && !offMinuteTime1.isEmpty() && offMinuteTime1 != null && !offMinuteTime1.isEmpty()) {
                    binding.textTimeOffValue.setText(timeAlertSetting.getOffHours() + " : " + timeAlertSetting.getOffMinutes());
                } else {
                    binding.textTimeOffValue.setText("00 : 00");
                }
            }

        }
        if (OnlookApplication.SELECTED_DEVICE != null) {

            if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
                isOffLine = false;
            } else {
                setDeviceOffline();
            }

        }



    }

    @SuppressLint("SetTextI18n")
    private void setFamilyAllSettings(TimeAlertsSettingResponse settingResponse) {
        if (settingResponse != null) {
            if (settingResponse.getSettings() != null) {
                timeAlertSetting = settingResponse.getSettings();

                if (timeAlertSetting.getIsMotion() != null) {
       //             binding.checkMotionAlerts.setChecked(timeAlertSetting.getIsMotion().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getIsPower() != null) {
         //           binding.checkPowerAlerts.setChecked(timeAlertSetting.getIsPower().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getIsVibration() != null) {
                    binding.checkVibrationAlert.setChecked(timeAlertSetting.getIsVibration().equalsIgnoreCase("true"));
                }

                setFamilyNotificationTone();
                setFamilyTonesStatus();

                if (timeAlertSetting.getFamNotificationEmail() != null) {
                    binding.checkNotificationEmail.setChecked(timeAlertSetting.getFamNotificationEmail().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getFamNotificationAll() != null) {
                    binding.checkBlockAllMobileNotification.setChecked(timeAlertSetting.getFamNotificationAll().equalsIgnoreCase("true"));
                    if (binding.checkBlockAllMobileNotification.isChecked()) {
                        binding.checkSilentAlert.setEnabled(false);
                        binding.checkNotificationAlert.setEnabled(false);
                        binding.checkRingerAlert.setEnabled(false);
                        binding.checkVibrationAlert.setEnabled(false);
                    } else {
                        binding.checkSilentAlert.setEnabled(true);
                        binding.checkNotificationAlert.setEnabled(true);
                        binding.checkRingerAlert.setEnabled(true);
                        binding.checkVibrationAlert.setEnabled(true);
                    }
                }

                if (timeAlertSetting.getFamNotificationSilent() != null) {
                    binding.checkSilentAlert.setChecked(timeAlertSetting.getFamNotificationSilent().equalsIgnoreCase("true"));
                    if (binding.checkSilentAlert.isChecked()) {
                        binding.checkNotificationAlert.setEnabled(false);
                        binding.checkRingerAlert.setEnabled(false);
                        binding.checkVibrationAlert.setEnabled(false);
                    } else {
                        binding.checkNotificationAlert.setEnabled(true);
                        binding.checkRingerAlert.setEnabled(true);
                        binding.checkVibrationAlert.setEnabled(true);
                    }
                }

                if (timeAlertSetting.getNotification1() != null) {

                    binding.checkNotificationTone1.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification1()));
                }

                if (timeAlertSetting.getNotification2() != null) {

                    binding.checkNotificationTone2.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification2()));
                }

                if (timeAlertSetting.getNotification3() != null) {

                    binding.checkNotificationTone3.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification3()));
                }

                if (timeAlertSetting.getNotification4() != null) {

                    binding.checkNotificationTone4.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotification4()));
                }

                if (timeAlertSetting.getNotes1() != null) {

                    binding.checkPhoneRinger.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes1()));
                }

                if (timeAlertSetting.getNotes2() != null) {

                    binding.checkAlertRing1.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes2()));
                }
                if (timeAlertSetting.getNotes3() != null) {

                    binding.checkAlertRing2.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes3()));
                }
                if (timeAlertSetting.getNotes4() != null) {
                    binding.checkAlertRing3.setChecked(Boolean.parseBoolean(timeAlertSetting.getNotes4()));
                }



                //         if (timeAlertSetting.getIsVibration() != null) {
                //           binding.checkVibrationAlerts.setChecked(timeAlertSetting.getIsVibration().equalsIgnoreCase("true"));
                //     }

                if (timeAlertSetting.getFamVibrationOnly() != null) {
                    binding.checkVibrationAlert.setChecked(timeAlertSetting.getFamVibrationOnly().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getFamNotificationOnly() != null) {
                    binding.checkNotificationAlert.setChecked(timeAlertSetting.getFamNotificationOnly().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getFamRingOnly() != null) {
                    binding.checkRingerAlert.setChecked(timeAlertSetting.getFamRingOnly().equalsIgnoreCase("true"));
                }

                binding.cardAutolight.setVisibility(View.GONE);
                binding.cardAutobuzzer.setVisibility(View.INVISIBLE);

            }
        }
    }

    private void setNotificationTone() {
        if (timeAlertSetting.getNotification1().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(true);
            binding.checkAlertRing1.setChecked(false);
            binding.checkAlertRing2.setChecked(false);
            binding.checkAlertRing3.setChecked(false);
        }
        if (timeAlertSetting.getNotification2().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(false);
            binding.checkAlertRing1.setChecked(true);
            binding.checkAlertRing2.setChecked(false);
            binding.checkAlertRing3.setChecked(false);
        }
        if (timeAlertSetting.getNotification3().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(false);
            binding.checkAlertRing1.setChecked(false);
            binding.checkAlertRing2.setChecked(true);
            binding.checkAlertRing3.setChecked(false);
        }
        if (timeAlertSetting.getNotification4().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(false);
            binding.checkAlertRing1.setChecked(false);
            binding.checkAlertRing2.setChecked(false);
            binding.checkAlertRing3.setChecked(true);
        }
    }

    private void setFamilyNotificationTone() {
        if (timeAlertSetting.getFamNotification1().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(true);
            binding.checkAlertRing1.setChecked(false);
            binding.checkAlertRing2.setChecked(false);
            binding.checkAlertRing3.setChecked(false);
        }
        if (timeAlertSetting.getFamNotification2().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(false);
            binding.checkAlertRing1.setChecked(true);
            binding.checkAlertRing2.setChecked(false);
            binding.checkAlertRing3.setChecked(false);
        }
        if (timeAlertSetting.getFamNotification3().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(false);
            binding.checkAlertRing1.setChecked(false);
            binding.checkAlertRing2.setChecked(true);
            binding.checkAlertRing3.setChecked(false);
        }
        if (timeAlertSetting.getFamNotification4().equalsIgnoreCase("true")) {
            binding.checkPhoneRinger.setChecked(false);
            binding.checkAlertRing1.setChecked(false);
            binding.checkAlertRing2.setChecked(false);
            binding.checkAlertRing3.setChecked(true);
        }
    }

    private void setTonesStatus() {
        if (timeAlertSetting.getNotes1().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(true);
            binding.checkNotificationTone2.setChecked(false);
            binding.checkNotificationTone3.setChecked(false);
            binding.checkNotificationTone4.setChecked(false);
        }
        if (timeAlertSetting.getNotes2().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(false);
            binding.checkNotificationTone2.setChecked(true);
            binding.checkNotificationTone3.setChecked(false);
            binding.checkNotificationTone4.setChecked(false);
        }
        if (timeAlertSetting.getNotes3().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(false);
            binding.checkNotificationTone2.setChecked(false);
            binding.checkNotificationTone3.setChecked(true);
            binding.checkNotificationTone4.setChecked(false);
        }
        if (timeAlertSetting.getNotes4().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(false);
            binding.checkNotificationTone2.setChecked(false);
            binding.checkNotificationTone3.setChecked(false);
            binding.checkNotificationTone4.setChecked(true);
        }
    }

    private void setFamilyTonesStatus() {
        if (timeAlertSetting.getFamTone1().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(true);
            binding.checkNotificationTone2.setChecked(false);
            binding.checkNotificationTone3.setChecked(false);
            binding.checkNotificationTone4.setChecked(false);
        }
        if (timeAlertSetting.getFamTone2().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(false);
            binding.checkNotificationTone2.setChecked(true);
            binding.checkNotificationTone3.setChecked(false);
            binding.checkNotificationTone4.setChecked(false);
        }
        if (timeAlertSetting.getFamTone3().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(false);
            binding.checkNotificationTone2.setChecked(false);
            binding.checkNotificationTone3.setChecked(true);
            binding.checkNotificationTone4.setChecked(false);
        }
        if (timeAlertSetting.getFamTone4().equalsIgnoreCase("true")) {
            binding.checkNotificationTone1.setChecked(false);
            binding.checkNotificationTone2.setChecked(false);
            binding.checkNotificationTone3.setChecked(false);
            binding.checkNotificationTone4.setChecked(true);
        }
    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TimeAlertSettingActivity.this, R.style.TimePickerTheme, this, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
        if (timeSelectFor == 1) { // SELECT OFF TIME 1

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
            binding.textTimeOffValue.setText(offHourTime1 + " : " + offMinuteTime1);
            PreferenceData.setLight1OnHoursTime(offHourTime1);
            PreferenceData.setLight1OnMinuteTime(offMinuteTime1);
            binding.checkBoxAlways.setChecked(false);
            binding.checkBoxAlways.setTextColor(Color.BLACK);
            /////////this will on sensor

            String sartttime=onHourTime1+":"+onMinuteTime1;
            String endtime=offHourTime1+":"+ offMinuteTime1;


            if (timeAlertSetting.getIsTimerActive().equalsIgnoreCase("1")) {
                timeAlertSetting.setIsTimerActive("0");


                setTimer("0", binding.checkBoxAlways.isChecked() ? "1" : "0");
                binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));
                binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));


                //  binding.checkBoxAlways.setChecked(false);
            } else {
                if (timeAlertSetting.getOnHours().equalsIgnoreCase("00") &&
                        timeAlertSetting.getOnMinutes().equalsIgnoreCase("00") &&
                        timeAlertSetting.getOffHours().equalsIgnoreCase("00") &&
                        timeAlertSetting.getOffMinutes().equalsIgnoreCase("00")) {
                    //     binding.checkBoxAlways.setChecked(true);
                }
                timeAlertSetting.setIsTimerActive("1");

                setTimer("1", binding.checkBoxAlways.isChecked() ? "1" : "0");
                binding.textTimeOn.setTextColor(Color.parseColor("#FF358500"));
                binding.textTimeOff.setTextColor(Color.parseColor("#FF358500"));


            }
            if(DBG) Log.d("Handler", "out");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(DBG) Log.d("Handler", "in");
                    //Do something after 100ms
                }
            }, 3000);



            //this will on sensor end




        } else if (timeSelectFor == 10) { //SELECT  ON TIME 1
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
            binding.textTimeOnValue.setText(onHourTime1 + " : " + onMinuteTime1);


            PreferenceData.setLight1OffHoursTime(onHourTime1);
            PreferenceData.setLight1OffMinuteTime(onMinuteTime1);

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

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        checkConnection(this, this);
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
            //          Utility.showDialog(context, context.getResources().getString(R.string.no_internet_title),
            //                context.getResources().getString(R.string.no_internet),
            //              context.getResources().getString(R.string.button_try_again),
            //            context.getResources().getString(R.string.button_setting),
            //          listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);
            Utility.showDialog(context, resources.getString(R.string.no_internet_title),
                    resources.getString(R.string.no_internet),
                    resources.getString(R.string.button_try_again),
                    resources.getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);


        }
    }

    public void playT(String noti) {
        if(DBG) Log.d(" RING NOTI VALUE",""+noti);
        if (noti.equalsIgnoreCase("1")) {
            if (player4 != null) {
                this.player4.stop();
            }
            if (player5 != null) {
                this.player5.stop();
            }
            if (player6 != null) {
                this.player6.stop();
            }
            if (player7 != null) {
                this.player7.stop();
            }
            startSound4("mp3/n1.mp3");
        }

        if (noti.equalsIgnoreCase("2")) {
            // if(DBG) Log.d("RING TONE PLAYING ","VIB 3");
            if (player4 != null) {
                this.player4.stop();
            }
            if (player5 != null) {
                this.player5.stop();
            }

            if (player6 != null) {
                this.player6.stop();
            }

            if (player7 != null) {
                this.player7.stop();
            }
            startSound5("mp3/n2.mp3");
        }

        if (noti.equalsIgnoreCase("3")) {
            if (player4 != null) {
                this.player4.stop();
            }
            if (player5 != null) {
                this.player5.stop();
            }
            if (player6 != null) {
                this.player6.stop();
            }
            if (player7 != null) {
                this.player7.stop();
            }
            startSound6("mp3/n3.mp3");
        }

        if (noti.equalsIgnoreCase("4")) {
            if (player4 != null) {
                this.player4.stop();
            }
            if (player5 != null) {
                this.player5.stop();
            }
            if (player6 != null) {
                this.player6.stop();
            }

            if (player7 != null) {
                this.player7.stop();
            }
            startSound7("mp3/n4.mp3");
        }

        ////////////
        if (noti.equalsIgnoreCase("5")) {  //RINGTONE
            if (ringtone != null) {
                this.ringtone.stop();
            }
            if (player1 != null) {
                this.player1.stop();
            }
            if (player2 != null) {
                this.player2.stop();
            }
            if (player3 != null) {
                this.player3.stop();
            }
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            this.ringtone = RingtoneManager.getRingtone(this, notification);
            ringtone.play();
        }

        // return START_NOT_STICKY;
        if (noti.equalsIgnoreCase("6")) {  //RINGTONE MP3
            if (ringtone != null) {
                this.ringtone.stop();
            }
            if (player1 != null) {
                this.player1.stop();
            }
            if (player2 != null) {
                this.player2.stop();
            }

            if (player3 != null) {
                this.player1.stop();
            }
            // if(DBG) Log.d("RINGING  PLAYING 2 ","VIB 3");
            startSound1("mp3/fire.mp3");
        }
        if (noti.equalsIgnoreCase("7")) {  //mp3
            if (ringtone != null) {
                this.ringtone.stop();
            }
            if (player1 != null) {
                this.player1.stop();
            }
            if (player2 != null) {
                this.player2.stop();
            }

            if (player3 != null) {
                this.player2.stop();
            }
            startSound2("mp3/siren2.mp3");
        }

        if (noti.equalsIgnoreCase("8")) {   //MP3
            if (ringtone != null) {
                this.ringtone.stop();
            }
            if (player1 != null) {
                this.player1.stop();
            }
            if (player2 != null) {
                this.player2.stop();
            }
            if (player3 != null) {
                this.player3.stop();
            }
            startSound3("mp3/burglar.mp3");
        }
    }






    private void startSound1(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player1 = new MediaPlayer();
        try {
            assert afd != null;
            player1.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player1.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player1.start();
    }

    private void startSound2(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player2 = new MediaPlayer();
        player2.setVolume(50, 50);
        try {
            assert afd != null;
            player2.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player2.start();
    }

    private void startSound3(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player3 = new MediaPlayer();
        try {
            assert afd != null;
            player3.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player3.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player3.start();
    }

    private void startSound4(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player4 = new MediaPlayer();
        try {
            assert afd != null;
            player4.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player4.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player4.start();
    }

    private void startSound5(String filename) {

        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player5 = new MediaPlayer();
        try {
            assert afd != null;
            player5.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player5.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player5.start();
    }

    private void startSound6(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player6 = new MediaPlayer();
        try {
            assert afd != null;
            player6.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player6.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player6.start();
    }


    private void startSound7(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player7 = new MediaPlayer();
        try {
            assert afd != null;
            player7.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player7.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player7.start();
    }

    public void stop() {
        // ringtone.stop();
        if (ringtone != null) {

            this.ringtone.stop();
        }
        if (player1 != null) {
            this.player1.stop();
        }
        if (player2 != null) {
            this.player2.stop();
        }

        if (player3 != null) {
            this.player3.stop();
        }


        if (player4 != null) {
            this.player4.stop();
        }
        if (player5 != null) {
            this.player5.stop();
        }

        if (player6 != null) {
            this.player6.stop();
        }

        if (player7 != null) {
            this.player7.stop();
        }


    }
}
