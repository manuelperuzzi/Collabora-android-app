package org.gammf.collabora_android.app.gui.module;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.spinner.StateSpinnerManager;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.Observer;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.utils.CollaborationType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateModuleFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";

    private String username;
    private String collaborationId;
    private String state;

    private EditText txtContentModule;

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

        final StateSpinnerManager spinnerManager = new StateSpinnerManager(StateSpinnerManager.NO_STATE, rootView, R.id.spinnerNewModuleState, CollaborationType.PROJECT);
        spinnerManager.addObserver(new Observer<String>() {
            @Override
            public void notify(String newState) {
                state = newState;
            }
        });

        FloatingActionButton btnAddModule = rootView.findViewById(R.id.btnAddModule);
        btnAddModule.setOnClickListener(this);
    }

    private void addModule(final String content, final String stateSelected) {
        final Module module = new ConcreteModule(null, content, stateSelected);
        final ModuleUpdateMessage message = new ConcreteModuleUpdateMessage(
                username, module, UpdateMessageType.CREATION, collaborationId);
        new SendMessageToServerTask(getContext()).execute(message);
    }

    @Override
    public void onClick(View view) {
        String insertedModuleName = txtContentModule.getText().toString();
        if (insertedModuleName.equals("")) {
            txtContentModule.setError(getResources().getString(R.string.fieldempty));
        } else {
            addModule(insertedModuleName, this.state);
        }
    }
}
