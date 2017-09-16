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
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.model.collaborations.shared_collaborations.Project;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.model.modules.Module;
import org.gammf.collabora_android.utils.app.LocalStorageUtils;
import org.gammf.collabora_android.utils.app.SingletonAppUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditModuleFragment extends Fragment {

    private static final String ARG_COLLABID = "collabid";
    private static final String ARG_MODULEID = "moduleid";

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
    public static EditModuleFragment newInstance(String collaborationId, String moduleId) {
        EditModuleFragment fragment = new EditModuleFragment();
        Bundle arg = new Bundle();
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
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
        }

        final Project project = (Project) LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
        module = project.getModule(moduleId);
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
    }

    private void updateModule(final String content) {
        module.setDescription(content);
        final ModuleUpdateMessage message = new ConcreteModuleUpdateMessage(
                SingletonAppUser.getInstance().getUsername(), module, UpdateMessageType.UPDATING, collaborationId);
        new SendMessageToServerTask(getContext()).execute(message);
    }

    private void checkUserUpdate() {
        String insertedModuleName = txtEditContentModule.getText().toString();
        if(insertedModuleName.equals("")) {
            txtEditContentModule.setError(getResources().getString(R.string.fieldempty));
        } else {
            updateModule(insertedModuleName);
        }
    }
}
