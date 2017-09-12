package org.gammf.collabora_android.app.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.alarm.Alarm;
import org.gammf.collabora_android.app.connectivity.NetworkChangeManager;
import org.gammf.collabora_android.app.connectivity.NetworkChangeObserver;
import org.gammf.collabora_android.app.gui.authentication.AuthenticationActivity;
import org.gammf.collabora_android.app.gui.collaboration.CollaborationFragment;
import org.gammf.collabora_android.app.location_geofence.GeofenceManager;
import org.gammf.collabora_android.app.rabbitmq.CollaborationsSubscriberService;
import org.gammf.collabora_android.app.rabbitmq.NotificationsSubscriberService;
import org.gammf.collabora_android.app.utils.IntentConstants;
import org.gammf.collabora_android.app.utils.PermissionManager;
import org.gammf.collabora_android.app.utils.TimeoutSender;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.notes.SimpleNoteBuilder;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.utils.AlarmAndGeofenceUtils;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.SingletonAppUser;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class MainActivity extends AppCompatActivity
        implements NetworkChangeObserver {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String SENDER = "MainActivity";

    private NavigationManager navigationManager;
    private User user;
    private ProgressBar progress;
    private PermissionManager permissionManager;
    private NetworkChangeManager networkManager = NetworkChangeManager.getInstance();
    private BroadcastReceiver receiver = new MainActivityReceiver();
    private boolean isNetworkManagerReceiverRegistered = false;

    public static String getReceiverIntentFilter() {
        return MainActivityReceiver.INTENT_FILTER;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FLUSSOANDROID", "onCreate");
        setContentView(R.layout.activity_main);
        ExceptionManager.init(this);
        Log.i("FLUSSOANDROID", "MainActivity; " + getApplicationContext().toString());
        try {
            SingletonAppUser.getInstance().loadUser(getApplicationContext());
            user = SingletonAppUser.getInstance().getUser();

            final TextView username = (TextView) findViewById(R.id.nameOfUser);
            username.setText(user.getUsername());
            final TextView email = (TextView) findViewById(R.id.emailOfUser);
            email.setText(user.getEmail());

            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.navigationManager = new NavigationManager(getApplicationContext(), this);
            final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, this.navigationManager.getDrawer(), toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            this.navigationManager.getDrawer().addDrawerListener(toggle);
            toggle.syncState();
            this.navigationManager.refreshCollaborationLists();

            this.networkManager.addNetworkChangeObserver(this);
            this.registerReceiver(this.networkManager, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
            this.isNetworkManagerReceiverRegistered = true;
        } catch (final FileNotFoundException e) {
            final Intent loginIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("FLUSSOANDROID", "onStart");
        this.permissionManager = new PermissionManager(this);
        if (!this.permissionManager.checkPermissions()) {
            this.permissionManager.requestPermissions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("FLUSSOANDROID", "onResume");
        this.progress = (ProgressBar) findViewById(R.id.progressBar);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(MainActivityReceiver.INTENT_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("FLUSSOANDROID", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("FLUSSOANDROID", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FLUSSOANDROID", "onDestroy");
        this.networkManager.clearObservers();
        if (isNetworkManagerReceiverRegistered) {
            this.unregisterReceiver(this.networkManager);
        }
        stopService(new Intent(this, CollaborationsSubscriberService.class));
        stopService(new Intent(this, NotificationsSubscriberService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (this.navigationManager.getDrawer().isDrawerOpen(GravityCompat.START)) {
                this.navigationManager.closeNavigator();
            } else {
                super.onBackPressed();
            }
        } else {
            final int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            getSupportFragmentManager().popBackStack();
            if (fragmentCount == 1) {
                setTitle("Collabora");
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        this.permissionManager.processPermissionsRequestResult(requestCode, grantResults);
    }

    /**
     * method used to delete all LocalStorage informations
     */
    public void onUserLogout(){
        this.navigationManager.closeNavigator();
        LocalStorageUtils.deleteUserFromFile(getApplicationContext());
        LocalStorageUtils.deleteAllCollaborations(getApplicationContext());

        final Intent serviceDeletionIntent = new Intent("subscriber.service.deletion");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(serviceDeletionIntent);

        final Intent authActivityIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(authActivityIntent);
        finish();
    }

    @Override
    public void onNetworkAvailable() {
        Log.i("FLUSSOANDROID", "onNetworkAvailable");
        final Intent notificationIntent = new Intent(getApplicationContext(), NotificationsSubscriberService.class);
        notificationIntent.putExtra("username", user.getUsername());
        notificationIntent.putStringArrayListExtra("collaborationsIds", new ArrayList<>(LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext()).getCollaborationsId()));
        startService(notificationIntent);

        final Intent collaborationIntent = new Intent(getApplicationContext(), CollaborationsSubscriberService.class);
        collaborationIntent.putExtra("username", user.getUsername());
        startService(collaborationIntent);
    }

    @Override
    public void onNetworkUnavailable() {
        Log.i("FLUSSOANDROID", "onNetworkUnavailable");
        stopService(new Intent(this, CollaborationsSubscriberService.class));
        stopService(new Intent(this, NotificationsSubscriberService.class));
    }

    private void openCollaborationFragment(final ShortCollaboration collaboration) {
        final Fragment fragment = CollaborationFragment.newInstance(SENDER, collaboration.getId());
        final FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        //fragmentManager.popBackStack(BACKSTACK_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.addToBackStack(BACKSTACK_FRAG);
        fragmentManager.replace(R.id.content_frame, fragment);
        fragmentManager.commit();
    }

    public void onLocalStorageCorrupted() {
        Toast.makeText(getApplicationContext(), "Memory corrupted! Logging out.", Toast.LENGTH_LONG).show();
        onUserLogout();
    }

    private class MainActivityReceiver extends BroadcastReceiver {

        private static final String INTENT_FILTER = "update.collaborations.on.gui";

        private static final int TIMEOUT_MILLIS = 5000;

        private int messagesReceived = 0;
        private int timeouts = 0;

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.i("FLUSSOANDROID", intent.getStringExtra(IntentConstants.MAIN_ACTIVITY_TAG));
            switch (intent.getStringExtra(IntentConstants.MAIN_ACTIVITY_TAG)) {
                case IntentConstants.NETWORK_ERROR:
                    Toast.makeText(context, intent.getStringExtra(IntentConstants.NETWORK_ERROR), Toast.LENGTH_SHORT).show();
                    break;
                case IntentConstants.TIMEOUT:
                    this.timeouts++;
                    if (this.timeouts > this.messagesReceived) {
                        Toast.makeText(context, "Timeout Error", Toast.LENGTH_SHORT).show();
                        timeouts--;
                        progress.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    break;
                case IntentConstants.MESSAGE_SENT:
                    navigationManager.closeNavigator();
                    progress.setVisibility(View.VISIBLE);
                    new TimeoutSender(getApplicationContext(), TIMEOUT_MILLIS);
                    break;
                case IntentConstants.NETWORK_MESSAGE_RECEIVED:
                    this.messagesReceived++;
                    progress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    final String collaborationId = intent.getStringExtra(IntentConstants.NETWORK_MESSAGE_RECEIVED);
                    if (collaborationId != null) {
                        if (intent.getStringExtra(IntentConstants.COLLABORATION_DELETION) != null) {
                            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                getSupportFragmentManager().popBackStack();
                            }
                            setTitle("Collabora");
                            navigationManager.refreshCollaborationLists();
                            navigationManager.openNavigator();
                        } else {
                            openCollaborationFragment(LocalStorageUtils
                                    .readShortCollaborationsFromFile(getApplicationContext()).getCollaboration(collaborationId));
                        }
                    } else {
                        navigationManager.refreshCollaborationLists();
                        navigationManager.openNavigator();
                    }
                    break;
                case IntentConstants.OPEN_FRAGMENT:
                    final String collID = intent.getStringExtra(IntentConstants.OPEN_FRAGMENT);
                    if (collID != null) {
                        openCollaborationFragment(LocalStorageUtils
                                .readShortCollaborationsFromFile(getApplicationContext()).getCollaboration(collID));
                    }
                    break;
                case IntentConstants.LOCAL_STORAGE_ERROR:
                    Log.i("FLUSSOANDROID", "local storage error");
                    Toast.makeText(context, "Local storage corrupted! Logging out.", Toast.LENGTH_SHORT).show();
                    onUserLogout();
                    break;
            }
        }
    }
}