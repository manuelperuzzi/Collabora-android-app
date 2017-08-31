package org.gammf.collabora_android.app.gui;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import org.gammf.collabora_android.app.rabbitmq.SendMessageToServerTask;
import org.gammf.collabora_android.collaborations.general.Collaboration;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.communication.update.notes.ConcreteNoteUpdateMessage;
import org.gammf.collabora_android.notes.Location;
import org.gammf.collabora_android.notes.Note;
import org.gammf.collabora_android.notes.NoteLocation;
import org.gammf.collabora_android.notes.NoteState;
import org.gammf.collabora_android.utils.LocalStorageUtils;
import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.IOException;
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

    private static final String CREATIONERROR_FRAG = "Error in creating fragment";
    private static final String MAPSEARCH_ERROR = "An error occurred: ";
    private static final String ERR_STATENOTSELECTED = "Please select state";
    private static final String SENDER = "editnotefrag";

    private static final String ARG_USERNAME = "username";
    private static final String ARG_COLLABID = "collabId";
    private static final String ARG_NOTEID = "noteId";

    private String username;
    private String noteStateEdited = "";
    private TextView dateViewEdited, timeViewEdited;
    private EditText txtContentNoteEdited;
    private MapView mapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private Spinner spinnerEditState;
    private DatePickerDialog.OnDateSetListener myDateListenerEdited;
    private TimePickerDialog.OnTimeSetListener myTimeListenerEdited;

    private String collaborationId, noteId;

    private static final Double startingLat = 42.50;
    private static final Double startingLng = 12.50;
    private static final int startingZoom = 15;
    private static final int animationZoom = 5;
    private static final int animationMsDuration = 2000;
    private static final int zoomNote = 17;
    private static final int bearingNote = 90;
    private static final int tiltNote = 30;

    private Note note;
    private int year, month, day, hour, minute;

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
    public static EditNoteFragment newInstance(String username, String collabId, String noteId) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_COLLABID, collabId);
        args.putString(ARG_NOTEID, noteId);
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
            this.noteId = getArguments().getString(ARG_NOTEID);
        }

        try {
            final Collaboration collaboration = LocalStorageUtils.readCollaborationFromFile(getContext(), collaborationId);
            note = collaboration.getNote(noteId);
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
            checkUserNoteUpdate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        initializeGuiComponent(rootView);

        return rootView;
    }

    private void initializeGuiComponent(View rootView){
        txtContentNoteEdited = rootView.findViewById(R.id.txtNoteContentEdit);
        txtContentNoteEdited.setText(note.getContent());
        txtContentNoteEdited.requestFocus();
        SupportPlaceAutocompleteFragment autocompleteFragmentEdited = new SupportPlaceAutocompleteFragment();
        getFragmentManager().beginTransaction().replace(R.id.place_autocomplete_fragment_edit, autocompleteFragmentEdited).commit();
        autocompleteFragmentEdited.setOnPlaceSelectedListener(this);

        spinnerEditState = rootView.findViewById(R.id.spinnerEditNoteState);
        setSpinner();

        dateViewEdited = rootView.findViewById(R.id.txtEditDateSelected);
        timeViewEdited = rootView.findViewById(R.id.txtEditTimeSelected);
        ImageButton btnSetDateExpiration = rootView.findViewById(R.id.btnEditDateExpiration);
        ImageButton btnSetTimeExpiration = rootView.findViewById(R.id.btnEditTimeExpiration);
        if (note.getExpirationDate() != null) {
            dateViewEdited.setText(note.getExpirationDate().toLocalDate().toString());
            timeViewEdited.setText(note.getExpirationDate().toLocalTime().toString());
            btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), myDateListenerEdited,
                            note.getExpirationDate().getYear(),
                            note.getExpirationDate().getMonthOfYear(),
                            note.getExpirationDate().getDayOfMonth()).show();
                }
            });
            btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getActivity(), myTimeListenerEdited,
                            note.getExpirationDate().getHourOfDay(),
                            note.getExpirationDate().getMinuteOfHour(), true).show();
                }
            });
        } else {
            final Calendar calendar = Calendar.getInstance();
            btnSetDateExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), myDateListenerEdited,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            btnSetTimeExpiration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(getActivity(), myTimeListenerEdited,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE), true).show();
                }
            });
        }

        myDateListenerEdited = this;
        myTimeListenerEdited = this;
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

    private void checkUserNoteUpdate(){
        String insertedNoteName = txtContentNoteEdited.getText().toString();
        if(insertedNoteName.equals("")){
            txtContentNoteEdited.setError(getResources().getString(R.string.fieldempty));
        }else{
            note.modifyContent(insertedNoteName);
            if (isDateTimeValid()) {
                note.modifyExpirationDate(new DateTime(year, month, day, hour, minute));
            }
            note.modifyState(new NoteState(noteStateEdited, null));

            new SendMessageToServerTask().execute(new ConcreteNoteUpdateMessage(
                username, note, UpdateMessageType.UPDATING, collaborationId));

            changeFragment(NoteFragment.newInstance(username, collaborationId, noteId));
        }
    }

    private boolean isDateTimeValid() {
        return year > 0 && month > 0 && day > 0 && hour >=0 && minute >= 0;
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
        note.modifyLocation(new NoteLocation(place.getLatLng().latitude, place.getLatLng().longitude));
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
        if (mapView != null && note.getLocation() != null ) {
            final Location location = note.getLocation();
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this.getActivity());

            LatLng italy = new LatLng(startingLat, startingLng);
            LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
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
        this.year = year;
        this.month = month + 1;
        this.day = day;
        showDate();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        showTime();
    }

    private void showDate() {
        dateViewEdited.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime() {
        timeViewEdited.setText(new StringBuilder().append(hour).append(":").append(minute));
    }
}
