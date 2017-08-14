package org.gammf.collabora_android.app.gui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.gammf.collabora_android.app.R;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class NoteFragment extends Fragment {

    TextView contentNote;
    String collabname;
    ProgressBar progressBarState;
    TextView lblState;
    TextView lblResponsible;
    MapView location;
    TextClock expiration;

    public NoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        Bundle bundle = getArguments();
        collabname =  bundle.getString("collabName");

        contentNote = rootView.findViewById(R.id.contentNote);
        contentNote.setText("Content Note");

        progressBarState = rootView.findViewById(R.id.progressBarState);
        lblState = rootView.findViewById(R.id.lblState);
        lblState.setText("Doing");
        lblResponsible = rootView.findViewById(R.id.lblResponsible);
        location = rootView.findViewById(R.id.mapViewLocation);
        expiration = rootView.findViewById(R.id.expiration);

        setStateProgressBar(lblState.getText().toString());


        return rootView;
    }

    private void setStateProgressBar(String state){
        switch(state){
            case "Doing" :{
                progressBarState.setProgress(50);
                progressBarState.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "To-do" :{
                progressBarState.setProgress(20);
                progressBarState.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                break;
            }
            case "Done" :{
                progressBarState.setProgress(100);
                progressBarState.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                break;
            }
            default:{
                progressBarState.setProgress(80);
                progressBarState.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                break;
            }
        }


        //Only for min API 21
        //progressBarState.setProgressTintList(ColorStateList.valueOf(Color.RED));
    }


    /*   Handler txtsettext = new Handler(Looper.getMainLooper());
        txtsettext.post(new Runnable() {
            public void run() {
                contentNote.setText(collabname);
            }
        });
*/

}
