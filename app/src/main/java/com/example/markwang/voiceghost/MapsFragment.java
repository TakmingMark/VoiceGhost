package com.example.markwang.voiceghost;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private final String TAG = "MapsFragment";
    SupportMapFragment mapFragment;
    private Context mContext;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Marker mLocationMarker;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;

    private ActivityCallback mActivityCallback;

    private TextView dropBlock;
    private int dropBlockHeight;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null)
                return;
            for (Location location : locationResult.getLocations()) {
                if (mLocationMarker != null)
                    mLocationMarker.remove();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    };

    public static MapsFragment newInstance() {
        MapsFragment mapsFragment = new MapsFragment();
        return mapsFragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View contentView = inflater.inflate(R.layout.activity_maps, null);
        initLoyout(contentView);

        return contentView;
    }

    private void initLoyout(View contentView) {
        dropBlock = contentView.findViewById(R.id.dropBlock);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dropBlock.post(new Runnable() {
            @Override
            public void run() {
                dropBlockHeight=dropBlock.getMeasuredHeight();
                returnInformation();
            }
        });

    }

    public void setActivityCallback(ActivityCallback activityCallback){
        mActivityCallback=activityCallback;
    }
    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this).addApi(LocationServices.API).build();
        //callback onConnected();
        mGoogleApiClient.connect();
    }

    private void moveMap(LatLng place) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void returnInformation(){
        mActivityCallback.onDropDownHeight(dropBlockHeight);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        buildGoogleApiClient();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //if GPS update position, will be callback mLocationCallback
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    interface ActivityCallback{
        void onDropDownHeight(int dropDownHeight);
    }
}
