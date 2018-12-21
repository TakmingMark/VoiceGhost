package com.example.markwang.voiceghost.fragment;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.component.SoundPositionManager;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, SoundPositionManager.Callback {

    private final String TAG = "MapsFragment";
    private SupportMapFragment mGoogleMapFragment;
    private Context mContext;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker mUserMarker;
    private Marker mCustomMarker;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private boolean isFirstLocation=false;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null)
                return;
            for (Location location : locationResult.getLocations()) {
                if (mUserMarker != null)
                    mUserMarker.remove();
                VoiceGhostInfo voiceGhostInfo = new VoiceGhostInfo();
                voiceGhostInfo.lattude = location.getLatitude() + "";
                voiceGhostInfo.longitude = location.getLongitude() + "";
                voiceGhostInfo.title = "Mark in Taiwan";
                mUserMarker = setMark(voiceGhostInfo, R.drawable.user_indicate);

                if(!isFirstLocation){
                    isFirstLocation=!isFirstLocation;
                    LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                    moveMap(latLng);
                }
            }
        }
    };

    public static MapsFragment newInstance(String str) {
        MapsFragment mapsFragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("wantStore", str);
        mapsFragment.setArguments(bundle);
        return mapsFragment;
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        mContext = context;
        super.onAttach(context);
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
        mGoogleMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //callback onMapReady;
        mGoogleMapFragment.getMapAsync(this);
    }


    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient");
        // Use the GoogleApiClient.Builder class to create an instance of the
        // Google Play Services API client//
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this).addApi(LocationServices.API).build();

        // Connect to Google Play Services, by calling the connect() method//
        //callback onConnected;
        mGoogleApiClient.connect();
        initGoogleMapListener();

    }

    private void moveMap(LatLng place) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(17).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void initGoogleMapListener() {
        SoundPositionManager.getInstance().setCallback(this);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "Map clicked:[" + latLng.latitude + "/" + latLng.longitude + "]");
            }
        });
    }

    private Bitmap reSizeIcon(int icon) {
        int height = 50;
        int width = 50;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(icon);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }

    private Marker setMark(VoiceGhostInfo voiceGhostInfo, int icon) {
        Marker marker;
        double lat = Double.valueOf(voiceGhostInfo.lattude);
        double lng = Double.valueOf(voiceGhostInfo.longitude);
        LatLng latLng = new LatLng(lat, lng);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(reSizeIcon(icon));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(voiceGhostInfo.title).icon(bitmapDescriptor);
        marker = mGoogleMap.addMarker(markerOptions);
        return marker;
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
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

    @Override
    public void onDataUpdate(ArrayList<VoiceGhostInfo> voiceGhostInfoArrayList) {
        if (mCustomMarker != null)
            mCustomMarker.remove();
        for (VoiceGhostInfo voiceGhostInfo : voiceGhostInfoArrayList) {
            mCustomMarker = setMark(voiceGhostInfo, R.drawable.custom_indicate);
        }
    }
}
