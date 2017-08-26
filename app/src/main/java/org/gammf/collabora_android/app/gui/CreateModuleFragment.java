package org.gammf.collabora_android.app.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gammf.collabora_android.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateModuleFragment extends Fragment {

    private static final String ARG_SENDER = "sender";
    private static final String ARG_COLLABID = "collabid";

    // TODO: Rename and change types of parameters
    private String sender, collaborationId;


    public CreateModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sender fragment caller
     * @param collaborationId collaboration id where the module will be added
     * @return A new instance of fragment CreateModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateModuleFragment newInstance(String sender, String collaborationId) {
        CreateModuleFragment fragment = new CreateModuleFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_SENDER, sender);
        arg.putString(ARG_COLLABID, collaborationId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.sender = getArguments().getString(ARG_SENDER);
            this.collaborationId = getArguments().getString(ARG_COLLABID);
        }
        getModuleDataFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_module, container, false);

        return rootView;
    }

    private void getModuleDataFromServer(){


    }

}
