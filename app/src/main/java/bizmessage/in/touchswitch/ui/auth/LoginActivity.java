package bizmessage.in.touchswitch.ui.auth;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.model.LoginRequestResponse;
import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.databinding.ActivityLoginBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import bizmessage.in.touchswitch.utils.Validation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener  {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.btnLogin.setOnClickListener(this);
        binding.txtForgotPassword.setOnClickListener(this);
        binding.llSignUp.setOnClickListener(this);


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

        binding.edtEmail.setHint(resources.getString(R.string.hint_user_email));///////////////
        binding.edtPassword.setHint(resources.getString(R.string.password));///////////////
        binding.btnLogin.setText(resources.getString(R.string.login));///////////////
        binding.txtForgotPassword.setText(resources.getString(R.string.forgot_your_password));///////////////
        binding.txtDontAccount.setText(resources.getString(R.string.don_t_have_an_account));///////////////
        binding.txtSignUp.setText(resources.getString(R.string.sign_up));///////////////



        if (PreferenceData.getLoginid().length() > 0){
            binding.edtEmail.setText(PreferenceData.getLoginid());
            binding.edtPassword.setText(PreferenceData.getPassword());
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.btnLogin:
                //if (isValidate()) {

                    userLogin();
                //}
                break;
            case R.id.ll_signUp:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
        }
    }

    private boolean isValidate() {
        if (!Validation.isRequiredField(binding.edtEmail.getText().toString())) {
            binding.edtEmail.setError(getResources().getString(R.string.please_enter_email));
            return false;
        } else if (!Validation.isRequiredField(binding.edtPassword.getText().toString())) {
            binding.edtPassword.setError(getString(R.string.please_enter_password));
            return false;
        } else if (!Validation.isValidPassword(binding.edtPassword.getText().toString())){
            binding.edtPassword.setError(getString(R.string.please_enter_valid_password));
            return false;
        }
        return true;
    }


    @SuppressLint("HardwareIds")
    private void userLogin() {
        if (!Utility.isNetworkAvailable(this)) {
            checkConnection(LoginActivity.this, this);
        } else {

            PackageInfo packageInfo = null;
            String mobileId = "";
            try {
                packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                mobileId = "";
            } catch (Exception e) {
                e.printStackTrace();
            }


            Utility.showProgress(LoginActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<LoginRequestResponse> responseCall = service.login(WebUtility.API_SOURCE,
                    packageInfo.versionCode + "",
                    mobileId, PreferenceData.getDeviceToken(),
                    PreferenceData.getFcmToken(),
                    binding.edtPassword.getText().toString().trim(),
                    binding.edtEmail.getText().toString(), WebUtility.LOGIN_OPS);
            responseCall.enqueue(new Callback<LoginRequestResponse>() {
                @Override
                public void onResponse(Call<LoginRequestResponse> call, Response<LoginRequestResponse> response) {
                    String responseStatus = response.toString();
                    if (response.isSuccessful()) {
                        try {
                            LoginRequestResponse responseBody = response.body();


                            if (responseBody != null) {

                                if (responseBody.getStatus().equalsIgnoreCase("sucesses")) {
                                    Utility.showSnackBar(binding.imgLogo,getString(R.string.message_login));
                                    PreferenceData.setLogin(true);
                                    PreferenceData.setPassword(binding.edtPassword.getText().toString().trim());
                                    PreferenceData.setLoginid(binding.edtEmail.getText().toString().trim());
                                    PreferenceData.setIsdealer(responseBody.getIsdealer());
                                    Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                    overridePendingTransition(0, 0);
                                    finish();

                                }
                                else if (responseBody.getStatus().equalsIgnoreCase("nouser")) {

                                    Utility.showSnackBar(binding.imgLogo, resources.getString(R.string.no_user_exist));

                                }


                                } else {
                                Utility.showSnackBar(binding.imgLogo, resources.getString(R.string.no_user_exist));
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        Utility.showSnackBar(binding.imgLogo, response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<LoginRequestResponse> call, Throwable t) {
                    Utility.hideProgress();
                }
            });
        }
    }

/*
    @SuppressLint("HardwareIds")
    private void userLogin() {
       if(DBG) Utility.showToast("FCM"+PreferenceData.getFcmToken());
        if (!Utility.isNetworkAvailable(this)) {
            checkConnection(LoginActivity.this,this);
        } else {
            Utility.showProgress(LoginActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            PackageInfo packageInfo = null;
            String mobileId = "";
            try {
                packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                mobileId = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
             Call<ResponseBody> responseCall = service.login(WebUtility.API_SOURCE,
                    packageInfo.versionCode + "",
                    mobileId, PreferenceData.getDeviceToken(),
                    PreferenceData.getFcmToken(),
                    binding.edtPassword.getText().toString().trim(),
                    binding.edtEmail.getText().toString(), WebUtility.LOGIN_OPS);
          //  if(DBG) Utility.showToast(PreferenceData.getFCM());
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody =response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_SUCCESS)) {
                                Utility.showSnackBar(binding.imgLogo,getString(R.string.message_login));
                                PreferenceData.setLogin(true);
                                PreferenceData.setPassword(binding.edtPassword.getText().toString().trim());
                                PreferenceData.setLoginid(binding.edtEmail.getText().toString().trim());
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent1);
                                overridePendingTransition(0, 0);
                                finish();
                            } else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_FAIL)) {
                                Utility.showSnackBar(binding.imgLogo,resources.getString(R.string.message_login_fail));

                            }else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_NO_USER)) {
                                Utility.showSnackBar(binding.imgLogo, resources.getString(R.string.message_login_fail));
                            }

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        Utility.showSnackBar(binding.imgLogo,response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast(resources.getString(R.string.status_website));
                    }
                }
            });
        }
    }
*/
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }
}
