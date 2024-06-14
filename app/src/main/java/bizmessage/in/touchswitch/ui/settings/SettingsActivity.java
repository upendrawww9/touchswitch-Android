package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import bizmessage.in.touchswitch.app.OnlookApplication;

import bizmessage.in.touchswitch.databinding.ActivitySettingsBinding;

import bizmessage.in.touchswitch.model.TimeAlertSetting;
import bizmessage.in.touchswitch.model.TimeAlertsSettingResponse;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.ui.others.Language;
import bizmessage.in.touchswitch.utils.AppConstant;
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
        binding.btnUpdate.setOnClickListener(this);
        binding.edtDevice.setText(OnlookApplication.SELECTED_DEVICE.getNikname());

        binding.cardWifi.setOnClickListener(this);
        binding.cardMotion.setOnClickListener(this);

        binding.checkpowerring.setVisibility(View.GONE);
        binding.textPowring.setVisibility(View.GONE);


        if(PreferenceData.getIsfire().equalsIgnoreCase("true")) { // For fast view look its duplication info
            binding.cardAlertFire.setVisibility(View.VISIBLE);
        }
        else{
            binding.cardAlertFire.setVisibility(View.GONE);
        }


        isFamily = isFamilyMember();
        binding.checkPowerAlerts.setOnClickListener((buttonView) -> {
        updateSettings(isFamily, "pownoti", String.valueOf(binding.checkPowerAlerts.isChecked()),1);
            if(binding.checkPowerAlerts.isChecked()) {
               // Utility.showToast("Ringer is set for Power Alerts");
                binding.checkpowerring.setVisibility(View.VISIBLE);
                binding.textPowring.setVisibility(View.VISIBLE);
            }
            else{
                //Utility.showToast("Ringer is disabled for Power Alerts");
                binding.checkpowerring.setVisibility(View.GONE);
                binding.textPowring.setVisibility(View.GONE);
            }

            });
        binding.checkpowerring.setOnClickListener((buttonView) -> {
            updateSettings(isFamily, "powring", String.valueOf(binding.checkpowerring.isChecked()),1);
       PreferenceData.setisPowring(binding.checkpowerring.isChecked());


        });


        binding.checkFireAlerts.setOnClickListener((buttonView) -> {

            if(binding.checkFireAlerts.isChecked()) {
                updateSettings(isFamily, "firenoti", String.valueOf(binding.checkFireAlerts.isChecked()),1);
                binding.checkautohooter.setVisibility(View.VISIBLE);
                binding.seekBarfire.setVisibility(View.VISIBLE);
                binding.textViewhooterInfo.setVisibility(View.VISIBLE);
                binding.textViewhootersubhead.setVisibility(View.VISIBLE);
                binding.textautoHooter.setVisibility(View.VISIBLE);


            }
            else{
                updateSettings(isFamily, "firenoti", String.valueOf(binding.checkFireAlerts.isChecked()),1);

                binding.textautoHooter.setVisibility(View.GONE);
                binding.checkautohooter.setVisibility(View.GONE);
                binding.seekBarfire.setVisibility(View.GONE);
                binding.textViewhooterInfo.setVisibility(View.GONE);
                binding.textViewhootersubhead.setVisibility(View.GONE);


            }
        });

        binding.checkautohooter.setOnClickListener((buttonView) -> {

            if(binding.checkautohooter.isChecked()) {
                updateSettings(isFamily, "autofirhot", String.valueOf(binding.checkautohooter.isChecked()),1);

                //  binding.textautoHooter.setVisibility(View.VISIBLE);
                binding.seekBarfire.setVisibility(View.VISIBLE);
                binding.textViewhooterInfo.setVisibility(View.VISIBLE);
                binding.textViewhootersubhead.setVisibility(View.VISIBLE);
             //   binding.checkautohooter.setVisibility(View.VISIBLE);
            }
            else{
                updateSettings(isFamily, "autofirhot", String.valueOf(binding.checkautohooter.isChecked()),1);

             //   binding.checkautohooter.setVisibility(View.GONE);
                //binding.textautoHooter.setVisibility(View.GONE);
                binding.seekBarfire.setVisibility(View.GONE);
                binding.textViewhooterInfo.setVisibility(View.GONE);
                binding.textViewhootersubhead.setVisibility(View.GONE);
            }
        });



            binding.cardlanguage.setOnClickListener(this);


        if (OnlookApplication.SELECTED_DEVICE != null){
            if(DBG)  Log.d("isvibrate",OnlookApplication.SELECTED_DEVICE.getisVibratemodel());



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
            binding.textDeviceName.setTextSize(13);

             binding.textWifi.setTextSize(13);
            binding.textMotion.setTextSize(13);
            binding.textReset.setTextSize(12);

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
            binding.textDeviceName.setTextSize(13);
             binding.textWifi.setTextSize(13);
            binding.textMotion.setTextSize(13);
            binding.textReset.setTextSize(13);

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


        if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {

           binding.cardWifi.setVisibility(View.GONE);
            binding.cardMotion.setVisibility(View.GONE);
            binding.cardDeviceName.setVisibility(View.GONE);
            binding.cardReset.setVisibility(View.GONE);
            binding.cardAlertType.setVisibility(View.GONE);
            binding.cardlanguage.setVisibility(View.VISIBLE);

          // finish();

        }else {
        }
        }
        binding.textAlertType.setText(resources.getString(R.string.motion_pow_alt));///////////////
        binding.textAlertTypeFire.setText(resources.getString(R.string.motion_mot_alt));///////////////
        binding.textFireAlertact.setText(resources.getString(R.string.motion_activate));///////////////
        binding.textPowerAlert.setText(resources.getString(R.string.motion_activate));///////////////
        binding.textautoHooter.setText(resources.getString(R.string.fire_dect_autobuzz_head));///////////////
        binding.textViewhootersubhead.setText(resources.getString(R.string.fire_dect_autobuzz_subhead));///////////////
        binding.textPowerAlert.setText(resources.getString(R.string.plug_activate));///////////////
        binding.textPowring.setText(resources.getString(R.string.motion_ringtone));///////////////

        binding.seekBarfire.setOnSeekBarChangeListener(seekBarChangeListener);
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
           // binding.cardWifi.setVisibility(View.GONE);
            binding.cardMotion.setVisibility(View.GONE);
            binding.cardDeviceName.setVisibility(View.GONE);
             binding.cardReset.setVisibility(View.GONE);
             binding.cardAlertType.setVisibility(View.GONE);

        }

        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        getSupportActionBar().setTitle(resources.getString(R.string.setup_setup));
        binding.textDeviceName.setText(resources.getString(R.string.setup_loc_name));///////////////
       // binding.edtDevice.setText(resources.getString(R.string.setup_hint_name));///////////////
        binding.btnUpdate.setText(resources.getString(R.string.setup_update));///////////////
         binding.textWifi.setText(resources.getString(R.string.setup_wifi_and_hot_spot_settings));///////////////
        binding.textMotion.setText(resources.getString(R.string.setup_motion_sensitivity));///////////////
         binding.textLan.setText(resources.getString(R.string.language));///////////////
        binding.textReset.setText(resources.getString(R.string.reset));///////////////






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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnUpdate:


                if (!binding.edtDevice.getText().toString().trim().isEmpty()) {
                    binding.btnUpdate.setBackgroundColor(Color.GRAY);
                    binding.edtDevice.setEnabled(false);
                    binding.btnUpdate.setEnabled(false);


                    updateSettings(isFamilyMember(), "nik", binding.edtDevice.getText().toString().trim(),1);
                } else {
                    Utility.showSnackBar(binding.btnUpdate, resources.getString(R.string.valid_device_name));
                }
                break;

            case R.id.cardReset:
                if (OnlookApplication.SELECTED_DEVICE != null) {
                    Utility.showProgress(SettingsActivity.this);
                    binding.cardReset.setEnabled(false);
                    binding.cardReset.setCardBackgroundColor(Color.GRAY);
                    updateSettings(isFamilyMember(), "reset", "reset",2);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.cardReset.setEnabled(true);
                            binding.cardReset.setCardBackgroundColor(Color.WHITE);
                            Utility.hideProgress();

                            //Do something after 100ms
                        }

                    }, 10000);



                }
                else{
                    showNoDeviceFound();
                }
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
            case R.id.cardMotion:
                Intent intentMotionSettings = new Intent(SettingsActivity.this, MotionControlSettingsActivity.class);
                startActivity(intentMotionSettings);
                overridePendingTransition(0, 0);
                break;
        }
    }



    // upendra end
    private void getSetting(boolean isShowing) {
        if (!Utility.isNetworkAvailable(SettingsActivity.this)) {
            checkConnection(this, this);
        } else {
            if (isShowing) {
                Utility.showProgress(SettingsActivity.this);
            }
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<TimeAlertsSettingResponse> responseCall = service.getDeviceSettingData(isFamilyMember(), OnlookApplication.SELECTED_DEVICE.getDevid());
            responseCall.enqueue(new Callback<TimeAlertsSettingResponse>() {
                @Override
                public void onResponse(@NotNull Call<TimeAlertsSettingResponse> call, @NotNull Response<TimeAlertsSettingResponse> response) {
                    if (response.isSuccessful()) {
                        TimeAlertsSettingResponse settingResponse = response.body();
                        if (isFamilyMember().contains("yes")) {
                            //    setFamilyAllSettings(settingResponse);
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
                    if (DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void setAllSettings(TimeAlertsSettingResponse settingResponse) {
        if (settingResponse != null) {
            if (settingResponse.getSettings() != null) {
               timeAlertSetting = settingResponse.getSettings();




                if(timeAlertSetting.getIsPowerring().equalsIgnoreCase("true")){
                   // binding.checkpowerring.setVisibility(View.VISIBLE);
                    PreferenceData.setisPowring(true); // For fast view look its duplication info
                     binding.checkpowerring.setChecked(true);
                }
                else{
                    binding.checkpowerring.setChecked(false);
                     PreferenceData.setisPowring(false);// For fast view look its duplication info
                }



                if(timeAlertSetting.getIsPower().equalsIgnoreCase("true")){
                     binding.checkpowerring.setVisibility(View.VISIBLE);
                    binding.textPowring.setVisibility(View.VISIBLE);

                }
                else{
                     binding.checkpowerring.setVisibility(View.GONE);
                    binding.textPowring.setVisibility(View.GONE);
                 }




                if(timeAlertSetting.getIsFiremodel().equalsIgnoreCase("true")){
                    binding.cardAlertFire.setVisibility(View.VISIBLE);
                    PreferenceData.setIsfire("true"); // For fast view look its duplication info
                }
                else{
                    binding.cardAlertFire.setVisibility(View.GONE);
                    PreferenceData.setIsfire("false");// For fast view look its duplication info
                }



                if (timeAlertSetting.getIsPower() != null) {
                         binding.checkPowerAlerts.setChecked(timeAlertSetting.getIsPower().equalsIgnoreCase("true"));
                }

                if (timeAlertSetting.getIsPower() != null) {
                    binding.checkPowerAlerts.setChecked(timeAlertSetting.getIsPower().equalsIgnoreCase("true"));
                }




//PreferenceData.setisPowring(binding.checkpowerring.isChecked());

                if (timeAlertSetting.getIsFire() != null) {
                  binding.checkFireAlerts.setChecked(timeAlertSetting.getIsFire().equalsIgnoreCase("true"));

               if(timeAlertSetting.getIsFire().equalsIgnoreCase("true")){

                   binding.checkautohooter.setVisibility(View.VISIBLE);
                   binding.seekBarfire.setVisibility(View.VISIBLE);
                   binding.textViewhooterInfo.setVisibility(View.VISIBLE);
                   binding.textViewhootersubhead.setVisibility(View.VISIBLE);
                   binding.textautoHooter.setVisibility(View.VISIBLE);

               }
               else{
                   binding.seekBarfire.setVisibility(View.GONE);
                   binding.textautoHooter.setVisibility(View.GONE);
                   binding.checkautohooter.setVisibility(View.GONE);
                   binding.seekBarfire.setVisibility(View.GONE);
                   binding.textViewhooterInfo.setVisibility(View.GONE);
                   binding.textViewhootersubhead.setVisibility(View.GONE);
               }

               }

                if (timeAlertSetting.getIsAutofirehooter() != null) {
                    binding.checkautohooter.setChecked(timeAlertSetting.getIsAutofirehooter().equalsIgnoreCase("true"));

if(timeAlertSetting.getIsAutofirehooter().equalsIgnoreCase("true")){
    binding.seekBarfire.setVisibility(View.VISIBLE);
}
else{
    binding.textViewhooterInfo.setVisibility(View.GONE);
    binding.textViewhootersubhead.setVisibility(View.GONE);
    binding.seekBarfire.setVisibility(View.GONE);
}

                }

                if (timeAlertSetting.getAutofirehooterTime() != null && timeAlertSetting.getAutofirehooterTime().length() > 0) {
                    int progress = Integer.parseInt(timeAlertSetting.getAutofirehooterTime());
                    binding.seekBarfire.setProgress(progress);
                }
                if(timeAlertSetting.getIsFire().equalsIgnoreCase("true")) {
                }
                else{
                    binding.seekBarfire.setVisibility(View.GONE);
                }




                }
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
                 responseCall = service.updateDeviceNameSettings(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), isChecked);
            }
            if(isFrom==2) {
                responseCall = service.updateDeviceNameSettings(isFamily, status, OnlookApplication.SELECTED_DEVICE.getId(), isChecked);
            }

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if(isFrom==1) {
                            Utility.showSnackBar(binding.btnUpdate, resources.getString(R.string.status_updated_successfully));
                        }
                        if(isFrom==2) {
                            Utility.showSnackBar(binding.btnUpdate, resources.getString(R.string.device_restarted));
                        }

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
         //   isAutoLightSlider = true;
            Hootertime = seekBar.getProgress() + "";

            updateSettings(isFamily, "firehooterTime", Hootertime,1);

            //updateSettings(isFamily, "autoltime", true);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            //autoLightTime = progress + "";

            binding.textViewhooterInfo.setText(progress + " Sec");

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