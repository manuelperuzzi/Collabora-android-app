package org.gammf.collabora_android.app.gui.module;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.utils.CollaborationType;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditModuleFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";
    private static final String ARG_MODULEID = "moduleid";

    private String username;
    private Module module;
    private String collaborationId, moduleId;
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

        final StateSpinnerManager spinnerManager = new StateSpinnerManager(this.module.getStateDefinition(), rootView, R.id.spinnerModuleStateEdited, CollaborationType.PROJECT);
        spinnerManager.addObserver(new Observer<String>() {
            @Override
            public void notify(String newState) {
                newStateSelected = newState;
            }
        });
    }

    private void updateModule(final String content, final String stateSelected) {
        module.setDescription(content);
        module.setStateDefinition(stateSelected);

        final ModuleUpdateMessage message = new ConcreteModuleUpdateMessage(
                username, module, UpdateMessageType.UPDATING, collaborationId);
        new SendMessageToServerTask(getContext()).execute(message);
    }

    private void checkUserUpdate() {
        String insertedModuleName = txtEditContentModule.getText().toString();
        if(insertedModuleName.equals("")) {
            txtEditContentModule.setError(getResources().getString(R.string.fieldempty));
        } else {
            updateModule(insertedModuleName, newStateSelected);
        }
    }
}
