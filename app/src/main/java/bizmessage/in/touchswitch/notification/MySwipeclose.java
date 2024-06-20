package bizmessage.in.touchswitch.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

import bizmessage.in.touchswitch.MainActivity;


public class MySwipeclose extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
if(DBG) Log.d("CALLED STOP","SWIPECLOSE");

        try {
            Intent intent1 = new Intent(MySwipeclose.this, MainActivity.class);
            startActivity(intent1);
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        finish();


    }

}