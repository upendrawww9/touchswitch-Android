package bizmessage.in.touchswitch.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import java.io.IOException;

import bizmessage.in.touchswitch.utils.PreferenceData;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;
public class RingtonePlayingService extends Service {
    private Ringtone ringtone1;
    private String id;
    private Vibrator vibrator;
    String allS, silS, notiS, ringS, vibS,noti,tone;
    MediaPlayer player1;
    MediaPlayer player2;
    MediaPlayer player3;
    MediaPlayer player4; //noti1
    MediaPlayer player5; //noti2
    MediaPlayer player6; //noti3
    MediaPlayer player7;  //noti4

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        silS = PreferenceData.getSilent();
        notiS = PreferenceData.getNoti();
        ringS = PreferenceData.getRing();
        vibS = PreferenceData.getVib();
        noti = PreferenceData.getNotinum();
        tone = PreferenceData.getRingnum();

        if (ringtone1 != null) {

            this.ringtone1.stop();
        }
        if (player1 != null) {
            this.player1.stop();
        }
        if (player2 != null) {
            this.player2.stop();
        }

        if (player3 != null) {
            this.player3.stop();
        }


        if (player4 != null) {
            this.player4.stop();
        }
        if (player5 != null) {
            this.player5.stop();
        }

        if (player6 != null) {
            this.player6.stop();
        }

        if (player7 != null) {
            this.player7.stop();
        }





       //if(DBG) Log.d("RING TONE PLAYER SIL ","SIL"+silS);
   if(DBG) Log.d("RING TONE PLAYER NOT ","IS NOTI"+ notiS);
     //    if(DBG) Log.d("RING TONE PLAYER RING ","RING"+ringS);
         if(DBG) Log.d("RING TONE PLAYER VIB ","VIB"+vibS);
         if(DBG) Log.d("RING TONE PLAYER VIB ","NOTI"+noti);
     //  if(DBG) Log.d("RING TONE PLAYER VIB ","TONE"+tone);



        if (vibS.equalsIgnoreCase("true")) {
            // if(DBG) Log.d("RING TONE PLAYER VIB ","VIB 1"+vibS);

            if (Build.VERSION.SDK_INT >= 26) {
                try {
                    // if(DBG) Log.d("RING TONE PLAYER VIB ","VIB 2"+vibS);
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(350,10));
                //} else {
                  //  ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
                //}
                              } catch (NullPointerException np) {
                    np.printStackTrace();
                }
            } else {
                // if(DBG) Log.d("RING TONE PLAYER VIB ","VIB 3"+vibS);

                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Start without a delay
                // Vibrate for 100 milliseconds
                // Sleep for 1000 milliseconds
                //   long[] pattern = {0, 300, 1000, 500, 200, 100, 500, 400, 100};
                long[] pattern = {0, 1000, 100, 3000, 300, 2000};

                // The '0' here means to repeat indefinitely
                // '0' is actually the index at which the pattern keeps repeating from (the start)
                // To repeat the pattern from any other point, you could increase the index, e.g. '1'
                //noinspection ConstantConditions
                vibrator.vibrate(pattern, -1);
            }
        }
/*
silS="false";
notiS="true";
noti="1";
tone="1";


        silS="false";
*/
        if (silS.equalsIgnoreCase("false")) {
            // if(DBG) Log.d("RING TONE PLAYER SIL ","SIL"+silS);
            Uri notification;
            if (notiS.equalsIgnoreCase("true")) {
                // if(DBG) Log.d("RING TONE PLAYER NOT ","NOTI"+ notiS);
                noti = PreferenceData.getNotinum();
           if(DBG) Log.d("RING TONE PLAYER NOT ","NOTI NUMBER "+ noti);

                if (noti.equalsIgnoreCase("noti1")) {
                    if (player4 != null) {
                        this.player4.stop();
                    }
                    if (player5 != null) {
                        this.player5.stop();
                    }
                    if (player6 != null) {
                        this.player6.stop();
                    }
                    if (player7 != null) {
                        this.player7.stop();
                    }
                    startSound4("mp3/n1.mp3");
                }

                if (noti.equalsIgnoreCase("noti2")) {
                    // if(DBG) Log.d("RING TONE PLAYING ","VIB 3");
                    if (player4 != null) {
                        this.player4.stop();
                    }
                    if (player5 != null) {
                        this.player5.stop();
                    }

                    if (player6 != null) {
                        this.player6.stop();
                    }

                    if (player7 != null) {
                        this.player7.stop();
                    }
                    startSound5("mp3/n2.mp3");
                }

                if (noti.equalsIgnoreCase("noti3")) {
                    if (player4 != null) {
                        this.player4.stop();
                    }
                    if (player5 != null) {
                        this.player5.stop();
                    }
                    if (player6 != null) {
                        this.player6.stop();
                    }
                    if (player7 != null) {
                        this.player7.stop();
                    }
                    startSound6("mp3/n3.mp3");
                }

                if (noti.equalsIgnoreCase("noti4")) {
                    if (player4 != null) {
                        this.player4.stop();
                    }
                    if (player5 != null) {
                        this.player5.stop();
                    }
                    if (player6 != null) {
                        this.player6.stop();
                    }

                    if (player7 != null) {
                        this.player7.stop();
                    }
                    startSound7("mp3/n4.mp3");
                }
            }

            // if(DBG) Log.d("RING TONE PLAYER RING ","RING"+ringS);
            if (ringS.equalsIgnoreCase("true")) {
                // if(DBG) Log.d("RING TONE PLAYER RING ","RING"+ringS);
                tone = PreferenceData.getRingnum();
                 if(DBG) Log.d("RING TONE PLAYER  ","TONE NUM"+tone);

                if (tone.equalsIgnoreCase("tone1")) {
                    if (ringtone1 != null) {
                        this.ringtone1.stop();
                    }
                    if (player1 != null) {
                        this.player1.stop();
                    }
                    if (player2 != null) {
                        this.player2.stop();
                    }
                    if (player3 != null) {
                        this.player3.stop();
                    }
                    notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    this.ringtone1 = RingtoneManager.getRingtone(this, notification);
                    ringtone1.play();
                }

                // return START_NOT_STICKY;
                if (tone.equalsIgnoreCase("tone2")) {
                    if (ringtone1 != null) {
                        this.ringtone1.stop();
                    }
                    if (player1 != null) {
                        this.player1.stop();
                    }
                    if (player2 != null) {
                        this.player2.stop();
                    }

                    if (player3 != null) {
                        this.player1.stop();
                    }
                    // if(DBG) Log.d("RINGING  PLAYING 2 ","VIB 3");
                    startSound1("mp3/fire.mp3");
                }
                if (tone.equalsIgnoreCase("tone3")) {
                    if (ringtone1 != null) {
                        this.ringtone1.stop();
                    }
                    if (player1 != null) {
                        this.player1.stop();
                    }
                    if (player2 != null) {
                        this.player2.stop();
                    }

                    if (player3 != null) {
                        this.player2.stop();
                    }
                    startSound2("mp3/siren2.mp3");
                }

                if (tone.equalsIgnoreCase("tone4")) {
                    if (ringtone1 != null) {
                        this.ringtone1.stop();
                    }
                    if (player1 != null) {
                        this.player1.stop();
                    }
                    if (player2 != null) {
                        this.player2.stop();
                    }
                    if (player3 != null) {
                        this.player3.stop();
                    }
                    startSound3("mp3/burglar.mp3");
                }
            }

        }

        return START_NOT_STICKY;

    }

    private void startSound1(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player1 = new MediaPlayer();
        try {
            assert afd != null;
            player1.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player1.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player1.start();
    }

    private void startSound2(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player2 = new MediaPlayer();
        player2.setVolume(50, 50);
        try {
            assert afd != null;
            player2.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player2.start();
    }

    private void startSound3(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player3 = new MediaPlayer();
        try {
            assert afd != null;
            player3.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player3.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player3.start();
    }

    private void startSound4(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player4 = new MediaPlayer();
        try {
            assert afd != null;
            player4.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player4.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player4.start();
    }

    private void startSound5(String filename) {

        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player5 = new MediaPlayer();
        try {
            assert afd != null;
            player5.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player5.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player5.start();
    }

    private void startSound6(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player6 = new MediaPlayer();
        try {
            assert afd != null;
            player6.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player6.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player6.start();
    }


    private void startSound7(String filename) {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player7 = new MediaPlayer();
        try {
            assert afd != null;
            player7.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player7.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player7.start();
    }

    public void stop() {
        ringtone1.stop();
       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RingtonePlayingService.this);
        String vibS = PreferenceData.getVib();
        if (vibS.equalsIgnoreCase("true")) {
            if (id != null) {
                try {
                    vibrator.cancel();
                } catch (RuntimeException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RingtonePlayingService.this);

        if (ringtone1 != null) {

            this.ringtone1.stop();
        }
        if (player1 != null) {
            this.player1.stop();
        }
        if (player2 != null) {
            this.player2.stop();
        }

        if (player3 != null) {
            this.player3.stop();
        }


        if (player4 != null) {
            this.player4.stop();
        }
        if (player5 != null) {
            this.player5.stop();
        }

        if (player6 != null) {
            this.player6.stop();
        }

        if (player7 != null) {
            this.player7.stop();
        }



        String ringS = PreferenceData.getRing();


        if (ringS.equalsIgnoreCase("true")) {
            try {
                if (ringtone1 != null) {
                    this.ringtone1.stop();
                }
                if (player1 != null) {
                    this.player1.stop();
                }
                if (player2 != null) {
                    this.player2.stop();
                }
                if (player3 != null) {
                    this.player3.stop();
                }
            } catch (RuntimeException exp) {
                exp.printStackTrace();
            }
        }
        String vibS = PreferenceData.getVib();
        if (id != null) {
            try {
                vibrator.cancel();
            } catch (RuntimeException exp) {
                exp.printStackTrace();
            }
        }
    }
}