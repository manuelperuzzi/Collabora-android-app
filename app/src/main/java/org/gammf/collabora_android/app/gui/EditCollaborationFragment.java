package org.gammf.collabora_android.app.gui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCollaborationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView memberList;
    private ArrayList<DataModel> memberItem;
    private DrawerItemCustomAdapter adapter;

    public EditCollaborationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCollaborationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCollaborationFragment newInstance(String param1, String param2) {
        EditCollaborationFragment fragment = new EditCollaborationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_collaboration, container, false);
        Button btnAddMember = rootView.findViewById(R.id.btnAddMember);
        final EditText txtNewTitle = rootView.findViewById(R.id.txtInsertEditedCollabName);
        memberList = rootView.findViewById(R.id.listViewCollabMember);
        memberItem = new ArrayList<DataModel>();
        memberItem.add(new DataModel(R.drawable.user, "Mario Rossi"));
        memberItem.add(new DataModel(R.drawable.user, "Luca Bianchi"));
        memberItem.add(new DataModel(R.drawable.user, "Giovanni Verdi"));
        adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.member_list_item, memberItem);
        memberList.setAdapter(adapter);

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberItem.add(new DataModel(R.drawable.user, "New Member"));
                adapter.notifyDataSetChanged();
            }
        });


        FloatingActionButton btnEditDone = rootView.findViewById(R.id.btnCollabEditDone);
        btnEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = txtNewTitle.getText().toString();
                if(newName.equals("")) {
                    CollaborationFragment collabFragment = new CollaborationFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("BOOLEAN_VALUE", false);
                    collabFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, collabFragment).commit();
                }else{
                    updateCollaboration(newName);
                }
            }
        });
        return rootView;
    }

    private void updateCollaboration(String collabName){
        ((MainActivity)getActivity()).updateCollaborationList(this, collabName);
    }

}
