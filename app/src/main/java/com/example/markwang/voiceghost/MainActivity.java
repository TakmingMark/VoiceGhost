package com.example.markwang.voiceghost;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mapContainer, MapsFragment.newInstance(), "f1")
                    .commit();
        }
    }

    private void requestPermissions() {
        int fineLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int networkStatePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int internetPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        ArrayList<String> permissions = new ArrayList<>();

        if (PackageManager.PERMISSION_GRANTED != fineLocationPermissionCheck)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED != coarseLocationPermissionCheck)
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED != writeExternalStoragePermissionCheck)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED != networkStatePermissionCheck)
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        if (PackageManager.PERMISSION_GRANTED != internetPermissionCheck)
            permissions.add(Manifest.permission.INTERNET);

        if (permissions.size() > 0)
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[]{}), 1);
    }
}
