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
import android.widget.TextView;

import com.example.markwang.voiceghost.R;
import com.example.markwang.voiceghost.component.FirebaseManager;
import com.example.markwang.voiceghost.component.SoundPositionManager;
import com.example.markwang.voiceghost.component.VoiceGhostInfo;
import com.example.markwang.voiceghost.sound.SoundPlayer;
import com.example.markwang.voiceghost.sound.SoundRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomFragment extends Fragment implements SoundPlayer.Callback, FirebaseManager.Callback {
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
    private Button uploadData;
    private Button clearCustomText;
    private Button clearState;
    private TextView audioState;
    private TextView uploadState;

    private SimpleDateFormat mSimpleDateFormat;
    private SoundRecord mSoundRecord;
    private SoundPlayer mSoundPlayer;

    private boolean isRecording = false;
    private boolean isPlaying = false;

    private String mFilePath;
    private String mFileName;

    //    If Android decides to recreate your Fragment later,
//    it's going to call the no-argument constructor of your fragment.
//    So overloading the constructor is not a solution.
    public static CustomFragment newInstance(String str) {
        CustomFragment customFragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("wantStore", str);
        customFragment.setArguments(bundle);
        return customFragment;
    }

    private void initLayout(View contentView) {
        customName = contentView.findViewById(R.id.customName);
        customLattude = contentView.findViewById(R.id.customLattude);
        customLongitude = contentView.findViewById(R.id.customLongitude);
        customTriggerRange = contentView.findViewById(R.id.customTriggerRange);
        customRecipient = contentView.findViewById(R.id.customRecipient);
        customTitle = contentView.findViewById(R.id.customTitle);
        recordAudio = contentView.findViewById(R.id.recordAudio);
        playAudio = contentView.findViewById(R.id.playAudio);
        uploadData = contentView.findViewById(R.id.uploadData);
        clearCustomText = contentView.findViewById(R.id.clearCustomText);
        clearState = contentView.findViewById(R.id.clearState);
        audioState = contentView.findViewById(R.id.audioState);
        uploadState = contentView.findViewById(R.id.uploadState);
    }

    private void initObject() {
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
        mSoundRecord = new SoundRecord(mContext);
        mSoundPlayer = new SoundPlayer(mContext);

        mSoundPlayer.setCallback(this);
        FirebaseManager.getInstance().setCallback(this);
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
                    recordAudio.setText("Stop");
                } else {
                    recordAudio.setText("Record");
                    audioState.setText("true");
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

        uploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceGhostInfo voiceGhostInfo = new VoiceGhostInfo();
                voiceGhostInfo.creator = customName.getText().toString();
                voiceGhostInfo.lattude = customLattude.getText().toString();
                voiceGhostInfo.longitude = customLongitude.getText().toString();
                voiceGhostInfo.triggerRange = customTriggerRange.getText().toString();
                voiceGhostInfo.recipient = customRecipient.getText().toString();
                voiceGhostInfo.title = customTitle.getText().toString();

                voiceGhostInfo.createAt = mSimpleDateFormat.format(new Date());
                voiceGhostInfo.expireAt = "00-00-02-00-00";
                voiceGhostInfo.readOnce = "true";
                voiceGhostInfo.voiceFileName = mFileName;

                FirebaseManager.getInstance().databaseInsert(voiceGhostInfo);
                FirebaseManager.getInstance().uploadFile(mFilePath + "/" + mFileName);
            }
        });

        clearCustomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customName.setText("");
                customLattude.setText("");
                customLongitude.setText("");
                customTriggerRange.setText("");
                customRecipient.setText("");
                customTitle.setText("");

            }
        });

        clearState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioState.setText("False");
                uploadState.setText("False");
            }
        });
    }

    private void defaultValue() {
        customName.setText("Mark");
        customLattude.setText("25.043066964818927");
        customLongitude.setText("121.5646205842495");
        customTriggerRange.setText("10");
        customRecipient.setText("Andy");
        customTitle.setText("How are you?");
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
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

    @Override
    public void onUploadFileSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploadState.setText("true");
            }
        });
    }

    @Override
    public void onDataInsertSuccess(ArrayList<VoiceGhostInfo> voiceGhostInfoArrayList) {
        SoundPositionManager.getInstance().putVoiceGhost(voiceGhostInfoArrayList);
    }
}
