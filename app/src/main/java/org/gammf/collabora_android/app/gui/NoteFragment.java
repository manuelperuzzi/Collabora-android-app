package org.gammf.collabora_android.app.gui;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.gammf.collabora_android.app.R;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class NoteFragment extends Fragment
implements OnMapReadyCallback{

    TextView contentNote;
    String collabname;
    ProgressBar progressBarState;
    TextView lblState;
    TextView lblResponsible;
    MapView mapView;
    GoogleMap googleMap;
    CameraPosition cameraPosition;
    TextView expiration;

    public NoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        Bundle bundle = getArguments();
        collabname =  bundle.getString("collabName");

        contentNote = rootView.findViewById(R.id.contentNote);
        contentNote.setText("Content note will be there, scrivo qualcosa per farlo andare su due linee");

        progressBarState = rootView.findViewById(R.id.progressBarState);
        lblState = rootView.findViewById(R.id.lblState);
        lblState.setText("Doing");
        lblResponsible = rootView.findViewById(R.id.lblResponsible);
      //  mapView = rootView.findViewById(R.id.mapViewLocation);
        expiration = rootView.findViewById(R.id.expiration);

        setStateProgressBar(lblState.getText().toString());


        return rootView;
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
                    .position(new LatLng(44.1390945, 12.2429281)));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this.getActivity());

            LatLng italy = new LatLng(42.50, 12.50);
            LatLng coordinates = new LatLng(44.1390945, 12.2429281);
            // Move the camera instantly to Italy with a zoom of 15.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(italy, 15));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            cameraPosition = new CameraPosition.Builder()
                    .target(coordinates)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
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
