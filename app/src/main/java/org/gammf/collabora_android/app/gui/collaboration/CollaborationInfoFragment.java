package org.gammf.collabora_android.app.gui.collaboration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.app.gui.CollaborationComponentInfo;
import org.gammf.collabora_android.app.gui.CollaborationComponentType;
import org.gammf.collabora_android.app.gui.DrawerItemCustomAdapter;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.collaborations.private_collaborations.PrivateCollaboration;
import org.gammf.collabora_android.collaborations.shared_collaborations.SharedCollaboration;
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
 * Use the {@link CollaborationInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollaborationInfoFragment extends Fragment {

    private static final String ADD_MEMBER_DIALOG_TAG = "AddMemberDialogFragment";
    private static final String CHOOSE_MEMBER_ACTION_DIALOG_TAG = "ChooseMemberActionDialogTag";
    private static final String EDIT_COLLABORATION_DIALOG_TAG = "EditCollaborationDialogTag";

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABORATION_ID = "collaborationId";

    private String username;
    private Collaboration collaboration;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param collaborationId collaboration id
     * @return A new instance of fragment CollaborationInfoFragment.
     */

    public static CollaborationInfoFragment newInstance(final String username, final String collaborationId) {
        final CollaborationInfoFragment fragment = new CollaborationInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABORATION_ID, collaborationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            this.username = getArguments().getString(ARG_USERNAME);
            final String collaborationId = getArguments().getString(ARG_COLLABORATION_ID);
            try {
                this.collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            } catch (final IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collaboration_info, container, false);
        initializeGuiComponent(rootView);

        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        final EditText txtName = rootView.findViewById(R.id.txtCollaborationName);
        txtName.setText(collaboration.getName());
        txtName.setKeyListener(null);
        final TextView txtCollaborationType = rootView.findViewById(R.id.txtCollabType);
        txtCollaborationType.setText(new ConcreteShortCollaboration(collaboration).getCollaborationType().name());
        final ListView membersListView = rootView.findViewById(R.id.listViewCollabMember);

        final ImageButton btnEditCollaborationName = rootView.findViewById(R.id.btnEditCollaboration);
        btnEditCollaborationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditCollaborationDialogFragment dialog = EditCollaborationDialogFragment.newInstance(collaboration.getId(), username);
                dialog.show(getActivity().getSupportFragmentManager(), EDIT_COLLABORATION_DIALOG_TAG);
            }
        });
        final ImageButton btnAddMember = rootView.findViewById(R.id.btnAddMember);
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberDialogFragment dialog = MemberDialogFragment.addMemberInstance(collaboration.getId(), username);
                dialog.show(getActivity().getSupportFragmentManager(), ADD_MEMBER_DIALOG_TAG);
            }
        });

        final AccessRight userRight;
        if (collaboration instanceof PrivateCollaboration) {
            userRight = AccessRight.ADMIN;
        } else {
            userRight = ((SharedCollaboration) collaboration).getMember(username).getAccessRight();
        }

        final boolean isCollaborationEditable = userRight.equals(AccessRight.ADMIN) && collaboration instanceof SharedCollaboration;
        if (! isCollaborationEditable) {
            btnEditCollaborationName.setVisibility(View.GONE);
            btnAddMember.setVisibility(View.GONE);
        }
        visualizeMembers(membersListView, isCollaborationEditable);
    }

    private void visualizeMembers(final ListView membersListView, final boolean isEditable) {
        final ArrayList<CollaborationComponentInfo> memberItem = new ArrayList<>();
        if (collaboration instanceof PrivateCollaboration) {
            memberItem.add(new CollaborationComponentInfo(username, username + " (" + AccessRight.ADMIN + ")", CollaborationComponentType.MEMBER));
        } else {
            final List<CollaborationMember> members = new ArrayList<>(((SharedCollaboration) collaboration).getAllMembers());
            Collections.sort(members, new MemberComparator());
            for (final CollaborationMember cm: members) {
                memberItem.add(new CollaborationComponentInfo(cm.getUsername(), cm.getUsername() + " (" + cm.getAccessRight() + ")", CollaborationComponentType.MEMBER));
            }
        }

        final DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(), R.layout.member_list_item, memberItem);
        membersListView.setAdapter(adapter);
        if (isEditable) {
            membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final CollaborationComponentInfo component = (CollaborationComponentInfo) adapterView.getItemAtPosition(i);
                    final AccessRight memberRight = ((SharedCollaboration) collaboration).getMember(component.getId()).getAccessRight();
                    final ChooseMemberActionDialogFragment dialog = ChooseMemberActionDialogFragment.newInstance(
                            collaboration.getId(), username, component.getId(), memberRight.name());
                    dialog.show(getActivity().getSupportFragmentManager(), CHOOSE_MEMBER_ACTION_DIALOG_TAG);
                }
            });
        }
    }

    private class MemberComparator implements Comparator<CollaborationMember> {
        public int compare(final CollaborationMember c1, final CollaborationMember c2) {
            final int comp = c1.getAccessRight().compareTo(c2.getAccessRight());
            return (comp == 0) ? Collator.getInstance(Locale.US).compare(c1.getUsername(), c2.getUsername()) : comp;
        }
    }
}
