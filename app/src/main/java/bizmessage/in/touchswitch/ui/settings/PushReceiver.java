package bizmessage.in.touchswitch.ui.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class PushReceiver extends BroadcastReceiver {

    private final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(DBG) Log.i(TAG, "onReceive: ");
    }

}