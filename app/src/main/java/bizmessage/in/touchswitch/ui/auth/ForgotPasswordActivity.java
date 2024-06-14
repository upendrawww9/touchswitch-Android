package bizmessage.in.touchswitch.ui.auth;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityForgotPasswordBinding;
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


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private ActivityForgotPasswordBinding binding;
    Context context;
    Resources resources;
String toNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.btnForgot.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.imgWhatsApp.setOnClickListener(this);
        binding.edtEmail.setText(PreferenceData.getLoginid());



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
        binding.textforgotpassHead.setText(resources.getString(R.string.forgot_password_head));///////////////
        binding.textforgotpassText.setText(resources.getString(R.string.forgot_password_text));///////////////
        binding.btnForgot.setText(resources.getString(R.string.button_recover_my_account));///////////////

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnForgot:
                if (!Validation.isRequiredField(binding.edtEmail.getText().toString())) {
                    binding.edtEmail.setError(getResources().getString(R.string.please_email));
                } else if (!Validation.isValidEmail(binding.edtEmail.getText().toString())) {
                    binding.edtEmail.setError(getString(R.string.please_enter_valid_email));
                } else {
                    forgotPasswordApi(binding.edtEmail.getText().toString().trim());
                }
                break;

            case R.id.imgWhatsApp:
                openWhatsApp();
                break;

        }
    }
    private void openWhatsApp(){
        String text;
        try {
            if (OnlookApplication.SELECTED_DEVICE != null) {


                text = "My Email id is " + PreferenceData.getEmail() + " .My device id : " + OnlookApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Support I Need : ";// Replace with your message.
                if(OnlookApplication.SELECTED_DEVICE.getCall911().length()>8) {
                    toNumber = "91" + OnlookApplication.SELECTED_DEVICE.getRWhatsapp(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
                }
                else{//if user is not set to dealer by mistake it will route to support number
                    text = "My support Not Set.My Email id is " + PreferenceData.getEmail() + " .My device id : " + OnlookApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Support I Need : ";// Replace with your message.

                    toNumber = "917900119635"; // Replace with mobile phone number without +Sign or leading zeros, but with country code

                }
            }
            else{ // ffor a new user or demo user
                text = "My Email id is " + PreferenceData.getEmail() + ". My App version is "+PreferenceData.getAppVersion()+" I need Support for forgot password : ";// Replace with your message.
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



    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(ForgotPasswordActivity.this,this);
    }

    private void forgotPasswordApi(String userEmail) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.ivBack,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(ForgotPasswordActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.forgotPassword(userEmail, WebUtility.FORGOT_PASSWORD_OPS);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.contains(AppConstant.API_STATUS_SENT)) {
                                Utility.showToast(getString(R.string.forgot_password_sent));
                                onBackPressed();
                            } else if (responseStatus.contains(AppConstant.API_STATUS_NO_USER)) {
                                Utility.showSnackBar(binding.ivBack,getString(R.string.no_user_exist));
                            } else if (responseStatus.contains(AppConstant.API_STATUS_FAIL)) {
                                Utility.showSnackBar(binding.ivBack,getString(R.string.forgot_password_fail));
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        Utility.showSnackBar(binding.ivBack,response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast("Website Not reachable, Please check internet");
                    }
                }
            });
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
        finish();
    }
}
