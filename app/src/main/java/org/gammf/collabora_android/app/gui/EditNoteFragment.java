package org.gammf.collabora_android.app.gui;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditNoteFragment extends Fragment implements PlaceSelectionListener,
        OnMapReadyCallback, AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String BACKSTACK_FRAG = "xyz";
    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String MAPSEARCH_ERROR = "An error occurred: ";
    private static final String ERR_STATENOTSELECTED = "Please select state";
    private static final String SENDER = "editnotefrag";

    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_MODULEID = "moduleId";
    private static final String ARG_NOTEID = "noteId";


    private SupportPlaceAutocompleteFragment autocompleteFragmentEdited;
    private String noteStateEdited = "";
    private Calendar calendarEdited;
    private TextView dateViewEdited, timeViewEdited;
    private int yearEdited, monthEdited, dayEdited, hourEdited, minuteEdited;
    private EditText txtContentNoteEdited;
    private MapView mapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private LatLng newCoordinates;
    private Spinner spinnerEditState;
    private DatePickerDialog.OnDateSetListener myDateListenerEdited;
    private TimePickerDialog.OnTimeSetListener myTimeListenerEdited;

    private String collaborationId, collabname, collabtype, moduleId, noteId;

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

    public EditNoteFragment() {
        setHasOptionsMenu(true);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditNoteFragment newInstance(String collabId, String moduleId, String noteId) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_MODULEID, moduleId);
        args.putString(ARG_NOTEID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            this.collaborationId = getArguments().getString(ARG_COLLABID);
            this.moduleId = getArguments().getString(ARG_MODULEID);
            this.noteId = getArguments().getString(ARG_NOTEID);
        }

        getNoteDataFromServer();
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
            String insertedNoteName = txtContentNoteEdited.getText().toString();
            if(insertedNoteName.equals("")){
                Resources res = getResources();
                txtContentNoteEdited.setError(res.getString(R.string.fieldempty));
            }else{

                //qui mettere il codice per aggiornare la nota
                //il nuovo content è in insertedNoteName
                //nuovo stato in noteStateEdited
                //le nuove coordinate sono in newCoordinates
                String newDateExp = dateViewEdited.getText().toString();
                String newTimeExp = timeViewEdited.getText().toString();

                changeFragment(NoteFragment.newInstance(collaborationId, noteId));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        txtContentNoteEdited = rootView.findViewById(R.id.txtNoteContentEdit);
        txtContentNoteEdited.requestFocus();
        autocompleteFragmentEdited = new SupportPlaceAutocompleteFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.place_autocomplete_fragment_edit, autocompleteFragmentEdited);
        ft.commit();
        autocompleteFragmentEdited.setOnPlaceSelectedListener(this);
        myDateListenerEdited = this;
        myTimeListenerEdited = this;
        spinnerEditState = (Spinner) rootView.findViewById(R.id.spinnerEditNoteState);
        setSpinner();

        dateViewEdited = rootView.findViewById(R.id.txtEditDateSelected);
        calendarEdited = Calendar.getInstance();
        yearEdited = calendarEdited.get(Calendar.YEAR);
        monthEdited = calendarEdited.get(Calendar.MONTH);
        dayEdited = calendarEdited.get(Calendar.DAY_OF_MONTH);
        showDate(yearEdited, monthEdited+1, dayEdited);

        timeViewEdited = rootView.findViewById(R.id.txtEditTimeSelected);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strTime = sdf.format(calendarEdited.getTime());
        timeViewEdited.setText(strTime);


        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnEditDateExpiration);
        btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),
                        myDateListenerEdited, yearEdited, monthEdited, dayEdited).show();
            }
        });

        ImageButton btnSetTimeExpiration = rootView.findViewById(R.id.btnEditTimeExpiration);
        btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(),
                        myTimeListenerEdited, hourEdited, minuteEdited, true).show();
            }
        });

        return rootView;
    }

    private void setSpinner(){
        List<NoteProjectState> stateList = new ArrayList<>();
        stateList.addAll(Arrays.asList(NoteProjectState.values()));
        ArrayAdapter<NoteProjectState> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditState.setAdapter(dataAdapter);
        spinnerEditState.setOnItemSelectedListener(this);
    }

    private void showDate(int year, int month, int day) {
        dateViewEdited.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime(int hour, int minute){
        timeViewEdited.setText(new StringBuilder().append(hour).append(":").append(minute));
    }
    private void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.remove(EditNoteFragment.this);
            fragmentTransaction2.commit();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Log.e(SENDER, CREATIONERROR_FRAG);
        }
    }
    @Override
    public void onPlaceSelected(Place place) {

        String placeDetailsStr = place.getName()+"";
        newCoordinates = place.getLatLng();
              /*
              PLACE INFO:
                 place.getName()
                 place.getId()
                 place.getLatLng().toString()
                 place.getAddress()
                 place.getAttributions()
              */

        updateMap(place.getLatLng());
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, MAPSEARCH_ERROR + status);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mapView = view.findViewById(R.id.mapViewLocationEdit);
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
            newCoordinates = coordinates;
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

    private void updateMap(LatLng newCoordinates){
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                .position(newCoordinates));
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(animationZoom), animationMsDuration, null);
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        cameraPosition = new CameraPosition.Builder()
                .target(newCoordinates)     // Sets the center of the map to Mountain View
                .zoom(zoomNote)             // Sets the zoom
                .bearing(bearingNote)       // Sets the orientation of the camera to east
                .tilt(tiltNote)             // Sets the tilt of the camera to 30 degrees
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

    private void getNoteDataFromServer(){

        //RECUPERARE I DATI QUI: ci sono gli id nei campi
        // e mettere i valori nelle rispettive variabili
        // poi settare sempre i dati all'utente col setText nel onViewCreated
        // per mettere i campi nella gui come sono prima della modifica
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        NoteProjectState item = (NoteProjectState) adapterView.getItemAtPosition(i);
        noteStateEdited = item.toString();
        Log.println(Log.ERROR, "ERRORONI", ""+noteStateEdited);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), ERR_STATENOTSELECTED, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        showDate(year, month+1, day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        showTime(hour,minute);
    }
}
