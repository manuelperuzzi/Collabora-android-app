package org.gammf.collabora_android.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.android.gms.maps.model.LatLng;

import org.gammf.collabora_android.app.location_geofence.GeofenceManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] personalitemName = new String[]{
            "Lista della spesa",
            "Lista verdura"
    };

    String[] itemName = new String[]{
            "Progetto 1",
            "Progetto 2",
            "Progetto 3",
            "Progetto 4"
    };

    ArrayList<String> personallistItem = new ArrayList<String>();
    ArrayList<String> projectlistItem = new ArrayList<String>();

    private static final int REQUEST_CODE = 1;
    private static final String LIST_TYPE = "listtype";
    private static final String LIST_NAME = "listname";
    private static final String LIST_DESCRIPTION = "listdescription";
    CustomListAdapter personalAdapter;
    CustomListAdapter projectAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private GeofenceManager geoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(getApplicationContext(), SubscriberService.class);
        intent.putExtra("username", "fone");
        startService(intent);

        TabHost host = (TabHost)findViewById(R.id.tabHostLists);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Personal List");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Project");
        host.addTab(spec);


        final ListView personallistView =(ListView) findViewById(R.id.personallistView);
        for(String s : personalitemName){
            personallistItem.add(s);
        }

        final ListView projectListView =(ListView) findViewById(R.id.listView);
        for(String s : itemName){
            projectlistItem.add(s);
        }

        projectAdapter = new CustomListAdapter(this, projectlistItem);
        projectListView.setAdapter(projectAdapter);

        personalAdapter=new CustomListAdapter(this, personallistItem);
        personallistView.setAdapter(personalAdapter);

        personallistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                final String listName = (String) adattatore.getItemAtPosition(pos);
                Intent myIntent = new Intent(MainActivity.this, SingleListActivity.class);
                myIntent.putExtra(LIST_NAME, listName);
                MainActivity.this.startActivity(myIntent);
            }
        });

        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
                final String listName = (String) adattatore.getItemAtPosition(pos);
                Intent myIntent = new Intent(MainActivity.this, SingleListActivity.class);
                myIntent.putExtra(LIST_NAME, listName);
                MainActivity.this.startActivity(myIntent);
            }
        });

        FloatingActionButton personalfab = (FloatingActionButton) findViewById(R.id.btnAddPersonalNotes);
        personalfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, AddListActivity.class);
                i.putExtra("type", "personallist");
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddNotes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this, AddListActivity.class);
                myIntent.putExtra("type", "projectlist"); //Optional parameters
                startActivityForResult(myIntent, REQUEST_CODE);
            }
        });

        this.geoManager = new GeofenceManager(this);

/*
        //simple examples, 2 set and 1 delete to test.
        Utility utility = new Utility();

        Calendar firstTry = Calendar.getInstance();
        firstTry.set(Calendar.YEAR, 2017);
        firstTry.set(Calendar.MONTH, 7);
        firstTry.set(Calendar.DAY_OF_MONTH, 4);
        firstTry.set(Calendar.HOUR_OF_DAY, 12);
        firstTry.set(Calendar.MINUTE, 37);
        firstTry.set(Calendar.SECOND, 0);

        Calendar secondTry = Calendar.getInstance();
        secondTry.set(Calendar.YEAR, 2017);
        secondTry.set(Calendar.MONTH, 7);
        secondTry.set(Calendar.DAY_OF_MONTH, 4);
        secondTry.set(Calendar.HOUR_OF_DAY, 12);
        secondTry.set(Calendar.MINUTE, 38);
        secondTry.set(Calendar.SECOND, 0);

        utility.setAlarm(this,"First Event",firstTry);
        utility.setAlarm(this,"Second Event",secondTry);
        utility.deleteAlarm(this,secondTry);


        */
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        }

        this.geoManager.addGeofence("id1","contenuto prima posizione",new LatLng(44.261746, 12.338030));
        this.geoManager.addGeofence("id2","contenuto seconda posizione",new LatLng(44.159825, 12.430086));

        //this.geoManager.removeGeofence("id2");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String listType = data.getStringExtra(LIST_TYPE);
                    String listName = data.getStringExtra(LIST_NAME);
                    String listDescription = data.getStringExtra(LIST_DESCRIPTION);
                    addNewList(listType, listName, listDescription);
                }
                break;
            }
        }
    }


    private void addNewList(String listType, String listName, String listDescription){
        if(listType.equals("personallist")) {
            personalAdapter.addList( listName, listDescription);
            personalAdapter.notifyDataSetChanged();
        }else if(listType.equals("projectlist")){
            projectAdapter.addList( listName, listDescription);
            projectAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}


