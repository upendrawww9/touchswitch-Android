package bizmessage.in.touchswitch.ui.adddevice;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.databinding.ActivityAddlightBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.ui.others.Language;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import bizmessage.in.touchswitch.utils.Validation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddJustlight extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    ActivityAddlightBinding binding;
    private static final String TAG = Language.class.getSimpleName();
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addlight);
        setSupportActionBar(binding.toolbar);
   //     getSupportActionBar().setTitle(R.string.title_change_user_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnAdddevice.setOnClickListener(this);



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
           // binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + TouchApplication.SELECTED_DEVICE.getNikname()+" : "+TouchApplication.SELECTED_DEVICE.getSsid());
        }
        binding.textAlertGroup.setText(resources.getString(R.string.newdev_add_head));///////////////
        binding.btnAdddevice.setText(resources.getString(R.string.newdev_add_head));///////////////
        binding.txtssid.setText(resources.getString(R.string.ssidonbox));///////////////
        binding.txthotspotpasswd.setText(resources.getString(R.string.passwdonbox));///////////////
        binding.txtssidd.setText(resources.getString(R.string.passwdonbox));///////////////


        //binding.edtSsid.setHint(resources.getString(R.string.newdev_add_head));///////////////
      //  binding.edtWifipassword.setHint(resources.getString(R.string.confirm_password));///////////////
        getSupportActionBar().setTitle(resources.getString(R.string.newdev_add_head));




    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdddevice) {
            if (!Validation.isRequiredField(binding.edtSsid.getText().toString().trim())) {
                binding.edtSsid.setError(resources.getString(R.string.ssidonbox));
                Utility.showToast(resources.getString(R.string.ssidonbox));
            } else if (!Validation.isValidPassword(binding.edtWifipassword.getText().toString().trim())) {
                binding.edtWifipassword.setError(resources.getString(R.string.passwdonbox));
                Utility.showToast(resources.getString(R.string.passwdonbox));
            }else {
                addDevice();
            }
        }else  if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(AddJustlight.this,this);
    }

    private void addDevice() {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.btnAdddevice,  resources.getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.addDevice(PreferenceData.getLoginid(),
                    PreferenceData.getPassword(),
                    PreferenceData.getLatitude(),
                    PreferenceData.getLongitude(),
                    PreferenceData.getLoctime(),
                    binding.edtSsid.getText().toString().trim(),
                    binding.edtWifipassword.getText().toString().trim()
                     );
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {


                        try {
                            ResponseBody responseBody =response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_NEWDEVICE)) {
                                Utility.showToast(getString(R.string.message_device_add));
                                Intent intent = new Intent(context, LoginActivity.class);
                                startActivity(intent);
                               finish();
                               //this.overridePendingTransition(0, 0);
                            } else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_COMPLETE)) {
                              //  Utility.showSnackBar(binding.imgAddDevice,getString(R.string.message_complete));
                            } else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_DUPDEVICE)) {
                                Utility.showSnackBar(binding.imgAlertGroup,  "Duplicate device");

                            }else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_NODEVICE)) {
                                Utility.showSnackBar(binding.imgAlertGroup,  resources.getString(R.string.message_nodevice));

                            }

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    else {
                        Utility.showSnackBar(binding.imgAlertGroup, response.message());
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