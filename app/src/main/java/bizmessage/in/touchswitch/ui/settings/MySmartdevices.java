package bizmessage.in.touchswitch.ui.settings;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.databinding.ActivityHelpBinding;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.ui.others.Language;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

public class MySmartdevices extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = MySmartdevices.class.getSimpleName();
    private ActivityHelpBinding binding;
    final Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Resources resources;
    String toNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        setSupportActionBar(binding.toolbar);
      //  getSupportActionBar().setTitle("Onlook App Version : "+PreferenceData.getAppVersion());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.imgWhatsApp.setOnClickListener(this);

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


          getSupportActionBar().setTitle(resources.getString(R.string.app_version)+PreferenceData.getAppVersion());


        Utility.showProgress(this);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
              //  Utility.hideProgress();

            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.hideProgress();


            }
        }, 5000);


        // binding.webView.loadUrl("http://www.google.com");
          Log.d(TAG, "WEBHELP "+WebUtility.WEB_Contrls);
        binding.webView.loadUrl(WebUtility.WEB_Contrls+ PreferenceData.getEmail()+"&pass="+PreferenceData.getPassword());

       // binding.webView.loadUrl(WebUtility.WEB_HELP+ PreferenceData.getEmail()+"&password   ="+PreferenceData.getPassword());

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
    private void openWhatsApp(){
        String text;
        try {
            if (TouchApplication.SELECTED_DEVICE != null) {


                text = "My Email id is " + PreferenceData.getEmail() + " .My device id : " + TouchApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Support I Need : ";// Replace with your message.
                if(TouchApplication.SELECTED_DEVICE.getCall911().length()>8) {
                    toNumber = "91" + TouchApplication.SELECTED_DEVICE.getRWhatsapp(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
                }
                else{//if user is not set to dealer by mistake it will route to support number
                    text = "My support Not Set.My Email id is " + PreferenceData.getEmail() + " .My device id : " + TouchApplication.SELECTED_DEVICE.getDevid() + ". My App version is "+PreferenceData.getAppVersion()+" Support I Need : ";// Replace with your message.

                    toNumber = "917900119635"; // Replace with mobile phone number without +Sign or leading zeros, but with country code

                }
            }
            else{ // ffor a new user or demo user
                text = "My Email id is " + PreferenceData.getEmail() + ". My App version is "+PreferenceData.getAppVersion()+" I need Support for : ";// Replace with your message.
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
        Intent intentLanguage = new Intent(MySmartdevices.this, SettingsActivity.class);
        startActivity(intentLanguage);

    }
/*
    @Override //to get back page on webview upendra
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
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

}
