package org.gammf.collabora_android.app.gui;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCollaborationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCollaborationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtCollabName;
    private Spinner spinnerCollabType;
    private String spinnerSelection;

    public CreateCollaborationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateCollaborationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCollaborationFragment newInstance(String param1, String param2) {
        CreateCollaborationFragment fragment = new CreateCollaborationFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_create_collaboration, container, false);
        txtCollabName = rootView.findViewById(R.id.txtInsertCollabNameD);
        txtCollabName.requestFocus();
        spinnerCollabType = (Spinner) rootView.findViewById(R.id.spinnerCollabType);
        List<String> list = new ArrayList<String>();
        list.add("Group");
        list.add("Project");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCollabType.setAdapter(dataAdapter);
        spinnerCollabType.setSelection(0);
        spinnerCollabType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSelection = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        FloatingActionButton btnAddCollaboration = rootView.findViewById(R.id.btnAddNewCollaborationDone);
        btnAddCollaboration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String insertedCollabName = txtCollabName.getText().toString();
                if(insertedCollabName.equals("")){
                    Resources res = getResources();
                    txtCollabName.setError(res.getString(R.string.fieldempty));
                }else {
                    addCollaboration(insertedCollabName);
                }
            }
        });
        return rootView;
    }

    private void addCollaboration(String collabName){
        ((MainActivity)getActivity()).updateCollaborationList(this, collabName);
    }
}
