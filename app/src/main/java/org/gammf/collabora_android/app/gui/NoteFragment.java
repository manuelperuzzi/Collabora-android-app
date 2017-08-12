package org.gammf.collabora_android.app.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.gammf.collabora_android.app.R;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class NoteFragment extends Fragment {

    TextView label;
    String collabname;

    public NoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        Bundle bundle = getArguments();
        collabname =  bundle.getString("collabName");
        label = rootView.findViewById(R.id.titleFragmentNote);
        label.setText(collabname);


     /*   Handler txtsettext = new Handler(Looper.getMainLooper());
        txtsettext.post(new Runnable() {
            public void run() {
                label.setText(collabname);
            }
        });
*/


        return rootView;
    }

}
