package bizmessage.in.touchswitch.ui.others;

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
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityFamilyMemberBinding;
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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
public class FamilyMemberActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = FamilyMemberActivity.class.getSimpleName();
    ActivityFamilyMemberBinding binding;

    Context context;
    Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family_member);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.title_family_member);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnCreate.setOnClickListener(this);
        binding.imgShare.setOnClickListener(this);
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

        getSupportActionBar().setTitle(resources.getString(R.string.title_family_member));

       binding.textAlertGroup.setText(resources.getString(R.string.alert_ops_login));///////////////
         binding.edtFamilyMemberEmail.setHint(resources.getString(R.string.family_member_email));///////////////
        binding.edtPassword.setHint(resources.getString(R.string.password));///////////////

        binding.btnCreate.setText(resources.getString(R.string.create_member));///////////////
        binding.textNote.setText(resources.getString(R.string.family_member_note));///////////////
        binding.textTiming1.setText(resources.getString(R.string.light_ops_login));///////////////

        binding.textNetworkName1.setText(resources.getString(R.string.family_member_light_login));///////////////

        binding.textNetworkName1.setText(resources.getString(R.string.family_member_light_login));///////////////
        binding.textPassword1.setText(resources.getString(R.string.password));///////////////
       binding.textPasswordinfo.setText(resources.getString(R.string.family_login_note));///////////////

     //   binding.textStatusFamilyadd.setText(resources.getString(R.string.family_member_status));///////////////

        binding.textPassword1Info.setText(OnlookApplication.SELECTED_DEVICE.getPaswd());




        if (OnlookApplication.SELECTED_DEVICE.getRFamEmail() != null) {
            if (!OnlookApplication.SELECTED_DEVICE.getRFamEmail().isEmpty()) {
                binding.edtFamilyMemberEmail.setText(OnlookApplication.SELECTED_DEVICE.getRFamEmail());
               // binding.edtPassword.setText(PreferenceData.getFamilyPassword());
                binding.edtFamilyMemberEmail.setClickable(false);
                binding.edtFamilyMemberEmail.setEnabled(false);
                binding.edtPassword.setEnabled(false);
                binding.edtPassword.setClickable(false);
                binding.btnCreate.setEnabled(false);
                binding.btnCreate.setClickable(false);
                binding.btnCreate.setVisibility(View.GONE);
              // binding.textStatusFamilyadd.setText("Status : Family member is added.");
                binding.textStatusFamilyInfo.setText(resources.getString(R.string.family_member_status_info));///////////////

                binding.textStatusFamilyInfo.setTextColor(Color.parseColor("#246024"));

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

                    addFamilyMember(binding.edtFamilyMemberEmail.getText().toString().trim(),
                            binding.edtPassword.getText().toString().trim());

                break;

                case R.id.imgShare:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,"Onlook Light Control Login \nEmail: member@onlook.in\n Password: "+OnlookApplication.SELECTED_DEVICE.getPaswd()+"\n Download app at: \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()+"\n Donot Share this. Please delete this message");
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
        checkConnection(FamilyMemberActivity.this,this);
    }

    private void addFamilyMember(String familyEmail, String newPassword) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.llMain,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(FamilyMemberActivity.this);

            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.addFamilyMember(PreferenceData.getEmail(),
                    familyEmail,
                    newPassword,
                    Build.MANUFACTURER,
                    Build.BRAND,
                    Utility.getAndroidVersion(),
                    PreferenceData.getLatitude(),
                    PreferenceData.getLongitude(),
                    WebUtility.ADD_FAMILY_MEMBER_OPS);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                            if (responseStatus.contains(AppConstant.API_STATUS_SUCCESS)) {
                                OnlookApplication.SELECTED_DEVICE.setRFamEmail(binding.edtFamilyMemberEmail.getText().toString().trim());
                                OnlookApplication.SELECTED_DEVICE.setFampass(binding.edtPassword.getText().toString().trim());
                                PreferenceData.setFamilyPassword(binding.edtPassword.getText().toString().trim());
                                binding.imgSharefam.setVisibility(View.VISIBLE);
                               //  Utility.showSnackBar(binding.appBarLayout, getString(R.string.email_sent_to) + binding.edtFamilyMemberEmail.getText().toString().trim() + getString(R.string.for_aproval));
                                Utility.showSnackBar(binding.appBarLayout,"Share it on Whatsapp");

                                // onBackPressed();
                                binding.btnCreate.setVisibility(View.GONE);

                            } else if (responseStatus.contains(AppConstant.API_STATUS_EXIST)) {
                                Utility.showSnackBar(binding.llMain,getString(R.string.message_add_family_exist));
                            } else if (responseStatus.contains(AppConstant.API_STATUS_NO_MOBILE)) {
                                Utility.showSnackBar(binding.llMain,getString(R.string.message_add_family_no_mobile));
                            } else if (responseStatus.contains(AppConstant.API_STATUS_NO_HEAD)) {
                                Utility.showSnackBar(binding.llMain,getString(R.string.message_add_family_no_head));
                            } else if (responseStatus.contains(AppConstant.API_STATUS_SAME)) {
                                Utility.showSnackBar(binding.llMain,getString(R.string.message_add_family_same));
                            } else if (responseStatus.contains(AppConstant.API_STATUS_FAIL)) {
                                Utility.showSnackBar(binding.llMain,getString(R.string.message_add_family_exist));
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
        finish();
    }

}