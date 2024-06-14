package bizmessage.in.touchswitch.ui.others;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityWhatsalertBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.auth.LoginActivity;
import bizmessage.in.touchswitch.ui.auth.RegistrationActivity;
import bizmessage.in.touchswitch.ui.settings.SettingsActivity;
import bizmessage.in.touchswitch.utils.AppConstant;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Whatsalert extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = Whatsalert.class.getSimpleName();
    ActivityWhatsalertBinding binding;

    Context context;
    Resources resources;
    Call<ResponseBody> responseCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_whatsalert);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.title_family_member);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnCreate.setOnClickListener(this);

        binding.imgSharefam.setOnClickListener(this);

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

        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        getSupportActionBar().setTitle(resources.getString(R.string.whatstitle));

        binding.textAlertGroup.setText(resources.getString(R.string.whatalert));///////////////
        // binding.edtWhatsnumber.setHint(resources.getString(R.string.whatsnumber));///////////////

        binding.btnCreate.setText(resources.getString(R.string.sendalerts));///////////////

        binding.textNote.setText(resources.getString(R.string.alertnote));///////////////

        binding.textalertvalid.setText(resources.getString(R.string.alertvalidity));///////////////

        //   binding.textStatusFamilyadd.setText(resources.getString(R.string.family_member_status));///////////////

//Utility.showToast(OnlookApplication.SELECTED_DEVICE.getIsWhatsalert());
        //Utility.showToast(OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());

        if(OnlookApplication.SELECTED_DEVICE.getIsWhatsalert().equalsIgnoreCase("false") ) {

            binding.btnCreate.setVisibility(View.GONE);
        }
        else{
            binding.btnCreate.setVisibility(View.VISIBLE);
            binding.textwhatsstatus.setText("Alerts Number:"+OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());
            // Utility.showToast(OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());

            if(OnlookApplication.SELECTED_DEVICE.getWhatsnum().length()>9 ) {
                //   Utility.showToast(OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());
                binding.edtWhatsnumber.setEnabled(false);



                binding.btnCreate.setText(resources.getString(R.string.stopalerts));///////////////
            }
            else{

            }
        }



        if (OnlookApplication.SELECTED_DEVICE.getRFamEmail() != null) {
            if (!OnlookApplication.SELECTED_DEVICE.getRFamEmail().isEmpty()) {
                //  binding.edtWhatsnumber.setText(OnlookApplication.SELECTED_DEVICE.getRFamEmail());
                // binding.edtPassword.setText(PreferenceData.getFamilyPassword());
                // binding.edtWhatsnumber.setClickable(false);
                //  binding.edtWhatsnumber.setEnabled(false);
                //  binding.btnCreate.setEnabled(false);
                //  binding.btnCreate.setClickable(false);
                //  binding.btnCreate.setVisibility(View.GONE);
                // binding.textStatusFamilyadd.setText("Status : Family member is added.");
                //   binding.textStatusFamilyInfo.setText(resources.getString(R.string.family_member_status_info));///////////////

                // binding.textStatusFamilyInfo.setTextColor(Color.parseColor("#246024"));

                //  binding.textStatusFamilyadd.setTextColor(Color.parseColor("#246024"));
                binding.imgSharefam.setVisibility(View.VISIBLE);
            }
            else{
                binding.imgSharefam.setVisibility(View.GONE);

            }

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case
                    R.id.btnCreate:
                if(OnlookApplication.SELECTED_DEVICE.getWhatsnum().length()>9 ) {
                    //   Utility.showToast(OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());
//                    binding.btnCreate.setVisibility(View.VISIBLE);
                    //                  binding.textwhatsstatus.setText("Alerts Number:"+OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());

                    //                binding.btnCreate.setText(resources.getString(R.string.stopalerts));///////////////
                    binding.btnCreate.setVisibility(View.GONE);
                    binding.textwhatsstatus.setText("Alerts Number:");

                    addWhatsNumber(binding.edtWhatsnumber.getText().toString().trim(), "delete");
                }
                else{
                    if(binding.edtWhatsnumber.getText().length()>9 ) {
                        addWhatsNumber(binding.edtWhatsnumber.getText().toString().trim(), "add");
                        binding.btnCreate.setVisibility(View.GONE);

                        binding.textwhatsstatus.setText("Alerts Number:" + OnlookApplication.SELECTED_DEVICE.getWhatsnum().toString().trim());
                    }
                    else{
                        Utility.showToast(resources.getString(R.string.whatsnumber));
                    }

                }


                break;

            case R.id.imgShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Onlook Alerts are sent to you on my Request");
                sendIntent.setType("text/plain");
                sendIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(sendIntent);
//OnlookApplication.SELECTED_DEVICE.getPaswd()

                break;

            case R.id.imgSharefam:


                Intent sendIntentfam = new Intent();
                sendIntentfam.setAction(Intent.ACTION_SEND);

                sendIntentfam.putExtra(Intent.EXTRA_TEXT, "Onlook Alert Control Login \n " +
                        "Email: " + OnlookApplication.SELECTED_DEVICE.getRFamEmail() + "\n Password: "+OnlookApplication.SELECTED_DEVICE.getFampass() + "\n Download app at: \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "\n Donot Share this. Please delete this message");


                sendIntentfam.setType("text/plain");
                sendIntentfam.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(sendIntentfam);


                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(Whatsalert.this,this);
    }

    private void addWhatsNumber(String whatnumber, String action) {

        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.llMain,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(Whatsalert.this);

            if(action.equalsIgnoreCase("add")) {
                WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
                responseCall = service.addWhatsNumber(OnlookApplication.SELECTED_DEVICE.getDevid(),
                        whatnumber,
                        PreferenceData.getLatitude(),
                        PreferenceData.getLongitude(),
                        PreferenceData.getEmail(),
                        "add");
            }
            if(action.equalsIgnoreCase("delete")) {
                WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
                responseCall = service.addWhatsNumber(OnlookApplication.SELECTED_DEVICE.getDevid(),
                        whatnumber,
                        PreferenceData.getLatitude(),
                        PreferenceData.getLongitude(),
                        PreferenceData.getEmail(),
                        "delete");
            }

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.contains(AppConstant.API_STATUS_SUCCESS)) {
                                OnlookApplication.SELECTED_DEVICE.setRFamEmail(binding.edtWhatsnumber.getText().toString().trim());
                                binding.imgSharefam.setVisibility(View.VISIBLE);
                                //  Utility.showSnackBar(binding.appBarLayout, getString(R.string.email_sent_to) + binding.edtFamilyMemberEmail.getText().toString().trim() + getString(R.string.for_aproval));
                                Utility.showSnackBar(binding.appBarLayout,"Update Sucesses. Share it on Whatsapp");
                                binding.textwhatsstatus.setText("Alerts Number:"+binding.edtWhatsnumber.getText().toString().trim());
                                // onBackPressed();
                                binding.btnCreate.setVisibility(View.GONE);
                                binding.edtWhatsnumber.setText("");
                                binding.edtWhatsnumber.setEnabled(false);


                            } else if (responseStatus.contains(AppConstant.API_STATUS_FAIL)) {
                                Utility.showSnackBar(binding.llMain,getString(R.string.whatserror));
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
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Whatsalert.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        //finish();



    }

}