package bizmessage.in.touchswitch.ui.alerts;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.OnlookApplication;
import bizmessage.in.touchswitch.databinding.ActivityAlertsListBinding;
import bizmessage.in.touchswitch.model.AlertsRequestResponse;
import bizmessage.in.touchswitch.notification.RingtonePlayingService;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.retrofit.WebUtility;
import bizmessage.in.touchswitch.utils.DialogButtonClickListener;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class AlertListActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonClickListener {

    private static final String TAG = AlertListActivity.class.getSimpleName();
    private ActivityAlertsListBinding binding;
    private AlertListAdapter alertListAdapter;
    private final ArrayList<String> alertList = new ArrayList<>();
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alerts_list);

        setSupportActionBar(binding.toolbar);
       // getSupportActionBar().setTitle(R.string.alerts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.cardWebView.setVisibility(View.GONE);
        binding.opencctv.setOnClickListener(this);

        if(OnlookApplication.SELECTED_DEVICE.getIscampackage().equals("") && OnlookApplication.SELECTED_DEVICE.getIscamurl().equals("")) {
            binding.opencctv.setVisibility(View.GONE);
        }
        else{
            binding.opencctv.setVisibility(View.VISIBLE);
        }


        RecylerViewVideoClickListener recylerViewVideoClickListener = alertText ->  Log.i(TAG, "onItemClickListener: "+alertText);


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

      //  binding.textDeviceId.setText(resources.getString(R.string.device)+" : "+OnlookApplication.SELECTED_DEVICE.getDevid());
        binding.textDeviceId.setText(resources.getString(R.string.device)+" - " + OnlookApplication.SELECTED_DEVICE.getNikname()+" : "+OnlookApplication.SELECTED_DEVICE.getSsid());

        //getSupportActionBar().setTitle(R.string.alerts);
        getSupportActionBar().setTitle(resources.getString(R.string.alerts)); // title setting here
      binding.textWebView.setText(resources.getString(R.string.btn_view_more_on_web_browser));


        binding.opencctv.setOnClickListener(v -> {

           launchNewActivity(OnlookApplication.SELECTED_DEVICE.getIscamurl(),OnlookApplication.SELECTED_DEVICE.getIscampackage());

        });

        binding.cardWebView.setOnClickListener(v -> {
            String reportWebUrl = WebUtility.BASE_URL+WebUtility.MORE_REPORTS_DATA+"uname="+ PreferenceData.getEmail()+"&randomp="+PreferenceData.getPassword()+"&myid="+ OnlookApplication.SELECTED_DEVICE.getDevid();
            Intent intentCardWebView = new Intent(Intent.ACTION_VIEW);
            intentCardWebView.setData(Uri.parse(reportWebUrl));
            startActivity(intentCardWebView);
        });

        alertListAdapter = new AlertListAdapter(this,alertList,recylerViewVideoClickListener);
        binding.recyclerViewAlertList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewAlertList.setAdapter(alertListAdapter);
    }

    public void launchNewActivity(String url,String packageName) {


        if (packageName!= null) {
             PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
            }
        } else {

           //  String reportWebUrl = BASE_URL11+WebUtility.MORE_REPORTS_DATA+"uname="+ PreferenceData.getEmail()+"&randomp="+PreferenceData.getPassword()+"&myid="+ OnlookApplication.SELECTED_DEVICE.getDevid();
         if(!url.equals("")) {
             Intent intentCardWebView = new Intent(Intent.ACTION_VIEW);
             intentCardWebView.setData(Uri.parse(url));
             startActivity(intentCardWebView);
         }
         else{
             Utility.showToast("Package Not Approved by Onlook.");

         }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAlertData();
       /* ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

        getAlertData();
            }
        }, 0, 15, TimeUnit.SECONDS);
*/


        stopRingService();


    }

    private void stopRingService(){
        try {
            Intent ringIntent = new Intent(getApplicationContext(), RingtonePlayingService.class);
            getApplicationContext().stopService(ringIntent);




        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void getAlertData() {
        if (!Utility.isNetworkAvailable(this)) {
            checkConnection(AlertListActivity.this,this);
        } else {
            Utility.showProgress(this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<AlertsRequestResponse> responseCall = service.getMyReportsData("all", OnlookApplication.SELECTED_DEVICE.getDevid());
            responseCall.enqueue(new Callback<AlertsRequestResponse>() {
                @Override
                public void onResponse(Call<AlertsRequestResponse> call, Response<AlertsRequestResponse> response) {
                    if (response.isSuccessful()) {
                        AlertsRequestResponse alertsRequestResponse = response.body();
                        alertList.clear();

                        if (alertsRequestResponse.getSuccess() == 1) {

                            if(alertsRequestResponse.getEsps().size() > 15) {
                                if (DBG) Log.i(TAG, "Aleert Size: " + alertsRequestResponse.getEsps().size());
                                binding.cardWebView.setVisibility(View.VISIBLE);
                            }


                            for (int i=0;i<alertsRequestResponse.getEsps().size();i++){
                                String desc = (String) ((LinkedTreeMap) response.body().getEsps().get(i)).get("desc");
                                alertList.add(desc);
                            }
                            alertListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Utility.showSnackBar(binding.cardWebView,response.message());
                    }
                    Utility.hideProgress();
                }

                @Override
                public void onFailure(Call<AlertsRequestResponse> call, Throwable t) {
                    if(DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                        Utility.showToast("Website Not reachable, Please check internet");
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
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
