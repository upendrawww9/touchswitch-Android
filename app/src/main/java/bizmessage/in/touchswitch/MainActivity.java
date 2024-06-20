/*
Look this
https://www.section.io/engineering-education/android-application-in-app-update-using-android-studio/

 */

package bizmessage.in.touchswitch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import bizmessage.in.touchswitch.databinding.ActivityMainBinding;
import bizmessage.in.touchswitch.location.LocationUpdatesService;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
//import me.pushy.sdk.Pushy;
//import me.pushy.sdk.util.PushyPowerSaving;

import static android.content.ContentValues.TAG;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
import android.content.res.Resources;
public class MainActivity extends AppCompatActivity implements DialogButtonClickListener {

    public static ActivityMainBinding bindingMain;
    private static WifiManager wifiManager;
    private static String ITEM_KEY;
    private static HashSet<HashMap<String, String>> arraylist;
    private static ArrayAdapter<Object> adapter;
    public int isFromMainActivity = 0;
    public static final String MESSAGE_STATUS = "message_status";
    int PERMISSION_ALL = 1;
    private static final ArrayList myArrayList = null;
    private TextView wifisignal;
    // String ITEM_KEY = "key";
    public static List<String> scanWifiResult = new ArrayList<>();
    public static List<ScanResult> scanResult=null;
    Context context;
    Resources resources;

   // String[] PERMISSIONS = {

     //       Manifest.permission.ACCESS_COARSE_LOCATION,
       //     android.Manifest.permission.ACCESS_FINE_LOCATION,
         //   Manifest.permission.WRITE_EXTERNAL_STORAGE,
           // Manifest.permission.ACCESS_WIFI_STATE,
           //Manifest.permission.CHANGE_WIFI_STATE,
          //  Manifest.permission.CHANGE_WIFI_MULTICAST_STATE
    //};
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;

    //private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;

    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;

    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
            new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity"))
    };

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    //WifiManager wifiManager;
    // UI elements.
    private Button mRequestLocationUpdatesButton;
    private Button mRemoveLocationUpdatesButton;
    final Handler handler = new Handler(Looper.getMainLooper());

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;


            mService = binder.getService();
            mBound = true;
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        bindingMain = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(bindingMain.toolbar);
        FirebaseAnalytics.getInstance(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add_device, R.id.navigation_other)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bindingMain.navView, navController);

/////FCM START
        reviewManager = ReviewManagerFactory.create(this);
        showRateApp();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d(TAG, "retrieve token successful : " + token);
                PreferenceData.setFCM(token);

            } else{
                Log.w(TAG, "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel

        }).addOnCompleteListener(task -> Log.v(TAG, "This is the token : " + task.getResult()));
        PreferenceData.setFCM(FirebaseMessaging.getInstance().getToken().toString());

        if(DBG)Log.d("FCM token", PreferenceData.getFCM());




        ///FCM END

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
        bindingMain.navView.getMenu().getItem(1).setTitle(resources.getString(R.string.title_add_device));
        bindingMain.navView.getMenu().getItem(2).setTitle(resources.getString(R.string.title_others));
        bindingMain.navView.getMenu().getItem(0).setTitle(resources.getString(R.string.title_home));


        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pInfo.versionCode;
            String ver = String.valueOf(versionNumber);
            //if(DBG)Utility.showToast(" VERSION "+ver);
            PreferenceData.setAppVersion(ver);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        bindingMain.switchTheme.setChecked(PreferenceData.isDarkTheme());

        if (PreferenceData.isDarkTheme()) {
            bindingMain.txtThemeName.setText(R.string.dark_theme);
            bindingMain.txtThemeName.setText(resources.getString(R.string.dark_theme));
        } else {
            bindingMain.txtThemeName.setText(R.string.light_theme);
            bindingMain.txtThemeName.setText(resources.getString(R.string.light_theme));
        }
        bindingMain.switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(DBG)Utility.showToast(" in MAIN"+isChecked);
            PreferenceData.setDarkTheme(isChecked);
            if (isChecked) {
                bindingMain.txtThemeName.setText(R.string.dark_theme);
                bindingMain.txtThemeName.setText(resources.getString(R.string.dark_theme));
                // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                PreferenceData.setSkip(true);
                triggerRebirth(getApplicationContext());

            } else {
                bindingMain.txtThemeName.setText(R.string.light_theme);
                bindingMain.txtThemeName.setText(resources.getString(R.string.light_theme));
                PreferenceData.setSkip(true);
                //     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                triggerRebirth(getApplicationContext());

            }


        });

        ////////////////////// work manager
        final WorkManager mWorkManager = WorkManager.getInstance();
        //  final OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
        //    PeriodicWorkRequest mRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
        //  mWorkManager.enqueue(mRequest);

        PeriodicWorkRequest.Builder myWorkBuilder =
                //        new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.HOURS);
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 16, TimeUnit.MINUTES);

        PeriodicWorkRequest myWork = myWorkBuilder.build();
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("onload", ExistingPeriodicWorkPolicy.KEEP, myWork);

        //Toast.makeText(MainActivity.this, "SUPER AND DUPER in function"+r_pushy_token, Toast.LENGTH_SHORT).show();


        mWorkManager.getWorkInfoByIdLiveData(myWork.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    //tvStatus.append(state.toString() + "\n");
                }
            }
        });
//////////// workmanager to start app at 12 am daily


        //work Manager


        // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }





     //   Pushy.setHeartbeatInterval(100, getApplicationContext());

        //.listen(this);
        //Pushy.setAppId("5f58951a291ae40f2441f1d7", getApplicationContext()); /// TO CHANGE if APP ID IS CHANGED
      //  Pushy.listen(this);
        //Pushy.setAppId("65abee5762800d1221104b05", getApplicationContext()); /// TO CHANGE if APP ID IS CHANGED
        //   Pushy.toggleFCM(true, getApplicationContext());
       // Pushy.setPushyDeviceCredentials();
   //Pushy.unregister(getApplicationContext());






        //if (!hasPermissions(this, PERMISSIONS)) {
          //  ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);


            //   Uri number = Uri.parse("tel:7021970585");
            // Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            // startActivity(callIntent);


        //} else {
//            confirm("Permsission Request","This will require Permissions to work and receive Notification Alerts");
       // }

        //if (!checkPermissions()) {
            //requestPermissions();
       // }
        //Pushy.unregister(getApplicationContext());
      //  PreferenceData.setDarkTheme(false);
        isFromMainActivity = getIntent().getIntExtra("IS_FROM", 0);


        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
           if(DBG)     Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();

                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if(DBG)    Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();

                removeInstallStateUpdateListener();
            } else {
                if(DBG)   Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
            }
        };

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        checkUpdateFlex();
         getWifi(getApplicationContext());
/*
        if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            initOPPO();
        } else if (Build.MANUFACTURER.equalsIgnoreCase("vivo")) {
            autoLaunchVivo(MainActivity.this);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/
    }

    //inapp rating

    public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                Log.d("myTag","Review can be requested");
                reviewInfo = task.getResult();

                Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, continue regardless of the result.
                // show native rate app dialog on error
                showRateAppFallbackDialog();
            }
        });
    }

    /**
     * Showing native dialog with three buttons to review the app
     * Redirect user to playstore to review the app
     */
    private void showRateAppFallbackDialog() {
        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle(R.string.rate_app_title)
                .setMessage(R.string.rate_app_message)
                .setPositiveButton(R.string.rate_btn_pos, (dialog, which) -> {

                })
                .setNegativeButton(R.string.rate_btn_neg,
                        (dialog, which) -> {
                        })
                .setNeutralButton(R.string.rate_btn_nut,
                        (dialog, which) -> {
                        })
                .setOnDismissListener(dialog -> {
                })
                .show();
    }

    public static void getWifi(Context context) {
        if (DBG) Log.d(TAG, "Wifi SSID AVAILABLE ENT");
        wifiManager = (WifiManager)
                context.getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess(context);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);
    }

    public static void scanSuccess(Context context) {
        scanResult = wifiManager.getScanResults();
        int size = scanResult.size();
        PreferenceData.setWifino(0);
        PreferenceData.setWifino(size);
        if(DBG) Log.d("WIFI UPDATE1", ""+PreferenceData.getWifino());
        if(DBG) Log.d("WIFI UPDATE2", ""+size);



        try {
            for (int i = 0; i < size; i++) {

                if (scanResult.get(i).SSID.startsWith("Million-")) {

                }
                else {
                    scanWifiResult.add(scanResult.get(i).SSID);
                    if(DBG) Log.d("WIFI UPDATE3", ""+scanResult.get(i).SSID);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }



    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
        checkConnection(this, this,resources);
    }

    public static void checkConnection(Context context, DialogButtonClickListener listener,Resources resources) {
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
        checkConnection(this, this,resources);
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

    private void checkUpdateFlex() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            }
        });
    }

    private void checkUpdateImdetiate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, MainActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Install", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.purple_500))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
        removeInstallStateUpdateListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdateFlex();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

        handler.postDelayed(() -> {
            try{
                    mService.requestLocationUpdates();
            }catch(Exception io){Log.d("location exp ",io.toString());}

            for (Intent intent : POWERMANAGER_INTENTS)
                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            // show dialog to ask user action


                            break;
                        }

            //Do something after 100ms
        }, 3000);
       if(DBG) Log.d("LOCATION UPDATE", "IN ONSTART");
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }


    /**
     * Returns the current state of the permissions needed.
     */





    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.getDefault());
         //      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

                String currentDateandTime = sdf.format(new Date());
             //  if(DBG) Utility.showToast(currentDateandTime);
               // if(DBG) Utility.showToast(Double.toString(location.getLatitude()));
              //  PreferenceData.setLatitude(Double.toString(location.getLatitude()));
               // PreferenceData.setLongitude(Double.toString(location.getLongitude()));
                PreferenceData.setLoctime(currentDateandTime);

            }
        }
    }

    private void initOPPO() {
        try {

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity"));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            try {

                Intent intent = new Intent("action.coloros.safecenter.FloatWindowListActivity");
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity"));
                startActivity(intent);
            } catch (Exception ee) {

                ee.printStackTrace();
                try {

                    Intent i = new Intent("com.coloros.safecenter");
                    i.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity"));
                    startActivity(i);
                } catch (Exception e1) {

                    e1.printStackTrace();
                }
            }

        }
    }

    private static void autoLaunchVivo(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                context.startActivity(intent);
            } catch (Exception ex) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                    context.startActivity(intent);
                } catch (Exception exx) {
                    ex.printStackTrace();
                }
            }
        }
    }


}