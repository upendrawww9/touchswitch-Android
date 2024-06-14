package bizmessage.in.touchswitch.ui.others;



import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityLanguageBinding;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
public class Language extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = Language.class.getSimpleName();
    ActivityLanguageBinding binding;
    Context context;
    Resources resources;
    String selectedlanguage="";
    boolean setlanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);

      //  binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

        // binding.textPassword1.setText("Password : "+OnlookApplication.SELECTED_DEVICE.getPaswd());
        setSupportActionBar(binding.toolbar);
        //getSupportActionBar().setTitle(R.string.language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnCreate.setOnClickListener(this);
        binding.imgShare.setOnClickListener(this);
       // binding.imgShare.setOnClickListener(this);
       // binding.imgSharefam.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            binding.textguja.setVisibility(View.VISIBLE);
            binding.checkguja.setVisibility(View.VISIBLE);
            // Do something for lollipop and above versions
        } else{
            binding.textguja.setVisibility(View.GONE);
            binding.checkguja.setVisibility(View.GONE);

            // do something for phones running an SDK before lollipop
        }



        if (OnlookApplication.SELECTED_DEVICE != null) {

        if (OnlookApplication.SELECTED_DEVICE.getRFamEmail() != null) {
            if (!OnlookApplication.SELECTED_DEVICE.getRFamEmail().isEmpty()) {
                 }
            else{
         //       binding.imgSharefam.setVisibility(View.GONE);

            }
            setLanguage(PreferenceData.getlanguage(),Boolean.parseBoolean(PreferenceData.getislanguage()));
        }

        }
        else{
            Utility.showSnackBar(binding.viewLine1, getString(R.string.device_not_found));
        }
        binding.checkenglish.setOnClickListener((buttonView) -> setLanguage("en",binding.checkenglish.isChecked()));
        binding.checkhindi.setOnClickListener((buttonView) -> setLanguage("hi",binding.checkhindi.isChecked()));
        binding.checkkanada.setOnClickListener((buttonView) -> setLanguage("ka",binding.checkkanada.isChecked()));
        binding.checktelugu.setOnClickListener((buttonView) -> setLanguage("te",binding.checktelugu.isChecked()));
        binding.checktamil.setOnClickListener((buttonView) -> setLanguage("ta",binding.checktamil.isChecked()));
        binding.checkmalaya.setOnClickListener((buttonView) -> setLanguage("ml",binding.checkmalaya.isChecked()));
        binding.checkmara.setOnClickListener((buttonView) -> setLanguage("mr",binding.checkmara.isChecked()));
        binding.checkguja.setOnClickListener((buttonView) -> setLanguage("gu",binding.checkguja.isChecked()));
        binding.checkben.setOnClickListener((buttonView) -> setLanguage("bn",binding.checkben.isChecked()));









    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.imgShare:
               openWhatsApp();

                break;
            case
                    R.id.btnCreate:


                PreferenceData.setLanguage(selectedlanguage);
                updateSettings(selectedlanguage);
               // triggerRebirth(getApplicationContext());




        }
    }

    private void openWhatsApp(){
        String text;
String toNumber;
        try {
            if (OnlookApplication.SELECTED_DEVICE != null) {


                text = "My language set to "+PreferenceData.getlanguage()+" My Email id is " + PreferenceData.getEmail() + " .My device id : " + OnlookApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Language  help for ";// Replace with your message.
                toNumber = "91"+OnlookApplication.SELECTED_DEVICE.getRWhatsapp(); // Replace with mobile phone number without +Sign or leading zeros, but with country code

            }
            else{
                text = "My language set to "+PreferenceData.getlanguage()+ ". I am New. My Languageis My Email id is " + PreferenceData.getEmail() +". Language Support ";// Replace with your message.
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
        checkConnection(Language.this,this);
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    private void setLanguage(String checkbox,boolean ischeck) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.llMain,getString(R.string.no_internet_connection));
        } else {

            if(checkbox.equalsIgnoreCase("en")){
                if(ischeck){
                    selectedlanguage="en";
                    //setlanguage=true;
                    //PreferenceData.setLanguage("en");
                    //PreferenceData.setisLanguage(Boolean.toString(ischeck));

                    getSupportActionBar().setTitle(R.string.language);
                    binding.checkhindi.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkmara.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);
                    context = LocaleHelper.setLocale(getApplicationContext(), "en");
                    resources = context.getResources();
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglishinfo.setText("");
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                }
                else{

                }

            }
            if(checkbox.equalsIgnoreCase("hi")){

                if(ischeck){
                    selectedlanguage="hi";

                    // PreferenceData.setLanguage("hi");
                    binding.checkenglish.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkmara.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);
                    context = LocaleHelper.setLocale(getApplicationContext(), "hi");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));

                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));


                }
                else{

                }
            }
            if(checkbox.equalsIgnoreCase("te")){
                if(ischeck){
                   // PreferenceData.setLanguage("te");
                    selectedlanguage="te";

                    binding.checkhindi.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checkenglish.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkmara.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);
                    context = LocaleHelper.setLocale(getApplicationContext(), "te");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));


                }
                else{

                }
            }
            if(checkbox.equalsIgnoreCase("ka")){
                if(ischeck){
                    selectedlanguage="ka";
                     //PreferenceData.setLanguage("ka");
                    binding.checkhindi.setChecked(false);
                    binding.checkenglish.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkmara.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);
                    context = LocaleHelper.setLocale(getApplicationContext(), "ka");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                }
                else{

                }
            }
            if(checkbox.equalsIgnoreCase("ta")){
                if(ischeck){
                    selectedlanguage="ta";
                    // PreferenceData.setLanguage("ta");
                    binding.checkhindi.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checkenglish.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkmara.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);
                    context = LocaleHelper.setLocale(getApplicationContext(), "ta");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));
                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                }
                else{

                }
            }

            ///////////////////////////////////////////////

            if(checkbox.equalsIgnoreCase("bn")) {
                if (ischeck) {
                    selectedlanguage = "bn";


                    binding.checkhindi.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkenglish.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkmara.setChecked(false);

                    context = LocaleHelper.setLocale(getApplicationContext(), "bn");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));
                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                } else {

                }

            }


            ////////////
            if(checkbox.equalsIgnoreCase("mr")) {
                if (ischeck) {
                    selectedlanguage = "mr";


                    binding.checkhindi.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkenglish.setChecked(false);
                    binding.checkmalaya.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);

                    context = LocaleHelper.setLocale(getApplicationContext(), "mr");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                } else {

                }

            }
                ///
                if(checkbox.equalsIgnoreCase("gu")) {
                    if (ischeck) {


                        selectedlanguage = "gu";
                        binding.checkhindi.setChecked(false);
                        binding.checkkanada.setChecked(false);
                        binding.checktelugu.setChecked(false);
                        binding.checktamil.setChecked(false);
                        binding.checkenglish.setChecked(false);
                        binding.checkmalaya.setChecked(false);
                        binding.checkmara.setChecked(false);
                        binding.checkben.setChecked(false);

                        context = LocaleHelper.setLocale(getApplicationContext(), "gu");
                        resources = context.getResources();
                        getSupportActionBar().setTitle(resources.getString(R.string.language));
                        binding.languageHeading.setText(resources.getString(R.string.language_heading));
                        binding.textEnglish.setText(resources.getString(R.string.language_english));
                        binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                        binding.btnCreate.setText(resources.getString(R.string.continue_language));
                        binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                        binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                        binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                    } else {

                    }
                }



                    ///////////////////////////////////////////////









                    if(checkbox.equalsIgnoreCase("ml")){
                if(ischeck){
                    selectedlanguage="ml";


                    binding.checkhindi.setChecked(false);
                    binding.checkkanada.setChecked(false);
                    binding.checktelugu.setChecked(false);
                    binding.checktamil.setChecked(false);
                    binding.checkenglish.setChecked(false);
                    binding.checkmara.setChecked(false);
                    binding.checkguja.setChecked(false);
                    binding.checkben.setChecked(false);
                    context = LocaleHelper.setLocale(getApplicationContext(), "ml");
                    resources = context.getResources();
                    getSupportActionBar().setTitle(resources.getString(R.string.language));
                    binding.languageHeading.setText(resources.getString(R.string.language_heading));
                    binding.textEnglish.setText(resources.getString(R.string.language_english));
                    binding.textEnglishinfo.setText(resources.getString(R.string.selected_language));
                    binding.btnCreate.setText(resources.getString(R.string.continue_language));
                    binding.supportInfo.setText(resources.getString(R.string.contact_language_support));
                    binding.textDeviceId.setText(resources.getString(R.string.selected_language_head));

                    binding.supportHead.setText(resources.getString(R.string.contact_language_error_head));

                }

                else{

                }
                binding.textEnglish.setText(resources.getString(R.string.language_english));
                binding.textKannada.setText(resources.getString(R.string.language_kannada));
                binding.texthindi.setText(resources.getString(R.string.language_hindi));
                binding.texttelugu.setText(resources.getString(R.string.language_telugu));
                binding.texttamil.setText(resources.getString(R.string.language_tamil));
                binding.textmalaya.setText(resources.getString(R.string.language_malaya));
                binding.textben.setText(resources.getString(R.string.language_ben));

                    }









            /*
            Utility.showProgress(Language.this);

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
                        Utility.showToast("Website Not reachable, Please check internet");
                    }
                }
            });
            */
        }
    }


    private void updateSettings( String lan) {
        if (!Utility.isNetworkAvailable(this)) {
            Utility.showSnackBar(binding.appBarLayout, getString(R.string.no_internet_connection));
        } else {
            Utility.showProgress(Language.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
//            Call<ResponseBody> responseCall = null;
            Call<ResponseBody> responseCall = service.updatelocale("setlocale",PreferenceData.getEmail(), lan);

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Utility.showSnackBar(binding.btnCreate, resources.getString(R.string.status_updated_successfully));

                    } else {
                        Utility.showSnackBar(binding.appBarLayout, response.message());
                    }

                    Utility.hideProgress();
                    Intent intent = new Intent(Language.this, MainActivity.class);
                    startActivity(intent);
                    finish();

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



    //resources.getString(R.string.button_setting)
    public void checkConnection(Context context, DialogButtonClickListener listener) {
        if (!Utility.isNetworkAvailable(context)) {
            Utility.showDialog(context, resources.getString(R.string.no_internet_title),
                    resources.getString(R.string.no_internet),
                    resources.getString(R.string.button_try_again),
                    resources.getString(R.string.button_setting),
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