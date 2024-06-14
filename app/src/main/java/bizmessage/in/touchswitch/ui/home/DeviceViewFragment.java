package bizmessage.in.touchswitch.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.databinding.FragmentDeviceViewBinding;
import bizmessage.in.touchswitch.model.DashboardRequestResponse;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.ui.settings.WifiAndHotSpotActivity;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class DeviceViewFragment extends Fragment implements DialogButtonClickListener, View.OnClickListener {

    FragmentDeviceViewBinding binding;
    DashboardRequestResponse.Esp espDetails = null;
    Context context;
    Resources resources;
    int vcc;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_device_view, container, false);

        espDetails = new DashboardRequestResponse().new Esp();
        espDetails = (DashboardRequestResponse.Esp) getArguments().getSerializable("DEVICE");


         if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getActivity(), "hi");
            resources = context.getResources();
                   }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getActivity(), "ka");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getActivity(), "ta");
            resources = context.getResources();
            binding.textDevicePower.setTextSize(15);
            binding.textDeviceStatus.setTextSize(12);
            binding.textDeviceStatusInfo.setTextSize(12);
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


        binding.textDeviceId.setText(resources.getString(R.string.device_id));
        binding.textDeviceName.setText(resources.getString(R.string.device_name));
       binding.textDeviceStatus.setText(resources.getString(R.string.onlook_status));
      binding.textDevicePower.setText(resources.getString(R.string.wifi_strength));
        binding.btnSetup.setText(resources.getString(R.string.wifi_planned_desc));
        binding.textBatPower.setText(resources.getString(R.string.battery_strength));




        if(DBG)Log.d("FRAME CHIN","DD"+espDetails.getLwt());

        Glide.with(getActivity())
                .load(WebUtility.BASE_URL+espDetails.getImage())
                .placeholder(R.drawable.img6)
                .error(R.drawable.img6)
                .into(binding.imgDevice);
        try {
            binding.textDeviceIdInfo.setText(espDetails.getDevid().split("-")[1]);
        }catch (ArrayIndexOutOfBoundsException axp){}
        if(espDetails.getNikname().length()>8) {
            binding.textDeviceNameInfo.setText(espDetails.getNikname().substring(0, 8));

        }
        else
        {
            binding.textDeviceNameInfo.setText(espDetails.getNikname());

        }

        if (espDetails.getLwt().equalsIgnoreCase("Online")){
           // binding.textStatus.setTextColor(Color.parseColor("#246024"));
            binding.textDeviceStatusInfo.setTextColor(Color.parseColor("#FF00C853"));
            binding.textDeviceStatusInfo.setText(resources.getString(R.string.online));


            binding.btnSetup.setVisibility(View.GONE);
        }else if (espDetails.getLwt().equalsIgnoreCase("planned")){
           // binding.textDevicePower.setVisibility(View.VISIBLE);
            binding.textDeviceStatusInfo.setText(resources.getString(R.string.planned));
            binding.textDeviceName.setVisibility(View.GONE);
            binding.textDeviceNameInfo.setVisibility(View.GONE);
            binding.textBatPower.setVisibility(View.GONE);
            binding.textBatPowerInfo.setVisibility(View.GONE);

            binding.textDeviceStatus.setVisibility(View.GONE);
            binding.textDeviceStatusInfo.setVisibility(View.GONE);

            binding.textDevicePower.setVisibility(View.GONE);
            binding.textDevicePowerInfo.setVisibility(View.GONE);

            binding.imgDevice.setVisibility(View.GONE);

            binding.btnSetup.setVisibility(View.VISIBLE);


            binding.textDeviceStatusInfo.setTextColor(getResources().getColor(R.color.red));
        }else {
            binding.textDevicePower.setVisibility(View.GONE);
            binding.textDevicePowerInfo.setVisibility(View.GONE);

           // binding.textBatPower.setVisibility(View.GONE);
           // binding.textBatPowerInfo.setVisibility(View.GONE);


            binding.textDeviceStatusInfo.setText(resources.getString(R.string.offline));



            binding.btnSetup.setVisibility(View.GONE);
            binding.textDeviceStatusInfo.setTextColor(getResources().getColor(R.color.red));
        }

        binding.btnSetup.setOnClickListener(this);

    vcc= Integer.parseInt(espDetails.getVcc());
//Utility.showToast(espDetails.getVcc());


             binding.textDevicePowerInfo.setText(espDetails.getRDevWifi()+" %");
        binding.textBatPowerInfo.setText(espDetails.getVcc()+" %");


        if(vcc > 90 ) {
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#FF00C853"));
        }
        if(vcc > 70 & vcc< 91) {
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#FF00C853"));
        }
        if(vcc > 60 & vcc< 71) {
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#FF00C853"));
        }
        if(vcc > 50 & vcc< 61) {
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#ffae42"));
                    }

        if(vcc > 40 & vcc< 51) {
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#ffae42"));
        }

        if(vcc > 5 & vcc< 41) {
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#cb410b"));
                    }

        if(vcc > 1 & vcc< 6) {
            binding.textBatPowerInfo.setText("5%");
            binding.textBatPowerInfo.setTextColor(Color.parseColor("#cb410b"));
        }

        if(vcc == 500) {
            binding.textBatPowerInfo.setVisibility(View.GONE);
            binding.textBatPower.setVisibility(View.GONE);

        }


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.checkConnection(getActivity(),this,resources);
    }

    public static DeviceViewFragment getInstance(int position, DashboardRequestResponse.Esp esp){
        DeviceViewFragment deviceViewFragment = new DeviceViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        bundle.putSerializable("DEVICE",esp);
        deviceViewFragment.setArguments(bundle);
        return deviceViewFragment;
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        if (dialogIdentifier == Utility.DIALOG_LOGOUT_IDENTIFIER){
           // PreferenceData.clear();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(0,0);
        }else{
            MainActivity.checkConnection(getActivity(),this,resources);
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == Utility.DIALOG_LOGOUT_IDENTIFIER){

        }else{
            openWifiSettings();
        }
    }

    private void openWifiSettings() {
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_setup:
                Intent intentSettings = new Intent(getActivity(), WifiAndHotSpotActivity.class);
                intentSettings.putExtra("IS_FROM",1);
                startActivity(intentSettings);
                getActivity().overridePendingTransition(0,0);
                break;
        }
    }
}