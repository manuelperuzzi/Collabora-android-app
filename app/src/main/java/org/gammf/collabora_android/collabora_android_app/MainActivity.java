package org.gammf.collabora_android.collabora_android_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utility utility = new Utility();

        Calendar firstTry = Calendar.getInstance();
        firstTry.set(Calendar.YEAR, 2017);
        firstTry.set(Calendar.MONTH, 7);
        firstTry.set(Calendar.DAY_OF_MONTH, 4);
        firstTry.set(Calendar.HOUR_OF_DAY, 10);
        firstTry.set(Calendar.MINUTE, 20);
        firstTry.set(Calendar.SECOND, 0);

        Calendar secondTry = Calendar.getInstance();
        secondTry.set(Calendar.YEAR, 2017);
        secondTry.set(Calendar.MONTH, 7);
        secondTry.set(Calendar.DAY_OF_MONTH, 4);
        secondTry.set(Calendar.HOUR_OF_DAY, 10);
        secondTry.set(Calendar.MINUTE, 21);
        secondTry.set(Calendar.SECOND, 0);

        utility.setAlarm(this,"First Event",firstTry);
        utility.setAlarm(this,"Second Event",secondTry);

    }
}


