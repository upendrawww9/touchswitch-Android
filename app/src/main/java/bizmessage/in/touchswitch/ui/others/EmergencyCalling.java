package bizmessage.in.touchswitch.ui.others;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.databinding.ActivityTryItOutBinding;
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

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class EmergencyCalling extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = EmergencyCalling.class.getSimpleName();
    ActivityTryItOutBinding binding;
    private final String isFamily = "no";
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_try_it_out);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.title_emergency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.cardHelp.setOnClickListener(this);
        binding.txtCallNow.setOnClickListener(this);


        binding.txtCallEdit.setOnClickListener(this);
        if (TouchApplication.SELECTED_DEVICE != null) {
            binding.edtEmergencyNumber.setText(TouchApplication.SELECTED_DEVICE.getCall911());
         }



        if (PreferenceData.getLoginid().equalsIgnoreCase(TouchApplication.SELECTED_DEVICE.getRFamEmail())) {
            binding.txtCallEdit.setEnabled(false);
        }
        if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)) {
            binding.txtCallEdit.setEnabled(false);
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

        if (TouchApplication.SELECTED_DEVICE != null) {
            binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + TouchApplication.SELECTED_DEVICE.getNikname()+" : "+TouchApplication.SELECTED_DEVICE.getSsid());
        }

        getSupportActionBar().setTitle(resources.getString(R.string.title_emergency));

        binding.textHelp.setText(resources.getString(R.string.title_emergency_number));///////////////

        binding.edtEmergencyNumber.setHint(resources.getString(R.string.hint_emergency_number));///////////////
        binding.txtCallEdit.setText(resources.getString(R.string.button_edit));///////////////
        binding.txtCallNow.setText(resources.getString(R.string.button_call_now));///////////////



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.txtCallEdit:

                if (binding.edtEmergencyNumber.isEnabled()) {
                    binding.edtEmergencyNumber.setEnabled(false);

                    binding.txtCallEdit.setText(resources.getString(R.string.button_edit));
                    binding.txtCallEdit.setEnabled(false);
                    binding.txtCallEdit.setTextColor(Color.GRAY);
                    binding.edtEmergencyNumber.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white1));

                    if (binding.edtEmergencyNumber.getText().toString().trim().isEmpty()|| binding.edtEmergencyNumber.getText().length()< 10){
                        Utility.showSnackBar(binding.edtEmergencyNumber,resources.getString(R.string.valid_emergency_number));
                    }else{
                        updateSettings(isFamily,"emer",binding.edtEmergencyNumber.getText().toString().trim(),false);

                    }
                } else {
                    binding.edtEmergencyNumber.setEnabled(true);
                    binding.edtEmergencyNumber.requestFocus();


                    binding.txtCallEdit.setText(resources.getString(R.string.button_save_now));
                    binding.edtEmergencyNumber.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white3));
                    //  binding.edtEmergencyNumber.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white1));
                    binding.edtEmergencyNumber.setTextColor(Color.BLACK);
                    //xx` binding.edtEmergencyNumber.setBackgroundColor(Color.);
                }
                break;
            case R.id.txtCallNow:
                if (binding.edtEmergencyNumber.getText().toString().trim().isEmpty()){
                    Utility.showSnackBar(binding.edtEmergencyNumber,resources.getString(R.string.valid_emergency_number));
                }else{
                     callNow();

                }
                break;
        }
    }

    private void callNow() {
        try {
            Uri number1 = Uri.parse("tel:" + binding.edtEmergencyNumber.getText().toString().trim());
            Intent callIntent1 = new Intent(Intent.ACTION_DIAL, number1);
            startActivity(callIntent1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void updateSettings(String isFamily, String status,String callNo, boolean isChecked) {
        if (!Utility.isNetworkAvailable(EmergencyCalling.this)) {
            Utility.showSnackBar(binding.appBarLayout,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(EmergencyCalling.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.updateEmergencyCallSettings(isFamily, status,callNo, PreferenceData.getEmail(), isChecked);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Utility.showSnackBar(binding.edtEmergencyNumber, resources.getString(R.string.message_emergency_no_success));


                    } else {
                        Utility.showSnackBar(binding.appBarLayout,response.message());
                    }
                    Utility.hideProgress();
                    Utility.showToast(resources.getString(R.string.message_emergency_no_success));
                    Intent intent = new Intent(EmergencyCalling.this, MainActivity.class);
                    startActivity(intent);
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

    private void userChangePassword(String newPassword) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.appBarLayout,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(EmergencyCalling.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.changePassword(PreferenceData.getEmail(), newPassword, WebUtility.CHANGE_PASSWORD_OPS);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                           /* if (responseStatus.contains("sucesses")) {

                            } else if (responseStatus.contains("failure")) {
                                Utility.showError(getString(R.string.message_login_fail));
                            }*/
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        Utility.showSnackBar(binding.appBarLayout,response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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
        checkConnection(EmergencyCalling.this,this);
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier, Object data) {
        if (dialogIdentifier == Utility.DIALOG_DOWNLOAD_REPORT_IDENTIFIER) {
            String urlDownloadReport = WebUtility.BASE_URL + WebUtility.DOWNLOAD_REPORT + "devid=" + PreferenceData.getDeviceToken() +
                    "&nik=" + PreferenceData.getNickName() + "&email=" + PreferenceData.getEmail();
            Intent intentDownloadReport = new Intent(Intent.ACTION_VIEW);
            intentDownloadReport.setData(Uri.parse(urlDownloadReport));
            startActivity(intentDownloadReport);
        }else {
            checkConnection(EmergencyCalling.this,this);
        }
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
            Utility.showDialog(context, context.getResources().getString(R.string.no_internet_title),
                    context.getResources().getString(R.string.no_internet),
                    context.getResources().getString(R.string.button_try_again),
                    context.getResources().getString(R.string.button_setting),
                    listener, Utility.DIALOG_OFFLINE_IDENTIFIER, null);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}