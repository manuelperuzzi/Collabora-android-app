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

    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABNAME = "collabName";
    private static final String ARG_COLLABTYPE = "collabType";
    private static final String ARG_MODULENAME = "moduleName";

    private FloatingActionButton btnAddNoteModule;
    private String sender, collabname, collabtype, moduleName;
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
    public static ModuleFragment newInstance(String sender, String collabName, String collabType, String moduleName) {
        ModuleFragment fragment = new ModuleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SENDER, sender);
        args.putString(ARG_COLLABNAME, collabName);
        args.putString(ARG_COLLABTYPE, collabType);
        args.putString(ARG_MODULENAME, moduleName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.sender = getArguments().getString(ARG_SENDER);
            this.collabname = getArguments().getString(ARG_COLLABNAME);
            this.collabtype = getArguments().getString(ARG_COLLABTYPE);
            this.moduleName = getArguments().getString(ARG_MODULENAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_module, container, false);
        moduleNotesList = (ListView) rootView.findViewById(R.id.moduleNotesListView);

        listItem = new ArrayList<DataModel>();
        listItem.add(new DataModel(R.drawable.note_icon, "Note Content 1"));
        listItem.add(new DataModel(R.drawable.note_icon, "Note Content 2"));
        listItem.add(new DataModel(R.drawable.note_icon, "Note Content 3"));
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, listItem);
        moduleNotesList.setAdapter(adapter);
        moduleNotesList.setOnItemClickListener(new ModuleFragment.DrawerItemClickListener());

        if(sender.equals("notecreation"))
        {
            //VALUE RECEIVED FROM CREATE NOTE FRAGMENT
            listItem.add(new DataModel(R.drawable.note_icon, "New Note Content"));
        }

        btnAddNoteModule = (FloatingActionButton) rootView.findViewById(R.id.btnAddNoteInModule);
        btnAddNoteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newNoteFragment = CreateNoteFragment.newInstance("modulefrag", collabname, collabtype, "fintocollabid", moduleName);
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
        Fragment openNoteFragment = new NoteFragment();
        Bundle fragmentArgument = new Bundle();
        fragmentArgument.putString("collabName", moduleName);
        openNoteFragment.setArguments(fragmentArgument);
        changeFragment(openNoteFragment);
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(ModuleFragment.this);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
