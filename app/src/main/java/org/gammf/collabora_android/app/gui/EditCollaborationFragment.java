package org.gammf.collabora_android.app.gui;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCollaborationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLLABNAME = "collabName";
    private static final String ARG_COLLABTYPE = "collabType";

    // TODO: Rename and change types of parameters
    private String collabName, collabType;
    private Resources res;
    private ListView memberList;
    private ArrayList<DataModel> memberItem;
    private DrawerItemCustomAdapter adapter;
    private EditText txtNewTitle;
    private Button btnAddMember;
    private FloatingActionButton btnEditDone;
    private TextView txtCollabType;

    private boolean memberHasChanged = false;

    public EditCollaborationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nameToEdit collaboration name
     * @param collabType collaboration type
     * @return A new instance of fragment EditCollaborationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCollaborationFragment newInstance(String nameToEdit, String collabType) {
        EditCollaborationFragment fragment = new EditCollaborationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLLABNAME, nameToEdit);
        args.putString(ARG_COLLABTYPE, collabType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            collabName = getArguments().getString(ARG_COLLABNAME);
            collabType = getArguments().getString(ARG_COLLABTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_collaboration, container, false);
        res = getResources();
        btnAddMember = rootView.findViewById(R.id.btnAddMember);
        txtNewTitle = rootView.findViewById(R.id.txtInsertEditedCollabName);
        memberList = rootView.findViewById(R.id.listViewCollabMember);
        txtNewTitle.setText(collabName);
        btnEditDone = rootView.findViewById(R.id.btnCollabEditDone);
        txtCollabType = rootView.findViewById(R.id.txtCollabType);
        txtCollabType.setText(collabType);

        memberItem = new ArrayList<DataModel>();
        getMemberAndFillList();
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMemberList();
            }
        });

        btnEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = txtNewTitle.getText().toString();
                //If newName is empty
                if(newName.equals("")) {
                    //display error for field required
                    txtNewTitle.setError(res.getString(R.string.fieldempty));

                    //if name isn't changed
                }else if(newName.equals(collabName)){

                    //check if member(s) was added.
                    if(memberHasChanged) {
                        returnToCollabFragment();
                    }else { //members not changed and name not modified

                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Edit discarded", Toast.LENGTH_SHORT);
                        toast.show();

                        returnToCollabFragment();
                    }
                }else{
                    updateCollaboration(newName);
                }
            }
        });
        return rootView;
    }

    private void getMemberAndFillList(){

        //HERE THE CODE FOR GET THE MEMBER LIST

        /*codice esempio per aggiungere alla lista*/
        memberItem.add(new DataModel(R.drawable.user, "Mario Rossi"));
        memberItem.add(new DataModel(R.drawable.user, "Luca Bianchi"));
        memberItem.add(new DataModel(R.drawable.user, "Giovanni Verdi"));

        //setting the list adapter
        adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.member_list_item, memberItem);
        memberList.setAdapter(adapter);
    }

    private void updateMemberList(){

        memberItem.add(new DataModel(R.drawable.user, "New Member"));
        adapter.notifyDataSetChanged();

        memberHasChanged = true; //importante per i controlli se è stata fatta una modifica o meno
    }

    private void updateCollaboration(String collabName){
        ((MainActivity)getActivity()).updateCollaborationList(this, collabName, collabType);
    }

    private void returnToCollabFragment(){
        CollaborationFragment collabFragment = CollaborationFragment.newInstance("editcollab", collabName, collabType);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, collabFragment).commit();

    }

}
