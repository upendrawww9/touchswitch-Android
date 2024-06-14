package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityWifiHotspotSettingsBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;



public class WifiAndHotSpotActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = WifiAndHotSpotActivity.class.getSimpleName();
    public static String[] wifiname;
    private ActivityWifiHotspotSettingsBinding binding;
    private MqttAndroidClient mqttAndroidClient;
    public String subscriptionTopic = "", isFamily = "no";
    private boolean isOffLine = false;
    private int isFrom = 0; // 1 = Planned , 0 = Other status.
    private static WifiManager wifiManager;
    private static String ITEM_KEY;
    public int isFromMainActivity = 0;
    int qos=2;
    public static final String MESSAGE_STATUS = "message_status";
    int PERMISSION_ALL = 1;
    private static final ArrayList myArrayList = null;
    private TextView wifisignal;
    final Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Resources resources;
    //String ap;
    boolean isOnline;
    boolean GpsStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wifi_hotspot_settings);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnSubmit.setOnClickListener(this);
        binding.btnWifiSettings.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);

        if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")) {

            binding.btnSubmit.setEnabled(false);
            binding.btnContinue.setEnabled(false);
        }

            if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "hi");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ka");
            resources = context.getResources();
            binding.textPassword.setTextSize(15);///////////////
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ta");
            resources = context.getResources();
            binding.textNetworkName.setTextSize(13);///////////////
            binding.textWifiStatus.setTextSize(15);
            binding.textWifiStatusInfo.setTextSize(15);
            binding.textPassword.setTextSize(13);///////////////
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
            binding.textNetworkName.setTextSize(13);///////////////
            binding.textTiming.setTextSize(13);
            binding.textContinue.setTextSize(13);

             binding.textPassword.setTextSize(13);///////////////

        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "en");
            resources = context.getResources();
            binding.textTiming.setTextSize(18);
            binding.textContinue.setTextSize(18);
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
        // binding.textNetworkName.setText("Network Name : " + OnlookApplication.SELECTED_DEVICE.getSsid());
       // binding.textPassword.setText("Network Password : " + OnlookApplication.SELECTED_DEVICE.getPaswd());
        getSupportActionBar().setTitle(resources.getString(R.string.setup_wifi_and_hot_spot_settings));


        binding.textWifino.setText(PreferenceData.getWifino()+" "+resources.getString(R.string.wifi_found)+" "+resources.getString(R.string.wifi_found_check));
        binding.textNetworkName.setText(resources.getString(R.string.wifi_hotspot_netname));///////////////
        binding.textNetworkNameInfo.setText(OnlookApplication.SELECTED_DEVICE.getSsid());///////////////
        binding.textPassword.setText(resources.getString(R.string.wifi_hotspot_password));///////////////
        binding.textPasswordInfo.setText(OnlookApplication.SELECTED_DEVICE.getPaswd());///////////////
        binding.textwifihotspotdesc.setText(resources.getString(R.string.wifi_hotspot_desc));///////////////
        binding.btnWifiSettings.setText(resources.getString(R.string.wifi_hotspot_btn));///////////////
        binding.btnContinue.setText(resources.getString(R.string.wifi_hotspot_btn_continue));///////////////
        binding.textwifisetuphead.setText(resources.getString(R.string.wifi_setup));///////////////
        binding.textWifiStatus.setText(resources.getString(R.string.wifi_status));///////////////
        binding.textWifiStatusDetails.setText(resources.getString(R.string.wifi_setup_text));///////////////

        binding.edtNetworkName.setHint(resources.getString(R.string.wifi_network_name));///////////////
        binding.edtPassword.setHint( resources.getString(R.string.wifi_network_password));///////////////
        binding.btnSubmit.setText(resources.getString(R.string.wifi_btn_submit));///////////////
        binding.textWifiName.setText(resources.getString(R.string.wifi_name));///////////////



        if (MainActivity.scanWifiResult != null && !MainActivity.scanWifiResult.isEmpty()){
            AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1,MainActivity.scanWifiResult);
            binding.edtNetworkName.setAdapter(autoCompleteAdapter);
        }
        isFrom = getIntent().getIntExtra("IS_FROM",0);
        if (isFrom == 1) {
            binding.textContinue.setVisibility(View.VISIBLE);
            binding.btnContinue.setVisibility(View.VISIBLE);
            binding.viewLineAlertType1.setVisibility(View.VISIBLE);
            binding.cardDeviceSetup.setVisibility(View.GONE);
           // binding.textTiming.setText("Step 1 :Turn on Hotspot, Configure");
           // binding.textContinue.setText("Step 2 : Reset onlook device and click Continue Button.");

            binding.textTiming.setText(resources.getString(R.string.wifi_hotspot_setup2));///////////////
            binding.textTiming.setTextColor(Color.BLACK);

            binding.textContinue.setText(resources.getString(R.string.wifi_hotspot_continue2));///////////////

        } else {
            binding.textContinue.setVisibility(View.GONE);
            binding.btnContinue.setVisibility(View.GONE);
            binding.viewLineAlertType1.setVisibility(View.GONE);
            binding.cardDeviceSetup.setVisibility(View.VISIBLE);
            setupMqttConnection();
        }
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
            binding.cardDeviceSetup.setVisibility(View.GONE);
            binding.btnSubmit.setVisibility(View.GONE);
        }

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
        }, 200000);

        CheckGpsStatus();
    }

    public void CheckGpsStatus(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == true) {
         // Utility.showToast("GPS Enabaled");

        } else {
           Utility.showToast("Enable GPS for Wifi Network Scan");
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(this,this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                if (validWIFI()) {
                    if(isOnline) {
                        binding.btnSubmit.setEnabled(false);
                        Utility.showToast("Please Wait");
                        updateSettings(isFamily, binding.edtNetworkName.getText().toString().trim(), "myap", false, binding.edtPassword.getText().toString().trim());
                        publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/SSID1", binding.edtNetworkName.getText().toString());
                    }
                    else{
                        Utility.showToast("Device is Offline");
                    }
                    }
                break;
            case R.id.btnContinue:
                binding.btnContinue.setEnabled(false);
                binding.btnContinue.setTextColor(getResources().getColor(R.color.red));
                new CountDownTimer(70000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        binding.btnContinue.setText("Working.." + millisUntilFinished / 1000+" Sec");

                        //here you can have your logic to set text to edittext
                    }
                    public void onFinish() {
                        binding.btnContinue.setText("done!");
                        confirm(2,resources.getString(R.string.wifi_noti), resources.getString(R.string.wifi_noti_info));
                    }
                }.start();
                break;
            case R.id.btnWifiSettings:
                openWifiSettings();
                break;
        }
    }

    private boolean validWIFI() {
        if (binding.edtNetworkName.getText().toString().trim().isEmpty() || binding.edtNetworkName.getText().toString().length() < 1) {
            Utility.showSnackBar(binding.edtNetworkName, getString(R.string.valid_wifi_name));
        } else if (binding.edtPassword.getText().toString().trim().isEmpty() || binding.edtPassword.getText().toString().length()< 8) {
            Utility.showSnackBar(binding.edtNetworkName, getString(R.string.valid_wifi_password));
        } else if (binding.edtPassword.getText().toString().trim().isEmpty() && binding.edtNetworkName.getText().toString().trim().isEmpty()) {
            Utility.showSnackBar(binding.edtNetworkName, getString(R.string.valid_wifi_name_pass));

        } else {
            return true;
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
        } catch (NullPointerException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        finish();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDeviceOffline() {
        Utility.showSnackBar(binding.btnSubmit, getString(R.string.message_device_offline));
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
                String inboundtopic = null;
                 inboundtopic = topic.concat("/"); //to handle on serversdide
                inboundvalue = new String(message.getPayload());

                if (inboundvalue.contains("Ap")) {
                    if(DBG) Log.i(TAG, "messageArrived: AP VALUE contain"+inboundvalue);
                  //  binding.textWifiStatusInfo.setText(inboundvalue);
                    JSONObject mainObject = new JSONObject(inboundvalue);
                    JSONObject uniObject = mainObject.getJSONObject("Ap");
                    String  apName = uniObject.getString("1");
                    //ap=inboundvalue;
               //   binding.textWifiStatusInfo.setText(resources.getString(R.string.online));
                  binding.textWifiNameInfo.setText(apName);
                   // inboundvalue.replace("Ap1", "Wifi");
                   // binding.textWifiStatus.setText("Onlook Status : " + inboundvalue);
                } else if(inboundvalue.contains("Online")) {
                  binding.textWifiStatusInfo.setText(resources.getString(R.string.online));
                  isOnline=true;

                }

                if (inboundvalue.equalsIgnoreCase("")) {
                    isOffLine = true;
                    if(DBG) Log.i(TAG, "messageArrived: DEVICE OFFLINE");
                    binding.textWifiStatusInfo.setText(resources.getString(R.string.offline));
                    setDeviceOffline();
                }

                if (inboundvalue.equalsIgnoreCase("Online")) {
                    subscriptionTopic = "stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/RESULT";
                    subscribeToTopic(subscriptionTopic);
                }

                if (inboundvalue.equalsIgnoreCase("Offline")) {
                    if(DBG) Log.i(TAG, "messageArrived: DEVICE OFFLINE");
                    isOffLine = true;
                    binding.textWifiStatusInfo.setText(resources.getString(R.string.offline));
                    setDeviceOffline();
                } else {
                    isOffLine = false;
                }

                if (inboundtopic.equalsIgnoreCase("stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/RESULT/") && inboundvalue.contains("SSId1")) {
                    publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Password1", binding.edtPassword.getText().toString());
                }

                if (inboundtopic.equalsIgnoreCase("stat/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/RESULT/") && inboundvalue.contains("Password1")) {
                  //  publishMessage("cmnd/" + OnlookApplication.SELECTED_DEVICE.getDevid() + "/Restart", "1");
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    Method method = wifiManager.getClass().getDeclaredMethod("getWifiApState");
                    method.setAccessible(true);
                    int actualState = (Integer) method.invoke(wifiManager, (Object[]) null);
                    //if (actualState == 13) {
                    //   confirm();
                    //} else {
                    confirm(1,"Device Wifi Configuration", "Device WiFi is Configured with " + binding.edtNetworkName.getText().toString() + ". Please restart the device now and click OK");
                    // }
                }
            }

            public String substringBetween(String str, String open,String close)
            {
                //  public static String substringBetween(String str, String open, String close) {
                if (str == null || open == null || close == null) {
                    return null;
                }
                int start = str.indexOf(open);
                if (start != -1) {
                    int end = str.indexOf(close, start + open.length());
                    if (end != -1) {
                        return str.substring(start + open.length(), end);
                    }
                }
                return null;

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
//    updateSettings(isFamily, binding.edtNetworkName.getText().toString().trim(), "myap", false, binding.edtPassword.getText().toString().trim());
//
    private void updateSettings(String isFamily, String wifiName, String status, boolean isChecked,String password) {
        if (!Utility.isNetworkAvailable(WifiAndHotSpotActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout, getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(WifiAndHotSpotActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.updateWifiSettings(isFamily, status, wifiName, OnlookApplication.SELECTED_DEVICE.getDevid(), isChecked,"",PreferenceData.getLatitude(),PreferenceData.getLongitude());

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

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

    private void confirm(int x,final String title, String info) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.AppTheme_Dialog);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.ic_action_wifi_spot);
        alertDialogBuilder
                .setMessage(info)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.dismiss();
                    if (x==1) {
                        try {
                            mqttAndroidClient.unregisterResources();
                            unSubscribe(mqttAndroidClient, subscriptionTopic);
                            mqttAndroidClient.close();
                            mqttAndroidClient.disconnect();
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }

                        new CountDownTimer(90000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                binding.textWifiStatus.setText(resources.getString(R.string.wifi_status));
                                binding.btnSubmit.setText("Working.." + millisUntilFinished / 1000+" Sec");
                                binding.btnSubmit.setBackgroundColor(Color.GRAY);
                            }

                            public void onFinish() {
                                binding.textWifiStatus.setText("done!");
                                Intent intent = new Intent(WifiAndHotSpotActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0,0);
                                //onBackPressed();
                            }

                        }.start();
                    }else if (x==2){
                        Intent intent = new Intent(WifiAndHotSpotActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                    }

                    if (x==3) {
                        onBackPressed();


                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Hotspot Info");
        alertDialogBuilder.setIcon(R.drawable.ic_action_logout);
        alertDialogBuilder
                .setMessage("Do You Want To Turn Off Hotspot")
                .setCancelable(false)
                .setPositiveButton(R.string.button_yes, (dialog, id) -> {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    confirm(3,"Device Wifi Configuration", "Device WiFi is Configured with " + binding.edtNetworkName.getText().toString() + ". Please restart the device now and click OK");
                })
                .setNegativeButton(R.string.button_no, (dialog, id) -> {
                    confirm(3,"Device Wifi Configuration", "Device WiFi is Configured with " + binding.edtNetworkName.getText().toString() + ". Please restart the device now and click OK");
                    dialog.cancel();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
            Utility.showDialog(context, resources.getString(R.string.no_internet_title),
                    resources.getString(R.string.no_internet),
                    resources.getString(R.string.button_try_again),
                    resources.getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);

        }
    }


}
