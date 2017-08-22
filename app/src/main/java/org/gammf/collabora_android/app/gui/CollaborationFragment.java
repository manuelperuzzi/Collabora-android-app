package org.gammf.collabora_android.app.gui;

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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class CollaborationFragment extends Fragment {

    Bundle arguments;
    TextView label;
    FloatingActionButton btnAddNote;
    String collabname, collabtype;
    ListView notesList, moduleList;
    ArrayList<DataModel> noteItems, moduleItems;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<String> group;
    List<String> project;
    HashMap<String, List<String>> expandableListDetail;

    public CollaborationFragment() {
        setHasOptionsMenu(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

        Boolean getValue= getArguments().getBoolean("BOOLEAN_VALUE");
        if(getValue)
        {
            //VALUE RECEIVED FROM DRAWER SELECTION
            collabname =  getArguments().getString("collabName");
            collabtype = getArguments().getString("collabType");

        }
        else
        {
            //VALUE RECEIVED FROM CREATE NOTE FRAGMENT
            noteItems.add(new DataModel(R.drawable.note_icon, "New Note Content"));
        }

/*
        notesList = rootView.findViewById(R.id.notesListView);
        noteItems = new ArrayList<>();
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 1"));
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 2"));
        noteItems.add(new DataModel(R.drawable.note_icon, "Note Content 3"));
        DrawerItemCustomAdapter noteListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, noteItems);
        notesList.setAdapter(noteListAdapter);
        notesList.setOnItemClickListener(new ListItemClickListener());

        moduleList = rootView.findViewById(R.id.modulesListView);
        moduleItems = new ArrayList<>();
        moduleItems.add(new DataModel(R.drawable.note_icon, "Module 1", true));
        moduleItems.add(new DataModel(R.drawable.note_icon, "Module 2", true));
        moduleItems.add(new DataModel(R.drawable.note_icon, "Module 3", true));
        DrawerItemCustomAdapter moduleListAdapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, moduleItems);
        moduleList.setAdapter(moduleListAdapter);
        moduleList.setOnItemClickListener(new ListItemClickListener());
*/

        group = new ArrayList<String>();
        group.add("Group 1");
        group.add("Group 2");
        group.add("Group 3");
        group.add("Group 4");
        group.add("Group 5");

        project = new ArrayList<String>();
        project.add("Project 1");
        project.add("Project 2");
        project.add("Project 3");
        project.add("Project 4");
        project.add("Project 5");


        btnAddNote = (FloatingActionButton) rootView.findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newNoteFragment = null;
                Bundle fragmentArgument = new Bundle();
                newNoteFragment = new CreateNoteFragment();
                fragmentArgument.putString("collabName", collabname);

                newNoteFragment.setArguments(fragmentArgument);
                changeFragment(newNoteFragment);
            }
        });

        return rootView;
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
            openFragment = new ModuleFragment();
            fragmentArgument.putBoolean("BOOLEAN_VALUE",true);
            fragmentArgument.putString("moduleName", itemSelected.getName());
        }else{
            openFragment = new NoteFragment();
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
