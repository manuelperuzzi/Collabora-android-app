package org.gammf.collabora_android.app.gui.collaboration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.github.clans.fab.FloatingActionMenu;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.gui.DeletionDialogFragment;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.module.CreateModuleFragment;
import org.gammf.collabora_android.app.gui.module.ModuleFragment;
import org.gammf.collabora_android.app.gui.note.CreateNoteFragment;
import org.gammf.collabora_android.app.gui.note.NoteFragment;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.ModuleComparator;
import org.gammf.collabora_android.app.utils.NoteComparator;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.AccessRightUtils;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.gammf.collabora_android.utils.SingletonAppUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollaborationFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String DELETION_DIALOG_TAG = "DeletionDialogTag";
    private static final String SENDER = "collabfrag";
    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABID = "collabid";
    private static final String NOMODULE = "nomodule";

    private ListView notesList, moduleList;
    private ArrayList<CollaborationComponentInfo> noteItems, moduleItems;

    private String username;
    private Collaboration collaboration;

    public CollaborationFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModuleFragment.
     */

    public static CollaborationFragment newInstance(String sender, String collaborationId) {
        CollaborationFragment fragment = new CollaborationFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_SENDER, sender);
        arg.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final String collaborationId = getArguments().getString(ARG_COLLABID);
            collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
        }
        username = SingletonAppUser.getInstance().getUsername();
        setHasOptionsMenu(true);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        if (collaboration.getCollaborationType().equals(CollaborationType.PRIVATE) ||
                !((SharedCollaboration)collaboration).getMember(username).getAccessRight().equals(AccessRight.ADMIN)) {
            final MenuItem item = menu.findItem(R.id.action_remove);
            item.setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_collaboration, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Method for editcollaboration click on toolbar
        trigger the @CollaborationInfoFragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            final Fragment collaborationInfoFragment = CollaborationInfoFragment.newInstance(collaboration.getId());
            changeFragment(collaborationInfoFragment);
        } else if (id == R.id.action_remove) {
            final UpdateMessage message = new ConcreteCollaborationUpdateMessage(username, collaboration, UpdateMessageType.DELETION);
            new SendMessageToServerTask(getContext()).execute(message);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(collaboration.getName());
        View rootView = inflater.inflate(R.layout.fragment_collaboration, container, false);

        notesList = rootView.findViewById(R.id.notesListView);
        moduleList = rootView.findViewById(R.id.modulesListView);

        final FloatingActionButton btnAddNote = rootView.findViewById(R.id.btnAddNote);
        final FloatingActionMenu btnMenuAdd = rootView.findViewById(R.id.floating_action_menu);
        final com.github.clans.fab.FloatingActionButton btnMenuAddNote = rootView.findViewById(R.id.floating_action_menu_addnote);
        final com.github.clans.fab.FloatingActionButton btnMenuAddModule = rootView.findViewById(R.id.floating_action_menu_addmodel);
        btnMenuAdd.setVisibility(View.INVISIBLE);

        moduleItems = new ArrayList<>();
        noteItems = new ArrayList<>();

        final TabHost tabHost = rootView.findViewById(R.id.tabhost);
        tabHost.setup();
        final TabHost.TabSpec tab1 = tabHost.newTabSpec("Module Tab Tag");
        final TabHost.TabSpec tab2 = tabHost.newTabSpec("Note Tab Tag");
        tab1.setIndicator(getResources().getString(R.string.title_modulelist));
        tab1.setContent(R.id.i_layout_2);
        tab2.setIndicator(getResources().getString(R.string.title_noteslist));
        tab2.setContent(R.id.i_layout_1);

        if(collaboration.getCollaborationType().equals(CollaborationType.PROJECT)) {
            tabHost.addTab(tab1);
            CollaborationMember member = AccessRightUtils.checkMemberAccess(collaboration, username);
            if (AccessRightUtils.checkIfUserHasAccessRight(member)) {
                btnMenuAdd.setVisibility(View.VISIBLE);
            }
            btnAddNote.setVisibility(View.INVISIBLE);
            fillModulesList();
        }
        if(collaboration.getCollaborationType().equals(CollaborationType.GROUP)) {
            CollaborationMember member = AccessRightUtils.checkMemberAccess(collaboration, username);
            if (!AccessRightUtils.checkIfUserHasAccessRight(member)) {
                btnAddNote.setVisibility(View.INVISIBLE);
            }
        }
        tabHost.addTab(tab2);
        fillNotesList();

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment newNoteFragment =
                        CreateNoteFragment.newInstance(collaboration.getId(), NOMODULE);
                changeFragment(newNoteFragment);
            }
        });
        btnMenuAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(CreateNoteFragment.newInstance(collaboration.getId(), NOMODULE));
            }
        });
        btnMenuAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(CreateModuleFragment.newInstance(collaboration.getId()));
            }
        });
        return rootView;
    }

    private void fillNotesList() {
        final List<Note> allNotes = new ArrayList<>(collaboration.getAllNotes());
        Collections.sort(allNotes, new NoteComparator());
        for (final Note note : allNotes) {
            if (!(note instanceof ModuleNote)) {
                noteItems.add(new CollaborationComponentInfo(note.getNoteID(), note.getContent(), CollaborationComponentType.NOTE,note.getState().getCurrentDefinition()));
            }
        }
        DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, noteItems);
        notesList.setAdapter(noteListAdapter);
        notesList.setOnItemClickListener(this);
        notesList.setOnItemLongClickListener(this);
    }

    private void fillModulesList() {
        if (collaboration.getCollaborationType().equals(CollaborationType.PROJECT)) {
            final List<Module> allModules = new ArrayList<>(((Project) collaboration).getAllModules());
            Collections.sort(allModules, new ModuleComparator());
            for (final Module module: allModules) {
                moduleItems.add(new CollaborationComponentInfo(module.getId(), module.getDescription(), CollaborationComponentType.MODULE,module.getStateDefinition()));
            }
        }
        DrawerItemCustomAdapter moduleListAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, moduleItems);
        moduleList.setAdapter(moduleListAdapter);
        moduleList.setOnItemClickListener(this);
        moduleList.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final CollaborationComponentInfo listName = (CollaborationComponentInfo) adapterView.getItemAtPosition(position);
        selectItem(listName);
    }

    private void selectItem(CollaborationComponentInfo itemSelected) {
        Fragment openFragment;
        if (itemSelected.getType().equals(CollaborationComponentType.MODULE)) {
            openFragment = ModuleFragment.newInstance(collaboration.getId(), itemSelected.getId());
        } else {
            openFragment = NoteFragment.newInstance(collaboration.getId(), itemSelected.getId(), NOMODULE);
        }
        changeFragment(openFragment);
    }

    private void changeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(BACKSTACK_FRAG);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final CollaborationComponentInfo listName = (CollaborationComponentInfo) adapterView.getItemAtPosition(position);
        final DeletionDialogFragment dialog = DeletionDialogFragment.newInstance(
                collaboration.getId(),listName.getId(),listName.getContent(),listName.getType());
        dialog.show(getActivity().getSupportFragmentManager(), DELETION_DIALOG_TAG);
        return true;
    }
}

