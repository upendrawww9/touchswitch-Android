package bizmessage.in.touchswitch.app;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import bizmessage.in.touchswitch.model.DashboardRequestResponse;
import bizmessage.in.touchswitch.utils.SharedPreferenceUtil;

import java.util.ArrayList;

public class OnlookApplication extends MultiDexApplication {

    private static OnlookApplication mInstance;
    public static ArrayList<DashboardRequestResponse.Esp> deviceList = new ArrayList<>();
    public static int currentDevicePosition = 0;
    public static DashboardRequestResponse.Esp SELECTED_DEVICE = null;

    public static synchronized OnlookApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SharedPreferenceUtil.init(this);
        SharedPreferenceUtil.save();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
