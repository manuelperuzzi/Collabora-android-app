package org.gammf.collabora_android.app.gui;


import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditModuleFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ERR_STATENOTSELECTED = "Please select state";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";
    private static final String ARG_MODULEID = "moduleid";

    private String username;
    private Module module;
    private String collaborationId, moduleId;
    private Spinner spinnerModuleStateEdited;
    private EditText txtEditContentModule;
    private String newStateSelected = "";


    public EditModuleFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId collaboration id where the module will be added
     * @param moduleId module id
     * @return A new instance of fragment EditModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditModuleFragment newInstance(String username, String collaborationId, String moduleId) {
        EditModuleFragment fragment = new EditModuleFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_USERNAME, username);
        arg.putString(ARG_COLLABID, collaborationId);
        arg.putString(ARG_MODULEID, moduleId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }

        try {
            final Project project = (Project) LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            module = project.getModule(moduleId);
        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.editdone_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_editdone) {
            checkUserUpdate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_module, container, false);
        initializeGuiComponent(rootView);
        return rootView;
    }

    private void initializeGuiComponent(View rootView) {
        txtEditContentModule = rootView.findViewById(R.id.txtModuleContentEdited);
        txtEditContentModule.setText(module.getDescription());
        spinnerModuleStateEdited = rootView.findViewById(R.id.spinnerModuleStateEdited);
        setSpinner();
    }

    private void setSpinner(){
        final List<NoteProjectState> stateList = new ArrayList<>();
        stateList.addAll(Arrays.asList(NoteProjectState.values()));
        ArrayAdapter<NoteProjectState> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModuleStateEdited.setAdapter(dataAdapter);
        spinnerModuleStateEdited.setSelection(0);
        spinnerModuleStateEdited.setOnItemSelectedListener(this);
    }

    private void updateModule(final String content, final String stateSelected) {
        module.setDescription(content);
        module.setStateDefinition(stateSelected);

        final ModuleUpdateMessage message = new ConcreteModuleUpdateMessage(
                username, module, UpdateMessageType.UPDATING, collaborationId);
        new SendMessageToServerTask().execute(message);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(EditModuleFragment.this);
        fragmentTransaction.commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void checkUserUpdate() {
        String insertedModuleName = txtEditContentModule.getText().toString();
        if(insertedModuleName.equals("")) {
            txtEditContentModule.setError(getResources().getString(R.string.fieldempty));
        } else {
            updateModule(insertedModuleName, newStateSelected);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        NoteProjectState item = (NoteProjectState) adapterView.getItemAtPosition(i);
        newStateSelected = item.toString();
        Log.println(Log.ERROR, "ERRORONI", ""+ newStateSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), ERR_STATENOTSELECTED, Toast.LENGTH_LONG);
        toast.show();
    }
}
