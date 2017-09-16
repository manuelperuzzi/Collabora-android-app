package org.gammf.collabora_android.app.gui.module;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.app.utils.NoteGroupState;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.modules.ConcreteModuleUpdateMessage;
import org.gammf.collabora_android.communication.update.modules.ModuleUpdateMessage;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.gammf.collabora_android.utils.SingletonAppUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateModuleFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_COLLABID = "collabid";

    private String collaborationId;
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
    public static CreateModuleFragment newInstance(final String collaborationId) {
        CreateModuleFragment fragment = new CreateModuleFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if(getArguments() != null) {
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
        FloatingActionButton btnAddModule = rootView.findViewById(R.id.btnAddModule);
        btnAddModule.setOnClickListener(this);
    }

    private void addModule(final String content) {
        final Module module = new ConcreteModule(null, content, NoteGroupState.TO_DO.toString());
        final ModuleUpdateMessage message = new ConcreteModuleUpdateMessage(
                SingletonAppUser.getInstance().getUsername(), module, UpdateMessageType.CREATION, collaborationId);
        new SendMessageToServerTask(getContext()).execute(message);
    }

    @Override
    public void onClick(View view) {
        String insertedModuleName = txtContentModule.getText().toString();
        if (insertedModuleName.equals("")) {
            txtContentModule.setError(getResources().getString(R.string.fieldempty));
        } else {
            addModule(insertedModuleName);
        }
    }
}
