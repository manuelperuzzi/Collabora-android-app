package org.gammf.collabora_android.app;

import android.app.IntentService;
import android.content.Intent;

import org.gammf.collabora_android.app.location_geofence.GeofenceManager;

/**
 * Service that manage rebooting procedure
 * Created by Federico on 04/08/2017.
 */

public class BootService extends IntentService {

    private GeofenceManager geoManager;

    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // reset here all allarms and all geofences...
        //take from db or saved locally collection...

        /*Alarm utility = new Alarm();

        Calendar firstTry = Calendar.getInstance();
        firstTry.set(Calendar.YEAR, 2017);
        firstTry.set(Calendar.MONTH, 7);
        firstTry.set(Calendar.DAY_OF_MONTH, 4);
        firstTry.set(Calendar.HOUR_OF_DAY, 10);
        firstTry.set(Calendar.MINUTE, 53);
        firstTry.set(Calendar.SECOND, 0);

        Calendar secondTry = Calendar.getInstance();
        secondTry.set(Calendar.YEAR, 2017);
        secondTry.set(Calendar.MONTH, 7);
        secondTry.set(Calendar.DAY_OF_MONTH, 4);
        secondTry.set(Calendar.HOUR_OF_DAY, 10);
        secondTry.set(Calendar.MINUTE, 54);
        secondTry.set(Calendar.SECOND, 0);

        utility.setAlarm(this,"First Event",firstTry);
        utility.setAlarm(this,"Second Event",secondTry);
        utility.deleteAlarm(this,secondTry);


        this.geoManager = new GeofenceManager(this);
        this.geoManager.addGeofence("nota1",new LatLng(44.261746, 12.338030));
        this.geoManager.addGeofence("nota2",new LatLng(44.159825, 12.430086));
        this.geoManager.removeGeofence("nota2");

        */
    }

}
