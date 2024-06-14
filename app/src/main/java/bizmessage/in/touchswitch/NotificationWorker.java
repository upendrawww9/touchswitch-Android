package bizmessage.in.touchswitch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;

import bizmessage.in.touchswitch.location.LocationUpdatesService;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.utils.PreferenceData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

/**
 * Created on : 02-09-2020
 * Author     : Upendra
 */

public class NotificationWorker extends Worker {
    private static final String WORK_RESULT = "work_result";
    private static final String TAG = NotificationWorker.class.getSimpleName();

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
         getApplicationContext().startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        Data taskData = getInputData();
        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        if(DBG) Log.d(TAG, "IS APP TO RESTART : FALSE 0" + cal.get(Calendar.HOUR_OF_DAY));
        restartApp();

        if (cal.get(Calendar.MINUTE) > 30  && cal.get(Calendar.MINUTE) < 59) {
            if(DBG) Log.d(TAG, "IS APP TO RESTART : FALSE 1");
          //  showNotification("Onlook is Running "  , taskDataString != null ? taskDataString : "");
           // restartApp();
        }

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        return Result.success(outputData);
    }

    private void showNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.dashimg);
      //  manager.notify(1, builder.build());
      //  builder.setDefaults(Notification.DEFAULT_ALL);
    }

    private void restartApp() {
        WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
        String email=PreferenceData.getEmail();
        String pass=null;
        if(email.equalsIgnoreCase("member@gmail.com")){
            pass=PreferenceData.getPassword();
        }

        Call<ResponseBody> responseCall = service.updateHartBeatService(email,pass,PreferenceData.getLatitude(),PreferenceData.getLongitude(),PreferenceData.getLoctime());
        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if(DBG) Log.i(TAG, "onFailure: " + t.toString());
                if(t.toString().contains("UnknownHostException")){
                   // Utility.showToast("Website Not reachable, Please check internet");
                }
            }
        });
    }

}