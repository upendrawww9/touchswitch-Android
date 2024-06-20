package bizmessage.in.touchswitch.ui.others;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.databinding.ActivityNewcustomerBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
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

public class AddCustomer extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = AddCustomer.class.getSimpleName();
    private ActivityNewcustomerBinding binding;
    private final int isFrom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_newcustomer);
        binding.btnAdddevice.setOnClickListener(this);

        binding.ivBack.setOnClickListener(this);




        binding.edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                }else{
                    if(binding.edtMobileNumber.getText().length()==10 && !Validation.isValidEmail(binding.edtEmail.getText().toString().trim())) {
                        binding.edtEmail.setText(binding.edtMobileNumber.getText() + "@onload.in");
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

            case R.id.btnAdddevice:

                if (isValidate()) {
                    if (Utility.isNetworkAvailable(AddCustomer.this)) {
                        userRegistration();
                    } else {
                        Utility.showSnackBar(binding.ivBack, getString(R.string.no_internet_connection));
                    }
                }
                break;
        }
    }

    private boolean isValidate() {
        Utility.showToast("DDDDDDDDDDD");

      //  if (!Validation.isRequiredField(binding.edtFirstName.getText().toString().trim())) {
        //    binding.edtFirstName.setError(getString(R.string.please_enter_first_name));
          //  return false;
       // } else if (!Validation.isRequiredField(binding.edtLastName.getText().toString().trim())) {
         //   binding.edtLastName.setError(getString(R.string.please_enter_last_name));
         //   return false;
       // } else
        if (!Validation.isRequiredField(binding.edtDevid.getText().toString().trim())) {
            binding.edtDevid.setError(getString(R.string.please_enter_mobile));
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
        else if (binding.edtDevid.getText().toString().length() > 9) {
            binding.edtDevid.setError("Enter Device ID ");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isFrom == 1) {
            Intent intent = new Intent(AddCustomer.this, LoginActivity.class);
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
            checkConnection(AddCustomer.this,this);
        } else {
            Utility.showProgress(AddCustomer.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            String mobileId = "";
            try {
                mobileId = "";
            } catch (Exception e) {
                e.printStackTrace();
            }

            Call<ResponseBody> responseCall = service.adddevice(
                    binding.edtDevid.getText().toString().trim(),
                    PreferenceData.getEmail(),
                    PreferenceData.getPassword(),
                    binding.edtEmail.getText().toString().trim(),
                    binding.edtFirstName.getText().toString().trim(),
                    binding.edtLastName.getText().toString().trim(),
                    PreferenceData.getLatitude(), PreferenceData.getLongitude(),

                    binding.edtMobileNumber.getText().toString().trim(),
                    binding.edtPassword.getText().toString().trim(),
                    "preloadeduser");
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.toLowerCase().contains(AppConstant.API_STATUS_SUCCESS)) {
                                Utility.showToast(getString(R.string.registration_success));
                                Intent intentRegistration = new Intent(AddCustomer.this, Dealerclients.class);
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
