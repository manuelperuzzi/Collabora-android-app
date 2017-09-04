package org.gammf.collabora_android.app.gui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.gammf.collabora_android.app.BuildConfig;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.CollaborationsSubscriberService;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.rabbitmq.NotificationsSubscriberService;
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
import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public final class MainActivity extends AppCompatActivity
        implements /*NavigationView.OnNavigationItemSelectedListener,*/ DialogCollabListener{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String SENDER = "MainActivity";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private NavigationManager navigationManager;
    private User user;
    private ProgressBar progress;

    private class MainActivityReceiver extends BroadcastReceiver {

        private static final String INTENT_FILTER = "update.collaborations.on.gui";

        private int messagesReceived = 0;
        private int timeouts = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getLongExtra("timeout", -1) != -1) {
                timeouts++;
                if(timeouts > messagesReceived) {
                    Toast.makeText(context, "Timeout Error", Toast.LENGTH_SHORT).show();
                    timeouts--;
                }
            } else if (intent.getStringExtra("network-error") != null) {
                Toast.makeText(context, intent.getStringExtra("network-error"), Toast.LENGTH_SHORT).show();
            } else {
                if (intent.getStringExtra("network-message") != null) {
                    messagesReceived++;
                }

                final String collaborationId = intent.getStringExtra("collaborationId");

                if(collaborationId != null) {
                    final ShortCollaboration collaboration = getUpdateCollaborationManager().getCollaboration(collaborationId);
                    openCollaborationFragment(collaboration);
                } else {
                    navigationManager.refreshCollaborationLists();
                    navigationManager.openNavigator();
                }
            }
            progress.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private BroadcastReceiver receiver = new MainActivityReceiver();

    public static String getReceverIntentFilter() {
        return MainActivityReceiver.INTENT_FILTER;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.navigationManager = new NavigationManager(getApplicationContext(), this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, this.navigationManager.getDrawer(), toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.navigationManager.getDrawer().addDrawerListener(toggle);
        toggle.syncState();

        try {
            final User temporaryUser = new SimpleUser.Builder().name("peru").surname("peruperu").username("peru13").birthday(new DateTime(675748765489L)).email("manuel.peruzzi@studio.unibo.it").build();
            LocalStorageUtils.writeUserToFile(getApplicationContext(), temporaryUser);
            this.user = LocalStorageUtils.readUserFromFile(getApplicationContext());

        } catch (final FileNotFoundException e) {
            //TODO show login/registration page
        } catch (final JSONException e) {
            //TODO ?
        } catch (IOException e) {
            //TODO ?
        } catch (MandatoryFieldMissingException e) {
            e.printStackTrace();
        }

        this.navigationManager.refreshCollaborationLists();

        final Intent notificationIntent = new Intent(getApplicationContext(), NotificationsSubscriberService.class);
        notificationIntent.putExtra("username", user.getUsername());
        notificationIntent.putStringArrayListExtra("collaborationsIds", new ArrayList<>(getExistingCollaborationsIds()));
        startService(notificationIntent);

        final Intent collaborationIntent = new Intent(getApplicationContext(), CollaborationsSubscriberService.class);
        collaborationIntent.putExtra("username", user.getUsername());
        startService(collaborationIntent);
    }

    private void openCollaborationFragment(final ShortCollaboration collaboration) {
        final Fragment fragment = CollaborationFragment.newInstance(SENDER, user.getUsername(), collaboration.getId());
        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.popBackStack(BACKSTACK_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        setTitle(collaboration.getName());
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

  /*  @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

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

        navigationManager.collapseCollaborations();

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
        new SendMessageToServerTask(getApplicationContext()).execute(message);

        dialog.dismiss();

        this.navigationManager.closeNavigator();
        showLoadingSpinner();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        new TimeoutSender(getApplicationContext(), 5000);
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Test", "MainActiviy onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Test", "MainActiviy onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(MainActivityReceiver.INTENT_FILTER));
        this.progress = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void showLoadingSpinner() {
        progress.setVisibility(View.VISIBLE);
    }

    public List<String> getExistingCollaborationsIds() {
        final List<String> collaborationIds = new ArrayList<>();
        final CollaborationsManager collaborationsManager = this.getUpdateCollaborationManager();
        if(collaborationsManager != null) {
            for (final ShortCollaboration collaboration : collaborationsManager.getAllCollaborations()) {
                collaborationIds.add(collaboration.getId());
            }
        }
        return collaborationIds;
    }

    private CollaborationsManager getUpdateCollaborationManager() {
        try {
            return LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext());
        } catch (final JSONException e) {
            e.printStackTrace(); // TODO error handling
            return null;
        }
    }
}