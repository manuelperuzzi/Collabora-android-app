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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.gammf.collabora_android.app.R;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class NoteFragment extends Fragment
implements OnMapReadyCallback{

    private static final double LAT = 32.084;
    private static final double LON = 34.8878;
    TextView contentNote;
    String collabname;
    ProgressBar progressBarState;
    TextView lblState;
    TextView lblResponsible;
    MapView mapView;
    GoogleMap googleMap;
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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.collaboration_icon))
                    .anchor(0.0f, 1.0f)
                    .position(new LatLng(44.1390945, 12.2429281)));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this.getActivity());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(44.1390945, 12.2429281));
            LatLngBounds bounds = builder.build();
            int padding = 0;
            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cameraUpdate);
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
