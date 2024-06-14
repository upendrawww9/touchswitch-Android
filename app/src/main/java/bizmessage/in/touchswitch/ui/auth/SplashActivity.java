package bizmessage.in.touchswitch.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.databinding.ActivitySplashBinding;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;

public class SplashActivity extends AppCompatActivity {

    public final int SPLASH_TIME = 1000;
    private ActivitySplashBinding binding;
boolean skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
         if (PreferenceData.isDarkTheme()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        startApp();
    }

    private void startApp() {

        if(PreferenceData.isSkip() &&PreferenceData.isLogin()){
            Utility.showToast("Theme Changing");
            PreferenceData.setSkip(false);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);

        }
        else {
            new Handler().postDelayed(() -> {
                if (PreferenceData.isLogin()) {


                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);


                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }, SPLASH_TIME);
        }
        }

    @Override
    public void onBackPressed() {

    }

}
