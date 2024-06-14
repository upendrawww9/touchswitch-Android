package bizmessage.in.touchswitch.ui.others;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.FragmentOthersBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.ui.help.HelpActivity;
import bizmessage.in.touchswitch.ui.others.youtube.YoutubeHelp;
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
import static bizmessage.in.touchswitch.utils.Utility.DIALOG_LOGOUT_IDENTIFIER;

public class OthersFragment extends Fragment implements DialogButtonClickListener, View.OnClickListener {

    private static final String TAG = OthersFragment.class.getSimpleName();
    FragmentOthersBinding binding;
    final Handler handler = new Handler(Looper.getMainLooper());

    Context context;
    Resources resources;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_others, container, false);


        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(getActivity(), "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(getActivity(), "gu");
            resources = context.getResources();
        }

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
            binding.textBuzz.setTextSize(14);
            binding.textContactUs.setTextSize(14);
            binding.textChangePassword.setTextSize(14);
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
          //  binding.textDownloadReport.setTextSize(13);
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
        }

        if (OnlookApplication.SELECTED_DEVICE != null) {


            binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        }
       // Utility.showToast(OnlookApplication.SELECTED_DEVICE.getDevid());
            binding.textChangePassword.setText(resources.getString(R.string.title_change_user_password));///////////////
        binding.textTryItOut.setText(resources.getString(R.string.family_login));///////////////
        binding.textshare.setText(resources.getString(R.string.share_my_app));///////////////
        binding.textHelp1.setText(resources.getString(R.string.title_emergency_number));///////////////
        binding.textDownloadReport.setText(resources.getString(R.string.title_download_reports));///////////////

        binding.textBuzz.setText(resources.getString(R.string.buzz));///////////////
        binding.textwhatalert.setText(resources.getString(R.string.whatalert));///////////////

        binding.textLan.setText(resources.getString(R.string.language));///////////////
         binding.textHelp.setText(resources.getString(R.string.help));///////////////
       binding.textContactUs.setText(resources.getString(R.string.offers));///////////////
        binding.textAlarm.setText(resources.getString(R.string.title_alarm));/////////
        binding.textnoti.setText(resources.getString(R.string.mobile_noti));/////////
        binding.textyoutube.setText(resources.getString(R.string.youtube_text));/////////
        binding.textsharedevice.setText(resources.getString(R.string.share_my_dev));/////////

        binding.textLogout.setText(resources.getString(R.string.logout));///////////////

        if (PreferenceData.getIsdealer().equalsIgnoreCase("yes")) {}
        else{
            binding.cardCustomer.setVisibility(View.GONE);
            binding.cardWebcust.setVisibility(View.GONE);
        }


       // binding.cardVibrate.setVisibility(View.GONE);

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

        if (OnlookApplication.SELECTED_DEVICE != null){


             binding.cardFamilyMember.setEnabled(true);
            if (OnlookApplication.SELECTED_DEVICE.getShareallow().equalsIgnoreCase("true")) {


                if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getIsshared())) {

                    binding.cardShareDevice.setVisibility(View.GONE);
                }


            }
            else{
                binding.cardShareDevice.setVisibility(View.GONE);

            }

          //  bindingMain.switchTheme.setVisibility(View.GONE);
          //  bindingMain.txtThemeName.setVisibility(View.GONE);
        }else {



            binding.cardnotset.setVisibility(View.GONE);
            binding.cardFamilyMember.setVisibility(View.GONE);
            binding.cardBuzz.setVisibility(View.GONE);
            binding.cardDownloadReports.setVisibility(View.GONE);

            binding.cardEmergencyNumber.setVisibility(View.GONE);
            binding.textDeviceId.setText("Device : Not Found");
            binding.cardFamilyMember.setEnabled(false);
        }


        binding.cardChangePassword.setOnClickListener(this);
        binding.cardFamilyMember.setOnClickListener(this);
        binding.cardHelp.setOnClickListener(this);
        binding.cardContactUs.setOnClickListener(this);
        binding.cardBuzz.setOnClickListener(this);
        binding.cardAlarm.setOnClickListener(this);
        binding.cardnotset.setOnClickListener(this);
        binding.cardShareDevice.setOnClickListener(this);
         binding.cardDownloadReports.setOnClickListener(this);
        binding.cardEmergencyNumber.setOnClickListener(this);
        binding.cardShareApp.setOnClickListener(this);
        binding.cardlanguage.setOnClickListener(this);
        binding.cardYoutube.setOnClickListener(this);
        binding.cardwhatalert.setOnClickListener(this);
        binding.cardCustomer.setOnClickListener(this);
        binding.cardWebcust.setOnClickListener(this);


        binding.cardLogout.setOnClickListener(v -> Utility.showDialog(getContext(), resources.getString(R.string.logout_title),
                resources.getString(R.string.logout_message),
                resources.getString(R.string.button_yes),
                resources.getString(R.string.button_no),
                OthersFragment.this, DIALOG_LOGOUT_IDENTIFIER, null));

        if (OnlookApplication.SELECTED_DEVICE != null) {
          //  handleFamilyUser();
        }
        else{
            binding.cardFamilyMember.setVisibility(View.GONE);
            binding.cardBuzz.setVisibility(View.GONE);
            binding.cardDownloadReports.setVisibility(View.GONE);
            binding.cardShareDevice.setVisibility(View.GONE);
            binding.cardEmergencyNumber.setVisibility(View.GONE);

        }



        if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            binding.cardChangePassword.setVisibility(View.GONE);
            binding.cardFamilyMember.setVisibility(View.GONE);
            binding.cardBuzz.setVisibility(View.GONE);
            binding.cardDownloadReports.setVisibility(View.GONE);
            binding.cardFamilyMember.setVisibility(View.GONE);


        }else if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){
            bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);



        }

       // Utility.showToast(OnlookApplication.SELECTED_DEVICE.getIsshared());

        if (OnlookApplication.SELECTED_DEVICE != null) {
            if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getIsshared())) {


               binding.cardShareDevice.setVisibility(View.GONE);
               binding.cardChangePassword.setVisibility(View.GONE);

            }
            handleFamilyUser();
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.checkConnection(getActivity(),this,resources);
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        if (dialogIdentifier == Utility.DIALOG_DOWNLOAD_REPORT_IDENTIFIER) {
            String urlDownloadReport = WebUtility.BASE_URL + WebUtility.DOWNLOAD_REPORT + "devid=" + PreferenceData.getDeviceToken() +
                    "&nik=" + PreferenceData.getNickName() + "&email=" + PreferenceData.getEmail();
            Intent intentDownloadReport = new Intent(Intent.ACTION_VIEW);
            intentDownloadReport.setData(Uri.parse(urlDownloadReport));
            startActivity(intentDownloadReport);
        }else if (dialogIdentifier == DIALOG_LOGOUT_IDENTIFIER){
            //PreferenceData.clear();
            if (OnlookApplication.SELECTED_DEVICE != null) {
                if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getIsshared())) {


                  PreferenceData.clear();
                 PreferenceData.setLogin(false);

                }
            }

            PreferenceData.setLogin(false);
//            OnlookApplication.SELECTED_DEVICE.setDevid("");
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
        if (dialogIdentifier == DIALOG_LOGOUT_IDENTIFIER){

        }else if (dialogIdentifier == Utility.DIALOG_DOWNLOAD_REPORT_IDENTIFIER) {

        }else{
            openWifiSettings();
        }
    }

    private void handleFamilyUser(){
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
            binding.cardShareDevice.setVisibility(View.GONE);
            binding.cardFamilyMember.setVisibility(View.GONE);
            MainActivity.bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
        }else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)){
            binding.cardShareDevice.setVisibility(View.GONE);
            binding.cardFamilyMember.setVisibility(View.GONE);
            MainActivity.bindingMain.navView.findViewById(R.id.navigation_add_device).setEnabled(false);
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

    private void setBuzz() {
        if (!Utility.isNetworkAvailable(getActivity())) {
            Utility.showSnackBar(binding.cardBuzz, getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(getActivity());
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.getBuzz(PreferenceData.getEmail());
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                    } else {
                        Utility.showSnackBar(binding.cardBuzz, response.message());
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

    private void showNoDeviceFound() {
        //  binding.cardSetting.setEnabled(false);
      //  Utility.showSnackBar(binding.cardAlerts, getString(R.string.device_not_found));
        Utility.showErrorLong(resources.getString(R.string.message_nodevice));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardChangePassword:
                if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){

                     Utility.showToast(resources.getString(R.string.not_for_demo));
                }
                else {
                    Intent intentUserAccount = new Intent(getActivity(), UserAccountActivity.class);
                    startActivity(intentUserAccount);
                    getActivity().overridePendingTransition(0, 0);
                }
                break;

            case R.id.cardYoutube:
                Intent intentUserAccount = new Intent(getActivity(), YoutubeHelp.class);
                startActivity(intentUserAccount);
                getActivity().overridePendingTransition(0, 0);

                break;
            case R.id.cardShareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Please look at the app I installed to gaurd my house, Onlook - Mobile Personal Security Gaurd App at: \n https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()+"\n\n For more details visit \n https://www.bizmessage.in.");
                sendIntent.setType("text/plain");
                getActivity().startActivity(sendIntent);
                break;

            case R.id.cardwhatalert:
                if (OnlookApplication.SELECTED_DEVICE != null) {

                    if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")) {
                        Utility.showToast(resources.getString(R.string.not_for_demo));
                    } else {
                        Intent intnt = new Intent(getActivity(), Whatsalert.class);
                        startActivity(intnt);
                        getActivity().overridePendingTransition(0, 0);
                    }
                }

                break;

            case R.id.cardAlarm:
                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, "Onlook Alarm");

                startActivity(i);
                break;

            case R.id.cardnotset:
                Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName())
                        .putExtra(Settings.EXTRA_CHANNEL_ID, 99);
                startActivity(settingsIntent);

                break;


            case R.id.cardFamilyMember:

                if (OnlookApplication.SELECTED_DEVICE != null) {

                    if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){
                        Utility.showToast(resources.getString(R.string.not_for_demo));
                    }

                    else {
                        Intent intentFamilyLogin = new Intent(getActivity(), FamilyMemberActivity.class);
                        startActivity(intentFamilyLogin);
                        getActivity().overridePendingTransition(0, 0);
                    }



                }
                else{
                    showNoDeviceFound();
                }

                break;


            case R.id.cardCustomer:

                if (PreferenceData.getIsdealer().equalsIgnoreCase("yes")) {



                        Intent intentShareDevice = new Intent(getActivity(), AddCustomer.class);
                        startActivity(intentShareDevice);
                        getActivity().overridePendingTransition(0, 0);




                }
                else{
                    showNoDeviceFound();
                }

                break;

            case R.id.cardWebcust:

                if (PreferenceData.getIsdealer().equalsIgnoreCase("yes")) {



                    Intent intentShareDevice = new Intent(getActivity(), Dealerclients.class);
                    startActivity(intentShareDevice);
                    getActivity().overridePendingTransition(0, 0);




                }
                else{
                    showNoDeviceFound();
                }

                break;


            case R.id.cardShareDevice:

                if (OnlookApplication.SELECTED_DEVICE != null) {

                    if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){
                        Utility.showToast(resources.getString(R.string.not_for_demo));
                    }

                    else {
                        Intent intentShareDevice = new Intent(getActivity(), ShareDevice.class);
                        startActivity(intentShareDevice);
                        getActivity().overridePendingTransition(0, 0);
                    }



                }
                else{
                    showNoDeviceFound();
                }

                break;


            case R.id.cardEmergencyNumber:
                if (OnlookApplication.SELECTED_DEVICE != null) {

                    if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){
                        Utility.showToast(resources.getString(R.string.not_for_demo));

                    }
                    else {

                        Intent intentEmergency = new Intent(getActivity(), EmergencyCalling.class);
                        startActivity(intentEmergency);
                        getActivity().overridePendingTransition(0, 0);
                    }

                    }
                else{
                    showNoDeviceFound();
                }

                break;
            case R.id.cardHelp:
                Intent intentHelpActivity = new Intent(getActivity(), HelpActivity.class);
                startActivity(intentHelpActivity);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.cardContactUs:
                Intent intentContactUs = new Intent(getActivity(),ContactUsActivity.class);
                startActivity(intentContactUs);
                getActivity().overridePendingTransition(0,0);
                break;

            case R.id.cardlanguage:
                Intent intentLanguage = new Intent(getActivity(),Language.class);
                startActivity(intentLanguage);
                getActivity().overridePendingTransition(0,0);
                break;



            case R.id.cardBuzz:


                binding.cardBuzz.setEnabled(false);
                binding.cardBuzz.setCardBackgroundColor(Color.GRAY);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        binding.cardBuzz.setEnabled(true);
                        binding.cardBuzz.setCardBackgroundColor(Color.WHITE);

                        //Do something after 100ms
                    }
                }, 2500);

                if (OnlookApplication.SELECTED_DEVICE != null) {

                    if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){
                        Utility.showToast(resources.getString(R.string.not_for_demo));

                    }
                    else {

                        setBuzz();
                    }
                }
                else{
                    showNoDeviceFound();
                }

                break;
            case R.id.cardDownloadReports:
                if (OnlookApplication.SELECTED_DEVICE != null) {

                    if (PreferenceData.getLoginid().equalsIgnoreCase("demo@onlook.in")){
                        Utility.showToast(resources.getString(R.string.not_for_demo));

                    }
                    else {
                        Utility.showDialog(getActivity(), resources.getString(R.string.title_confirm_download_report)
                                , resources.getString(R.string.dialog_message), resources.getString(R.string.button_yes)
                                , resources.getString(R.string.button_no), this,
                                Utility.DIALOG_DOWNLOAD_REPORT_IDENTIFIER, "");





                    }
                    }
                else{
            showNoDeviceFound();
        }

                break;
        }
    }

    public void checkConnection(Context context, DialogButtonClickListener listener) {
        if (!Utility.isNetworkAvailable(context)) {
            Utility.showDialog(context, context.getResources().getString(R.string.no_internet_title),
                    context.getResources().getString(R.string.no_internet),
                    context.getResources().getString(R.string.button_try_again),
                    context.getResources().getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);
        }
    }
}