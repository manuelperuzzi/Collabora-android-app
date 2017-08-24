package org.gammf.collabora_android.app.gui;

import android.content.res.Resources;
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

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class CollaborationFragment extends Fragment {

    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABNAME = "collabName";
    private static final String ARG_COLLABTYPE = "collabType";

    private FloatingActionButton btnAddNote;
    private String sender;
    private String collabname, collabtype;
    private ListView notesList, moduleList;
    private ArrayList<DataModel> noteItems, moduleItems;


    public CollaborationFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModuleFragment.
     */
    public static CollaborationFragment newInstance(String sender, String name, String type) {
        CollaborationFragment fragment = new CollaborationFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_SENDER, sender);
        arg.putString(ARG_COLLABNAME, name);
        arg.putString(ARG_COLLABTYPE, type);
        Log.println(Log.ERROR, "ERRORONI", "new instance --> "+type);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Method for editcollaboration click on toolbar
        trigger the @EditCollaborationFragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Fragment editCollabFragment = EditCollaborationFragment.newInstance(collabname, collabtype);
            changeFragment(editCollabFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_collaboration, container, false);
        this.sender = getArguments().getString(ARG_SENDER);
        this.collabname = getArguments().getString(ARG_COLLABNAME);
        collabtype = getArguments().getString(ARG_COLLABTYPE);
        Log.println(Log.ERROR, "ERRORONI", "new instance2 --> "+getArguments().getString(ARG_COLLABTYPE));
        Log.println(Log.ERROR, "ERRORONI", "new instance2 --> "+getArguments().getString(ARG_COLLABNAME));
        Log.println(Log.ERROR, "ERRORONI", "new instance22 --> "+collabtype);
        notesList = rootView.findViewById(R.id.notesListView);
        moduleList = rootView.findViewById(R.id.modulesListView);
        btnAddNote = rootView.findViewById(R.id.btnAddNote);

        Resources res = getResources();
        moduleItems = new ArrayList<>();
        noteItems = new ArrayList<>();

        TabHost tabHost = rootView.findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Module Tab Tag");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Note Tab Tag");
        tab1.setIndicator(res.getString(R.string.title_modulelist));
        tab1.setContent(R.id.i_layout_2);
        tab2.setIndicator(res.getString(R.string.title_noteslist));
        tab2.setContent(R.id.i_layout_1);
        tabHost.addTab(tab2);
        if(collabtype.equals(res.getString(R.string.project_drawer))) {
            tabHost.addTab(tab1);
            fillModulesList();
        }

        if(sender.equals("notecreation"))
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
                        CreateNoteFragment.newInstance("collabfrag", collabname,collabtype,"59806a4af27da3fcfe0ac0ca", "nomodule");

                changeFragment(newNoteFragment);
            }
        });

        return rootView;
    }


    private void fillNotesList(){
        //HERE THE CODE FOR FILL NOTES LIST
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 1"));
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 2"));
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 3"));
        DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, noteItems);
        notesList.setAdapter(noteListAdapter);
        notesList.setOnItemClickListener(new ListItemClickListener());
    }

    private void fillModulesList(){
        //HERE THE CODE FOR FILL MODULES LIST
        moduleItems.add(new DataModel(R.drawable.module32, "Module 1", true));
        moduleItems.add(new DataModel(R.drawable.module32, "Module 2", true));
        moduleItems.add(new DataModel(R.drawable.module32, "Module 3", true));
        DrawerItemCustomAdapter moduleListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, moduleItems);
        moduleList.setAdapter(moduleListAdapter);
        moduleList.setOnItemClickListener(new ListItemClickListener());
    }

    private void addNewNote(){
        //HERE THE CODE FOR ADD NEW NOTE
        noteItems.add(new DataModel(R.drawable.note_icon, "New Note Content"));
    }

    private class ListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final DataModel listName = (DataModel) parent.getItemAtPosition(position);
            selectItem(position, listName);
        }

    }

    private void selectItem(int position, DataModel itemSelected) {
        Fragment openFragment = null;
        if(itemSelected.getIfIsModule()){
            openFragment = ModuleFragment.newInstance("collabfrag", collabname, collabtype, itemSelected.getName());
        }else{
            openFragment = NoteFragment.newInstance("collabfrag",collabname, collabtype, "nomodule", itemSelected.getName());
        }
        changeFragment(openFragment);
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(CollaborationFragment.this);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
