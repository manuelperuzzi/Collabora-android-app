package org.gammf.collabora_android.app.gui.collaboration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
import org.gammf.collabora_android.communication.update.collaborations.CollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.collaborations.ConcreteCollaborationUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.short_collaborations.ConcreteShortCollaboration;
import org.gammf.collabora_android.users.CollaborationMember;
import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.json.JSONException;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCollaborationFragment extends Fragment {

    private static final String MEMBER_DIALOG_TAG = "MemberDialogFragment";

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabid";

    private String username;
    private String collaborationId;
    private Collaboration collaboration;
    private ListView memberList;
    private EditText txtNewTitle;

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
        final Button btnAddMember = rootView.findViewById(R.id.btnAddMember);
        txtNewTitle = rootView.findViewById(R.id.txtInsertEditedCollabName);
        memberList = rootView.findViewById(R.id.listViewCollabMember);
        txtNewTitle.setText(collaboration.getName());
        final TextView txtCollabType = rootView.findViewById(R.id.txtCollabType);
        txtCollabType.setText(new ConcreteShortCollaboration(collaboration).getCollaborationType().name());
        if (! ((SharedCollaboration) collaboration).getMember(username).getAccessRight().equals(AccessRight.ADMIN)) {
            btnAddMember.setVisibility(View.GONE);
            txtNewTitle.setKeyListener(null);
        }
        getMemberAndFillList();

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberDialogFragment dialog = MemberDialogFragment.addMemberInstance(collaborationId, username);
                dialog.show(getActivity().getSupportFragmentManager(), MEMBER_DIALOG_TAG);
            }
        });
    }

    private void getMemberAndFillList(){
        final ArrayList<CollaborationComponentInfo> memberItem = new ArrayList<>();
        final List<CollaborationMember> members = new ArrayList<>(((SharedCollaboration) collaboration).getAllMembers());
        Collections.sort(members, new MemberComparator());
        for (final CollaborationMember cm: members) {
            memberItem.add(new CollaborationComponentInfo(cm.getUsername(), cm.getUsername() + " (" + cm.getAccessRight() + ")", CollaborationComponentType.MEMBER));
        }

        final DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(), R.layout.member_list_item, memberItem);
        memberList.setAdapter(adapter);
    }

    public class MemberComparator implements Comparator<CollaborationMember> {
        public int compare(final CollaborationMember c1, final CollaborationMember c2) {
            final int comp = c1.getAccessRight().compareTo(c2.getAccessRight());
            return (comp == 0) ? Collator.getInstance(Locale.US).compare(c1.getUsername(), c2.getUsername()) : comp;
        }
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
        } else {
            // TODO go back to collaboration fragment
        }
    }
}
