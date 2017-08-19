package org.gammf.collabora_android.app.gui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class CollaborationFragment extends Fragment {

    Bundle arguments;
    TextView label;
    FloatingActionButton btnAddNote;
    String collabname;
    ListView notesList;
    ArrayList<DataModel> drawerItem;

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

        notesList = (ListView) rootView.findViewById(R.id.notesListView);

        drawerItem = new ArrayList<DataModel>();
        drawerItem.add(new DataModel(R.drawable.note_icon, "Connect"));
        drawerItem.add(new DataModel(R.drawable.note_icon, "Fixtures"));
        drawerItem.add(new DataModel(R.drawable.note_icon, "Table"));
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, drawerItem);
        notesList.setAdapter(adapter);
        notesList.setOnItemClickListener(new CollaborationFragment.DrawerItemClickListener());

        Boolean getValue= getArguments().getBoolean("BOOLEAN_VALUE");
        if(getValue)
        {
            //VALUE RECEIVED FROM DRAWER SELECTION
            collabname =  getArguments().getString("collabName");
        }
        else
        {
            //VALUE RECEIVED FROM FRAGMENT CREATE NOTE
            drawerItem.add(new DataModel(R.drawable.note_icon, "New Note"));
        }
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final DataModel listName = (DataModel) parent.getItemAtPosition(position);
            selectItem(position, listName.getName());
        }

    }

    private void selectItem(int position, String itemName) {

        Fragment openNoteFragment = null;
        Bundle fragmentArgument = new Bundle();
        openNoteFragment = new NoteFragment();
        fragmentArgument.putString("collabName", collabname);

        openNoteFragment.setArguments(fragmentArgument);
        changeFragment(openNoteFragment);
    }


    private void changeFragment(Fragment fragment){
        if (fragment != null) {

            FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(CollaborationFragment.this);
            //fragmentTransaction2.add(android.R.id.content, fragment);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();


        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
