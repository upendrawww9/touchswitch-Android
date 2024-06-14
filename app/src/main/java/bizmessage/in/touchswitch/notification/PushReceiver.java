package bizmessage.in.touchswitch.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.MainActivity;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.location.LocationUpdatesService;
import bizmessage.in.touchswitch.utils.PreferenceData;
import me.pushy.sdk.Pushy;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
public class PushReceiver extends BroadcastReceiver {

    private String timestamp;
    NotificationCompat.Builder builder = null;
    String allS, silS, notiS, ringS, vibS;
    private String myid;
    private String nikname;
    private String alert;
    private String alerttype;
    String mytone,mynoti="empty",is_sil,is_noti,is_ring,is_vib;

    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Resources resources;


        @Override
    public void onReceive(final Context context, Intent intent) {
            context .startService(new Intent(context, LocationUpdatesService.class));
            if(DBG) Log.d(" From PUSHY PLAYER  ","TONE NUM"+intent.getStringExtra("body"));
        Intent in = new Intent(context, RingtonePlayingService.class);
        context.stopService(in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if(PreferenceData.isLogin()) {
                    context.startService(new Intent(context, RingtonePlayingService.class));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if(DBG) Log.d("RING TONE PLAYER VIB2 ","LOGGEDI");
        if (PreferenceData.isLogin()) {
            if(DBG) Log.d("RING TONE PLAYER VIB ","LOGGEDI");
            if (intent.getStringExtra("devid") != null) {
                myid = intent.getStringExtra("devid");
                alert = intent.getStringExtra("alert");
                nikname = intent.getStringExtra("nikname");
                mytone = intent.getStringExtra("mytone");
                mynoti = intent.getStringExtra("mynoti");
                is_sil = intent.getStringExtra("is_sil");
                is_noti = intent.getStringExtra("is_noti");
                is_ring = intent.getStringExtra("is_ring");
                is_vib = intent.getStringExtra("is_vib");



        //Log.d("PUSHY ALERT IS ",alerttype);
                PreferenceData.setSilent(is_sil);
                PreferenceData.setNoti(is_noti);
                PreferenceData.setRing(is_ring);
                PreferenceData.setVib(is_vib);
                PreferenceData.setNotinum(mynoti);
                PreferenceData.setRingnum(mytone);

                if(!PreferenceData.getisPowerring()  && alert.equalsIgnoreCase("Online")){
                    PreferenceData.setSilent("true");
                    PreferenceData.setVib("false");
                }
                if(!PreferenceData.getisPowerring()  && alert.equalsIgnoreCase("Offline")){
                    PreferenceData.setSilent("true");
                    PreferenceData.setVib("false");
                }
                if(alert.equalsIgnoreCase("battery")){
                    PreferenceData.setSilent("true");
                    PreferenceData.setVib("false");
                }


                if (alert.equalsIgnoreCase("subscription")) {

                    PreferenceData.setSilent("true");


                }
                if (alert.equalsIgnoreCase("TEST")) { //for andmin only

                   // PreferenceData.setSilent("true"); //for admin only
                    alert="MOTION";
                  //  PreferenceData.setNoti("true"); //for admin
                   // PreferenceData.setNotinum("3"); //for admin
                }

                if (alert.equalsIgnoreCase("serverdown")) { //for andmin only
                    Log.d("PUSHY ALERT IS ","SERVERDOWN");
                  PreferenceData.setSilent("false"); //for admin only
                      PreferenceData.setNoti("true"); //for admin
                     PreferenceData.setNotinum("noti4"); //for admin
                    PreferenceData.setVib("true");
                   // PreferenceData.setRingnum("tone2");




                }

                if (alert.equalsIgnoreCase("connected")) { //for andmin only
                    Log.d("PUSHY ALERT IS ","CONNECTED");
                    PreferenceData.setSilent("true"); //for admin only

                    PreferenceData.setNoti("true"); //for admin
                    PreferenceData.setNotinum("noti4"); //for admin
                    PreferenceData.setVib("false");
                    // PreferenceData.setRingnum("tone2");
                }



                    String mtone = PreferenceData.getRingnum();
                if(DBG) Log.d("PUSHY PLAYER  ","TONE NUM"+mtone);

                String mring = PreferenceData.getRingnum();
                if(DBG) Log.d("PUSHY PLAYER  ","TONE NUM"+mring);


                alerttype = intent.getStringExtra("alerttype");
                timestamp = intent.getStringExtra("timestamp");
            }

            if(PreferenceData.isLogin()) {

                sendalarm(context, intent);
            }


        }
    }

    public void sendalarm(Context context, Intent intent) {

        //   try {
        ///       Uri notification = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.n1);
        //     Ringtone r = RingtoneManager.getRingtone(context, notification);
        //     r.play();
        //  } catch (Exception e) {
        //      e.printStackTrace();
        // }

        silS = PreferenceData.getSilent();
        notiS = PreferenceData.getNoti();
        ringS = PreferenceData.getRing();
        vibS = PreferenceData.getVib();


        if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(context, "ka");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(context, "te");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(context, "bn");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();


        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
        }


        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(context, "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(context, "gu");
            resources = context.getResources();
        }


        if (alert.equalsIgnoreCase("buzz")) {
            vibS = "true";
        }

        Intent i = new Intent(context, RingtonePlayingService.class);
        i.putExtra("id", "1");
        if (ringS=="true") {
            context.stopService(i);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, RingtonePlayingService.class));
        } else {
            context.startService(new Intent(context, RingtonePlayingService.class));
        }
        context.startService(i);


            Intent resultIntent1 = new Intent(context, MySwipeclose.class);
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.putExtra("IS_FROM",1);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1 /* Request code */, resultIntent, PendingIntent.FLAG_IMMUTABLE);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 1 /* Request code */, resultIntent1, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap;
        if (alert.equalsIgnoreCase("VIBRATION")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.vibration);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.vibration)
                    //.setContentTitle("ONLOOK VIBRATION ALERT @ : " + nikname)
                    .setContentTitle(resources.getString(R.string.pushy_vibration_alert)+" @" + nikname)

                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);
            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }




        if (alert.equalsIgnoreCase("battery")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.lowbattery);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSound(null)
                    .setSmallIcon(R.drawable.lowbattery)
                    .setContentTitle(resources.getString(R.string.lowbattery_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
//resources.getString(R.string.plug_control)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(2, builder.build());
        }



        if (alert.equalsIgnoreCase("MOTION")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.motionalert);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSound(null)
                   .setLights(Color.CYAN, 1000, 500)
                    .setSmallIcon(R.drawable.motionalert)
                    .setContentTitle(resources.getString(R.string.pushy_motion_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
//resources.getString(R.string.plug_control)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(2, builder.build());
        }


        //////
        if (alert.equalsIgnoreCase("FIRE")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.fire);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.fire)
                    //.setContentTitle("ONLOOK VIBRATION ALERT @ : " + nikname)
                    .setContentTitle(resources.getString(R.string.pushy_fire_alert)+" @" + nikname)

                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);
            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }

        if (alert.equalsIgnoreCase("MOTION")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.motionalert);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSound(null)
                    .setSmallIcon(R.drawable.motionalert)
                    .setContentTitle(resources.getString(R.string.pushy_motion_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
//resources.getString(R.string.plug_control)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(2, builder.build());
        }













        if (alert.equalsIgnoreCase("ServerTime")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.red);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.red)
                    .setContentTitle("ONLOOK SERVER ALERT @ : " + nikname)
                    .setContentText(" Time " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(3, builder.build());
        }

        if (alert.equalsIgnoreCase("Online")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.plugon);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.plugon)
                    .setContentTitle(resources.getString(R.string.pushy_connection_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/

                    .setAutoCancel(true)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(3, builder.build());

        }




/*
        if (alert.equalsIgnoreCase("serverdown")) {  //FOR ADMIN/OPERATION ONLY
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.server);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.server)
                    .setContentTitle("SERVER DOWN")
                    .setContentText("Time : "+ alerttype)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))

                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(4, builder.build());

        }



            if (alert.equalsIgnoreCase("connected")) {  //FOR ADMIN/OPERATION ONLY
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.connected);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.connected)
                    .setContentTitle("SERVER CONNECTED")
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentText("Time : "+ alerttype)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))


                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(4, builder.build());

        }
*/


        if (alert.equalsIgnoreCase("Offline")) {

            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.plugoff);
            // Prepare a notification with vibration, sound and lights
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.plugoff)
                    .setContentTitle(resources.getString(R.string.pushy_connection_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setDeleteIntent(pendingIntent1)
                    .setContentIntent(pendingIntent);

            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(3, builder.build());
        }

        if (alert.equalsIgnoreCase("buzz")) {

            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.bigdevice);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.device)
                    .setContentTitle(resources.getString(R.string.pushy_motion_alert)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.pushy_time)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true);

            Pushy.setNotificationChannel(builder, context);
            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Build the notification and display it
            notificationManager.notify(4, builder.build());
        }

        if (alert.equalsIgnoreCase("subscription")) {

           // Intent intent3 = new Intent(context, MainActivity.class);
           // context.startActivity(intent3);

            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.subscription);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.subscription)
                    .setContentTitle(resources.getString(R.string.onlook_sub)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.sub_end)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true);

            Pushy.setNotificationChannel(builder, context);
            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Build the notification and display it
            notificationManager.notify(4, builder.build());
        }

        if (alert.equalsIgnoreCase("subscription")) {

            // Intent intent3 = new Intent(context, MainActivity.class);
            // context.startActivity(intent3);

            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.subscription);
            // Prepare a notification with vibration, sound and lights
            builder = new NotificationCompat.Builder(context)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.subscription)
                    .setContentTitle(resources.getString(R.string.onlook_sub)+" @ " + nikname)
                    .setContentText(resources.getString(R.string.sub_end)+" " + timestamp)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setAutoCancel(true);

            Pushy.setNotificationChannel(builder, context);
            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Build the notification and display it
            notificationManager.notify(4, builder.build());
        }



    }

}