package bizmessage.in.touchswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MillionBootReceiver extends BroadcastReceiver {
    private static final String TAG = "Onlook";
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent myIntent = new Intent(context, bizmessage.in.touchswitch.ui.auth.SplashActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);

    }
}
