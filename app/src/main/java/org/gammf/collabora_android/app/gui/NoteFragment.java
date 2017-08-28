package org.gammf.collabora_android.app.gui;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.gammf.collabora_android.app.R;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class NoteFragment extends Fragment
implements OnMapReadyCallback{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String SENDER = "notefrag";

    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_NOTEID = "noteId";

    private TextView contentNote;
    private String collaborationId, noteId;
    private String collabname, collabtype, notename;
    private ProgressBar progressBarState;
    private TextView lblState;
    private TextView lblResponsible;
    private MapView mapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private TextView expiration;

    private Double startingLat = 42.50;
    private Double startingLng = 12.50;
    private int startingZoom = 15;
    private int animationZoom = 5;
    private int animationMsDuration = 2000;
    private int zoomNote = 17;
    private int bearingNote = 90;
    private int tiltNote = 30;

    private Double latitudeNote = 44.1390945;
    private Double longitudeNote = 12.2429281;


    public NoteFragment() {
        setHasOptionsMenu(true);
    }

    public static NoteFragment newInstance(String collabId, String noteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_NOTEID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.noteId = getArguments().getString(ARG_NOTEID);
        }
        setHasOptionsMenu(true);
        getNoteDataFromServer();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_note, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Method for editcollaboration click on toolbar
        trigger the @EditCollaborationFragment
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_editnote) {
            Fragment editNoteFragment = EditNoteFragment.newInstance(collaborationId, "FINTOIDMODULE", notename);
            changeFragment(editNoteFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        initializeGuiComponent(rootView);
        setStateProgressBar(lblState.getText().toString());

        return rootView;
    }

    private void initializeGuiComponent(View rootView) {
        contentNote = rootView.findViewById(R.id.contentNote);
        contentNote.setText("Content note will be there, scrivo qualcosa per farlo andare su due linee");

        progressBarState = rootView.findViewById(R.id.progressBarState);
        lblState = rootView.findViewById(R.id.lblState);
        lblState.setText("Doing");
        lblResponsible = rootView.findViewById(R.id.lblResponsible);
        expiration = rootView.findViewById(R.id.expiration);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mapView = view.findViewById(R.id.mapViewLocation);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }

    private void setUpMap(){
        if (mapView != null) {
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                    .position(new LatLng(latitudeNote, longitudeNote)));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this.getActivity());

            LatLng italy = new LatLng(startingLat, startingLng);
            LatLng coordinates = new LatLng(latitudeNote, longitudeNote);
            // Move the camera instantly to Italy with a zoom of 15.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(italy, startingZoom));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(animationZoom), animationMsDuration, null);
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            cameraPosition = new CameraPosition.Builder()
                    .target(coordinates)      // Sets the center of the map to Mountain View
                    .zoom(zoomNote)                   // Sets the zoom
                    .bearing(bearingNote)                // Sets the orientation of the camera to east
                    .tilt(tiltNote)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    return false;
                }
            });
        }
    }

    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.addToBackStack(BACKSTACK_FRAG);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }
    /***
     * DA SISTEMARE SIA LA CHIAMATA AL METODO CHE IL METODO STESSO
     *
     * La progress bar è da decidere come settare i vari stati in base al valore
     *
     * @param state stato della nota contenuto in @NoteProjectState
     */
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
    }


    private void getNoteDataFromServer(){

        //prendere i dati dal server e metterli dentro le variabili adatte

        //poi DENTRO ONVIEWCREATED fare tutti i setText sui rispettivi campi per visualizzarli all'utente
    }

}
