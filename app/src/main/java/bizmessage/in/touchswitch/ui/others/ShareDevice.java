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
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivitySharedevBinding;
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
public class ShareDevice extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = ShareDevice.class.getSimpleName();
    ActivitySharedevBinding binding;

    Context context;
    Resources resources;
    int state=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sharedev);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.share_my_dev);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v ->
        {
        Intent intent = new Intent(ShareDevice.this, MainActivity.class);
             startActivity(intent);


        });




        binding.btnSharedev.setOnClickListener(this);

        binding.imgSharedev.setOnClickListener(this);





        if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "hi");
            binding.textStatusInfo.setTextSize(12);
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ka");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "ta");
            binding.textStatusInfo.setTextSize(14);
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(getApplicationContext(), "te");
            binding.textStatusInfo.setTextSize(14);
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

        getSupportActionBar().setTitle(resources.getString(R.string.share_my_dev));

        binding.textAlertGroup.setText(resources.getString(R.string.share_my_dev));///////////////
        binding.edtShareDev.setHint(resources.getString(R.string.share_mobile));///////////////

      //  binding.btnSharedev.setText(resources.getString(R.string.share_my_dev));///////////////
        binding.textNote.setText(resources.getString(R.string.share_note));///////////////
     // binding.edtShareDev.setHint(resources.getString(R.string.share_mobile));/////////////
       handleFamilyUser();


        if(OnlookApplication.SELECTED_DEVICE.getIsshared().length() > 5)

        {

            binding.edtShareDev.setText(OnlookApplication.SELECTED_DEVICE.getIsshared());
         //   binding.textStatusInfo.setText("Status :  Device Shared");
            binding.textStatusInfo.setTextColor(Color.parseColor("#f1c70623"));
            binding.textStatusInfo.setText(resources.getString(R.string.share_start));/////////////

             binding.btnSharedev.setText(resources.getString(R.string.stop_share));
            binding.edtShareDev.setEnabled(false);
        }
        else{
           // binding.textStatusInfo.setText("Status : Sharing stopped");
            binding.textStatusInfo.setTextColor(Color.parseColor("#FF358500"));
            binding.textStatusInfo.setText(resources.getString(R.string.share_stop));/////////////

            binding.btnSharedev.setText(resources.getString(R.string.share_my_dev));
            binding.imgSharedev.setVisibility(View.GONE);
            binding.edtShareDev.setEnabled(true);
        }


    }

    private void handleFamilyUser(){
        if (PreferenceData.getLoginid().equalsIgnoreCase(OnlookApplication.SELECTED_DEVICE.getRFamEmail())) {
           binding.btnSharedev.setVisibility(View.GONE);
           binding.imgSharedev.setVisibility(View.GONE);
        }else if (PreferenceData.getLoginid().equalsIgnoreCase(AppConstant.MEMBER_EMAIL)){
            binding.btnSharedev.setVisibility(View.GONE);
            binding.imgSharedev.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case   R.id.btnSharedev:
            //    if (binding.edtShareDev.getText().length() >5) {


                if(OnlookApplication.SELECTED_DEVICE.getIsshared().length() > 5)
                {
                    sharemyDevice(PreferenceData.getDevshare(),"delete");

                    binding.edtShareDev.setText("");
                   // binding.textStatusInfo.setText("Status :  Device Sharing stopped");
                    binding.textStatusInfo.setTextColor(Color.parseColor("#FF358500"));
                    binding.textStatusInfo.setText(resources.getString(R.string.share_stop));/////////////
                    binding.imgSharedev.setVisibility(View.GONE);

                    binding.btnSharedev.setText(resources.getString(R.string.share_my_dev));

                    binding.edtShareDev.setEnabled(true);


                }
                else {
                    //not there
                  //
                    if (binding.edtShareDev.getText().length() > 5) {

                        binding.edtShareDev.setEnabled(true);
                         binding.imgSharedev.setVisibility(View.VISIBLE);
                        // binding.imgSharedev.setVisibility(View.GONE);
                       // binding.textStatusInfo.setText("Status : Device Sharing Started");
                        binding.textStatusInfo.setTextColor(Color.parseColor("#f1c70623"));
                        binding.textStatusInfo.setText(resources.getString(R.string.share_start));/////////////

                        binding.btnSharedev.setText(resources.getString(R.string.stop_share));
                        sharemyDevice(binding.edtShareDev.getText().toString().trim(),
                                "add");


                    } else {

                        Utility.showToast(resources.getString(R.string.please_enter_valid_mobile)+"");
                    }

                }

                break;

            case R.id.imgSharedev:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Onlook Device Share \n\n Deviec ID:"+OnlookApplication.SELECTED_DEVICE.getSsid()+"\n\nLogin Details\nLogin id: "+binding.edtShareDev.getText().toString().trim()+ "\n Password :"+binding.edtShareDev.getText().toString().trim()+"\n\n\n Download app at: \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()+"\n Donot Share this. Please note alerts stop coming when owner stops sharing, sharing will be deactivated automatically in 7 days");
                sendIntent.setType("text/plain");
                sendIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(sendIntent);


                break;

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(ShareDevice.this,this);
    }

    private void sharemyDevice(String familyEmail, String ops) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.llMain,getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(ShareDevice.this);

            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.shareDevice(
                    binding.edtShareDev.getText().toString().trim(),
                    OnlookApplication.SELECTED_DEVICE.getSsid(),
                    PreferenceData.getEmail(),
                    Build.MANUFACTURER,
                    Build.BRAND,
                    Utility.getAndroidVersion(),
                    PreferenceData.getLatitude(),
                    PreferenceData.getLongitude(),
                    ops,
                    WebUtility.DEVICE_SHARE_OPS);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseBody responseBody = response.body();
                            String responseStatus = responseBody.string();
                            Utility.hideProgress();
                             if (responseStatus.contains(AppConstant.API_STATUS_SUCCESS)) {

                                 if(ops=="add"){
                                     Utility.showToast(resources.getString(R.string.share_start));


                                     Intent sendIntent = new Intent();
                                     sendIntent.setAction(Intent.ACTION_SEND);
                                     sendIntent.putExtra(Intent.EXTRA_TEXT,"Onlook Device Share \n\n Deviec ID:"+OnlookApplication.SELECTED_DEVICE.getSsid()+"\n\nLogin Details\nLogin id: "+binding.edtShareDev.getText().toString().trim()+ "\n Password :"+binding.edtShareDev.getText().toString().trim()+"\n\n\n Download app at: \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()+"\n Donot Share this. Please note alerts stop coming when owner stops sharing, sharing will be deactivated automatically in 7 days");
                                     sendIntent.setType("text/plain");
                                     sendIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                     getApplicationContext().startActivity(sendIntent);
                                     binding.btnSharedev.setEnabled(false);
                                     binding.btnSharedev.setBackgroundColor(Color.GRAY);
                                 }
                                 if(ops=="delete"){

                                     Utility.showToast(resources.getString(R.string.share_stop));
                                     Intent intent = new Intent(ShareDevice.this, MainActivity.class);
                                     startActivity(intent);

                                 }








                              }else if (responseStatus.toLowerCase().contains(AppConstant.API_DUP_MOBILE)) {
                                Utility.showSnackBar(binding.llMain, getString(R.string.user_dupmobile));
                            }
                            else if (responseStatus.contains(AppConstant.API_STATUS_EXIST)) {
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

        Intent intent = new Intent(ShareDevice.this, MainActivity.class);
        startActivity(intent);
    }

}