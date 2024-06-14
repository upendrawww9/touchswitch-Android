package bizmessage.in.touchswitch.ui.auth;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.databinding.ActivityRegistrationBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.settings.AutoCompleteAdapter;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import bizmessage.in.touchswitch.utils.Validation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private ActivityRegistrationBinding binding;
    private final int isFrom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        binding.btnSignUp.setOnClickListener(this);
        binding.llSignIn.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        ArrayList<String> aList = new ArrayList<String>();

        aList.add("Andaman and Nicobar (UT)");
        aList.add("Andhra Pradesh");
        aList.add("Arunachal Pradesh");
        aList.add("Assam");
        aList.add("Bihar");
        aList.add("Chandigarh (UT)");
        aList.add("Chhattisgarh");
        aList.add("Dadra and Nagar Haveli (UT)");
        aList.add("Daman and Diu (UT)");
        aList.add("Delhi");
        aList.add("Goa");
        aList.add("Gujarat");
        aList.add("Haryana");
        aList.add("Himachal Pradesh");
        aList.add("Jammu and Kashmir");
        aList.add("Jharkhand");
        aList.add("Karnataka");
        aList.add("Kerala");
        aList.add("Lakshadweep (UT)");
        aList.add("Madhya Pradesh");
        aList.add("Maharashtra");
        aList.add("Manipur");
        aList.add("Meghalaya");
        aList.add("Mizoram");
        aList.add("Nagaland");
        aList.add("Orissa");
        aList.add("Puducherry (UT)");
        aList.add("Punjab");
        aList.add("Rajasthan");
        aList.add("Sikkim");
        aList.add("Tamil Nadu");
        aList.add("Telangana");
        aList.add("Tripura");
        aList.add("Uttar Pradesh");
        aList.add("Uttarakhand");
        aList.add("West Bengal");



        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, aList);
        binding.edtState.setAdapter(autoCompleteAdapter);

        binding.edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                }else{
                    if(binding.edtMobileNumber.getText().length()==10 && !Validation.isValidEmail(binding.edtEmail.getText().toString().trim())) {
                        binding.edtEmail.setText(binding.edtMobileNumber.getText() + "@onlook.in");
                    }
                    }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_signIn:
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.btnSignUp:
                if (isValidate()) {
                    if (Utility.isNetworkAvailable(RegistrationActivity.this)) {
                        userRegistration();
                    } else {
                        Utility.showSnackBar(binding.ivBack, getString(R.string.no_internet_connection));
                    }
                }
                break;
        }
    }

    private boolean isValidate() {
        if (!Validation.isRequiredField(binding.edtFirstName.getText().toString().trim())) {
            binding.edtFirstName.setError(getString(R.string.please_enter_first_name));
            return false;
        } else if (!Validation.isRequiredField(binding.edtLastName.getText().toString().trim())) {
            binding.edtLastName.setError(getString(R.string.please_enter_last_name));
            return false;
        } else if (!Validation.isRequiredField(binding.edtMobileNumber.getText().toString().trim())) {
            binding.edtMobileNumber.setError(getString(R.string.please_enter_mobile));
            return false;
        } else if (!Validation.isValidMobile(binding.edtMobileNumber.getText().toString().trim())) {
            binding.edtMobileNumber.setError(getString(R.string.please_enter_valid_mobile));
            return false;
        } else if (!Validation.isRequiredField(binding.edtEmail.getText().toString().trim())) {
            binding.edtEmail.setError(getString(R.string.please_enter_email));
            return false;
        } else if (!Validation.isValidEmail(binding.edtEmail.getText().toString().trim())) {
            binding.edtEmail.setError(getString(R.string.please_enter_valid_email));
            return false;
        } else if (!Validation.isRequiredField(binding.edtPassword.getText().toString().trim())) {
            binding.edtPassword.setError(getString(R.string.please_enter_password));
            return false;
        } else if (!Validation.isValidPassword(binding.edtPassword.getText().toString().trim())) {
            binding.edtPassword.setError(getString(R.string.please_enter_valid_password));
            return false;
        } else if (!Validation.isRequiredField(binding.edtConfirmPassword.getText().toString().trim())) {
            binding.edtConfirmPassword.setError(getString(R.string.please_enter_confirm_password));
            return false;
        } else if (!Validation.isPasswordsMatch(binding.edtPassword.getText().toString().trim(),
                binding.edtConfirmPassword.getText().toString().trim())) {
            binding.edtConfirmPassword.setError(getString(R.string.please_check_both_password));
            return false;
        }
        else if (binding.edtState.getText().toString().length() < 3) {
            binding.edtState.setError("Enter State where Device is kept");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isFrom == 1) {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @SuppressLint("HardwareIds")
    private void userRegistration() {
        if (!Utility.isNetworkAvailable(this)) {
            checkConnection(RegistrationActivity.this,this);
        } else {
            Utility.showProgress(RegistrationActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            String mobileId = "";
            try {
                mobileId = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<ResponseBody> responseCall = service.register(mobileId, Build.MANUFACTURER,
                    Build.BRAND,
                    Utility.getAndroidVersion(), PreferenceData.getDeviceToken(),
                    binding.edtEmail.getText().toString().trim(),
                    binding.edtFirstName.getText().toString().trim(),
                    binding.edtLastName.getText().toString().trim(),
                    PreferenceData.getLatitude(), PreferenceData.getLongitude(),
                    binding.edtState.getText().toString().trim(),
                    binding.edtMobileNumber.getText().toString().trim(),
                    binding.edtPassword.getText().toString().trim(),
                    WebUtility.REGISTER_OPS);
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_SUCCESS)) {
                                PreferenceData.setLoginid(binding.edtEmail.getText().toString().trim());
                                PreferenceData.setPassword(binding.edtPassword.getText().toString().trim());
                                Utility.showToast(getString(R.string.registration_success));
                                Intent intentRegistration = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intentRegistration);
                                overridePendingTransition(0, 0);
                                finish();
                                Utility.showToast(responseStatus.toLowerCase());
                            } else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_FAIL)) {
                                Utility.showSnackBar(binding.ivBack, getString(R.string.user_registration_fail));
                            }else if (responseStatus.toLowerCase().contains(AppConstant.API_DUP_MOBILE)) {
                                Utility.showSnackBar(binding.ivBack, getString(R.string.user_dupmobile));
                            }
                            else if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_EXIST)) {
                                Utility.showSnackBar(binding.ivBack, getString(R.string.user_already_exist));
                            } else {
                                Utility.showSnackBar(binding.ivBack, getString(R.string.user_registration_fail));
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        Utility.showSnackBar(binding.ivBack, response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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

}
