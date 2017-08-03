package org.gammf.collabora_android.collabora_android_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);

        Calendar firstTry = Calendar.getInstance();
        firstTry.set(Calendar.YEAR, 2017);
        firstTry.set(Calendar.MONTH, 7);
        firstTry.set(Calendar.DAY_OF_MONTH, 3);
        firstTry.set(Calendar.HOUR_OF_DAY, 12);
        firstTry.set(Calendar.MINUTE, 59);
        firstTry.set(Calendar.SECOND, 0);

        Calendar secondTry = Calendar.getInstance();
        secondTry.set(Calendar.YEAR, 2017);
        secondTry.set(Calendar.MONTH, 7);
        secondTry.set(Calendar.DAY_OF_MONTH, 3);
        secondTry.set(Calendar.HOUR_OF_DAY, 13);
        secondTry.set(Calendar.MINUTE, 00);
        secondTry.set(Calendar.SECOND, 0);

        setAllarm(am,"First Event",firstTry);
        setAllarm(am,"Second Event",secondTry);

    }

    private void setAllarm(AlarmManager allarm, String message, Calendar timeToSpawn){
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("title",message);
        final int _id = (int) System.currentTimeMillis();// pendingIntend MUST have different id if we want multiple allarms to set
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, _id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        allarm.set(AlarmManager.RTC_WAKEUP, timeToSpawn.getTimeInMillis(), pendingIntent);
    }
}


