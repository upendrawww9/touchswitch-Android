package bizmessage.in.touchswitch.ui.others;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityContactUsBinding;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = ContactUsActivity.class.getSimpleName();
    ActivityContactUsBinding binding;
    String toNumber;
    Context context;
    Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);

        setSupportActionBar(binding.toolbar);
       // getSupportActionBar().setTitle(R.string.title_offers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.imgWhatsApp.setOnClickListener(this);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        Utility.showProgress(this);


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

        getSupportActionBar().setTitle(resources.getString(R.string.offers));///////////////



        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Utility.hideProgress();
            }
        });
      //  binding.webView.loadUrl(WebUtility.WEB_CONTACT_US);
        binding.webView.loadUrl(WebUtility.WEB_CONTACT_US+ PreferenceData.getEmail()+"&pass="+PreferenceData.getPassword());



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.imgWhatsApp:
                openWhatsApp();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(ContactUsActivity.this,this);
    }

    private void openWhatsApp(){
        String text;
          try {
              if (OnlookApplication.SELECTED_DEVICE != null) {


                  text = "My Email id is " + PreferenceData.getEmail() + " .My device id : " + OnlookApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" I need help related to Offer: ";// Replace with your message.
                  toNumber = "91"+OnlookApplication.SELECTED_DEVICE.getRWhatsapp(); // Replace with mobile phone number without +Sign or leading zeros, but with country code

              }
              else{
                  text = "My Email id is " + PreferenceData.getEmail() + " My App version is "+PreferenceData.getAppVersion()+" I am new need help related to Offer: ";// Replace with your message.
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


    private void openEmailApp(){
        String userDetails;
        try {
            if (OnlookApplication.SELECTED_DEVICE != null) {

                 userDetails = OnlookApplication.SELECTED_DEVICE.toString();
            }
            else{
                userDetails = PreferenceData.getEmail();

            }

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", WebUtility.SUPPORT_EMAIL, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_build)+
                    PreferenceData.getAppVersion()+"\n Log Attached \n"+
                    userDetails+getString(R.string.support_email_body_text));
            startActivity(Intent.createChooser(emailIntent, getString(R.string.sub_title_support_email)));
        } catch (Exception e){
            e.printStackTrace();
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