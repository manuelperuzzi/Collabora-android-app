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
import android.widget.ListView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModuleFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String SENDER = "ModuleFragment";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";

    private static final String CALLER_NOTECREATION = "notecreationfrag";

    private static final String ARG_SENDER = "sender";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_MODULEID = "moduleId";

    private String sender, collaborationId, moduleId;
    private ListView moduleNotesList;
    private ArrayList<DataModel> listItem;

    private String username;
    private Module module;

    public ModuleFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModuleFragment.
     */
    public static ModuleFragment newInstance(String sender, String username, String collabId, String moduleId) {
        ModuleFragment fragment = new ModuleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SENDER, sender);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_MODULEID, moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.sender = getArguments().getString(ARG_SENDER);
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }
        setHasOptionsMenu(true);

        try {
            final Project project = (Project) LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            module = project.getModule(moduleId);
            getActivity().setTitle(project.getName() + " - " + module.getDescription());
        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_collabmodule, menu);
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
        if(id == R.id.action_editcollab){
            Fragment editCollabFragment = EditCollaborationFragment.newInstance(username, this.collaborationId);
            changeFragment(editCollabFragment);
            return true;
        }else if (id == R.id.action_editmodule){
            Fragment editModuleFragment = EditModuleFragment.newInstance(username, this.collaborationId, moduleId);
            changeFragment(editModuleFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_module, container, false);
        initializeGuiComponent(rootView);

        fillNoteList();

        if(sender.equals(CALLER_NOTECREATION))
        {
            //VALUE RECEIVED FROM CREATE NOTE FRAGMENT
            listItem.add(new DataModel(R.drawable.note_icon, "FintoID", "New Note Content", false));
        }

        return rootView;
    }

    private void initializeGuiComponent(View rootView) {
        moduleNotesList = rootView.findViewById(R.id.moduleNotesListView);
        listItem = new ArrayList<>();
        FloatingActionButton btnAddNoteModule = rootView.findViewById(R.id.btnAddNoteInModule);
        btnAddNoteModule.setOnClickListener(this);
    }

    private void fillNoteList() {

        for (final Note n: module.getAllNotes()) {
            listItem.add(new DataModel(R.drawable.note_icon, n.getNoteID(), n.getContent(), false));
        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, listItem);
        moduleNotesList.setAdapter(adapter);
        moduleNotesList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final DataModel listItem = (DataModel) adapterView.getItemAtPosition(position);
        selectItem(listItem.getId());
    }

    private void selectItem(final String itemId) {
        Fragment openNoteFragment = NoteFragment.newInstance(username, collaborationId, itemId);
        changeFragment(openNoteFragment);
    }

    @Override
    public void onClick(View view) {
        Fragment newNoteFragment = CreateNoteFragment.newInstance(username, collaborationId, moduleId);
        changeFragment(newNoteFragment);
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.addToBackStack(BACKSTACK_FRAG);
            fragmentTransaction2.hide(ModuleFragment.this);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }
}
