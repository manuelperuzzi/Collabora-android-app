package org.gammf.collabora_android.app.gui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.gammf.collabora_android.app.BuildConfig;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.SubscriberService;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogCollabListener{


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private GeofenceManager geoManager;


    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private List<String> group;
    private List<String> project;
    private List<String> personal;
    private HashMap<String, List<String>> expandableListDetail;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        personal = new ArrayList<String>();
        personal.add("My List");
        group = new ArrayList<String>();
        project = new ArrayList<String>();

        fillCollabList();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton btnAddCollaborations = (ImageButton) findViewById(R.id.btnAddCollaborations);
        btnAddCollaborations.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showNoticeDialog();

            }
        });

        expandableListDetail = new HashMap<>();
        res = getResources();
        res.getString(R.string.rg_group);
        expandableListDetail.put(res.getString(R.string.personal_drawer), personal);
        expandableListDetail.put(res.getString(R.string.groups_drawer), group);
        expandableListDetail.put(res.getString(R.string.project_drawer), project);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListCollaborations);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        for(int i=0; i < expandableListAdapter.getGroupCount(); i++)
            expandableListView.expandGroup(i);
        //In esecuzione quando si clicca su un elemento del menu
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String listName =
                        expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(childPosition);
                selectItem(groupPosition, expandableListTitle.get(groupPosition), listName);
                /*
                       Per il nome del gruppo: expandableListTitle.get(groupPosition)
                       Per il nome selezionato:
                                expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition)
                */
                return false;
            }
        });

        final Intent intent = new Intent(getApplicationContext(), SubscriberService.class);
        intent.putExtra("username", "fone");
        startService(intent);

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
    /*
    Metodo per riempire le liste nel menu:
    -viene chiamato quando si apre l'app, alla creazione dell'activity

    -qui recuperare le collaborazioni e inserirle nelle liste
    -il parametro passato è il nome della collaborazione
     */
    private void fillCollabList(){

        group.add("Group 1");
        group.add("Group 2");
        group.add("Group 3");
        group.add("Group 4");
        group.add("Group 5");

        project.add("Project 1");
        project.add("Project 2");
        project.add("Project 3");
        project.add("Project 4");
        project.add("Project 5");
    }

    /**
     * Used for open the collaboration selected
     *
     * @param position forse si può togliere, è li se diventa utile la posizione in lista ma non penso.
     * @param itemType collaboration type
     * @param itemName collaboration name
     */
    private void selectItem(int position, String itemType, String itemName) {

        Fragment fragment = null;
        Bundle fragmentArgument = new Bundle();
        fragment = CollaborationFragment.newInstance("drawerSelection", itemName, itemType);

        fragment.setArguments(fragmentArgument);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

          //  mDrawerList.setItemChecked(position, true);
          //  mDrawerList.setSelection(position);
            setTitle(itemName);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    /**
     * Used for EDIT collaboration
     *
     * @param sender fragment that wants to update the collaboration list.
     * @param collabname new collaboration name
     */
    public void updateCollaborationList(Fragment sender, String collabname, String collabType){

        //in collabType c'è il tipo per capire in quale lista va modificata la collaboration

        if(sender instanceof EditCollaborationFragment){
            // TO-DO qui bisogna
            // aggiornare il server col nome nuovo
            // rimuovere la collab precedente dalla lista
            // aggiungere quella nuova


        }

        Fragment fragment = CollaborationFragment.newInstance("drawerSelection", collabname, collabType);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(collabname);
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        /*
        Log.i("MainActivity", "onActivityResult");
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
        */
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

    /**
     * Used for ADD NEW COLLABORATION
     * TRIGGERED FROM DIALOGNEWCOLLAB ON CREATE BUTTON CLICK
     *
     * @param dialog dialog that have triggered this method
     * @param collabName new collaboration name
     * @param collabType new collaboration type
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String collabName, String collabType) {

        //close drawer lists, used for update the list.
        for(int i=0; i < expandableListAdapter.getGroupCount(); i++) {
            expandableListView.collapseGroup(i);
        }

        //check the collab type
        if(collabType.equals("Group")) {
            //QUI LA AGGIUNGO ALLA LISTA DEI GRUPPI
            group.add(collabName);
            //qui invece metto nel fragment il tipo della collab per evitare problemi di equalsss
            collabType = res.getString(R.string.groups_drawer);
        }else if(collabType.equals("Project")) {
            //QUI LA AGGIUNGO ALLA LISTA DEI PROGETTI
            project.add(collabName);
            collabType = res.getString(R.string.project_drawer);
        }


        //prepare fragment for new collab inserted
        Fragment fragment = CollaborationFragment.newInstance("drawerselection", collabName, collabType);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(collabName);
        }

        Context context = getApplicationContext();
        CharSequence text = ""+collabType+" created!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DialogNewCollaborationFragment.newInstance();
        dialog.show(getSupportFragmentManager(), "NewCollabDialogFragment");
    }



}


