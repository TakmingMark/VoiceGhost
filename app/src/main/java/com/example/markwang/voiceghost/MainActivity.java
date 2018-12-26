package com.example.markwang.voiceghost;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.markwang.voiceghost.component.FirebaseManager;
import com.example.markwang.voiceghost.component.SoundPositionManager;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;
import com.example.markwang.voiceghost.fragment.CustomFragment;
import com.example.markwang.voiceghost.fragment.MapsFragment;
import com.example.markwang.voiceghost.fragment.TriggerFragment;
import com.example.markwang.voiceghost.fragment.UserFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements MapsFragment.Callback {
    private final String TAG = "MainActivity";
    private FrameLayout mainContainer;
    private MapsFragment mMapsFragment;
    private CustomFragment mCustomFragment;
    private UserFragment mUserFragment;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        requestPermissions();
        initLayout();
        reconfigurationLayout();
        initObject();
        initFragment(savedInstanceState);
        initListener();
    }

    private void initLayout() {
        mainContainer = findViewById(R.id.mainContainer);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void reconfigurationLayout() {
        final int[] mainContainerHeight = {0};
        mainContainer.post(new Runnable() {
            @Override
            public void run() {
                mainContainerHeight[0] = mainContainer.getHeight();
            }
        });

        mBottomNavigationView.post(new Runnable() {
            @Override
            public void run() {
                int mainLayoutHeight;
                int bottomNavigationViewHeight;

                bottomNavigationViewHeight = mBottomNavigationView.getHeight();
                mainLayoutHeight = mainContainerHeight[0] - bottomNavigationViewHeight;
                ViewGroup.LayoutParams params = mainContainer.getLayoutParams();
                params.height = mainLayoutHeight;
                params = null;
            }
        });
    }

    private void initObject() {
        mCustomFragment = CustomFragment.newInstance("test");
        mUserFragment = UserFragment.newInstance("test");
        mMapsFragment = MapsFragment.newInstance("test");

        mMapsFragment.setCallback(this);
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState != null) {
            mCustomFragment = (CustomFragment) getSupportFragmentManager().findFragmentByTag("custom");
            mUserFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag("user");
            mMapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag("map");
        } else {
            fragmentTransaction
                    .add(R.id.mainContainer, mCustomFragment, "custom");
            fragmentTransaction
                    .add(R.id.mainContainer, mUserFragment, "user");
            fragmentTransaction
                    .add(R.id.mainContainer, mMapsFragment, "map");
        }
        fragmentTransaction.show(mCustomFragment);
        fragmentTransaction.hide(mUserFragment);
        fragmentTransaction.hide(mMapsFragment);
        fragmentTransaction.commit();
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.custom:
                        displayFragment(mCustomFragment, mUserFragment, mMapsFragment);
                        break;
                    case R.id.user:
                        displayFragment(mUserFragment, mCustomFragment, mMapsFragment);
                        break;
                    case R.id.map:
                        displayFragment(mMapsFragment, mUserFragment, mCustomFragment);
                        break;
                }
                return false;
            }
        });
    }

    private void displayFragment(Fragment showFragment, Fragment hideFragment1, Fragment hideFragment2) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(showFragment);
        fragmentTransaction.hide(hideFragment1);
        fragmentTransaction.hide(hideFragment2);
        fragmentTransaction.commit();
    }

    private void requestPermissions() {
        int fineLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int networkStatePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int internetPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int recordAudioRermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

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
        if (PackageManager.PERMISSION_GRANTED != recordAudioRermissionCheck)
            permissions.add(Manifest.permission.RECORD_AUDIO);

        if (permissions.size() > 0)
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[]{}), 1);
    }

    private void showTriggerFragment(Map.Entry<Float, VoiceGhostInfo> triggerPoint) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        TriggerFragment triggerFragment = (TriggerFragment) getSupportFragmentManager().findFragmentByTag("trigger");

        if(triggerFragment!=null && triggerFragment.isAdded())
            triggerFragment.dismiss();

        if (triggerFragment == null)
            triggerFragment = new TriggerFragment();


        triggerFragment.setTriggerPoint(triggerPoint);
        triggerFragment.show(fragmentTransaction, "trigger");
    }

    private void initManager() {
        SoundPositionManager.getInstance().initialized();
        FirebaseManager.getInstance().initialized();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundPositionManager.getInstance().onDestory();
        FirebaseManager.getInstance().onDestory();
    }

    @Override
    public void onTouchMap(LatLng latLng) {
        mCustomFragment.setLatLngFromGoogleMapTouch(latLng);
    }

    @Override
    public void onUserMove(LatLng userLatLng) {

        Map.Entry<Float, VoiceGhostInfo> triggerPoint;
        triggerPoint = SoundPositionManager.getInstance().getTriggerPoint(userLatLng);
        if (triggerPoint != null) {
            if (triggerPoint.getKey() < Float.valueOf(triggerPoint.getValue().triggerRange)) {
                showTriggerFragment(triggerPoint);
            }
        }
    }

    //Because the database return data will cause error when google map not already draw mark.
    @Override
    public void onMapAlready() {
        initManager();
    }
}

