package org.gammf.collabora_android.app.gui.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import org.gammf.collabora_android.app.utils.AbstractObservableSource;
import org.gammf.collabora_android.notes.Location;
import org.gammf.collabora_android.notes.NoteLocation;

/**
 * A class that manages Google Map, used for searching and set location in notes.
 */
public class MapManager extends AbstractObservableSource<Location> implements PlaceSelectionListener, OnMapReadyCallback {

    /**
     * The default location, used when no default location is needed.
     */
    public static final Location NO_LOCATION = null;

    private static final String TAG = MapManager.class.getSimpleName();
    private static final String MAPSEARCH_ERROR = "An error occurred in map: ";
    private static final Double STARTING_LATITUDE = 42.50;
    private static final Double STARTING_LONGITUDE = 12.50;
    private static final int STARTING_ZOOM = 15;
    private static final int ANIMATION_ZOOM = 5;
    private static final int ANIMATION_DURATION = 2000;
    private static final int ZOOM_NOTE = 17;
    private static final int BEARING_NOTE = 90;
    private static final int TILT_NOTE = 30;

    private MapView mapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private final Location initialLocation;
    private final Context context;

    /**
     * Builds a MapManager, starting with the given start {@link Location}. If no location is known, use MapManager.NO_LOCATION
     * @param initialLocation the initial location, displayed by this map.
     */
    public MapManager(final Location initialLocation, final Context context) {
        this.initialLocation = initialLocation;
        this.context = context;
    }

    @Override
    public void onPlaceSelected(final Place place) {
        this.notifyObservers(new NoteLocation(place.getLatLng().latitude, place.getLatLng().longitude));
        this.updateMap(place.getLatLng());
    }

    @Override
    public void onError(final Status status) {
        Log.i(TAG, MAPSEARCH_ERROR + status);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.googleMap = map;
        this.setUpMap();
    }

    /**
     * Creates the map view, based on the given view, and the saved instace passed.
     * @param view the view of the map
     * @param savedInstanceState the saved instance.
     */
    public void createMap(final MapView view, final Bundle savedInstanceState) {
        this.mapView = view;
        this.mapView.onCreate(savedInstanceState);
        this.mapView.onResume();
        this.mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
    }

    private void setUpMap() {
        if (this.mapView != null && this.initialLocation != NO_LOCATION) {
            this.googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                    .position(new LatLng(this.initialLocation.getLatitude(), this.initialLocation.getLongitude())));
            if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            this.googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this.context);

            final LatLng italy = new LatLng(STARTING_LATITUDE, STARTING_LONGITUDE);
            final LatLng coordinates = new LatLng(this.initialLocation.getLatitude(), this.initialLocation.getLongitude());
            // Move the camera instantly to Italy with a zoom of 15.
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(italy, STARTING_ZOOM));
            // Zoom in, animating the camera.
            this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(ANIMATION_ZOOM), ANIMATION_DURATION, null);
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            this.cameraPosition = new CameraPosition.Builder()
                    .target(coordinates)      // Sets the center of the map to Mountain View
                    .zoom(ZOOM_NOTE)                   // Sets the zoom
                    .bearing(BEARING_NOTE)                // Sets the orientation of the camera to east
                    .tilt(TILT_NOTE)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    return false;
                }
            });
        }
    }

    private void updateMap(LatLng newCoordinates){
        this.googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                .position(newCoordinates));
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(ANIMATION_ZOOM), ANIMATION_DURATION, null);
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        this.cameraPosition = new CameraPosition.Builder()
                .target(newCoordinates)     // Sets the center of the map to Mountain View
                .zoom(ZOOM_NOTE)             // Sets the zoom
                .bearing(BEARING_NOTE)       // Sets the orientation of the camera to east
                .tilt(TILT_NOTE)             // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(this.cameraPosition));
        this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return false;
            }
        });
    }

}
