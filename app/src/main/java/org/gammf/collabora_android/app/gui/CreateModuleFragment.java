package org.gammf.collabora_android.app.gui;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateModuleFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private static final String SENDER = "modulefrag";
    private static final String ERR_STATENOTSELECTED = "Please select state";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";

    private String username;
    private String collaborationId;
    private Spinner spinnerModuleState;
    private EditText txtContentModule;
    private FloatingActionButton btnAddModule;

    private String stateSelected = "";

    public CreateModuleFragment() {
        setHasOptionsMenu(false);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId collaboration id where the module will be added
     * @return A new instance of fragment CreateModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateModuleFragment newInstance(String username, String collaborationId) {
        CreateModuleFragment fragment = new CreateModuleFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_USERNAME, username);
        arg.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if(getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
        }
        getModuleDataFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_module, container, false);
        initializeGuiComponent(rootView);
        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        txtContentModule = rootView.findViewById(R.id.txtNewModuleContent);
        spinnerModuleState = rootView.findViewById(R.id.spinnerNewModuleState);
        setSpinner();
        btnAddModule = rootView.findViewById(R.id.btnAddModule);
        btnAddModule.setOnClickListener(this);
    }

    private void setSpinner(){
        final List<NoteProjectState> stateList = new ArrayList<>();
        stateList.addAll(Arrays.asList(NoteProjectState.values()));
        ArrayAdapter<NoteProjectState> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModuleState.setAdapter(dataAdapter);
        spinnerModuleState.setSelection(0);
        spinnerModuleState.setOnItemSelectedListener(this);
    }

    private void getModuleDataFromServer(){


    }


    private void addModule(final String content, final String stateSelected){
        CollaborationFragment collabFragment = CollaborationFragment.newInstance(SENDER, username, collaborationId);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, collabFragment).commit();
    }

    @Override
    public void onClick(View view) {
        String insertedNoteName = txtContentModule.getText().toString();
        if(insertedNoteName.equals("")){
            Resources res = getResources();
            txtContentModule.setError(res.getString(R.string.fieldempty));
        }else {
            //avete un moduleID che può essere nomodule
            //per verificare se la nota va aggiunta in un modulo o è solo nella collaborazione
            addModule(insertedNoteName, stateSelected);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        NoteProjectState item = (NoteProjectState) adapterView.getItemAtPosition(i);
        stateSelected = item.toString();
        Log.println(Log.ERROR, "ERRORONI", ""+stateSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), ERR_STATENOTSELECTED, Toast.LENGTH_LONG);
        toast.show();
    }
}
