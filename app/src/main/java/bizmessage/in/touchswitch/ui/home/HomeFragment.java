package bizmessage.in.touchswitch.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import androidx.core.app.ActivityCompat;
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
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.databinding.FragmentHomeBinding;
import bizmessage.in.touchswitch.location.LocationUpdatesService;
import bizmessage.in.touchswitch.model.DashboardRequestResponse;
import bizmessage.in.touchswitch.notification.RingtonePlayingService;
import bizmessage.in.touchswitch.permissionManagerViews.Permission;
import bizmessage.in.touchswitch.permissionManagerViews.PermissionManagerInstance;
import bizmessage.in.touchswitch.permissionManagerViews.PermissionManagerListener;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
//import bizmessage.in.touchswitch.ui.alerts.AlertListActivity;
import bizmessage.in.touchswitch.ui.adddevice.AddJustlight;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
//import bizmessage.in.touchswitch.ui.settings.LedActivity;
import bizmessage.in.touchswitch.ui.settings.TouchDimmer;
import bizmessage.in.touchswitch.ui.settings.TouchSwitch;


//import bizmessage.in.touchswitch.ui.settings.LightTwoActivity;

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

import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchListener;
import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchListener;

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

        // binding.cardAlertMode.setOnClickListener(this);
        // binding.cardAlerts.setOnClickListener(this);
        //     binding.cardLed.setOnClickListener(this);
        binding.cardLight1.setOnClickListener(this);


       // binding.cardWebView.setOnClickListener(this);
       // binding.cardDeviceStatus.setOnClickListener(this);
        binding.imgLeftArrow.setOnClickListener(this);
        binding.imgRightArrow.setOnClickListener(this);
   //    binding.cardSetting.setOnClickListener(this);
        // binding.cardVibration.setOnClickListener(this);
        //binding.cardBuzzer.setOnClickListener(this);


//PreferenceData.setDarkTheme(true);
        //  PreferenceData.setDarkTheme(false);

        if (PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getActivity(), "hi");
            resources = context.getResources();
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getActivity(), "ka");
            resources = context.getResources();
            //   binding.textDeviceAlertModeStatus.setTextSize(9);
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getActivity(), "ta");
            resources = context.getResources();
            // binding.textDeviceAlertMode.setTextSize(14);
          // binding.textDeviceStatus.setTextSize(15);
            //  binding.textDeviceAlertModeStatus.setTextSize(8);

            binding.textNoDevice.setTextSize(14);


        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(getActivity(), "te");
            resources = context.getResources();
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(getActivity(), "bn");
            resources = context.getResources();
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(getActivity(), "ml");
            resources = context.getResources();
            binding.textNoDevice.setTextSize(15);
            //   binding.textDeviceAlertMode.setTextSize(12);
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
        }

        if (PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(getActivity(), "mr");
            resources = context.getResources();
        }

        if (PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getActivity(), "gu");
            resources = context.getResources();
        }

       // binding.imgWhatsApp.setVisibility(View.GONE);
        //  binding.textDeviceAlertMode.setText(resources.getString(R.string.motion_sensor));
        // binding.textDeviceLed.setText(resources.getString(R.string.led));
        //  binding.textDeviceLight1.setText(resources.getString(R.string.light_1));
        //  binding.textDeviceLight2.setText(resources.getString(R.string.light_2));
        // binding.textDeviceLight3.setText(resources.getString(R.string.settings));
        // binding.textDeviceAlert.setText(resources.getString(R.string.alerts));
       // binding.textDeviceStatus.setText(resources.getString(R.string.device_status));
        binding.textDeviceLight1.setText(resources.getString(R.string.light_1));
      //    binding.textSetting.setText(resources.getString(R.string.settings));

        // binding.textDeviceVibration.setText(resources.getString(R.string.vibration));
        //binding.textDeviceBuzzer.setText(resources.getString(R.string.buzzer));
        //  binding.imgDeviceshare.setVisibility(View.GONE);

        DayNightSwitch dayNightSwitch1 = (DayNightSwitch)binding.touchdash;

        dayNightSwitch1.setDuration(450);
        // dayNightSwitch.setIsNight(ThemeSettings.getInstance(this).nightMode);
        dayNightSwitch1.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean touchdash) {

                Utility.showToast(touchdash+" <-TouchDash");
            }
        });



        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                TouchApplication.SELECTED_DEVICE = TouchApplication.deviceList.get(position);
                TouchApplication.currentDevicePosition = position;
                PreferenceData.setCurrentSelectedPage(position);

                //        binding.textDeviceAlertCount.setText(TouchApplication.deviceList.get(position).getAlertsCount());
                if (DBG)
                    Log.d("CURRENT DEVICE POS ", "" + TouchApplication.currentDevicePosition);
                if (DBG)
                    Log.d("CURRENT DEVICE SIZE ", "" + TouchApplication.deviceList.size());
                if (TouchApplication.currentDevicePosition == 0) {
                    binding.imgLeftArrow.setVisibility(View.GONE);
                } else {
                    binding.imgLeftArrow.setVisibility(View.VISIBLE);
                }
                if (TouchApplication.currentDevicePosition == TouchApplication.deviceList.size() - 1) {
                    binding.imgRightArrow.setVisibility(View.GONE);
                } else {
                    binding.imgRightArrow.setVisibility(View.VISIBLE);
                }

                if (TouchApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
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

        getActivity().startService(new Intent(getActivity(), LocationUpdatesService.class));

        getWifiScanList();
        return binding.getRoot();
    }

    private void setAlertMode() {

        if(TouchApplication.SELECTED_DEVICE.getIsshared().length()>5) {

           // binding.imgDeviceshare.setVisibility(View.VISIBLE);

        }
        else{
           // binding.imgDeviceshare.setVisibility(View.GONE);
        }

        if(TouchApplication.SELECTED_DEVICE.getOffer().equalsIgnoreCase("1")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ilmc.xyz/onlook/setoffer.php?email="+PreferenceData.getEmail()));
            startActivity(browserIntent);
        }
        if(TouchApplication.SELECTED_DEVICE.getOffer().equalsIgnoreCase("2")){


            openWhatsApp("1");
        }


        if(  TouchApplication.deviceList.size() >0)
        {
            if(PreferenceData.isDarkTheme()){
                // nothing
            }
            else {
                //   binding.cardSetting.setCardBackgroundColor(Color.parseColor("#fffff0"));

                //    binding.cardAlerts.setCardBackgroundColor(Color.parseColor("#fffff0"));
            }



        }

        if(TouchApplication.SELECTED_DEVICE.getRoomtype().equalsIgnoreCase("My Hall") ) {
    binding.cardLight1.setBackgroundResource(R.drawable.hall1);
    binding.textDeviceLight1.setText(TouchApplication.SELECTED_DEVICE.getRoomtype());
        }

        if(TouchApplication.SELECTED_DEVICE.getRoomtype().equalsIgnoreCase("My Bedroom") ) {
            binding.cardLight1.setBackgroundResource(R.drawable.bedroom1);
            binding.textDeviceLight1.setText(TouchApplication.SELECTED_DEVICE.getRoomtype());
        }


        ////////////////////222222222
        if(TouchApplication.SELECTED_DEVICE.getIslighton().equalsIgnoreCase("on") ) {
      binding.imgDeviceLight1.setImageResource(R.drawable.ledon);
            if(PreferenceData.isDarkTheme()){
             //orig   binding.cardLight1.setCardBackgroundColor(Color.parseColor("#d3d3d3"));
              //  binding.cardLight1.setCardBackgroundColor(Color.parseColor("#000F00"));

            }
            else {
             //orig  binding.cardLight1.setCardBackgroundColor(Color.parseColor("#ffdf32"));
              //  binding.cardLight1.setCardBackgroundColor(Color.parseColor("#000F00"));
            }

        }
        else
        {
            binding.cardLight1.setCardBackgroundColor(getResources().getColor(R.color.card_view_gb));
        }
        /////////////////////////////222222222222






    }

    private void openWhatsApp(String x) {

        WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
        Call<ResponseBody> responseCall = service.resetwhatsappoffer(PreferenceData.getEmail(), x);

        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {
             //       Utility.showSnackBar(binding.cardWebView, response.message());
                }
                Utility.hideProgress();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if (DBG) Log.i(TAG, "onFailure: " + t.toString());
                Utility.hideProgress();
                if (t.toString().contains("UnknownHostException")) {
                    Utility.showToast(resources.getString(R.string.status_website));
                }
            }
        });


        String text;
        String toNumber;
        try {
            if (TouchApplication.SELECTED_DEVICE != null) {


                text = "My Email id is " + PreferenceData.getEmail() + " .My device id : " + TouchApplication.SELECTED_DEVICE.getDevid() + ". My App version is " + PreferenceData.getAppVersion() + " Regarding Offer : ";// Replace with your message.
                if (TouchApplication.SELECTED_DEVICE.getCall911().length() > 8) {
                    toNumber = "91" + TouchApplication.SELECTED_DEVICE.getCall911(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
                } else {//if user is not set to dealer by mistake it will route to support number
                    text = "My support Not Set.My Email id is " + PreferenceData.getEmail() + " .My device id : " + TouchApplication.SELECTED_DEVICE.getDevid() + ". My App version is " + PreferenceData.getAppVersion() + " Regarding Offer : ";// Replace with your message.

                    toNumber = "917900119635"; // Replace with mobile phone number without +Sign or leading zeros, but with country code

                }
            } else { // ffor a new user or demo user
                text = "My Email id is " + PreferenceData.getEmail() + ". My App version is " + PreferenceData.getAppVersion() + " I need Support for : ";// Replace with your message.
                toNumber = "917900119635"; // Replace with mobile phone number without +Sign or leading zeros, but with country code

            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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

            case R.id.card_top_card:
                if (isDeviceFound) {
                Utility.showToast(resources.getString(R.string.message_nodevice));
                Intent intentLight = new Intent(getContext(), AddJustlight.class);
                startActivity(intentLight);

        }
        break;

        case R.id.cardLight1:
                if (isDeviceFound) {
                    if (!TouchApplication.SELECTED_DEVICE.getRIsSwitch().equalsIgnoreCase("true")) {
                        Utility.showToast(resources.getString(R.string.not_ebable));
                        //getActivity().finish();

                    }

                    if(TouchApplication.SELECTED_DEVICE.getShow().equalsIgnoreCase("0")){

                        //Utility.showToast(resources.getString(R.string.message_nodevice));


                        Intent intentLight = new Intent(getActivity(), FirstTimeHotSpotActivity.class);
                        startActivity(intentLight);
                        getActivity().overridePendingTransition(0, 0);

                    }

                    else {
                        if(TouchApplication.SELECTED_DEVICE.getTouchtype().equalsIgnoreCase("touchswitch"))
                        {
                            Intent intentLight = new Intent(getActivity(), TouchSwitch.class);
                            startActivity(intentLight);
                        }
                        if(TouchApplication.SELECTED_DEVICE.getTouchtype().equalsIgnoreCase("dimmer"))
                        {
                            Intent intentDimmer = new Intent(getActivity(), TouchDimmer.class);
                            startActivity(intentDimmer);
                        }

                     //   Intent intentLight = new Intent(getActivity(), RoomDetailsActivity.class);


                        getActivity().overridePendingTransition(0, 0);
                    }

                } else {
                 Utility.showToast(resources.getString(R.string.message_nodevice));
                    Intent intentLight = new Intent(getContext(), AddJustlight.class);
                    startActivity(intentLight);

                }
                break;


         /*
           case R.id.cardDeviceStatus:

                if (isDeviceFound) {
                    Intent intentDeviceStatus = new Intent(getActivity(), MyDeviceActivity.class);
                    startActivity(intentDeviceStatus);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    showNoDeviceFound();
                    Utility.showToast(resources.getString(R.string.message_nodevice));
                    Intent intentLight = new Intent(getContext(), AddJustlight.class);
                    startActivity(intentLight);
                }
                break;
          */
            case R.id.imgLeftArrow:
                if (DBG)
                    Log.d("CURRENT DEVICE POS ", "" + TouchApplication.currentDevicePosition);
                if (DBG)
                    Log.d("CURRENT DEVICE SIZE ", "" + TouchApplication.deviceList.size());

                if (TouchApplication.currentDevicePosition > 0 && TouchApplication.currentDevicePosition < TouchApplication.deviceList.size()) {
                    binding.viewPager.setCurrentItem(TouchApplication.currentDevicePosition - 1);
                }
                break;
            case R.id.imgRightArrow:
                if (DBG)
                    Log.d("CURRENT DEVICE POS ", "" + TouchApplication.currentDevicePosition);
                if (DBG)
                    Log.d("CURRENT DEVICE SIZE ", "" + TouchApplication.deviceList.size());
                if (TouchApplication.currentDevicePosition == 0) {
                    binding.imgRightArrow.setVisibility(View.GONE);
                } else {
                    binding.imgRightArrow.setVisibility(View.VISIBLE);
                }
                if (TouchApplication.currentDevicePosition < TouchApplication.deviceList.size() && TouchApplication.currentDevicePosition >= 0) {
                    binding.viewPager.setCurrentItem(TouchApplication.currentDevicePosition + 1);
                }
                break;
          /*  case R.id.cardSetting:
                if (isDeviceFound) {
                    // if (!PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
                    if (TouchApplication.SELECTED_DEVICE.getLwt().equalsIgnoreCase("planned")) {
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
                   // showNoDeviceFound();
                    Utility.showToast(resources.getString(R.string.message_nodevice));
                    Intent intentLight = new Intent(getContext(), AddJustlight.class);
                    startActivity(intentLight);
                }
                break;
                */

        }
    }

    private void handleFamilyUser() {
        if (PreferenceData.getLoginid().equalsIgnoreCase(TouchApplication.SELECTED_DEVICE.getRFamEmail())) {
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        } else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        } else if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onload.in")) {
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        }
    }

    private void showNoDeviceFound() {
        //  binding.cardSetting.setEnabled(false);
        Utility.showSnackBar(binding.cardLight1, resources.getString(R.string.message_nodevice));


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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
            return DeviceViewFragment.getInstance(position, TouchApplication.deviceList.get(position));
        }

        @Override
        public int getItemCount() {
            return TouchApplication.deviceList.size();
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
         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ilmc.xyz/justlight/setoffer.php?initoffer=yes&email=" + PreferenceData.getEmail()));
         startActivity(browserIntent);
     }
     if (initoffer.equalsIgnoreCase("2")) {


         openWhatsApp("0");
     }

 }

                             TouchApplication.deviceList = (ArrayList<DashboardRequestResponse.Esp>) dashboardRequestResponse.getEsps();
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
                            binding.viewPager.setOffscreenPageLimit(TouchApplication.deviceList.size());

                            int currentPageSelected = PreferenceData.getCurrentSelectedPage();
                            binding.viewPager.setCurrentItem(currentPageSelected);

                            DashboardRequestResponse.Esp esp = TouchApplication.deviceList.get(0);


                            PreferenceData.setEmail(esp.getEmail());


                            if (!TouchApplication.deviceList.isEmpty()) {


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

                                bindingMain.txtUserName.setText(resources.getString(R.string.justlight_cube));

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
                                    if(PreferenceData.getDeviceToken().isEmpty() && esp.getLwt().equalsIgnoreCase("Online") && !esp.getEmail().equalsIgnoreCase("member@onload.in")) {
                                     new RegisterForPushNotificationsAsync().execute();
                                 }

                                } else if (esp.getRIsPushy().equalsIgnoreCase("renew") || esp.getRFamIspushy().equalsIgnoreCase("renew")) {

                                            new RegisterForPushNotificationsAsync().execute();
                                 //   if(PreferenceData.getDeviceToken().isEmpty() && esp.getLwt().equalsIgnoreCase("Online") && !esp.getEmail().equalsIgnoreCase("member@onload.in")) {
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
                             //   binding.cardAlerts.performClick();
                            }
                            //setThemeData();
                        } else {
                            bindingMain.txtUserName.setText("Welcome Guest");
                            deviceFound(dashboardRequestResponse.getMessage(), false);
                            Utility.showSnackBar(binding.imgDeviceLight1, dashboardRequestResponse.getMessage());
                        }
                    } else {
                        Utility.showSnackBar(binding.imgDeviceLight1, response.message());
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
     //   binding.cardAlerts.setEnabled(false);
      //  binding.cardLed.setEnabled(false);
        binding.cardLight1.setEnabled(false);

      //  binding.cardLight2.setEnabled(false);
       // binding.cardSetting.setEnabled(false);
       // binding.cardAlertMode.setEnabled(false);
        //binding.cardWebView.setEnabled(false);
      //  binding.cardDeviceStatus.setEnabled(false);
    }

    private void enableClick() {
      //  binding.cardAlerts.setEnabled(true);
      //  binding.cardLed.setEnabled(true);
        binding.cardLight1.setEnabled(true);
       // binding.cardLight1.setBackgroundColor(android.R.color.black);
      //  binding.cardLight2.setEnabled(true);
   //     binding.cardSetting.setEnabled(true);
      //  binding.cardAlertMode.setEnabled(true);
      //  binding.cardWebView.setEnabled(true);
        //binding.cardDeviceStatus.setEnabled(true);
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
           // binding.imgWhatsApp.setVisibility(View.GONE);
            binding.textNoDevice.setVisibility(View.GONE);
            binding.imgLeftArrow.setVisibility(View.VISIBLE);
            binding.imgRightArrow.setVisibility(View.VISIBLE);
            binding.viewPager.setVisibility(View.VISIBLE);
            if (TouchApplication.deviceList.size() < 2) {
                binding.imgLeftArrow.setVisibility(View.GONE);
                binding.imgRightArrow.setVisibility(View.GONE);
            } else {
                binding.imgLeftArrow.setVisibility(View.VISIBLE);
                binding.imgRightArrow.setVisibility(View.VISIBLE);
            }

        } else {
          binding.textNoDevice.setText(resources.getString(R.string.message_nodevice));

 //binding.imgWhatsApp.setVisibility(View.VISIBLE);
            binding.imgLeftArrow.setVisibility(View.GONE);
            binding.imgRightArrow.setVisibility(View.GONE);
            binding.textNoDevice.setVisibility(View.VISIBLE);
        //    binding.textDeviceAlertModeStatus.setText(resources.getString(R.string.alert_off));

            binding.viewPager.setVisibility(View.GONE);
        }
    }

    private void updateDeviceToken(boolean isRegister) {

        if (!Utility.isNetworkAvailable(getActivity())) {
            Utility.showSnackBar(binding.imgDeviceLight1, getString(R.string.no_internet_connection));
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

        if (PreferenceData.getEmail().equalsIgnoreCase(TouchApplication.deviceList.get(0).getEmail())) {
            return "yes";
        }else if (PreferenceData.getEmail().equalsIgnoreCase("oper@onload.in")) {
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
                if (TouchApplication.deviceList.get(0).getParent().equalsIgnoreCase("0")) {
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
                if (TouchApplication.deviceList.get(0).getParent().equalsIgnoreCase("1")) {
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