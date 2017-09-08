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
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.gui.module.CreateModuleFragment;
import org.gammf.collabora_android.app.gui.module.ModuleFragment;
import org.gammf.collabora_android.app.gui.note.CreateNoteFragment;
import org.gammf.collabora_android.app.gui.note.NoteFragment;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.Group;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.ModuleNote;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.AccessRightUtils;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class CollaborationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String SENDER = "collabfrag";
    //private static final String CALLER_NOTECREATION = "notecreationfrag";
    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABID = "collabid";
    private static final String ARG_USERNAME = "username";
    private static final String NOMODULE = "nomodule";
    private CollaborationMember member;

    private ListView notesList, moduleList;
    private ArrayList<CollaborationComponentInfo> noteItems, moduleItems;

    private String username;
    private String collaborationId;
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

    public static CollaborationFragment newInstance(String sender, String username, String collaborationId) {
        CollaborationFragment fragment = new CollaborationFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_USERNAME, username);
        arg.putString(ARG_SENDER, sender);
        arg.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
        }
        setHasOptionsMenu(true);

        try {
            collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            if(collaboration instanceof Project || collaboration instanceof Group){
                this.member = AccessRightUtils.checkMemebrAccess(collaboration,username);
            }
        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if(collaboration instanceof Project || collaboration instanceof Group) {
            if (AccessRightUtils.checkIfUserHasAccessRight(member)) {
                inflater.inflate(R.menu.edit_collaboration, menu);
            }
        }else
            inflater.inflate(R.menu.edit_collaboration, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Method for editcollaboration click on toolbar
        trigger the @EditCollaborationFragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Fragment editCollabFragment = EditCollaborationFragment.newInstance(username, collaborationId);
            changeFragment(editCollabFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        if(collaboration instanceof Project) {
            tabHost.addTab(tab1);
            if (!AccessRightUtils.checkIfUserHasAccessRight(member)) {
                btnAddNote.setVisibility(View.INVISIBLE);
            }else{
                btnMenuAdd.setVisibility(View.VISIBLE);
                btnAddNote.setVisibility(View.INVISIBLE);
            }
            fillModulesList();
        }
        tabHost.addTab(tab2);

        if(collaboration instanceof Project || collaboration instanceof Group) {
            if (AccessRightUtils.checkIfUserHasAccessRight(member)) {
            }
        }

        /*if(sender.equals(CALLER_NOTECREATION))
        {
            //FRAGMENT CALLED BY CreateNoteFragment:
            //  -things to do: add note
            addNewNote();
        }*/

        fillNotesList();

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment newNoteFragment =
                        CreateNoteFragment.newInstance(username, collaborationId, NOMODULE);
                changeFragment(newNoteFragment);
            }
        });
        btnMenuAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //qui gli va aggiunto l'id del modulo
                changeFragment(CreateNoteFragment.newInstance(username, collaborationId, NOMODULE));
            }
        });
        btnMenuAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changeFragment(CreateModuleFragment.newInstance(username, collaborationId));
            }
        });
        return rootView;
    }

    private void fillNotesList(){
        for (final Note note: collaboration.getAllNotes()) {
            if (! (note instanceof ModuleNote)) {
                noteItems.add(new CollaborationComponentInfo(note.getNoteID(), note.getContent(), CollaborationComponentType.NOTE));
            }
        }

        final DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, noteItems);
        notesList.setAdapter(noteListAdapter);
        notesList.setOnItemClickListener(this);
    }

    private void fillModulesList() {
        if (collaboration instanceof Project) {
            for (final Module module: ((Project) collaboration).getAllModules()) {
                moduleItems.add(new CollaborationComponentInfo(module.getId(), module.getDescription(), CollaborationComponentType.MODULE));
            }
        }

        final DrawerItemCustomAdapter moduleListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, moduleItems);
        moduleList.setAdapter(moduleListAdapter);
        moduleList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final CollaborationComponentInfo listName = (CollaborationComponentInfo) adapterView.getItemAtPosition(position);
        selectItem(listName);
    }

    private void selectItem(CollaborationComponentInfo itemSelected) {
        Fragment openFragment;
        if(itemSelected.getType().equals(CollaborationComponentType.MODULE)){
            openFragment = ModuleFragment.newInstance(SENDER, username, collaborationId, itemSelected.getId());
        }else{
            openFragment = NoteFragment.newInstance(username, collaborationId, itemSelected.getId(),NOMODULE);
        }
        changeFragment(openFragment);
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.addToBackStack(BACKSTACK_FRAG);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }

}
