package org.gammf.collabora_android.app.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.CollaborationsSubscriberService;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.rabbitmq.NotificationsSubscriberService;
import org.gammf.collabora_android.app.utils.PermissionManager;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteGroup;
import org.gammf.collabora_android.collaborations.shared_collaborations.ConcreteProject;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
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

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public final class MainActivity extends AppCompatActivity
        implements DialogCollabListener{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String SENDER = "MainActivity";

    private NavigationManager navigationManager;
    private User user;
    private ProgressBar progress;
    private PermissionManager permissionManager;

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
                    final ShortCollaboration collaboration = LocalStorageUtils
                            .readShortCollaborationsFromFile(getApplicationContext()).getCollaboration(collaborationId);
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
        notificationIntent.putStringArrayListExtra("collaborationsIds", new ArrayList<>(
                LocalStorageUtils.readShortCollaborationsFromFile(getApplicationContext()).getCollaborationsId()));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.permissionManager = new PermissionManager(this);
        if (!this.permissionManager.checkPermissions()) {
            this.permissionManager.requestPermissions();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        this.permissionManager.processPermissionsRequestResult(requestCode, grantResults);
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

}