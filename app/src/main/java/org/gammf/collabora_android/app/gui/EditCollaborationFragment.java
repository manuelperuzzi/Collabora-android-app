package org.gammf.collabora_android.app.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCollaborationFragment extends Fragment {

    private static final String TOAST_ERR_EDITCANCEL = "Edit discarded";

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";

    private String username;
    private String collaborationId;
    private Collaboration collaboration;
    private ListView memberList;
    private ArrayList<CollaborationComponentInfo> memberItem;
    private DrawerItemCustomAdapter adapter;
    private EditText txtNewTitle;
    private Button btnAddMember;
    private TextView txtCollabType;

    private boolean memberHasChanged = false;

    public EditCollaborationFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId collaboration id
     * @return A new instance of fragment EditCollaborationFragment.
     */

    public static EditCollaborationFragment newInstance(String username, String collaborationId) {
        EditCollaborationFragment fragment = new EditCollaborationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
        }

        try {
            collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
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
            checkUserInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_collaboration, container, false);
        initializeGuiComponent(rootView);

        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        btnAddMember = rootView.findViewById(R.id.btnAddMember);
        txtNewTitle = rootView.findViewById(R.id.txtInsertEditedCollabName);
        memberList = rootView.findViewById(R.id.listViewCollabMember);
        txtNewTitle.setText(collaboration.getName());
        txtCollabType = rootView.findViewById(R.id.txtCollabType);
        txtCollabType.setText(new ConcreteShortCollaboration(collaboration).getCollaborationType().name());

        memberItem = new ArrayList<>();
        getMemberAndFillList();
        /*btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMemberList();
            }
        });*/
    }

    private void getMemberAndFillList(){

        final Set<CollaborationMember> members = ((SharedCollaboration) collaboration).getAllMembers();
        for (final CollaborationMember cm: members) {
            memberItem.add(new CollaborationComponentInfo(cm.getUsername(), cm.getUsername(), CollaborationComponentType.MEMBER));
        }

        //setting the list adapter
        adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.member_list_item, memberItem);
        memberList.setAdapter(adapter);
    }

    private void checkUserInput() {

        final String newName = txtNewTitle.getText().toString();
        if (newName.equals("")) {
            txtNewTitle.setError(getResources().getString(R.string.fieldempty));
        } else if (! newName.equals(collaboration.getName())) {
            collaboration.setName(newName);
            final CollaborationUpdateMessage message = new ConcreteCollaborationUpdateMessage(
                    username, collaboration, UpdateMessageType.UPDATING);
            new SendMessageToServerTask(getContext()).execute(message);
            ((MainActivity)getActivity()).showLoadingSpinner();
            new TimeoutSender(getContext(), 5000);
        }





        /*String newName = txtNewTitle.getText().toString();
        //If newName is empty
        if(newName.equals("")) {
            //display error for field required
            txtNewTitle.setError(res.getString(R.string.fieldempty));

            //if name isn't changed
        }else if(newName.equals(collaboration.getName())){

            //check if member(s) was added.
            if(memberHasChanged) {
                returnToCollabFragment();
            }else { //members not changed and name not modified

                Toast toast =
                        Toast.makeText(getActivity().getApplicationContext(), TOAST_ERR_EDITCANCEL, Toast.LENGTH_SHORT);
                toast.show();

                returnToCollabFragment();
            }
        }else{
            updateCollaboration(newName);
        }*/
    }
}
