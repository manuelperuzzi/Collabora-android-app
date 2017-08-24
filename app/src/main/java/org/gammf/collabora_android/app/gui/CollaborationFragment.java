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

    private FloatingActionButton btnAddNote;
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
    public static CollaborationFragment newInstance() {
        CollaborationFragment fragment = new CollaborationFragment();
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
            Fragment editCollabFragment = new EditCollaborationFragment();
            Bundle fragmentArgument = new Bundle();
            fragmentArgument.putString("collabName", collabname);

            editCollabFragment.setArguments(fragmentArgument);
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

        Boolean getValue= getArguments().getBoolean("BOOLEAN_VALUE");
        String getSender = getArguments().getString("sender");
        if(getSender.equals("drawerSelection"))
        {
            //ARGUMENTS RECEIVED FROM DRAWER SELECTION
            collabname =  getArguments().getString("collabName");
            collabtype = getArguments().getString("collabType");
            if(collabtype.equals(res.getString(R.string.project_drawer))) {
                tabHost.addTab(tab1);
                fillModuleList();
            }
        }
        else if(getSender.equals("notecreation"))
        {
            //VALUE RECEIVED FROM CREATE NOTE FRAGMENT
            addNewNote();

        }else if(getSender.equals("editnote")){

            //ARGUMENTS RECEIVED FROM EDIT NOTE FRAGMENT
        }

        fillNoteList();

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment newNoteFragment = CreateNoteFragment.newInstance("59806a4af27da3fcfe0ac0ca");

                changeFragment(newNoteFragment);
            }
        });

        return rootView;
    }


    private void fillNoteList(){
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 1"));
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 2"));
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 3"));
        DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, noteItems);
        notesList.setAdapter(noteListAdapter);
        notesList.setOnItemClickListener(new ListItemClickListener());
    }

    private void fillModuleList(){
        moduleItems.add(new DataModel(R.drawable.module32, "Module 1", true));
        moduleItems.add(new DataModel(R.drawable.module32, "Module 2", true));
        moduleItems.add(new DataModel(R.drawable.module32, "Module 3", true));
        DrawerItemCustomAdapter moduleListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, moduleItems);
        moduleList.setAdapter(moduleListAdapter);
        moduleList.setOnItemClickListener(new ListItemClickListener());
    }

    private void addNewNote(){
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
        Bundle fragmentArgument = new Bundle();
        fragmentArgument.putString("collabName", collabname);
        if(itemSelected.getIfIsModule()){
            openFragment = ModuleFragment.newInstance();
            fragmentArgument.putBoolean("BOOLEAN_VALUE",true);
            fragmentArgument.putString("moduleName", itemSelected.getName());
        }else{
            openFragment = NoteFragment.newInstance();
        }
        openFragment.setArguments(fragmentArgument);
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
