package bizmessage.in.touchswitch.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
//import com.google.firebase.quickstart.fcm.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.app.TouchApplication;
import bizmessage.in.touchswitch.location.LocationUpdatesService;
import bizmessage.in.touchswitch.retrofit.WebServiceCaller;
import bizmessage.in.touchswitch.utils.PreferenceData;
import bizmessage.in.touchswitch.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
import android.content.res.Resources;
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FIREBASE UPENDRA";
    NotificationCompat.Builder notificationBuilder;
    private String timestamp;
    String allS, silS, notiS, ringS, vibS;
    private String myid;
    private String nikname;
    private String alert;
    private String alerttype;
    String mytone,mynoti="empty",is_sil,is_noti,is_ring,is_vib;
    Resources resources;
    Context context;
    NotificationCompat.Builder builder = null;
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage);

        getApplicationContext().startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        stopService(new Intent(this,RingtonePlayingService.class));

        if(PreferenceData.isLogin()) {

            startService(new Intent(this, RingtonePlayingService.class));
        }


      //  PreferenceData.setSilent("false"); //for admin only

      //  PreferenceData.setNoti("true"); //for admin
      //  PreferenceData.setNotinum("noti3"); //for admin
      //  PreferenceData.setVib("true");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
        //    Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("title").toString());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message From " + remoteMessage.getFrom()); //sender ID
            Log.d(TAG, "Message Notification Title " + remoteMessage.getNotification().getTitle()); //notification title
            Log.d(TAG, "Message Notification Body " + remoteMessage.getNotification().getBody()); //notification body
       //     Log.e(TAG, "Data Payload 2: " + remoteMessage.getData().get("body").toString());
       //     sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            PreferenceData.setOffer(remoteMessage.getNotification().getBody());
if(remoteMessage.getNotification().getTitle().length()>3) {
 // sendNotification("onload");



}

        }


        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.
    //    Log.e(TAG, "Data Payload: " + remoteMessage.getData().get("body").toString());
        //message will contain the Push Message
        String message = remoteMessage.getData().get("id");
        String body = remoteMessage.getData().get("body");
        Log.d(TAG, "Message data payload BODY: " + body);
        Log.d(TAG, "Message data payload message: " + message);
        updateFiretoken(PreferenceData.getFcmToken());
        //imageUri will contain URL of the image to be displayed with Notification
        String imageUri = remoteMessage.getData().get("image");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");
///


        ////

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(message!=null) {
            if(PreferenceData.isLogin()) {
                sendNotification(remoteMessage, "onload", message);
            }
        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        if(token.length()>5) {
            PreferenceData.setFcmToken(token);
            updateFiretoken(token);
        }


    }

    private void updateFiretoken(String token) {
        if (!Utility.isNetworkAvailable(MyFirebaseMessagingService.this)) {
          //  Utility.showSnackBar(binding.appBarLayout,getString(R.string.no_internet_connection));
        } else {
          //  Utility.showProgress(MotionControlSettingsActivity.this);
            WebServiceCaller.ApiInterface service = WebServiceCaller.getClient();
            Call<ResponseBody> responseCall = service.updateToken(token, PreferenceData.getEmail(),PreferenceData.getLatitude(),PreferenceData.getLongitude(),PreferenceData.getLoctime());

            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                    } else {
                       // Utility.showSnackBar(binding.appBarLayout,response.message());
                    }
                   // Utility.hideProgress();
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    if(DBG) Log.i(TAG, "onFailure: " + t.toString());
                    Utility.hideProgress();
                    if(t.toString().contains("UnknownHostException")){
                     //   Utility.showToast(resources.getString(R.string.status_website));
                    }
                }
            });
        }
    }


    private void sendNotification(RemoteMessage remoteMessage,String messageBody,String id) {

        Context context;
        myid = remoteMessage.getData().get("devid");
        alert = remoteMessage.getData().get("alert");
        nikname = remoteMessage.getData().get("nikname");
        mytone = remoteMessage.getData().get("mytone");
        mynoti = remoteMessage.getData().get("mynoti");
        is_sil = remoteMessage.getData().get("is_sil");
        is_noti = remoteMessage.getData().get("is_noti");
        is_ring = remoteMessage.getData().get("is_ring");
        is_vib = remoteMessage.getData().get("is_vib");

        alerttype = remoteMessage.getData().get("alerttype");
        timestamp = remoteMessage.getData().get("timestamp");
      //  Utility.showToast("TIME"+timestamp);

        if (TouchApplication.SELECTED_DEVICE != null) {
            if (TouchApplication.SELECTED_DEVICE.getIsshared().equals(PreferenceData.getLoginid())) {

                PreferenceData.setSilent(is_sil);
                PreferenceData.setNoti(is_noti);
                PreferenceData.setRing(is_ring);
                PreferenceData.setVib(is_vib);
                PreferenceData.setNotinum(mynoti);
                PreferenceData.setRingnum(mytone);

            }
            else{


                PreferenceData.setSilent(is_sil);
                PreferenceData.setNoti(is_noti);
                PreferenceData.setRing(is_ring);
                PreferenceData.setVib(is_vib);
                PreferenceData.setNotinum(mynoti);
                PreferenceData.setRingnum(mytone);
            }
        }



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


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "22";
        Bitmap bitmap;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        if (alert.equalsIgnoreCase("battery")) {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.lowbattery);
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.lowbattery)
                            .setLargeIcon(bitmap)/*Notification icon image*/

                            .setContentTitle(resources.getString(R.string.lowbattery_alert)+" @ " + nikname)
                            .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap))/*Notification with Image*/
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

        }

        if (alert.equalsIgnoreCase("MOTION")) {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.motionalert);
          notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.motionalert)
                            .setLargeIcon(bitmap)/*Notification icon image*/

                            .setContentTitle(resources.getString(R.string.pushy_motion_alert)+" @ " + nikname)
                            .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap))/*Notification with Image*/
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

        }


        if (alert.equalsIgnoreCase("VIBRATION")) {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.vibration);
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.vibration)
                            .setLargeIcon(bitmap)/*Notification icon image*/
                            .setSmallIcon(R.drawable.vibration)
                            //.setContentTitle("ONLOOK VIBRATION ALERT @ : " + nikname)
                            .setContentTitle(resources.getString(R.string.pushy_vibration_alert) + " @" + nikname)

                            .setContentText(resources.getString(R.string.pushy_time) + " " + timestamp)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

        }


        if (alert.equalsIgnoreCase("FIRE")) {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.fire);
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.fire)
                            .setLargeIcon(bitmap)/*Notification icon image*/
                            .setSmallIcon(R.drawable.fire)
                            //.setContentTitle("ONLOOK VIBRATION ALERT @ : " + nikname)
                            .setContentTitle(resources.getString(R.string.pushy_fire_alert) + " @" + nikname)

                            .setContentText(resources.getString(R.string.pushy_time) + " " + timestamp)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

        }


        if (alert.equalsIgnoreCase("Online")) {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.plugon);

            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setLargeIcon(bitmap)/*Notification icon image*/
                            .setSmallIcon(R.drawable.plugon)
                            .setContentTitle(resources.getString(R.string.pushy_connection_alert)+" @ " + nikname)
                            .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap))/*Notification with Image*/

                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


        }

        if (alert.equalsIgnoreCase("Offline")) {

            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.plugoff);

            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                         .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.plugoff)
                    .setContentTitle(resources.getString(R.string.pushy_connection_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/

                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


        }

        if (alert.equalsIgnoreCase("buzz")) {

            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_bulb_on);

            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                       .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.device)
                    .setContentTitle(resources.getString(R.string.pushy_motion_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


        }












        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "onload-FCM",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(10 /* ID of notification */, notificationBuilder.build());










    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
