package bizmessage.in.touchswitch.ui.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;

import bizmessage.in.touchswitch.databinding.ActivitySettingsBinding;

import bizmessage.in.touchswitch.model.TimeAlertSetting;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.ui.others.Language;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    ActivitySettingsBinding binding;
    final Handler handler = new Handler(Looper.getMainLooper());
    public String subscriptionTopic = "", isFamily = "no";
    Context context;
    Resources resources;
    String Hootertime;
    private TimeAlertSetting timeAlertSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());



       // binding.checkpowerring.setVisibility(View.GONE);




        //isFamily = isFamilyMember();


        binding.cardlanguage.setOnClickListener(this);
        binding.cardSwctogrp.setOnClickListener(this);
        binding.cardMygroup.setOnClickListener(this);
        binding.cardMyswitch.setOnClickListener(this);
        binding.cardWifi.setOnClickListener(this);
        binding.cardSmartdevices.setOnClickListener(this);
        binding.cardMyswitch.setVisibility(View.GONE);

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


             binding.textWifi.setTextSize(13);

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
              binding.textWifi.setTextSize(13);

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



        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + TouchApplication.SELECTED_DEVICE.getNikname()+" : "+TouchApplication.SELECTED_DEVICE.getSsid());

        getSupportActionBar().setTitle(resources.getString(R.string.setup_setup));
           binding.textWifi.setText(resources.getString(R.string.setup_wifi_and_hot_spot_settings));///////////////
          binding.textLan.setText(resources.getString(R.string.language));///////////////
        binding.textsmartdevices.setText(resources.getString(R.string.entsmart));///////////////
        binding.textgrp.setText(resources.getString(R.string.entgrp));///////////////
        binding.textswctogrp.setText(resources.getString(R.string.swtogrp));///////////////







    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.cardMygroup:
                Intent intentgrp = new Intent(SettingsActivity.this, Mygroup.class);
                startActivity(intentgrp);
                SettingsActivity.this.overridePendingTransition(0,0);
                break;

            case R.id.cardSmartdevices:
                Intent intentswitch = new Intent(SettingsActivity.this, MySmartdevices.class);
                startActivity(intentswitch);
                SettingsActivity.this.overridePendingTransition(0,0);
                break;


            case R.id.cardSwctogrp:
                Intent intentswgrp = new Intent(SettingsActivity.this, SwitctoGrp.class);
                startActivity(intentswgrp);
                SettingsActivity.this.overridePendingTransition(0,0);
                break;


            case R.id.cardlanguage:
                Intent intentLanguage = new Intent(SettingsActivity.this, Language.class);
                startActivity(intentLanguage);
                SettingsActivity.this.overridePendingTransition(0,0);
                break;

             case R.id.cardWifi:
                Intent intentWifiSettings = new Intent(SettingsActivity.this, WifiAndHotSpotActivity.class);
                startActivity(intentWifiSettings);
                overridePendingTransition(0, 0);
                break;

        }
    }







    private void showNoDeviceFound() {
        //  binding.cardSetting.setEnabled(false);
        //  Utility.showSnackBar(binding.cardAlerts, getString(R.string.device_not_found));
        Utility.showErrorLong("No Devices Found");
    }

    private void updateSettings(String isFamily, String status, String isChecked,int isFrom) {
        if (!Utility.isNetworkAvailable(SettingsActivity.this)) {
            Utility.showSnackBar(binding.appBarLayout,resources.getString(R.string.no_internet_connection));
        } else {
           // Utility.showProgress(SettingsActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall=null;
            if(isFrom==1) {
                 responseCall = service.updateDeviceNameSettings(isFamily, status, TouchApplication.SELECTED_DEVICE.getId(), isChecked);
            }
            if(isFrom==2) {
                responseCall = service.updateDeviceNameSettings(isFamily, status, TouchApplication.SELECTED_DEVICE.getId(), isChecked);
            }

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

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(SettingsActivity.this,this);

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
         //   isAutoLightSlider = true;
            Hootertime = seekBar.getProgress() + "";

            updateSettings(isFamily, "firehooterTime", Hootertime,1);

            //updateSettings(isFamily, "autoltime", true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            //autoLightTime = progress + "";

           // binding.textViewhooterInfo.setText(progress + " Sec");

        }

    };

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion + "-" + release + "";
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