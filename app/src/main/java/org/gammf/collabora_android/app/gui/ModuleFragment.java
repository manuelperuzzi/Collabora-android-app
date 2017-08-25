package org.gammf.collabora_android.app.gui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.modules.Module;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModuleFragment extends Fragment {

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String SENDER = "ModuleFragment";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";

    private static final String CALLER_NOTECREATION = "notecreationfrag";

    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_MODULEID = "moduleId";

    private FloatingActionButton btnAddNoteModule;
    private String sender, collaborationId, moduleId, collabname, collabtype, moduleName;
    private ListView moduleNotesList;
    private ArrayList<DataModel> listItem;

    public ModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModuleFragment.
     */
    public static ModuleFragment newInstance(String sender, String collabId, String moduleId) {
        ModuleFragment fragment = new ModuleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SENDER, sender);
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
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }

        getModuleDataFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_module, container, false);
        moduleNotesList = (ListView) rootView.findViewById(R.id.moduleNotesListView);

        listItem = new ArrayList<DataModel>();
        listItem.add(new DataModel(R.drawable.note_icon, "FintoID", "Note Content 1"));
        listItem.add(new DataModel(R.drawable.note_icon, "FintoID", "Note Content 2"));
        listItem.add(new DataModel(R.drawable.note_icon, "FintoID", "Note Content 3"));
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, listItem);
        moduleNotesList.setAdapter(adapter);
        moduleNotesList.setOnItemClickListener(new ModuleFragment.DrawerItemClickListener());

        if(sender.equals(CALLER_NOTECREATION))
        {
            //VALUE RECEIVED FROM CREATE NOTE FRAGMENT
            listItem.add(new DataModel(R.drawable.note_icon, "FintoID", "New Note Content"));
        }

        btnAddNoteModule = (FloatingActionButton) rootView.findViewById(R.id.btnAddNoteInModule);
        btnAddNoteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newNoteFragment = CreateNoteFragment.newInstance(SENDER, collaborationId, moduleName);
                changeFragment(newNoteFragment);
            }
        });

        return rootView;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final DataModel listItem = (DataModel) parent.getItemAtPosition(position);
            selectItem(position, listItem.getId());
        }

    }

    private void selectItem(int position, String itemId) {
        Fragment openNoteFragment = NoteFragment.newInstance(SENDER, collaborationId, itemId);
        changeFragment(openNoteFragment);
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

    private void getModuleDataFromServer(){
        //stessa cosa delle note
        //prendere i dati dal server e metterli dentro le variabili adatte
        //poi DENTRO ONVIEWCREATED fare tutti i setText sui rispettivi campi per visualizzarli all'utente
    }

}
