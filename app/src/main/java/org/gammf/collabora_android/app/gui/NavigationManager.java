package org.gammf.collabora_android.app.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.collaboration.CreateCollaborationDialogFragment;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.IntentConstants;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.short_collaborations.CollaborationsManager;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.AccessRightUtils;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages the navigator. The navigator is the left menu, which contains user's
 * collaborations.
 */

public class NavigationManager extends View implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NEW_COLLABORATION_DIALOG_TAG = "NewCollaborationDialogFragment";

    private final Context context;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private MainActivity mainActivity;

    /**
     * Constructor used for compatibility with View.
     * @param context the context of the application.
     */
    public NavigationManager(final Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Build a new navigation manager.
     * @param context the context of the application.
     * @param mainActivity the app main activity.
     */
    public NavigationManager(final Context context, final MainActivity mainActivity) {
        this(context);
        this.mainActivity = mainActivity;

       /*final NavigationView navigationView = (NavigationView) this.mainActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ImageButton btnAddCollaborations = (ImageButton) this.mainActivity.findViewById(R.id.btnAddCollaborations);
        btnAddCollaborations.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DialogFragment dialog = CreateCollaborationDialogFragment.newInstance();
                dialog.show(mainActivity.getSupportFragmentManager(), NEW_COLLABORATION_DIALOG_TAG);
            }
        });


*/

        final NavigationView navigationView = (NavigationView) this.mainActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ImageButton btnAddCollaborations = (ImageButton) this.mainActivity.findViewById(R.id.btnAddCollaborations);
        btnAddCollaborations.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final DialogFragment dialog = CreateCollaborationDialogFragment.newInstance();
                dialog.show(mainActivity.getSupportFragmentManager(), NEW_COLLABORATION_DIALOG_TAG);
            }
        });
        final Button btnLogout = (Button) this.mainActivity.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("CAUTION");
                builder.setMessage("If you press Continue, you will logout from Collabora, are you sure?");
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mainActivity.onUserLogout();
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        this.getDrawer().closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Refreshes the collaborations showed in the navigator menu, taking them in the application memory.
     */
    public void refreshCollaborationLists() {
        final CollaborationsManager collaborationsManager = LocalStorageUtils.readShortCollaborationsFromFile(this.context);
        final List<Pair<String, List<ShortCollaboration>>> collaborationsList = new ArrayList<>();
        collaborationsList.add(new Pair<>(getResources().getString(R.string.personal_drawer),
                collaborationsManager.filterByGroup(CollaborationType.PRIVATE)));
        collaborationsList.add(new Pair<>(getResources().getString(R.string.groups_drawer),
                collaborationsManager.filterByGroup(CollaborationType.GROUP)));
        collaborationsList.add(new Pair<>(getResources().getString(R.string.project_drawer),
                collaborationsManager.filterByGroup(CollaborationType.PROJECT)));

        this.expandableListView = (ExpandableListView) this.mainActivity.findViewById(R.id.expandableListCollaborations);
        this.expandableListAdapter = new CustomExpandableListAdapter(this.context, collaborationsList);
        this.expandableListView.setAdapter(this.expandableListAdapter);
        this.expandCollaborations();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v,
                                        final int groupPosition, final int childPosition, final long id) {
                final ShortCollaboration selectedCollaboration =
                        collaborationsList.get(groupPosition).second.get(childPosition);

                final Intent intent = new Intent(MainActivity.getReceiverIntentFilter());
                intent.putExtra(IntentConstants.MAIN_ACTIVITY_TAG, IntentConstants.OPEN_FRAGMENT);
                intent.putExtra(IntentConstants.OPEN_FRAGMENT, selectedCollaboration.getId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                getDrawer().closeDrawer(GravityCompat.START);
                return true;
            }
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    final ShortCollaboration selectedCollaboration =
                            collaborationsList.get(groupPosition).second.get(childPosition);
                    Collaboration collaboration = null;
                    try {
                        collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), selectedCollaboration.getId());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if(!selectedCollaboration.getCollaborationType().equals(CollaborationType.PRIVATE)) {
                        CollaborationMember member = AccessRightUtils.checkMemebrAccess(collaboration, mainActivity.getUser().getUsername());
                        if(AccessRightUtils.checkIfUserHasAccessRight(member)){
                            deletingCollaborationDialog(collaboration);
                        }
                    }
                    return true;
                }

                return false;
            }
        });

    }

    private void deletingCollaborationDialog(final Collaboration collaborationToDelete){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Warning - deleting collaboration!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to delete the collaboration: " + collaborationToDelete.getName()+" ? "+"(this operation cannot be undone)")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new SendMessageToServerTask(getContext()).execute(new ConcreteCollaborationUpdateMessage(
                                mainActivity.getUser().getUsername(), collaborationToDelete, UpdateMessageType.DELETION));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openNavigator() {
        this.getDrawer().openDrawer(GravityCompat.START);
    }
    
    public void closeNavigator() {
        this.getDrawer().closeDrawer(GravityCompat.START);
    }

    public void expandCollaborations() {
        for (int i = 0; i < this.expandableListAdapter.getGroupCount(); i++) {
            this.expandableListView.expandGroup(i);
        }
    }

    public void lockHidden() {
        this.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlock() {
        this.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public DrawerLayout getDrawer() {
        return (DrawerLayout) this.mainActivity.findViewById(R.id.drawer_layout);
    }
}
