package org.gammf.collabora_android.app.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import org.gammf.collabora_android.app.connectivity.NetworkChangeManager;
import org.gammf.collabora_android.app.connectivity.NetworkChangeObserver;
import org.gammf.collabora_android.app.gui.collaboration.CollaborationFragment;
import org.gammf.collabora_android.app.rabbitmq.CollaborationsSubscriberService;
import org.gammf.collabora_android.app.rabbitmq.NotificationsSubscriberService;
import org.gammf.collabora_android.app.utils.IntentConstants;
import org.gammf.collabora_android.app.utils.PermissionManager;
import org.gammf.collabora_android.app.utils.TimeoutSender;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.users.User;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;


    public static String getReceiverIntentFilter() {
        return MainActivityReceiver.INTENT_FILTER;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.navigationManager = new NavigationManager(getApplicationContext(), this);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.toggle = new ActionBarDrawerToggle(
                this, this.navigationManager.getDrawer(), this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.navigationManager.getDrawer().addDrawerListener(toggle);
        this.toggle.syncState();
        this.networkManager.addNetworkChangeObserver(this);

        try {
            user = LocalStorageUtils.readUserFromFile(getApplicationContext());
        } catch (final FileNotFoundException e) {
            Fragment fragment = LoginFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(BACKSTACK_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            leaveMenu();
        } catch (final JSONException | IOException e) {
            //TODO ?
        }

        this.navigationManager.refreshCollaborationLists();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.permissionManager = new PermissionManager(this);
        if (!this.permissionManager.checkPermissions()) {
            this.permissionManager.requestPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        this.unregisterReceiver(this.networkManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(MainActivityReceiver.INTENT_FILTER));
        this.progress = (ProgressBar) findViewById(R.id.progressBar);
        this.registerReceiver(this.networkManager, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        this.permissionManager.processPermissionsRequestResult(requestCode, grantResults);
    }

    /**
     * method used to insert lateral menu after user login
     */
    public void insertLateralMenu(){
        this.navigationManager.getDrawer().addDrawerListener(this.toggle);
        this.toggle.syncState();
        this.navigationManager.unlock();
    }

    /**
     * method used to hide lateral menu after user logout
     */
    public void leaveMenu(){
        this.toolbar.setNavigationIcon(null);
        this.navigationManager.lockHidden();
    }

    /**
     * method used to delete all LocalStorage informations
     */
    public void deleteUserInfo(){
        this.navigationManager.closeNavigator();
        LocalStorageUtils.deleteUserInFile(getApplicationContext());
        LocalStorageUtils.deleteAllCollaborations(getApplicationContext());
        leaveMenu();
        // QUI CANCELLARE I SERVIZI RELATIVI AGLI EXCHANGE !!!
    }

    /**
     * method called after login or registration that update lateral menu with all the user information and collaboration
     */
    public void updateUserInfo() {
        try {
            user = LocalStorageUtils.readUserFromFile(getApplicationContext());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        TextView username = (TextView) findViewById(R.id.nameOfUser);
        username.setText(user.getUsername());
        TextView email = (TextView) findViewById(R.id.emailOfUser);
        email.setText(user.getEmail());

        onNetworkAvailable();

        Fragment fragment = HomepageFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        this.navigationManager.refreshCollaborationLists();
    }

    @Override
    public void onNetworkAvailable() {
        if (user != null) {
            Log.i("CIAO", "dovrei entrare here");
            final Intent notificationIntent = new Intent(getApplicationContext(), NotificationsSubscriberService.class);
            notificationIntent.putExtra("username", user.getUsername());
            notificationIntent.putStringArrayListExtra("collaborationsIds", new ArrayList<>(LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext()).getCollaborationsId()));
            startService(notificationIntent);

            final Intent collaborationIntent = new Intent(getApplicationContext(), CollaborationsSubscriberService.class);
            collaborationIntent.putExtra("username", user.getUsername());
            startService(collaborationIntent);
        }
    }

    @Override
    public void onNetworkUnavailable() {
        stopService(new Intent(this, CollaborationsSubscriberService.class));
        stopService(new Intent(this, NotificationsSubscriberService.class));
    }

    private void openCollaborationFragment(final ShortCollaboration collaboration) {
        final Fragment fragment = CollaborationFragment.newInstance(SENDER, user.getUsername(), collaboration.getId());
        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.popBackStack(BACKSTACK_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        setTitle(collaboration.getName());
    }

    private class MainActivityReceiver extends BroadcastReceiver {

        private static final String INTENT_FILTER = "update.collaborations.on.gui";

        private static final int TIMEOUT_MILLIS = 5000;

        private int messagesReceived = 0;
        private int timeouts = 0;

        @Override
        public void onReceive(final Context context, final Intent intent) {

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
                        openCollaborationFragment(LocalStorageUtils
                                .readShortCollaborationsFromFile(getApplicationContext()).getCollaboration(collaborationId));
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
            }
        }
    }
}