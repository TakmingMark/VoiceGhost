package com.example.markwang.voiceghost.component;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class FirebaseManager extends BaseManager {
    private final String TAG = "FirebaseManager";
    private static FirebaseManager mInstance = new FirebaseManager();
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private Callback mCallback;

    public static FirebaseManager getInstance() {
        if (mInstance == null)
            mInstance = new FirebaseManager();
        return mInstance;
    }

    public void initialized() {
        mFirebaseStorage = FirebaseStorage.getInstance("gs://voiceghost-38502.appspot.com");
        mStorageReference = mFirebaseStorage.getReference();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mDatabaseReference = mFirebaseDatabase.getReference("orii").child("audio");
        setDatabaseEventListener();
        mDatabaseReference.keepSynced(true);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;

    }

    private void setDatabaseEventListener() {
        //read from firebase when firebase data change,
        //reference https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<VoiceGhostInfo> voiceGhostInfoArrayList = new ArrayList<>();
                Log.d(TAG, "dataSnapshot size:" + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    VoiceGhostInfo value = postSnapshot.getValue(VoiceGhostInfo.class);
                    Log.d(TAG, value.print());
                    voiceGhostInfoArrayList.add(value);
                }
                mCallback.onDataInsertSuccess(voiceGhostInfoArrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void databaseInsert(VoiceGhostInfo voiceGhostInfo) {
        mDatabaseReference.push().setValue(voiceGhostInfo);//push is random unique ID
    }

    public void uploadFile(String filePathAndName) {
        Uri file = Uri.fromFile(new File(filePathAndName));
        StorageReference oriiReference = mStorageReference.child("orii/" + file.getLastPathSegment());
        UploadTask uploadTask = oriiReference.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "uploadTask is onFailure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mCallback.onUploadFileSuccess();
                Log.d(TAG, "uploadTask is onSuccess");
            }
        });
    }

    @Override
    public void onDestory() {
        mInstance = null;
    }

    public interface Callback {
        void onUploadFileSuccess();

        void onDataInsertSuccess(ArrayList<VoiceGhostInfo> voiceGhostInfoArrayList);
    }

}
