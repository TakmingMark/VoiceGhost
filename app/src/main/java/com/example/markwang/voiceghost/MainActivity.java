package com.example.markwang.voiceghost;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.markwang.voiceghost.fragment.CustomFragment;
import com.example.markwang.voiceghost.fragment.MapsFragment;
import com.example.markwang.voiceghost.fragment.UserFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    MapsFragment mMapsFragment;
    CustomFragment mCustomFragment;
    UserFragment mUserFragment;
    FrameLayout mainContainer;
    BottomNavigationView mBottomNavigationView;

    int mainLayoutHeight;
    int mainContainerHeight;
    int mBottomNavigationViewHeight;
    float mLastPosY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        requestPermissions();
        initLayout();
        initObject();
        initMainContainer();
        initListener();

//        testFirebase();
    }

    private void initLayout() {
        mainContainer = findViewById(R.id.mainContainer);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        mainContainer.post(new Runnable() {
            @Override
            public void run() {
                mainContainerHeight = mainContainer.getHeight();
            }
        });

        mBottomNavigationView.post(new Runnable() {
            @Override
            public void run() {
                mBottomNavigationViewHeight = mBottomNavigationView.getHeight();
                mainLayoutHeight = mainContainerHeight - mBottomNavigationViewHeight;
                ViewGroup.LayoutParams params = mainContainer.getLayoutParams();
                params.height = mainLayoutHeight;
                params = null;
            }
        });

    }

    private void initObject() {
        mMapsFragment = MapsFragment.newInstance();
        mCustomFragment = CustomFragment.newInstance("test");
        mUserFragment = UserFragment.newInstance("test");
    }

    private void initMainContainer() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, mCustomFragment, "custom")
                .commit();
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.custom:
                        displayFragment(mCustomFragment,mUserFragment,mMapsFragment,"custom");
                        break;
                    case R.id.user:
                        displayFragment(mUserFragment,mCustomFragment,mMapsFragment,"user");
                        break;
                    case R.id.map:
                        displayFragment(mMapsFragment,mUserFragment,mCustomFragment,"map");
                        break;
                }
                return false;
            }
        });
    }

    private void displayFragment(Fragment showFragment,Fragment hideFragment1,Fragment hideFragment2,String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(showFragment.isAdded()){
            fragmentTransaction.show(showFragment);
        }else{
            fragmentTransaction.add(R.id.mainContainer, showFragment, tag);
        }
        if(hideFragment1.isAdded()){
            fragmentTransaction.hide(hideFragment1);
        }
        if(hideFragment2.isAdded()){
            fragmentTransaction.hide(hideFragment2);
        }

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


//    private void testFirebase() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("audio");
//
//        VoiceGhostInfo voiceGhostInfo = new VoiceGhostInfo();
//        voiceGhostInfo.mCreator = "Mark2";
//        voiceGhostInfo.mRecipient = "Andy2";
//        voiceGhostInfo.location = "25.0423922-121.5649822";
//        voiceGhostInfo.mTriggerRange = "100";
//        voiceGhostInfo.createAt = "20181217-1526";
//        voiceGhostInfo.expireAt = "00-00-02-00-00";
//        voiceGhostInfo.readOnce = "true";
//        voiceGhostInfo.mTitle = "Hello world";
//        myRef.child("1").setValue(voiceGhostInfo);
//
//
//        //read from firebase when firebase data change
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//
//                VoiceGhostInfo value = dataSnapshot.child("1").getValue(VoiceGhostInfo.class);
//                Log.d(TAG, value.print());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
//    }
//


}

