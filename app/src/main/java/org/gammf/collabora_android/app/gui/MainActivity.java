package org.gammf.collabora_android.app.gui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.gammf.collabora_android.app.BuildConfig;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.CollaborationsSubscriberService;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.rabbitmq.NotificationsSubscriberService;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteGroup;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.users.SimpleCollaborationMember;
import org.gammf.collabora_android.users.SimpleUser;
import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.MandatoryFieldMissingException;
import org.gammf.collabora_android.utils.MessageUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogCollabListener{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String SENDER = "MainActivity";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String TOAST_COLLABCREATED = " created!";
    private static final String DIALOGNAME = "NewCollabDialogFragment";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private User user;
    private CollaborationsManager collaborationsManager;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshCollaborationLists();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("update.collaborations.on.gui"));

        try {
            final User temporaryUser = new SimpleUser.Builder().name("peru").surname("peruperu").username("peru13").birthday(new DateTime(675748765489L)).email("manuel.peruzzi@studio.unibo.it").build();
            LocalStorageUtils.writeUserToFile(getApplicationContext(), temporaryUser);
            //LocalStorageUtils.deleteUserInFile(getApplicationContext());
            user = LocalStorageUtils.readUserFromFile(getApplicationContext());
        } catch (final FileNotFoundException e) {
            Fragment fragment = LoginFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(BACKSTACK_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            leaveMenu();
        } catch (final JSONException | IOException e) {
            //TODO ?
        } catch (MandatoryFieldMissingException e) {
            e.printStackTrace();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton btnAddCollaborations = (ImageButton) findViewById(R.id.btnAddCollaborations);
        btnAddCollaborations.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showNoticeDialog();
            }
        });

        if(user!= null) {
            TextView username = (TextView) findViewById(R.id.nameOfUser);
            username.setText(user.getUsername());
            TextView email = (TextView) findViewById(R.id.emailOfUser);
            email.setText(user.getEmail());

            final Intent notificationIntent = new Intent(getApplicationContext(), NotificationsSubscriberService.class);
            notificationIntent.putExtra("username", user.getUsername());
            notificationIntent.putStringArrayListExtra("collaborationsIds", new ArrayList<>(getExistingCollaborationsIds()));
            startService(notificationIntent);

            final Intent collaborationIntent = new Intent(getApplicationContext(), CollaborationsSubscriberService.class);
            collaborationIntent.putExtra("username", user.getUsername());
            startService(collaborationIntent);

            Fragment fragment = HomepageFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        Button btnLogout = (Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("CAUTION");
                builder.setMessage("If you press Continue, you will logout from Collabora, are you sure?");
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Fragment fragment = LoginFragment.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        drawer.closeDrawers();
                        LocalStorageUtils.deleteUserInFile(getApplicationContext());
                        leaveMenu();
                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        refreshCollaborationLists();




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

    /**
     * Used for open the collaboration selected
     *
     * @param collab collaboration name
     */
    private void selectItem(ShortCollaboration collab) {

        Fragment fragment = null;
        fragment = CollaborationFragment.newInstance(SENDER, user.getUsername(), collab.getId());

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(BACKSTACK_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            setTitle(collab.getName());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }

    /**
     * Used for EDIT collaboration
     *
     * @param sender fragment that wants to update the collaboration list.
     * @param collabId collaborationId
     */
    public void updateCollaborationList(Fragment sender, String collabId){

        //dall'id della collaboration recuperare le info per aggiornare il menu a fianco

        if(sender instanceof EditCollaborationFragment){
            // TO-DO qui bisogna
            // rimuovere la collab precedente dalla lista
            // aggiungere quella nuova


        }

        Fragment fragment = CollaborationFragment.newInstance(SENDER, user.getUsername(), collabId);

        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.remove(sender);
            fragmentTransaction2.commit();
            getSupportFragmentManager().popBackStack();
            setTitle("METTERE IL NOME RECUPERATO");
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            getSupportFragmentManager().popBackStack();
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
     * @param collaborationName new collaboration name
     * @param collaborationType new collaboration type
     */
    @Override
    public void onDialogCreateClick(DialogFragment dialog, String collaborationName, String collaborationType) {

        //close drawer lists, used for update the list.
        for(int i=0; i < expandableListAdapter.getGroupCount(); i++) {
            expandableListView.collapseGroup(i);
        }

        final CollaborationType type = CollaborationType.valueOf(collaborationType);
        final SharedCollaboration collaboration;
        if(type.equals(CollaborationType.PROJECT)) {
            collaboration = new ConcreteProject(null, collaborationName);
        } else {
            collaboration = new ConcreteGroup(null, collaborationName);
        }

        collaboration.addMember(new SimpleCollaborationMember(user.getUsername(), AccessRight.ADMIN));

        final UpdateMessage message = new ConcreteCollaborationUpdateMessage(user.getUsername(),
                collaboration, UpdateMessageType.CREATION);
        try {
            Log.e("UpdateMessage", MessageUtils.updateMessageToJSON(message).toString());
        } catch (final Exception e) {}
        new SendMessageToServerTask().execute(message);

        /*//prepare fragment for new collab inserted
        Fragment fragment = CollaborationFragment.newInstance(SENDER, collaborationId);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(collaborationName);
        }

        Toast toast = Toast.makeText(getApplicationContext(), ""+collaborationType+TOAST_COLLABCREATED, Toast.LENGTH_SHORT);
        toast.show();*/
        dialog.dismiss();
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DialogNewCollaborationFragment.newInstance();
        dialog.show(getSupportFragmentManager(), DIALOGNAME);
    }

    private void closeDrawerGroup(){
        //close drawer lists, used for update the list.
        for(int i=0; i < expandableListAdapter.getGroupCount(); i++) {
            expandableListView.collapseGroup(i);
        }
    }

    private List<ShortCollaboration> filterCollaborationsFromManager(final CollaborationType collaborationType) {
        final List<ShortCollaboration> collaborations = new ArrayList<>();
        if(collaborationsManager != null) {
            for (final ShortCollaboration collaboration : collaborationsManager.getAllCollaborations()) {
                if (collaboration.getCollaborationType().equals(collaborationType)) {
                    collaborations.add(collaboration);
                }
            }
        }
        return collaborations;
    }

    public List<String> getExistingCollaborationsIds() {
        final List<String> collaborationIds = new ArrayList<>();
        if(collaborationsManager != null) {
            for (final ShortCollaboration collaboration : collaborationsManager.getAllCollaborations()) {
                collaborationIds.add(collaboration.getId());
            }
        }
        return collaborationIds;
    }

    public void refreshCollaborationLists() {
        try {
            collaborationsManager = LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext());
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        final Map<String, List<ShortCollaboration>> expandableListDetail = new HashMap<>();
        expandableListDetail.put(getResources().getString(R.string.personal_drawer), filterCollaborationsFromManager(CollaborationType.PRIVATE));
        expandableListDetail.put(getResources().getString(R.string.groups_drawer), filterCollaborationsFromManager(CollaborationType.GROUP));
        expandableListDetail.put(getResources().getString(R.string.project_drawer), filterCollaborationsFromManager(CollaborationType.PROJECT));

        final List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListCollaborations);
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        for(int i=0; i < expandableListAdapter.getGroupCount(); i++)
            expandableListView.expandGroup(i);
        //In esecuzione quando si clicca su un elemento del menu
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final ShortCollaboration collabSelected =
                        expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(childPosition);
                selectItem(collabSelected);
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
    }


    /**
     * method used to insert lateral menu after user login
     */
    public void insertLateralMenu(){
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * method used to hide lateral menu after user logout
     */
    public void leaveMenu(){
        this.toolbar.setNavigationIcon(null);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * method called after login or registration that update lateral menu with all the user information and collaboration
     */
    public void updateMenuInfo(){
        try {
            user = LocalStorageUtils.readUserFromFile(getApplicationContext());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        TextView username = (TextView) findViewById(R.id.nameOfUser);
        username.setText(user.getUsername());
        TextView email = (TextView) findViewById(R.id.emailOfUser);
        email.setText(user.getEmail());
        //PROBABILMENTE QUA CI DOVREBBERO ANDARE ANCHE LE INIZIALIZZAZIONI DEI SERVIZI DI
        //COLLABORATIONS E NOTIFICATION + refreshCollaborationLists()
    }
}


