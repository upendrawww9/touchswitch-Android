package bizmessage.in.touchswitch.ui.others;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.databinding.ActivityUserAccountBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import bizmessage.in.touchswitch.utils.Validation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    ActivityUserAccountBinding binding;

    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_account);
        setSupportActionBar(binding.toolbar);
   //     getSupportActionBar().setTitle(R.string.title_change_user_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnChangePassword.setOnClickListener(this);



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
        binding.textAlertGroup.setText(resources.getString(R.string.user_account));///////////////
        binding.btnChangePassword.setText(resources.getString(R.string.change_password));///////////////
        binding.edtPassword.setHint(resources.getString(R.string.password));///////////////
        binding.edtConfirmPassword.setHint(resources.getString(R.string.confirm_password));///////////////
        getSupportActionBar().setTitle(resources.getString(R.string.change_password));



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnChangePassword) {
            if (!Validation.isRequiredField(binding.edtPassword.getText().toString().trim())) {
                binding.edtPassword.setError(getString(R.string.please_enter_password));
            } else if (!Validation.isValidPassword(binding.edtPassword.getText().toString().trim())) {
                binding.edtPassword.setError(getString(R.string.please_enter_valid_password));
            } else if (!Validation.isRequiredField(binding.edtConfirmPassword.getText().toString().trim())) {
                binding.edtConfirmPassword.setError(getString(R.string.please_enter_confirm_password));
            } else if (!binding.edtPassword.getText().toString().trim().equalsIgnoreCase(binding.edtConfirmPassword.getText().toString())) {
                binding.edtConfirmPassword.setError(getString(R.string.password_and_confirm_password_not_match));
            } else {
                userChangePassword(binding.edtConfirmPassword.getText().toString().trim());
            }
        }else  if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(UserAccountActivity.this,this);
    }

    private void userChangePassword(String newPassword) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.appBarLayout,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(UserAccountActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.changePassword(PreferenceData.getEmail(), newPassword , WebUtility.CHANGE_PASSWORD_OPS);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody =response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.contains(AppConstant.API_STATUS_SUCCESS)) {
                                PreferenceData.setPassword(binding.edtPassword.getText().toString().trim());

                                Intent intentUserAccount = new Intent(UserAccountActivity.this, LoginActivity.class);
                                startActivity(intentUserAccount);
                                overridePendingTransition(0,0);
                            } else if (responseStatus.contains(AppConstant.API_STATUS_FAIL)) {
                                Utility.showSnackBar(binding.appBarLayout,getString(R.string.change_password_fail));
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        Utility.showSnackBar(binding.appBarLayout,response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
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