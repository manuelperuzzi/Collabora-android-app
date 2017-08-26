package org.gammf.collabora_android.app.gui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class CollaborationFragment extends Fragment implements View.OnTouchListener, AdapterView.OnItemClickListener {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String SENDER = "collabfrag";
    private static final String CALLER_NOTECREATION = "notecreationfrag";
    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABID = "collabid";
    private static final String NOMODULE = "nomodule";

    private static final int MAXSWIPE = 100;
    private static final int NOTETABINDEX = 0;
    private static final int MODULETABINDEX = 1;

    private static final String TYPE_PROJECT = "Project";
    private static final String TYPE_GROUP = "Group";
    private int downX;
    private int upX;
    private FloatingActionMenu btnMenuAdd;
    private com.github.clans.fab.FloatingActionButton btnMenuAddNote, btnMenuAddModule;
    private FloatingActionButton btnAddNote;
    private String sender, collabId;
    private String collabname, collabtype;
    private ListView notesList, moduleList;
    private ArrayList<DataModel> noteItems, moduleItems;
    private TabHost tabHost;

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
        if(getArguments() != null) {
            this.sender = getArguments().getString(ARG_SENDER);
            this.collabId = getArguments().getString(ARG_COLLABID);
        }
        setHasOptionsMenu(true);
        getDataFromServer(this.collabId);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
            Fragment editCollabFragment = EditCollaborationFragment.newInstance(collabId);
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
        btnAddNote = rootView.findViewById(R.id.btnAddNote);
        btnMenuAdd = rootView.findViewById(R.id.floating_action_menu);
        btnMenuAddNote = rootView.findViewById(R.id.floating_action_menu_addnote);
        btnMenuAddModule = rootView.findViewById(R.id.floating_action_menu_addmodel);
        btnMenuAdd.setVisibility(View.INVISIBLE);

        Resources res = getResources();
        moduleItems = new ArrayList<>();
        noteItems = new ArrayList<>();

        tabHost = rootView.findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Module Tab Tag");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Note Tab Tag");
        tab1.setIndicator(res.getString(R.string.title_modulelist));
        tab1.setContent(R.id.i_layout_2);
        tab2.setIndicator(res.getString(R.string.title_noteslist));
        tab2.setContent(R.id.i_layout_1);
        tabHost.addTab(tab2);
        if(collabtype.equals(TYPE_PROJECT)) {
            tabHost.addTab(tab1);
            tabHost.setOnTouchListener(this);
            btnMenuAdd.setVisibility(View.VISIBLE);
            btnAddNote.setVisibility(View.INVISIBLE);
            fillModulesList();
        }

        if(sender.equals(CALLER_NOTECREATION))
        {
            //FRAGMENT CALLED BY CreateNoteFragment:
            //  -things to do: add note
            addNewNote();
        }

        fillNotesList();

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment newNoteFragment =
                        CreateNoteFragment.newInstance(collabId, NOMODULE);

                changeFragment(newNoteFragment);
            }
        });
        btnMenuAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //qui gli va aggiunto l'id del modulo
                changeFragment(CreateNoteFragment.newInstance(collabId, NOMODULE));
            }
        });
        btnMenuAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changeFragment(CreateModuleFragment.newInstance(collabId));
            }
        });
        return rootView;
    }


    private void fillNotesList(){
        //HERE THE CODE FOR FILL NOTES LIST
        noteItems.add(new DataModel(R.drawable.note_icon, "FintoID", "Note Content 1"));
        noteItems.add(new DataModel(R.drawable.note_icon, "FintoID", "Note Content 2"));
        noteItems.add(new DataModel(R.drawable.note_icon, "FintoID", "Note Content 3"));
        DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, noteItems);
        notesList.setAdapter(noteListAdapter);
        notesList.setOnItemClickListener(this);
    }

    private void fillModulesList(){
        //HERE THE CODE FOR FILL MODULES LIST
        moduleItems.add(new DataModel(R.drawable.module32, "FintoID", "Module 1", true));
        moduleItems.add(new DataModel(R.drawable.module32, "FintoID", "Module 2", true));
        moduleItems.add(new DataModel(R.drawable.module32, "FintoID", "Module 3", true));
        DrawerItemCustomAdapter moduleListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, moduleItems);
        moduleList.setAdapter(moduleListAdapter);
        moduleList.setOnItemClickListener(this);
    }

    private void addNewNote(){
        //HERE THE CODE FOR ADD NEW NOTE
        noteItems.add(new DataModel(R.drawable.note_icon, "FintoID", "New Note Content"));
    }

    private void addNewModule(){
        //HERE THE CODE FOR ADD NEW MODULE
        moduleItems.add(new DataModel(R.drawable.module32, "FintoID", "New Module", true));
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            upX = (int) event.getX();
            if (upX - downX > MAXSWIPE) {
                tabHost.setCurrentTab(NOTETABINDEX);
            } else if (downX - upX > -MAXSWIPE) {
                tabHost.setCurrentTab(MODULETABINDEX);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final DataModel listName = (DataModel) adapterView.getItemAtPosition(position);
        selectItem(position, listName);
    }

    private void selectItem(int position, DataModel itemSelected) {
        Fragment openFragment = null;
        if(itemSelected.getIfIsModule()){
            openFragment = ModuleFragment.newInstance(SENDER, collabId, itemSelected.getId());
        }else{
            openFragment = NoteFragment.newInstance(collabId, itemSelected.getId());
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

    private void getDataFromServer(String collabId){




        this.collabname = "Nome finto";
        //scegliere fra TYPE_GROUP oppure TYPE_PROJECT
        this.collabtype = TYPE_PROJECT;
    }

}
