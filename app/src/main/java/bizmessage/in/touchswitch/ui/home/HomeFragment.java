
package bizmessage.in.touchswitch.ui.home;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.FragmentHomeBinding;
import bizmessage.in.touchswitch.location.LocationUpdatesService;
import bizmessage.in.touchswitch.model.DashboardRequestResponse;
import bizmessage.in.touchswitch.notification.RingtonePlayingService;
import bizmessage.in.touchswitch.permissionManagerViews.Permission;
import bizmessage.in.touchswitch.permissionManagerViews.PermissionManagerInstance;
import bizmessage.in.touchswitch.permissionManagerViews.PermissionManagerListener;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.ui.alerts.AlertListActivity;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.ui.mydevice.MyDeviceActivity;
import bizmessage.in.touchswitch.ui.settings.LedActivity;
import bizmessage.in.touchswitch.ui.settings.LightOneActivity;
import bizmessage.in.touchswitch.ui.settings.PlugActivity;
import bizmessage.in.touchswitch.ui.settings.SettingsActivity;
import bizmessage.in.touchswitch.ui.settings.SirenActivity;
import bizmessage.in.touchswitch.ui.settings.TimeAlertSettingActivity;
import bizmessage.in.touchswitch.ui.settings.VibrationCtrl;
import bizmessage.in.touchswitch.ui.settings.WifiAndHotSpotActivity;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import me.pushy.sdk.Pushy;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WIFI_SERVICE;
import static bizmessage.in.touchswitch.MainActivity.bindingMain;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class HomeFragment extends Fragment implements View.OnClickListener, PermissionManagerListener, DialogButtonClickListener {

    private final String TAG = HomeFragment.class.getSimpleName();
    FragmentHomeBinding binding;
    private boolean isDeviceFound = false;
    PermissionManagerInstance permissionManagerInstance;
    String deviceToken = "";
    final Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Resources resources;
    String initoffer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);

        binding.cardAlertMode.setOnClickListener(this);
        binding.cardAlerts.setOnClickListener(this);
        binding.cardLed.setOnClickListener(this);
        binding.cardLight1.setOnClickListener(this);
        binding.cardLight2.setOnClickListener(this);
        binding.cardWebView.setOnClickListener(this);
        binding.cardDeviceStatus.setOnClickListener(this);
        binding.imgLeftArrow.setOnClickListener(this);
        binding.imgRightArrow.setOnClickListener(this);
        binding.cardSetting.setOnClickListener(this);
        binding.cardVibration.setOnClickListener(this);
        binding.cardBuzzer.setOnClickListener(this);

//PreferenceData.setDarkTheme(true);
      //  PreferenceData.setDarkTheme(false);

        if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getActivity(), "hi");
            resources = context.getResources();
                   }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getActivity(), "ka");
            resources = context.getResources();
            binding.textDeviceAlertModeStatus.setTextSize(9);
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getActivity(), "ta");
            resources = context.getResources();
            binding.textDeviceAlertMode.setTextSize(14);
            binding.textDeviceStatus.setTextSize(15);
            binding.textDeviceAlertModeStatus.setTextSize(8);

            binding.textNoDevice.setTextSize(14);


        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(getActivity(), "te");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(getActivity(), "bn");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(getActivity(), "ml");
            resources = context.getResources();
            binding.textNoDevice.setTextSize(15);
            binding.textDeviceAlertMode.setTextSize(12);
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(getActivity(), "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getActivity(), "gu");
            resources = context.getResources();
        }

        binding.imgWhatsApp.setVisibility(View.GONE);
        binding.textDeviceAlertMode.setText(resources.getString(R.string.motion_sensor));
        binding.textDeviceLed.setText(resources.getString(R.string.led));
        binding.textDeviceLight1.setText(resources.getString(R.string.light_1));
        binding.textDeviceLight2.setText(resources.getString(R.string.light_2));
        binding.textDeviceLight3.setText(resources.getString(R.string.settings));
        binding.textDeviceAlert.setText(resources.getString(R.string.alerts));
        binding.textDeviceStatus.setText(resources.getString(R.string.device_status));
        binding.textDeviceVibration.setText(resources.getString(R.string.vibration));
        binding.textDeviceBuzzer.setText(resources.getString(R.string.buzzer));
        binding.imgDeviceshare.setVisibility(View.GONE);




        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                OnlookApplication.SELECTED_DEVICE = OnlookApplication.deviceList.get(position);
                OnlookApplication.currentDevicePosition = position;
                PreferenceData.setCurrentSelectedPage(position);

                binding.textDeviceAlertCount.setText(OnlookApplication.deviceList.get(position).getAlertsCount());
                if (DBG)
                    Log.d("CURRENT DEVICE POS ", "" + OnlookApplication.currentDevicePosition);
                if (DBG)
                    Log.d("CURRENT DEVICE SIZE ", "" + OnlookApplication.deviceList.size());
                if (OnlookApplication.currentDevicePosition == 0) {
                    binding.imgLeftArrow.setVisibility(View.GONE);
                } else {
                    binding.imgLeftArrow.setVisibility(View.VISIBLE);
                }
                if (OnlookApplication.currentDevicePosition == OnlookApplication.deviceList.size() - 1) {
                    binding.imgRightArrow.setVisibility(View.GONE);
                } else {
                    binding.imgRightArrow.setVisibility(View.VISIBLE);
                }

                if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
                    disableClick();
                } else {
                    enableClick();
                }
                handleFamilyUser();
                setAlertMode();

            }
        });

        permissionManagerInstance = new PermissionManagerInstance(getActivity());

        //to remove fragment in backstack so that backbutton will close
      //  FragmentManager fm =  getActivity().getSupportFragmentManager();
     //   for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
       //     fm.popBackStack();
        //}

        getActivity().startService(new Intent(getActivity(),LocationUpdatesService.class));

        getWifiScanList();
        return binding.getRoot();
    }

    private void openWhatsApp(String x){

        WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
        Call<ResponseBody> responseCall = service.resetwhatsappoffer( PreferenceData.getEmail(),x);

        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {
                    Utility.showSnackBar(binding.cardWebView,response.message());
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


        String text;
        String toNumber;
        try {
            if (OnlookApplication.SELECTED_DEVICE != null) {


                text = "My Email id is " + PreferenceData.getEmail() + " .My device id : " + OnlookApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Regarding Offer : ";// Replace with your message.
                if(OnlookApplication.SELECTED_DEVICE.getCall911().length()>8) {
                    toNumber = "91" + OnlookApplication.SELECTED_DEVICE.getCall911(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
                }
                else{//if user is not set to dealer by mistake it will route to support number
                    text = "My support Not Set.My Email id is " + PreferenceData.getEmail() + " .My device id : " + OnlookApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Regarding Offer : ";// Replace with your message.

                    toNumber = "917900119635"; // Replace with mobile phone number without +Sign or leading zeros, but with country code

                }
            }
            else{ // ffor a new user or demo user
                text = "My Email id is " + PreferenceData.getEmail() + ". My App version is "+PreferenceData.getAppVersion()+" I need Support for : ";// Replace with your message.
                toNumber = "917900119635"; // Replace with mobile phone number without +Sign or leading zeros, but with country code

            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setAlertMode() {

        if(OnlookApplication.SELECTED_DEVICE.getIsshared().length()>5) {

            binding.imgDeviceshare.setVisibility(View.VISIBLE);

        }
        else{
            binding.imgDeviceshare.setVisibility(View.GONE);
        }

        if(OnlookApplication.SELECTED_DEVICE.getOffer().equalsIgnoreCase("1")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ilmc.xyz/onlook/setoffer.php?email="+PreferenceData.getEmail()));
            startActivity(browserIntent);
        }
        if(OnlookApplication.SELECTED_DEVICE.getOffer().equalsIgnoreCase("2")){


          openWhatsApp("1");
        }



        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
         //   binding.textDeviceAlertModeStatus.setText(OnlookApplication.SELECTED_DEVICE.getAlertModef());
            if (OnlookApplication.SELECTED_DEVICE.getAlertModef().contains("Blocked")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.blocked));

                binding.textDeviceAlertModeStatus.setTextColor(Color.MAGENTA);
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_silent_icon));
            } else if (OnlookApplication.SELECTED_DEVICE.getAlertModef().contains("RingMode")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.ring_mode));
                binding.textDeviceAlertModeStatus.setTextColor(Color.BLUE);
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ring));

            } else if (OnlookApplication.SELECTED_DEVICE.getAlertModef().contains("VibrationMode")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.vibration_mode));
                binding.textDeviceAlertModeStatus.setTextColor(Color.GRAY);

                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_vibration));
            }else if (OnlookApplication.SELECTED_DEVICE.getAlertMode().contains("Alerts Off")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.alert_off));
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_silent_icon));
            }
            else {
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_online));
            }
        }



        if(  OnlookApplication.deviceList.size() ==1)
        {
         //   binding.cardSetting.setCardBackgroundColor(Color.parseColor("#FFAF89AF"));
        //    binding.cardAlerts.setCardBackgroundColor(Color.parseColor("#FFB1A2A2"));

        }
        if(  OnlookApplication.deviceList.size() >0)
        {
            if(PreferenceData.isDarkTheme()){
                // nothing
            }
            else {
             //   binding.cardSetting.setCardBackgroundColor(Color.parseColor("#fffff0"));

            //    binding.cardAlerts.setCardBackgroundColor(Color.parseColor("#fffff0"));
            }



        }


        if(OnlookApplication.SELECTED_DEVICE.getRActTimer().equalsIgnoreCase("1") && OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("Online")) {
               if(PreferenceData.isDarkTheme()){
                binding.cardAlertMode.setCardBackgroundColor(Color.parseColor("#fbeaf3"));
            }
else {                             /////////////////////////111111111111111
                   binding.cardAlertMode.setCardBackgroundColor(Color.parseColor("#F1EDD107"));
               }
        }
        else
        {
            binding.cardAlertMode.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        }



        if(OnlookApplication.SELECTED_DEVICE.getIsledon().equalsIgnoreCase("on") ) {

            if(PreferenceData.isDarkTheme()){
                binding.cardLed.setCardBackgroundColor(Color.parseColor("#fbeaf3"));
            }
else {                                      //////////////////1111111111111111
                binding.cardLed.setCardBackgroundColor(Color.parseColor("#F1EDD107"));
            }
        }
        else
        {
            binding.cardLed.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        }

            ////////////////////222222222
        if(OnlookApplication.SELECTED_DEVICE.getIslighton().equalsIgnoreCase("on") ) {

    if(PreferenceData.isDarkTheme()){
                binding.cardLight1.setCardBackgroundColor(Color.parseColor("#d3d3d3"));
            }
else {
            binding.cardLight1.setCardBackgroundColor(Color.parseColor("#ffdf32"));
        }

        }
        else
        {
            binding.cardLight1.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        }
         /////////////////////////////222222222222
        if(OnlookApplication.SELECTED_DEVICE.getIsplughotteron().equalsIgnoreCase("on") ) {

       if(PreferenceData.isDarkTheme()){
                binding.cardLight2.setCardBackgroundColor(Color.parseColor("#d3d3d3"));
            }
else {
           binding.cardLight2.setCardBackgroundColor(Color.parseColor("#ffdf32"));


       }
        }
        if(OnlookApplication.SELECTED_DEVICE.getIsplughotteron().equalsIgnoreCase("off") ) {
            binding.cardLight2.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        }



          if(OnlookApplication.SELECTED_DEVICE.getisHooter().equalsIgnoreCase("true")) {
               binding.imgDeviceLight2.setImageDrawable(getResources().getDrawable(R.drawable.siren));
           binding.textDeviceLight2.setText(resources.getString(R.string.light_3));

      }
        if(OnlookApplication.SELECTED_DEVICE.getisHooter().equalsIgnoreCase("false")) {
              binding.imgDeviceLight2.setImageDrawable(getResources().getDrawable(R.drawable.ic_plug));
         //    binding.cardLight2.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
           binding.textDeviceLight2.setText(resources.getString(R.string.light_2));
         }


             if(OnlookApplication.SELECTED_DEVICE.getIsbuzzeron().equalsIgnoreCase("on") ) {
//////////////////////////////////////////////////////////
           if(PreferenceData.isDarkTheme()){
                binding.cardBuzzer.setCardBackgroundColor(Color.parseColor("#e8fbfb"));
            }
else {
                               ///////////////////////////////////3333
               binding.cardBuzzer.setCardBackgroundColor(Color.parseColor("#ffdf32"));
           }
        }
        else
        {
            binding.cardBuzzer.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        }



        if(OnlookApplication.SELECTED_DEVICE.getisVibratemodel().equalsIgnoreCase("false") || OnlookApplication.SELECTED_DEVICE.getisVibratemodel()==null){
          // Utility.showToast(resources.getString(R.string.not_ebable));
        }
        else {
            if (OnlookApplication.SELECTED_DEVICE.getRVibAlert().equalsIgnoreCase("true")) {

           if(PreferenceData.isDarkTheme()){
                binding.cardVibration.setCardBackgroundColor(Color.parseColor("#e8fbfb"));
            }
else {
                          /////////////////////////////////////////////////3333333
               binding.cardVibration.setCardBackgroundColor(Color.parseColor("#ffdf32"));
           }


            }
            if (OnlookApplication.SELECTED_DEVICE.getRVibAlert().equalsIgnoreCase("false")) {
                binding.cardVibration.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
            }

        }




        //     if(  OnlookApplication.SELECTED_DEVICE.getIsplughotteron().equalsIgnoreCase("true")) {
      //        binding.imgDeviceLight2.setImageDrawable(getResources().getDrawable(R.drawable.siren));
      //        binding.textDeviceLight2.setText(resources.getString(R.string.light_3));

        //}

       // if(OnlookApplication.SELECTED_DEVICE.getIsplughotteron().equalsIgnoreCase("true")) {
         //   binding.cardLight2.setCardBackgroundColor(Color.parseColor("#f1c70623"));
         //   binding.imgDeviceLight2.setImageDrawable(getResources().getDrawable(R.drawable.sirenon));
       // }
      //  else{
        //    binding.cardLight2.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        //    binding.imgDeviceLight2.setImageDrawable(getResources().getDrawable(R.drawable.siren));

       // }













        if (PreferenceData.getEmail().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getEmail())) {

           // binding.textDeviceAlertModeStatus.setText(OnlookApplication.SELECTED_DEVICE.getAlertMode());
            if (OnlookApplication.SELECTED_DEVICE.getAlertMode().contains("Blocked")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.blocked));
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_silent_icon));
            } else if (OnlookApplication.SELECTED_DEVICE.getAlertMode().contains("RingMode")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.ring_mode));
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ring));
            } else if (OnlookApplication.SELECTED_DEVICE.getAlertMode().contains("VibrationMode")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.vibration_mode));
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_vibration));
            } else if (OnlookApplication.SELECTED_DEVICE.getAlertMode().contains("SilentMode")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.silent_mode));
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_silent));
            }else if (OnlookApplication.SELECTED_DEVICE.getAlertMode().contains("Alerts Off")) {
                binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.alert_off));
                binding.imgDeviceAlertMode2.setImageDrawable(getResources().getDrawable(R.drawable.ic_silent_icon));
            }


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        stopRingService();
        getDashboardData();

        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
     //   bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(true);
              //

            }
        }, 5000);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardAlerts:
                if (isDeviceFound) {
                    Intent intentAlert = new Intent(getActivity(), AlertListActivity.class);
                    startActivity(intentAlert);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    showNoDeviceFound();
                }
                break;
            case R.id.cardVibration:


                 if(!PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in") || !PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {

                     if (OnlookApplication.SELECTED_DEVICE != null) {

                     if (OnlookApplication.SELECTED_DEVICE.getisVibratemodel().equalsIgnoreCase("false") || OnlookApplication.SELECTED_DEVICE.getisVibratemodel() == null || OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
                         Utility.showToast(resources.getString(R.string.not_ebable));
                     } else {
                         if (OnlookApplication.SELECTED_DEVICE != null) {
                             Intent intentSiren2 = new Intent(getActivity(), VibrationCtrl.class);
                             startActivity(intentSiren2);

                         }

                     }
                 }
                }
                else {
                     Utility.showToast(resources.getString(R.string.not_ebable));
                     if (OnlookApplication.SELECTED_DEVICE != null) {
                         Intent intentSiren2 = new Intent(getActivity(), VibrationCtrl.class);
                        startActivity(intentSiren2);

                    } else {
                        showNoDeviceFound();
                    }
                }
                break;

            case R.id.cardBuzzer:
             //   if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
                if (OnlookApplication.SELECTED_DEVICE != null && !OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
                    Intent intentSiren1 = new Intent(getActivity(), SirenActivity.class);
                    startActivity(intentSiren1);

                }
                else{
                    showNoDeviceFound();
                }
                break;



            case R.id.cardLed:
                if (isDeviceFound) {
                if (!OnlookApplication.SELECTED_DEVICE.getRIsLed().equalsIgnoreCase("true")) {
                    Utility.showToast(resources.getString(R.string.not_ebable));

                                    }


                if(OnlookApplication.SELECTED_DEVICE.getRIsLed().equalsIgnoreCase("true")) {


                        Intent intentLed = new Intent(getActivity(), LedActivity.class);
                        startActivity(intentLed);
                        getActivity().overridePendingTransition(0, 0);
                    } else {
                        Utility.showSnackBar(binding.cardAlerts, resources.getString(R.string.device_not_found));
                    }
                } else {
                    showNoDeviceFound();
                }
                break;
            case R.id.cardLight1:
                if (isDeviceFound) {
                if (!OnlookApplication.SELECTED_DEVICE.getRIsSwitch().equalsIgnoreCase("true")) {
                    Utility.showToast(resources.getString(R.string.not_ebable));
                    //getActivity().finish();

                }

                if(OnlookApplication.SELECTED_DEVICE.getRIsSwitch().equalsIgnoreCase("true")) {


                    Intent intentLight = new Intent(getActivity(), LightOneActivity.class);
                    startActivity(intentLight);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    showNoDeviceFound();
                }
        }
                else{}
                break;
            case R.id.cardLight2:
                if (isDeviceFound) {
                if (!OnlookApplication.SELECTED_DEVICE.getRIsPlug().equalsIgnoreCase("true")) {
                    Utility.showToast(resources.getString(R.string.not_ebable));
                    //getActivity().finish();


                }

if(OnlookApplication.SELECTED_DEVICE.getRIsPlug().equalsIgnoreCase("true")) {

        Intent intentLight2 = new Intent(getActivity(), PlugActivity.class);
        startActivity(intentLight2);
        getActivity().overridePendingTransition(0, 0);
    } else {
        Utility.showSnackBar(binding.cardAlerts, resources.getString(R.string.message_nodevice));
    }
                } else {
                    showNoDeviceFound();
                }
                break;
            case R.id.cardAlertMode:
                if (isDeviceFound) {
                    Intent intentAlertMode = new Intent(getActivity(), TimeAlertSettingActivity.class);
                    startActivity(intentAlertMode);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    showNoDeviceFound();
                }
                break;
            case R.id.cardDeviceStatus:
                if (isDeviceFound) {
                    Intent intentDeviceStatus = new Intent(getActivity(), MyDeviceActivity.class);
                    startActivity(intentDeviceStatus);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    showNoDeviceFound();
                }
                break;
            case R.id.imgLeftArrow:
                if (DBG)
                    Log.d("CURRENT DEVICE POS ", "" + OnlookApplication.currentDevicePosition);
                if (DBG)
                    Log.d("CURRENT DEVICE SIZE ", "" + OnlookApplication.deviceList.size());

                if (OnlookApplication.currentDevicePosition > 0 && OnlookApplication.currentDevicePosition < OnlookApplication.deviceList.size()) {
                    binding.viewPager.setCurrentItem(OnlookApplication.currentDevicePosition - 1);
                }
                break;
            case R.id.imgRightArrow:
                if (DBG)
                    Log.d("CURRENT DEVICE POS ", "" + OnlookApplication.currentDevicePosition);
                if (DBG)
                    Log.d("CURRENT DEVICE SIZE ", "" + OnlookApplication.deviceList.size());
                if (OnlookApplication.currentDevicePosition == 0) {
                    binding.imgRightArrow.setVisibility(View.GONE);
                } else {
                    binding.imgRightArrow.setVisibility(View.VISIBLE);
                }
                if (OnlookApplication.currentDevicePosition < OnlookApplication.deviceList.size() && OnlookApplication.currentDevicePosition >= 0) {
                    binding.viewPager.setCurrentItem(OnlookApplication.currentDevicePosition + 1);
                }
                break;
            case R.id.cardSetting:
                if (isDeviceFound) {
               // if (!PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
                    if (OnlookApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
                        Intent intentSettings = new Intent(getActivity(), WifiAndHotSpotActivity.class);
                        intentSettings.putExtra("IS_FROM", 1);
                        startActivity(intentSettings);
                        getActivity().overridePendingTransition(0, 0);
                    } else {
                        Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intentSettings);
                        getActivity().overridePendingTransition(0, 0);
                    }
              //  }
                } else {
                    showNoDeviceFound();
                }
                break;
        }
    }

    private void handleFamilyUser() {
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        } else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        } else if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")) {
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        }
    }

    private void showNoDeviceFound() {
       //  binding.cardSetting.setEnabled(false);
        Utility.showSnackBar(binding.cardAlerts, resources.getString(R.string.message_nodevice));




    }

    @Override
    public void permissionCallback(String[] permissions, Permission[] grantResults, boolean allGranted) {
        if (grantResults.length > 0) {
            getWifiScanList();
        }
    }

    public void getWifiScanList() {
        WifiManager wifiManager1 = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager1.startScan();
        if (wifiManager1.isWifiEnabled()) {
            final List<ScanResult> results1 = wifiManager1.getScanResults();
            if (DBG) Log.i(TAG, "getWifiScanList: " + results1.size());
        }
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        getDashboardData();
        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
       // bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(true);
               // bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.VISIBLE);
            }
        }, 5000);

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

    private class DeviceViewSlidePagerAdapter extends FragmentStateAdapter {
        public DeviceViewSlidePagerAdapter(FragmentActivity fa) {

               super(fa);
            getActivity().getFragmentManager();


        }

        @Override
        public Fragment createFragment(int position) {
            return DeviceViewFragment.getInstance(position, OnlookApplication.deviceList.get(position));
        }

        @Override
        public int getItemCount() {
            return OnlookApplication.deviceList.size();
        }
    }

    private void stopRingService() {
        try {
            Intent ringIntent = new Intent(getActivity(), RingtonePlayingService.class);
            getActivity().stopService(ringIntent);


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void getDashboardData() {
        bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
       // bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(true);
             //   bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.VISIBLE);
            }
        }, 5000);

        if (!Utility.isNetworkAvailable(getActivity())) {
            MainActivity.checkConnection(getActivity(), this,resources);
        } else {

            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<DashboardRequestResponse> responseCall = service.getJsonData(PreferenceData.getAppVersion(), PreferenceData.getLoginid(), PreferenceData.getPassword(), PreferenceData.getLatitude(), PreferenceData.getLongitude(),PreferenceData.getAppVersion(),PreferenceData.getLoctime());
            responseCall.enqueue(new Callback<DashboardRequestResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<DashboardRequestResponse> call, Response<DashboardRequestResponse> response) {


                    if (response.isSuccessful()) {


                        DashboardRequestResponse dashboardRequestResponse = response.body();

                        initoffer= dashboardRequestResponse.getInitoffer();


                    // PreferenceData.setEmail(esp.getEmail());



 if(initoffer!=null) {
     if (initoffer.equalsIgnoreCase("1")) {
         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ilmc.xyz/onlook/setoffer.php?initoffer=yes&email=" + PreferenceData.getEmail()));
         startActivity(browserIntent);
     }
     if (initoffer.equalsIgnoreCase("2")) {


         openWhatsApp("0");
     }

 }

                             OnlookApplication.deviceList = (ArrayList<DashboardRequestResponse.Esp>) dashboardRequestResponse.getEsps();
                         bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(false);
                   //     bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                bindingMain.navView.findViewById(R.id.navigation_home).setEnabled(true);
                             //   bindingMain.navView.findViewById(R.id.navigation_home).setVisibility(View.VISIBLE);
                            }
                        }, 5000);


                        if (dashboardRequestResponse.getSuccess() == 1) {




                            DeviceViewSlidePagerAdapter deviceViewSlidePagerAdapter = new DeviceViewSlidePagerAdapter(getActivity());

                            binding.viewPager.setAdapter(deviceViewSlidePagerAdapter);
                            binding.viewPager.setCurrentItem(0, true);
                            binding.viewPager.setOffscreenPageLimit(OnlookApplication.deviceList.size());

                            int currentPageSelected = PreferenceData.getCurrentSelectedPage();
                            binding.viewPager.setCurrentItem(currentPageSelected);

                            DashboardRequestResponse.Esp esp = OnlookApplication.deviceList.get(0);


                            PreferenceData.setEmail(esp.getEmail());


                            if (!OnlookApplication.deviceList.isEmpty()) {


                            if(esp.getPlayupdate().equalsIgnoreCase("1")) {


                                 final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                       getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }






                                bindingMain.switchTheme.setVisibility(View.VISIBLE);
                                bindingMain.txtThemeName.setVisibility(View.VISIBLE);

                                bindingMain.txtUserName.setText(resources.getString(R.string.onlook_cube));

                            }
                            //PreferenceData.setEmail(esp.getEmail());

                            if (esp.getEmail().equalsIgnoreCase(PreferenceData.getEmail()) ||
                                    esp.getRFamEmail().equalsIgnoreCase(PreferenceData.getEmail())) {
                                //upen changed below line to accomidate fam token
                                if(DBG) Log.d("Token bizmessage.in",esp.getRIsPushy());

                                if (esp.getRIsPushy().equalsIgnoreCase("stop") || esp.getRFamIspushy().equalsIgnoreCase("stop")) {
                                    Pushy.unregister(getActivity().getApplicationContext());
                                    PreferenceData.setDeviceToken("");
                                    PreferenceData.setDarkTheme(false);
                                    PreferenceData.setEmail("");
                                    logout();
                                } else if (esp.getRIsPushy().equalsIgnoreCase("reset") || esp.getRFamIspushy().equalsIgnoreCase("reset")) {

                            //        new RegisterForPushNotificationsAsync().execute();
                                    if(PreferenceData.getDeviceToken().isEmpty() && esp.getLwt().equalsIgnoreCase("Online") && !esp.getEmail().equalsIgnoreCase("member@onlook.in")) {
                                     new RegisterForPushNotificationsAsync().execute();
                                 }

                                } else if (esp.getRIsPushy().equalsIgnoreCase("renew") || esp.getRFamIspushy().equalsIgnoreCase("renew")) {

                                            new RegisterForPushNotificationsAsync().execute();
                                 //   if(PreferenceData.getDeviceToken().isEmpty() && esp.getLwt().equalsIgnoreCase("Online") && !esp.getEmail().equalsIgnoreCase("member@onlook.in")) {
                                   //     new RegisterForPushNotificationsAsync().execute();
                                   // }


                                } else if (esp.getRIsPushy().equalsIgnoreCase("false") || esp.getRFamIspushy().equalsIgnoreCase("false")) {
                                    if(DBG) Log.d("Token bizmessage.in","80000000000000");

                                    Pushy.unregister(getActivity().getApplicationContext());
                                    PreferenceData.setDeviceToken("");
                                    updateDeviceToken(false);
                                }
                            }
                            /*  upen
                            else if (esp.getRFamEmail().equalsIgnoreCase(PreferenceData.getEmail())) {
                                if(DBG) Log.d("Token bizmessage.in","80000000000000");
                                if(DBG) Log.d("bizmessage.in getRFAMI","80000000000000"+esp.getRFamEmail());
                                if(DBG) Log.d("bizmessage.in PREF","80000000000000"+PreferenceData.getEmail());
                                if (esp.getRIsPushy().equalsIgnoreCase("stop")) {
                                    Pushy.unregister(getActivity().getApplicationContext());
                                } else if (esp.getRIsPushy().equalsIgnoreCase("reset")) {
                                    new RegisterForPushNotificationsAsync().execute();
                                } else if (esp.getRIsPushy().equalsIgnoreCase("false")) {
                                    Pushy.unregister(getActivity().getApplicationContext());
                                    updateDeviceToken(false);
                                }
                            }
                            */
                            binding.textNoDevice.setVisibility(View.GONE);
                            binding.viewPager.setVisibility(View.VISIBLE);
                            deviceFound("", true);
                            if (((MainActivity) getActivity()).isFromMainActivity == 1) {
                                ((MainActivity) getActivity()).isFromMainActivity = 0;
                                binding.cardAlerts.performClick();
                            }
                            //setThemeData();
                        } else {
                            bindingMain.txtUserName.setText("Welcome Guest");
                            deviceFound(dashboardRequestResponse.getMessage(), false);
                            Utility.showSnackBar(binding.imgDeviceAlert, dashboardRequestResponse.getMessage());
                        }
                    } else {
                        Utility.showSnackBar(binding.imgDeviceAlert, response.message());
                        if (DBG) Log.i(TAG, "onFailure: " + response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<DashboardRequestResponse> call, Throwable t) {
                    if (DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast(resources.getString(R.string.status_website));
                    }
                }




            });

        }

    }

    private void disableClick() {
        binding.cardAlerts.setEnabled(false);
        binding.cardLed.setEnabled(false);
        binding.cardLight1.setEnabled(false);
        binding.cardLight2.setEnabled(false);
        binding.cardSetting.setEnabled(false);
        binding.cardAlertMode.setEnabled(false);
        binding.cardWebView.setEnabled(false);
        binding.cardDeviceStatus.setEnabled(false);
    }

    private void enableClick() {
        binding.cardAlerts.setEnabled(true);
        binding.cardLed.setEnabled(true);
        binding.cardLight1.setEnabled(true);
        binding.cardLight2.setEnabled(true);
        binding.cardSetting.setEnabled(true);
        binding.cardAlertMode.setEnabled(true);
        binding.cardWebView.setEnabled(true);
        binding.cardDeviceStatus.setEnabled(true);
    }

    private void logout(){
       // PreferenceData.clear();
         PreferenceData.setLoginid("");
        Intent intentSettings = new Intent(getActivity(), LoginActivity.class);
                 startActivity(intentSettings);

    }
    private void deviceFound(String message, boolean isFound) {
        isDeviceFound = isFound;
        if (isFound) {
            binding.imgWhatsApp.setVisibility(View.GONE);
            binding.textNoDevice.setVisibility(View.GONE);
            binding.imgLeftArrow.setVisibility(View.VISIBLE);
            binding.imgRightArrow.setVisibility(View.VISIBLE);
            binding.viewPager.setVisibility(View.VISIBLE);
            if (OnlookApplication.deviceList.size() < 2) {
                binding.imgLeftArrow.setVisibility(View.GONE);
                binding.imgRightArrow.setVisibility(View.GONE);
            } else {
                binding.imgLeftArrow.setVisibility(View.VISIBLE);
                binding.imgRightArrow.setVisibility(View.VISIBLE);
            }

        } else {
          binding.textNoDevice.setText(resources.getString(R.string.message_nodevice));

 binding.imgWhatsApp.setVisibility(View.VISIBLE);
            binding.imgLeftArrow.setVisibility(View.GONE);
            binding.imgRightArrow.setVisibility(View.GONE);
            binding.textNoDevice.setVisibility(View.VISIBLE);
            binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.alert_off));

            binding.viewPager.setVisibility(View.GONE);
        }
    }

    private void updateDeviceToken(boolean isRegister) {

        if (!Utility.isNetworkAvailable(getActivity())) {
            Utility.showSnackBar(binding.imgDeviceAlert, getString(R.string.no_internet_connection));
        } else {
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            @SuppressLint("HardwareIds") String mobile_id = "";
            Call<ResponseBody> responseCall = null;
            if (isRegister) {
                responseCall = service.registerPushy(isParent(), mobile_id, "pushy", PreferenceData.getEmail(), PreferenceData.getDeviceToken());
            } else {
                responseCall = service.unRegisterPushy(isParent(), PreferenceData.getEmail());
            }

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                    logout();

                    Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    if (DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast(resources.getString(R.string.status_website));
                    }
                }
            });
        }
    }

    private String isParent() {
        // Check if isFamily Member access or not.

        if (PreferenceData.getEmail().equalsIgnoreCase(OnlookApplication.deviceList.get(0).getEmail())) {
            return "yes";
        }else if (PreferenceData.getEmail().equalsIgnoreCase("oper@onlook.in")) {
            return "yes";
        }
        else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            return "no";
        } else {
            return "no";
        }
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {

        protected Exception doInBackground(Void... params) {
            try {
                if (OnlookApplication.deviceList.get(0).getParent().equalsIgnoreCase("0")) {
                    Pushy.listen(getActivity().getApplicationContext());
                    if (!Pushy.isRegistered(getActivity().getApplicationContext())) {
                        Pushy.toggleFCM(true, getActivity().getApplicationContext());
                        deviceToken = Pushy.register(getActivity().getApplicationContext());
                        // if(DBG) Log.d("TOKEN VALUE UPEN", deviceToken);
                    } else {
                        deviceToken = Pushy.getDeviceCredentials(getActivity().getApplicationContext()).token;
                    }
                    // isFamily = "no";  //upen
                    PreferenceData.setDeviceToken(deviceToken);
             if(DBG) Log.d("UPDATE bizmessage.in","30000000000000");

                 updateDeviceToken(true);

                }
                if (OnlookApplication.deviceList.get(0).getParent().equalsIgnoreCase("1")) {
                    if (!Pushy.isRegistered(getActivity().getApplicationContext())) {
                        Pushy.toggleFCM(true, getActivity().getApplicationContext());
                        deviceToken = Pushy.register(getActivity().getApplicationContext());
                    } else {
                        deviceToken = Pushy.getDeviceCredentials(getActivity().getApplicationContext()).token;
                    }
                    PreferenceData.setDeviceToken(deviceToken);
                    // isFamily = "yes";   //upen
                     if(DBG) Log.d("UPDATE bizmessage.in","40000000000000");
                    updateDeviceToken(true);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                return exc;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            PreferenceData.setDeviceToken(deviceToken);
            if (DBG) Log.i(TAG, "DEVICE TOKEN onPostExecute: " + deviceToken);
        }
    }



}