package bizmessage.in.touchswitch.ui.adddevice;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.databinding.FragmentAddDeviceBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.ui.auth.SplashActivity;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class AddDeviceFragment extends Fragment  {

    private final String TAG = AddDeviceFragment.class.getSimpleName();
    private AddDeviceViewModel addDeviceViewModel;
    FragmentAddDeviceBinding binding;
    int PERMISSION_ALL = 1;
    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    Context context;
    Resources resources;
    boolean GpsStatus ;
    String[] PERMISSIONS = {

            Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (PreferenceData.getLoginid().equalsIgnoreCase("demo@justlight.in")) {
            getActivity().finish();
        }

        if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {

            getActivity().finish();
        }
        CheckGpsStatus();
        addDeviceViewModel =
                new ViewModelProvider(this).get(AddDeviceViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_device, container, false);


        if (PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getActivity(), "hi");
            resources = context.getResources();
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getActivity(), "ka");
            resources = context.getResources();
        }
        if (PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getActivity(), "ta");
            resources = context.getResources();
            binding.textDevice.setTextSize(20);///////////////
            binding.textAddDevice.setTextSize(20);///////////////
            binding.btnScanBarCode.setTextSize(20);///////////////
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
            binding.textDevice.setTextSize(20);///////////////
            binding.textAddDevice.setTextSize(20);///////////////
            binding.btnScanBarCode.setTextSize(20);///////////////

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
        binding.textDevice.setText(resources.getString(R.string.newdev_add_head));///////////////
        binding.textAddDevice.setText(resources.getString(R.string.scan_description));///////////////
        binding.btnScanBarCode.setText(resources.getString(R.string.newdev_add_head));///////////////
     //   binding.btnaddBarCode.setText(resources.getString(R.string.wifi_btn_submit));///////////////

       // binding.btnaddBarCode.setVisibility(View.GONE);
       // binding.edtDevid.setVisibility(View.GONE);

        if (TouchApplication.SELECTED_DEVICE != null) {
            if (!PreferenceData.getEmail().equals(TouchApplication.SELECTED_DEVICE.getEmail())) {
                Utility.showToast("Not for family member");
                //   binding.btnScanBarCode.setVisibility(View.GONE);
            }
            if (!PreferenceData.getEmail().equals(TouchApplication.SELECTED_DEVICE.getEmail())) {
                Utility.showToast("Not for family member");
                binding.btnScanBarCode.setVisibility(View.GONE);
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        binding.btnScanBarCode.setOnClickListener(v -> {



            Intent intent = new Intent(getActivity(), AddJustlight.class);
            startActivity(intent);
            //finish();

    });



        if (TouchApplication.SELECTED_DEVICE != null) {
           // showDevicePreviewSlider();
        }


        return binding.getRoot();
    }

    public void CheckGpsStatus(){
        LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus) {
            // Utility.showToast("GPS Enabaled");

        } else {
            Utility.showToast("Enable GPS for Wifi Network Scan");
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showDevicePreviewSlider() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_slider_add_device, null);
        Dialog popupWindow = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupWindow.getWindow().setBackgroundDrawableResource(R.color.transparent3);

        ViewPager2 viewPager2 = layout.findViewById(R.id.viewPagerAddDevice);
        WormDotsIndicator wormDotsIndicator = layout.findViewById(R.id.dots_indicator);
        Button btnOkGotIt = layout.findViewById(R.id.btnOkGotIt);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

            }
        });
try {
    AddDeviceViewSlidePagerAdapter deviceViewSlidePagerAdapter = new AddDeviceViewSlidePagerAdapter(getActivity());
    viewPager2.setAdapter(deviceViewSlidePagerAdapter);
    viewPager2.setCurrentItem(0, true);
    viewPager2.setOffscreenPageLimit(TouchApplication.deviceList.size());
    wormDotsIndicator.setViewPager2(viewPager2);

    btnOkGotIt.setOnClickListener(v -> popupWindow.dismiss());
}catch(IllegalArgumentException ire){}
        popupWindow.setContentView(layout);
        popupWindow.setCancelable(false);
        popupWindow.show();
    }

    private class AddDeviceViewSlidePagerAdapter extends FragmentStateAdapter {
        public AddDeviceViewSlidePagerAdapter(FragmentActivity fa) {
            super(fa);

        }

        @Override
        public Fragment createFragment(int position) {
            return SliderAddDeviceViewFragment.getInstance(position);
        }

        @Override
        public int getItemCount() {
            return 6;
        } // to add slide frames number
    }

    @Override
    public void onResume() {
        super.onResume();
       // MainActivity.checkConnection(getActivity(), this,resources);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //scan have an error
                Utility.showSnackBar(binding.btnScanBarCode, resources.getString(R.string.not_proper_device));
 //               binding.btnaddBarCode.setVisibility(View.VISIBLE);
                binding.btnScanBarCode.setVisibility(View.GONE);
                binding.textAddDevice.setVisibility(View.GONE);
   //             binding.edtDevid.setVisibility(View.VISIBLE);
            } else {
                //scan is successful
                if (DBG) Log.i(TAG, "onActivityResult: " + result);
                String contents = "";
                try {
                    contents = data.getStringExtra("SCAN_RESULT");
                    if (contents.length() > 3) {
       //                 addDevice(contents);
                    } else {
                        Utility.showSnackBar(binding.btnScanBarCode,  resources.getString(R.string.not_proper_device));
                   //     binding.btnaddBarCode.setVisibility(View.VISIBLE);
                        binding.btnScanBarCode.setVisibility(View.GONE);
                        binding.textAddDevice.setVisibility(View.GONE);
                     //   binding.edtDevid.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException exception) {
                    Utility.showSnackBar(binding.btnScanBarCode,  resources.getString(R.string.not_proper_device));
                   // binding.btnaddBarCode.setVisibility(View.VISIBLE);
                    binding.btnScanBarCode.setVisibility(View.GONE);
                    binding.textAddDevice.setVisibility(View.GONE);
                  //  binding.edtDevid.setVisibility(View.VISIBLE);
                    exception.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
/*
    private void addDevice(String deviceSSID) {
        if (!Utility.isNetworkAvailable(getActivity())) {
            Utility.showSnackBar(binding.btnScanBarCode,  resources.getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(getActivity());
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.addDevice(PreferenceData.getLoginid(),
                    PreferenceData.getLatitude(),
                    PreferenceData.getLongitude(),
                    PreferenceData.getLoctime(),
                    "Light-" + deviceSSID,
                    deviceSSID);
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {


                        try {
                            ResponseBody responseBody =response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_NEWDEVICE)) {
                                Utility.showToast(getString(R.string.message_device_add));
                                 Intent intent = new Intent(getActivity(), LoginActivity.class);
                                 startActivity(intent);
                                 getActivity().finish();
                                 getActivity().overridePendingTransition(0, 0);
                            } else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_COMPLETE)) {
                                Utility.showSnackBar(binding.imgAddDevice,getString(R.string.message_complete));
                            } else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_DUPDEVICE)) {
                                Utility.showSnackBar(binding.imgAddDevice,  "Duplicate device");

                            }else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_NODEVICE)) {
                                Utility.showSnackBar(binding.imgAddDevice,  resources.getString(R.string.message_nodevice));

                            }

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    else {
                        Utility.showSnackBar(binding.imgAddDevice, response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast(resources.getString(R.string.status_website));
                    }
                }
            });
        }
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        MainActivity.checkConnection(getActivity(), this,resources);
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {
        openWifiSettings();
    }
*/
    private void openWifiSettings() {
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}