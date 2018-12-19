package com.example.markwang.voiceghost.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.component.FirebaseManager;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;
import com.example.markwang.voiceghost.sound.SoundPlayer;
import com.example.markwang.voiceghost.sound.SoundRecord;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFragment extends Fragment implements SoundPlayer.SoundPlayerCallback {
    private final String TAG = "CustomFragment";
    private Context mContext;
    private EditText customName;
    private EditText customLattude;
    private EditText customLongitude;
    private EditText customTriggerRange;
    private EditText customRecipient;
    private EditText customTitle;
    private Button recordAudio;
    private Button playAudio;
    private Button updateData;

    private SimpleDateFormat mSimpleDateFormat;
    private SoundRecord mSoundRecord;
    private SoundPlayer mSoundPlayer;

    private boolean isRecording = false;
    private boolean isPlaying = false;

    private String mFilePath;

    private String mFileName;

    public static CustomFragment newInstance(String str) {
        CustomFragment customFragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("test", str);
        customFragment.setArguments(bundle);
        return customFragment;
    }

    private void initLayout(View contentView) {
        customName = contentView.findViewById(R.id.customNameET);
        customLattude = contentView.findViewById(R.id.customLattudeET);
        customLongitude = contentView.findViewById(R.id.customLongitudeET);
        customTriggerRange = contentView.findViewById(R.id.customTriggerRangeET);
        customRecipient = contentView.findViewById(R.id.customRecipientET);
        customTitle = contentView.findViewById(R.id.customTitleET);
        recordAudio = contentView.findViewById(R.id.recordAudioBT);
        playAudio = contentView.findViewById(R.id.playAudioBT);
        updateData = contentView.findViewById(R.id.updateDataBT);
    }

    private void initObject() {
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
        mSoundRecord = new SoundRecord(mContext);
        mSoundPlayer = new SoundPlayer(mContext);
        mSoundPlayer.setSoundPlayerCallback(this);

        mFilePath = mContext.getExternalCacheDir().getAbsolutePath();
    }

    private void initListener() {
        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;

                if (isRecording) {
                    mFileName = mSimpleDateFormat.format(new Date()) + ".3gp";
                    mSoundRecord.setFilePathAndName(mFilePath + "/" + mFileName);
                    Log.d(TAG, mFileName);
                    recordAudio.setText("Stop");
                } else {
                    recordAudio.setText("Record");
                }
                mSoundRecord.onRecord(isRecording);
            }
        });

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;

                if (isPlaying) {
                    mSoundPlayer.setFilePathAndName(mFilePath + "/" + mFileName);
                    playAudio.setText("Stop");
                } else {
                    playAudio.setText("Play");
                }
                mSoundPlayer.onPlay(isPlaying);
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceGhostInfo voiceGhostInfo=new VoiceGhostInfo();
                voiceGhostInfo.creator =customName.getText().toString();
                voiceGhostInfo.lattude=customLattude.getText().toString();
                voiceGhostInfo.longitude=customLongitude.getText().toString();
                voiceGhostInfo.triggerRange=customTriggerRange.getText().toString();
                voiceGhostInfo.createAt=mSimpleDateFormat.format(new Date());
                voiceGhostInfo.expireAt = "00-00-02-00-00";
                voiceGhostInfo.readOnce = "true";
                voiceGhostInfo.title = "Hello world";
                FirebaseManager.getInstance().databaseInsert(voiceGhostInfo);
                FirebaseManager.getInstance().uploadFile(mFilePath+"/"+mFileName);
            }
        });


    }

    private void defaultValue() {
        customName.setText("Mark");
        customLattude.setText("123");
        customLongitude.setText("456");
        customTriggerRange.setText("10");
        customRecipient.setText("Andy");
        customTitle.setText("How are you?");
    }


    private void FirebaseUploadFile() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://voiceghost-38502.appspot.com");
        StorageReference storageReference = firebaseStorage.getReference();

        Uri file = Uri.fromFile(new File(mFilePath + "/" + mFileName));
        StorageReference oriiRef = storageReference.child("orii/" + file.getLastPathSegment());

        UploadTask uploadTask = oriiRef.putFile(file);
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

    @Override
    public void onAttach(Context context) {
        mContext = context;
        //getArguments.getString(test);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.custom, null);
        initLayout(contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initObject();
        initListener();
        defaultValue();
    }

    @Override
    public void playerCompletion() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playAudio.setText("Play");
                isPlaying = !isPlaying;
            }
        });
    }
}
