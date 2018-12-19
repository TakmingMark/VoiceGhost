package com.example.markwang.voiceghost.component;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseManager {
    private final String TAG="FirebaseManager";
    private final static FirebaseManager mFirebaseManager = new FirebaseManager();
    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;

    public static FirebaseManager getInstance() {
        return mFirebaseManager;
    }

    private FirebaseManager() {
        mFirebaseStorage = FirebaseStorage.getInstance("gs://voiceghost-38502.appspot.com");
        mStorageReference = mFirebaseStorage.getReference();
    }

    public void databaseInsert( VoiceGhostInfo voiceGhostInfo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orii").child("audio");
        myRef.push().setValue(voiceGhostInfo);//push is random unique ID

        //read from firebase when firebase data change,
        //reference https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG,"dataSnapshot size:"+dataSnapshot.getChildrenCount());
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    VoiceGhostInfo value = postSnapshot.getValue(VoiceGhostInfo.class);
                    Log.d(TAG, value.print());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

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
                Log.d(TAG, "uploadTask is onSuccess");
            }
        });
    }
}
