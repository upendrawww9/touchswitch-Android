package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityLedBinding;
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

public class LedActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener, AdapterView.OnItemSelectedListener, DialogButtonClickListener {

    private static final String TAG = LedActivity.class.getSimpleName();
    private ActivityLedBinding binding;
    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "";
    private boolean isSwitchOn = false, isOffLine = true;
    private int seekBarValue = 1, brightnessProgress = 0;
    private static final String[] paths = {"Select","Vij","Sri","lal","Nit","Bha","Tej","Chi","Rag","Ram","Pad","Adi","Shr","Kom","Nav","Ish" };
    final Handler handler = new Handler(Looper.getMainLooper());
    int state=0;
    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private final float deltaXMax = 0;
    private final float deltaYMax = 0;
    private final float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    int qos=2;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_led);
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.imgSwitch.setVisibility(View.GONE);
        binding.imgSwitchBulb.setVisibility(View.GONE);

        binding.cardSpinner.setVisibility(View.GONE);
        binding.cardScheme.setVisibility(View.GONE);
        binding.cardMatrix.setVisibility(View.GONE);

       // Utility.showProgress(LedActivity.this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }

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
            binding.textColorScheme.setTextSize(13);
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
            binding.textStatus.setTextSize(12);
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

        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "gu");
            resources = context.getResources();
        }


        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "gu");
            resources = context.getResources();
        }




        if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
            isOffLine = false;
           // binding.colorPickerView.setVisibility(View.GONE);
            binding.colorPickerView.setVisibility(View.VISIBLE);

            binding.offled.setVisibility(View.GONE);

        } else {

            binding.offled.setVisibility(View.VISIBLE);
            binding.colorPickerView.setVisibility(View.GONE);
            binding.cardSpinner.setVisibility(View.GONE);
            binding.cardScheme.setVisibility(View.GONE);
            binding.cardMatrix.setVisibility(View.GONE);


           // setDeviceOffline();

        }


        setupMqttConnection();
        binding.i1.setOnClickListener(this);
        binding.i2.setOnClickListener(this);
        binding.i3.setOnClickListener(this);
        binding.i4.setOnClickListener(this);
        binding.i5.setOnClickListener(this);
        binding.i6.setOnClickListener(this);
        binding.i7.setOnClickListener(this);
        binding.i8.setOnClickListener(this);
        binding.i9.setOnClickListener(this);
        binding.i10.setOnClickListener(this);
        binding.i11.setOnClickListener(this);
        binding.i12.setOnClickListener(this);
        binding.cminus.setOnClickListener(this);
        binding.cplus.setOnClickListener(this);
        binding.seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        binding.seekBarspeed.setOnSeekBarChangeListener(seekBarChangeListener1);

        // binding.textProgress.setText("Brightness: " + brightnessProgress);
     //binding.textProgressInfo.setText(brightnessProgress);///////////////
       // binding.imgSwitchBulb.setVisibility(View.GONE);
       // Utility.showToast("Please wait");

        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        binding.textControl.setText(resources.getString(R.string.led_control));///////////////
        binding.textStatus.setText(resources.getString(R.string.led_status));///////////////
        binding.textStatusValue.setText(resources.getString(R.string.led_off));///////////////
        binding.textColorWheel.setText(resources.getString(R.string.led_wheel));///////////////
        binding.textColorScheme.setText(resources.getString(R.string.led_scheme));///////////////
        binding.textProgress.setText(resources.getString(R.string.led_brightness));///////////////
        binding.textSpeed.setText(resources.getString(R.string.led_speed));///////////////

        Utility.showToast(resources.getString(R.string.please_wait));
        getSupportActionBar().setTitle(resources.getString(R.string.title_led));

        if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("ONLINE")) {
            isOffLine = false;
        } else {
            setDeviceOffline();
        }


        binding.colorPickerView.addOnColorChangedListener(selectedColor -> {

            if (isOffLine) {
                setDeviceOffline();

            } else {
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color1", "#" + Integer.toHexString(selectedColor).toUpperCase().substring(2, 8));
                } catch (IllegalArgumentException irc) {
                    irc.printStackTrace();
                }
            }
        });

        binding.imgSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOffLine) {
                    setDeviceOffline();
                } else {
                    updateSwitchStatus(isSwitchOn);
                }
                binding.imgSwitch.setEnabled(false);
                binding.imgSwitchBulb.setEnabled(false);
               // Utility.showProgress(LedActivity.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.imgSwitch.setEnabled(true);
                        binding.imgSwitchBulb.setEnabled(true);
                        Utility.hideProgress();
                        //Do something after 100ms
                    }
                }, 5000);
            }

        });

        binding.imgSwitchBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOffLine) {
                    setDeviceOffline();
                } else {
                    updateSwitchStatus(isSwitchOn);
                }
              //  Utility.showProgress(LedActivity.this);
                binding.imgSwitch.setEnabled(false);
                binding.imgSwitchBulb.setEnabled(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.imgSwitch.setEnabled(true);
                        binding.imgSwitchBulb.setEnabled(true);
                        Utility.hideProgress();

                        //Do something after 100ms
                    }
                }, 5000);
            }

        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LedActivity.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);

         handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Utility.showToast("Closing Connection for saftey");

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
        }, 120000);


    }
/*
    public void delay(){
        Utility.showProgress(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.hideProgress();

                //Do something after 100ms
            }
        }, 500);
    }
*/
    public boolean isOffline() {
        if (isOffLine) {
            binding.offled.setVisibility(View.VISIBLE);
            setDeviceOffline();
            binding.offled.setVisibility(View.VISIBLE);
            binding.colorPickerView.setVisibility(View.GONE);

            return true;
        } else {
            binding.offled.setVisibility(View.GONE);
            binding.colorPickerView.setVisibility(View.VISIBLE);
            return false;
        }
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.i1:
                if (!isOffline()) {
                    seekBarValue = 1;
                    binding.i0.setBackgroundColor(Color.RED);
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "1");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                }
                break;
            case R.id.i2:
                if (!isOffline()) {
                    seekBarValue = 2;
                    binding.i0.setBackgroundColor(Color.parseColor("#00FF00"));
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "2");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                }
                break;

            case R.id.i3:
                if (!isOffline()) {
                    seekBarValue = 3;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "3");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#0000ff"));
                }
                break;
            case R.id.i4:
                if (!isOffline()) {
                    seekBarValue = 4;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "4");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#FFA500"));
                }
                break;
            case R.id.i5:
                if (!isOffline()) {
                    seekBarValue = 5;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "5");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#90EE90"));
                }
                break;
            case R.id.i6:
                if (!isOffline()) {
                    seekBarValue = 6;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "6");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#add8e6"));
                }
                break;
            case R.id.i7:
                if (!isOffline()) {
                    seekBarValue = 7;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "7");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#FFBF00"));
                }
                break;
            case R.id.i8:
                if (!isOffline()) {
                    seekBarValue = 8;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "8");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#00FFFF"));
                }
                break;
            case R.id.i9:
                if (!isOffline()) {
                    seekBarValue = 9;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "9");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#800080"));
                }
                break;
            case R.id.i10:
                if (!isOffline()) {
                    seekBarValue = 10;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "10");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#FFFF00"));
                }
                break;
            case R.id.i11:
                if (!isOffline()) {
                    seekBarValue = 11;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "11");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#FFC0CB"));
                }
                break;
            case R.id.i12:
                if (!isOffline()) {
                    seekBarValue = 12;
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "12");
                    } catch (IllegalArgumentException irc) {
                        irc.printStackTrace();
                    }
                    binding.i0.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                break;
            case R.id.cplus:
                if (!isOffline()) {
                    if (seekBarValue > 0 && seekBarValue < 13) {
                        try {
                            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "+");
                        } catch (IllegalArgumentException irc) {
                            irc.printStackTrace();
                        }
                        seekBarValue = seekBarValue + 1;
                        if (seekBarValue == 13) {
                            seekBarValue = 12;
                        }
                        if (seekBarValue == 1) {
                            binding.i0.setBackgroundColor(Color.RED);

                        }
                        if (seekBarValue == 2) {
                            binding.i0.setBackgroundColor(Color.parseColor("#00FF00"));

                        }
                        if (seekBarValue == 3) {
                            binding.i0.setBackgroundColor(Color.parseColor("#0000ff"));

                        }
                        if (seekBarValue == 4) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFA500"));

                        }
                        if (seekBarValue == 5) {
                            binding.i0.setBackgroundColor(Color.parseColor("#90EE90"));

                        }
                        if (seekBarValue == 6) {
                            binding.i0.setBackgroundColor(Color.parseColor("#add8e6"));

                        }
                        if (seekBarValue == 7) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFBF00"));

                        }
                        if (seekBarValue == 8) {
                            binding.i0.setBackgroundColor(Color.parseColor("#00FFFF"));

                        }
                        if (seekBarValue == 9) {
                            binding.i0.setBackgroundColor(Color.parseColor("#800080"));

                        }
                        if (seekBarValue == 10) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFFF00"));

                        }
                        if (seekBarValue == 11) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFC0CB"));

                        }
                        if (seekBarValue == 12) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        }
                    }
                }
                break;
            case R.id.cminus:
                if (!isOffline()) {
                    if (seekBarValue > 0 && seekBarValue < 13) {
                        seekBarValue = seekBarValue - 1;
                        if (seekBarValue == 0) {
                            seekBarValue = 1;
                        }
                        try {
                            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Color", "-");
                        } catch (IllegalArgumentException irc) {
                            irc.printStackTrace();
                        }
                        if (seekBarValue == 1) {
                            binding.i0.setBackgroundColor(Color.RED);

                        }
                        if (seekBarValue == 2) {
                            binding.i0.setBackgroundColor(Color.parseColor("#00FF00"));

                        }

                        if (seekBarValue == 3) {
                            binding.i0.setBackgroundColor(Color.parseColor("#0000ff"));

                        }
                        if (seekBarValue == 4) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFA500"));

                        }
                        if (seekBarValue == 5) {
                            binding.i0.setBackgroundColor(Color.parseColor("#90EE90"));

                        }
                        if (seekBarValue == 6) {
                            binding.i0.setBackgroundColor(Color.parseColor("#add8e6"));

                        }
                        if (seekBarValue == 7) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFBF00"));

                        }
                        if (seekBarValue == 8) {
                            binding.i0.setBackgroundColor(Color.parseColor("#00FFFF"));

                        }
                        if (seekBarValue == 9) {
                            binding.i0.setBackgroundColor(Color.parseColor("#800080"));

                        }
                        if (seekBarValue == 10) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFFF00"));

                        }
                        if (seekBarValue == 11) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFC0CB"));

                        }
                        if (seekBarValue == 12) {
                            binding.i0.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                }
                break;
            case R.id.ivBack:
                Utility.hideProgress();
                onBackPressed();
                break;
        }
    }

     SeekBar.OnSeekBarChangeListener seekBarChangeListener1 = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBabrr
            if (!isOffline()) {
                try {

                    if(brightnessProgress==0){
                        brightnessProgress=1;
                    }
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Speed", brightnessProgress + "");
                } catch (IllegalArgumentException irc) {
                    irc.printStackTrace();
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb


            brightnessProgress = progress;

            // binding.textProgress.setText("Brightness: " + progress);
            binding.textspeedinfo.setText(resources.getString(R.string.led_speed_info));///////////////);///////////////

        }

    };





    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            if (!isOffline()) {
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Dimmer", brightnessProgress + "");
                } catch (IllegalArgumentException irc) {
                    irc.printStackTrace();
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            brightnessProgress = progress;
           // binding.textProgress.setText("Brightness: " + progress);
            binding.textProgressInfo.setText(""+progress);///////////////

        }

    };

    //////////

    ///////

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
        checkConnection(LedActivity.this,this);
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


    private void updateSwitchStatus(boolean isSwitchOn) {
     //   Utility.showProgress(LedActivity.this);
        if(DBG) Log.i(TAG, "updateSwitchStatus: IS SWITCH -" + isSwitchOn);
        if (isSwitchOn) {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER4", "ON");
        } else {
            publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER4", "OFF");
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLightOff() {

        PreferenceData.setisLedon(false);
        binding.imgSwitch.setImageDrawable(getDrawable(R.drawable.ic_switch_off));
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.ic_led_off));
        binding.textStatusValue.setText(R.string.light_off);
        binding.textStatusValue.setText(resources.getString(R.string.led_off));///////////////

        binding.textStatusValue.setTextColor(getResources().getColor(R.color.red));
        setTimer("off",1);
        if (isOffLine) {
           // Utility.showSnackBar(binding.imgSwitch, getString(R.string.message_device_offline));
            Utility.showSnackBar(binding.imgSwitch,resources.getString(R.string.message_device_offline));

        }
        Utility.hideProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        PreferenceData.setisLedon(false);
        binding.imgSwitch.setVisibility(View.GONE);
        binding.textStatus.setVisibility(View.GONE);
        binding.offled.setVisibility(View.VISIBLE);
        setTimer("off",1);
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.ic_led_off));
       // binding.textStatusValue.setText(R.string.device_offline);
       //  binding.textControl.setText(resources.getString(R.string.device_offline));///////////////
        binding.textStatusValue.setTextSize(20);
        binding.textStatusValue.setTextColor(getResources().getColor(R.color.red));
        binding.textStatusValue.setText(resources.getString(R.string.device_offline));
        Utility.showSnackBar(binding.imgSwitch, resources.getString(R.string.message_device_offline));
        Utility.hideProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLightOn() {
        PreferenceData.setisLedon(true);
        binding.imgSwitch.setImageDrawable(getDrawable(R.drawable.ic_switch_on));
        binding.imgSwitchBulb.setImageDrawable(getDrawable(R.drawable.ic_led_colour));
        binding.textStatusValue.setText(R.string.light_on);
        binding.textStatusValue.setText(resources.getString(R.string.led_on));///////////////
        setTimer("on",1);
        binding.textStatusValue.setTextColor(getResources().getColor(R.color.green));
        if (isOffLine) {
            Utility.showSnackBar(binding.imgSwitch, resources.getString(R.string.message_device_offline));
        }
        Utility.hideProgress();
    }

    private void setupMqttConnection() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(WebUtility.MQTT_USER_ID);
        mqttConnectOptions.setPassword(WebUtility.MQTT_PASSWORD.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);

      //  mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), WebUtility.MQTT_SERVER_URL,
        //        WebUtility.LED_CLIENT_ID + System.currentTimeMillis());

        mqttAndroidClient =new MqttAndroidClient(getApplicationContext(), WebUtility.MQTT_SERVER_URL,
                WebUtility.LED_CLIENT_ID + System.currentTimeMillis(), Ack.AUTO_ACK);



        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER4";
                    subscribeToTopic(subscriptionTopic);
                    try {
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER4", "");
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


                if(DBG) Log.i(TAG, "messageArrived: inboundtopic "+ inboundtopic);
                if(DBG) Log.i(TAG, "messageArrived: inboundvalue  "+ inboundvalue);
                if(!OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("Online")){
                    if(DBG) Log.i(TAG, "ISOFFLINE: COMING HERE");

                    if(DBG) Log.i(TAG, "ISOFFLINE: COMING HERE "+isOffLine);

                    setTimer("Online",2);
                    Utility.hideProgress();
                    Utility.showToast(resources.getString(R.string.button_try_again));
                    Intent intent = new Intent(LedActivity.this, MainActivity.class);
                    startActivity(intent);
                    //  OnlookApplication.SELECTED_DEVICE.setLwt("Online");
                }


                if (inboundvalue.equalsIgnoreCase("ON")) {
                    isSwitchOn = false;
                    if(DBG) Log.i(TAG, "messageArrived: SWITCH ON");
                    binding.textStatus.setVisibility(View.VISIBLE);

                    binding.cardSpinner.setVisibility(View.VISIBLE);
                    binding.cardScheme.setVisibility(View.VISIBLE);
                    binding.cardMatrix.setVisibility(View.VISIBLE);



                    setLightOn();

                    binding.offled.setVisibility(View.GONE);
                    binding.colorPickerView.setVisibility(View.VISIBLE);
                    binding.imgSwitch.setVisibility(View.VISIBLE);
                    binding.imgSwitchBulb.setVisibility(View.VISIBLE);



                }

                if (inboundvalue.equalsIgnoreCase("OFF")) {

                    binding.textStatus.setVisibility(View.VISIBLE);
                    binding.cardSpinner.setVisibility(View.GONE);
                    binding.cardScheme.setVisibility(View.GONE);
                    binding.cardMatrix.setVisibility(View.GONE);
                    isSwitchOn = true;
                    if(DBG) Log.i(TAG, "messageArrived: SWITCH OFF");

                    setLightOff();
                    binding.offled.setVisibility(View.VISIBLE);
                    binding.colorPickerView.setVisibility(View.GONE);
                    binding.imgSwitch.setVisibility(View.VISIBLE);
                    binding.imgSwitchBulb.setVisibility(View.VISIBLE);


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
             //  Utility.hideProgress();
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
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER4";
                    subscribeToTopic(subscriptionTopic);
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/POWER4", "");
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


    private void setTimer(String activate,int isFrom) {
        if (!Utility.isNetworkAvailable(LedActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout,resources.getString(R.string.no_internet_connection));
        } else {
           // Utility.showProgress(LedActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = null;

            if (isFrom == 1) {
                responseCall = service.setSwitch(activate, WebUtility.SET_LED_ONOFF, "x", "x", "x", "x", OnlookApplication.SELECTED_DEVICE.getDevid());
            }


            if (isFrom == 2) {
                responseCall = service.setSwitch(activate, WebUtility.SET_LWT, "x", "x", "x", "x", OnlookApplication.SELECTED_DEVICE.getDevid());
            }
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                       // Utility.showSnackBar(binding.textDeviceId  ,resources.getString(R.string.status_updated_successfully));
                        //getSetting(false);
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

                        //    Utility.showToast("Website Not reachable, Please check internet");
                        Utility.showToast(resources.getString(R.string.status_website));


                    }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case 1:


                break;

            case 2:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "10");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 3:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "3");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 4:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "4");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 5:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "5");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 6:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "6");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 7:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "7");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 8:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "8");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 9:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "9");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 10:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "2");
                } catch (IllegalArgumentException irc) {
                }
                break;


            case 11:
                try {
                     publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "11");










                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 12:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "11");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;
            case 13:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "12");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;

            case 14:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "12");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;

            case 15:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "0");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;

            case 16:
                try {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Scheme", "9");
                } catch (IllegalArgumentException irc) {
                    irc.fillInStackTrace();
                }
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            Utility.showDialog(context, resources.getString(R.string.no_internet_title),
                    resources.getString(R.string.no_internet),
                    resources.getString(R.string.button_try_again),
                    resources.getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);

        }
    }
}
