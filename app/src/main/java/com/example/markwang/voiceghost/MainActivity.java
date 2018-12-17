package com.example.markwang.voiceghost;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MapsFragment.ActivityCallback {
    private final String TAG = "MainActivity";
    MapsFragment mapsFragment;
    ConstraintLayout mainLayout;
    FrameLayout mapContainer;


    int mainLayoutHeight;
    int mDropDownHeight;

    float mLastPosY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        initLayout();
        initObject();
        initListener();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mapContainer, mapsFragment, "map")
                    .commit();
        }
    }

    private void initLayout() {
        mapContainer = findViewById(R.id.mapContainer);
        mainLayout = findViewById(R.id.mainLayout);


        //在onCreate並不能保證onLayout已經執行，所以要用post丟到隊伍列尾確保已經完成onLayout
        mainLayout.post(new Runnable() {
            @Override
            public void run() {
                mainLayoutHeight = mainLayout.getHeight();
            }
        });
        mainLayoutHeight = mainLayout.getMeasuredHeight();
    }

    private void initObject() {
        mapsFragment = MapsFragment.newInstance();
        mapsFragment.setActivityCallback(this);
    }

    private void initListener() {
        mapContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastPosY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        double currentPosition = event.getY();
                        double deltaY = mLastPosY - currentPosition;

                        if (deltaY < 0) {
                            v.animate().setInterpolator(new AccelerateInterpolator()).translationY(mainLayoutHeight - mDropDownHeight).setDuration(500);
                        } else if (deltaY > 0) {
                            v.animate().setInterpolator(new AccelerateInterpolator()).translationY(0).setDuration(500);

                        }

//                        float translationY = v.getTranslationY();
//                        translationY -= deltaY;
//
//                        if (translationY < 0)
//                            translationY = 0;
//                        if (translationY > mainLayoutHeight - mDropDownHeight)
//                            translationY = mainLayoutHeight - mDropDownHeight;

//                        v.setTranslationY(translationY);
                        return true;
                }
                return true;
            }
        });
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


    @Override
    public void onDropDownHeight(int dropDownHeight) {
        mDropDownHeight = dropDownHeight;
    }
}
